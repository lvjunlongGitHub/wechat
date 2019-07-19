package com.jincou.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jincou.wechat.config.WeChatConfig;
import com.jincou.wechat.domain.User;
import com.jincou.wechat.enums.GenderEnum;
import com.jincou.wechat.service.UserService;
import com.jincou.wechat.utils.HttpUtils;
import com.jincou.wechat.utils.http.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private HTTP httpClient;

    @Value("${qq.host}")
    private String h5QQurl;

    @Value("${qq.appid}")
    private String h5QQAppId;

    @Value("${qq.appSecret}")
    private String h5QQAppKey;

    @Value("${qq.redirectUrl}")
    private String h5QQRedirectUrl;


    @Override
    public User saveWeChatUser(String code) {

        //1、通过openAppid和openAppsecret和微信返回的code，拼接URL
        String accessTokenUrl = String.format(WeChatConfig.OPEN_ACCESS_TOKEN_URL, weChatConfig.getOpenAppid().trim(), weChatConfig.getOpenAppsecret().trim(), code);

        //2、通过URL再去回调微信接口 (使用了httpclient和gson工具）
        Map<String, Object> baseMap = HttpUtils.doGet(accessTokenUrl);

        //3、回调成功后获取access_token和oppid
        if (baseMap == null || baseMap.isEmpty()) return null;
        String accessToken = (String) baseMap.get("access_token");
        String openId = (String) baseMap.get("openid");

        //4、access_token和openid拼接URL（openid是用户唯一标志）
        String userInfoUrl = String.format(WeChatConfig.OPEN_USER_INFO_URL, accessToken, openId);

        //5、通过URL再去调微信接口获取用户基本信息
        Map<String, Object> baseUserMap = HttpUtils.doGet(userInfoUrl);

        if (baseUserMap == null || baseUserMap.isEmpty()) return null;

        //6、获取用户姓名、性别、城市、头像等基本信息
        String nickname = (String) baseUserMap.get("nickname");
        Integer sex = (Integer) baseUserMap.get("sex");
        String province = (String) baseUserMap.get("province");
        String city = (String) baseUserMap.get("city");
        String country = (String) baseUserMap.get("country");
        String headimgurl = (String) baseUserMap.get("headimgurl");
        return null;
    }

    @Override
    public void qqLogin(String authCode) {
        try {
            /*根据code获取AccessToken*/
            byte[] accessTokenJson = getAccessTokenJson(authCode);
            String accessTokenString = new String(accessTokenJson);
            if (accessTokenString.contains("callback")) {
                //return new OperationResult(BizErrorCode.LOGIN_QQ_ERROR);
            }
            //GwsLogger.info("qq: 根据code获取AccessToken", Arrays.toString(accessTokenJson));
            String accessToken = accessTokenString.split("&")[0].split("=")[1];

            /*根据accessToken获取openId*/
            JSONObject openIdJson = getOpenId(accessToken);
            //GwsLogger.info("qq: 根据accessToken获取openId", openIdJson.toString());
            if(openIdJson.containsKey("error")){
                //return new OperationResult(BizErrorCode.LOGIN_QQ_ERROR);
            }
            String openId = openIdJson.getString("openid");
            String unionId = openIdJson.getString("unionid");

            /*根据openId获取用户信息*/
            JSONObject userJson = getUserInfo(accessToken, openId);
            if(!userJson.containsKey("nickname")){
                //return new OperationResult(BizErrorCode.LOGIN_QQ_ERROR);
            }
            String userName = userJson.getString("nickname");
            String iconURL = userJson.getString("figureurl_qq_2");
            String sexName = userJson.getString("gender");
            Integer sex = Objects.requireNonNull(GenderEnum.getEnum(sexName)).getCode();
        } catch (Exception e) {

        }
    }

    /**
     *获取AccessToken
     * @param authCode qq平台返回的code码
     * @return JsonObject
     * @throws IOException {@link IOException}
     */
    private byte[] getAccessTokenJson(String authCode) throws IOException {
        /*根据code获取AccessToken*/
        Map<String, String> accessTokenParams = Maps.newHashMap();
        accessTokenParams.put("grant_type", "authorization_code");
        accessTokenParams.put("client_id", h5QQAppId);
        accessTokenParams.put("client_secret", h5QQAppKey);
        accessTokenParams.put("code", authCode);
        accessTokenParams.put("state", "yxmlogin");
        accessTokenParams.put("redirect_uri", h5QQRedirectUrl);
        return httpClient.GET(h5QQurl + "oauth2.0/token", accessTokenParams);

    }

    /**
     * 根据accessToken获取openId
     * @param accessToken 上一步获取的token
     * @return openID
     * @throws IOException {@link IOException}
     */
    private JSONObject getOpenId(String accessToken) throws IOException {
        Map<String, String> openIdparams = Maps.newHashMap();
        openIdparams.put("access_token",accessToken);
        openIdparams.put("state", "yxmlogin");
        openIdparams.put("unionid", "1");
        byte[] openIdbytes = httpClient.GET(h5QQurl + "oauth2.0/me", openIdparams);
        return JSON.parseObject(new String(openIdbytes).replace("callback(", "").replace(");", ""));
    }

    /**
     * 根据accessT和openId获取用户信息
     * @param accessToken 上一步获取的token
     * @param openId 上一步获取的openId
     * @return 用户信息
     * @throws IOException {@link IOException}
     */
    private JSONObject getUserInfo(String accessToken, String openId) throws IOException {
        /*根据openId获取用户信息*/
        Map<String, String> userParams = Maps.newHashMap();
        userParams.put("access_token",accessToken);
        userParams.put("oauth_consumer_key",h5QQAppId);
        userParams.put("openid",openId);
        byte[] userByte = httpClient.POST(h5QQurl + "user/get_user_info", userParams);
        return JSON.parseObject(new String(userByte).replace("callback(", "").replace(");", ""));
    }
}
