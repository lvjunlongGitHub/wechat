package com.jincou.wechat.utils.http.impl;

import com.alibaba.fastjson.JSON;
import com.jincou.wechat.utils.http.HTTP;
import okhttp3.*;
import okhttp3.HttpUrl.Builder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Map;

/**
 * 封装http协议，简化操作
 *
 * @author zwp
 */
public class HttpImpl implements HTTP {
    public final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    public final MediaType MEDIA_XML = MediaType.parse("application/xml; charset=utf-8");

    /**
     * 自定义配置，见类HttpClientConfig.java
     */
    @Autowired
    private OkHttpClient httpClient;

    /**
     * GET极简同步方法
     *
     * @param url
     * @return
     * @throws HttpException
     * @throws IOException
     */
    @Override
    public byte[] GET(String url) throws HttpException, IOException {
        Request request = new Request.Builder()
                .url(url).get()
                .build();
        Response response = ReqExecute(request);
        if (!response.isSuccessful()) {
            throw new HttpException(response, "exception code:" + response.code());
        }
        return response.body().bytes();
    }

    /**
     * POST极简同步方法，JSON内容
     *
     * @param url
     * @param jsonBody
     * @return
     * @throws HttpException
     * @throws IOException
     */
    @Override
    public byte[] POST(String url, String jsonBody) throws HttpException, IOException {
        RequestBody body = RequestBody.create(MEDIA_JSON, jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = ReqExecute(request);
        if (!response.isSuccessful()) {
            throw new HttpException(response, "exception code:" + response.code());
        }
        return response.body().bytes();
    }

    /**
     * POST极简同步方法，xml内容
     *
     * @param url
     * @param xmlBody
     * @return
     * @throws IOException
     */
    @Override
    public byte[] POSTXML(String url, String xmlBody) throws HttpException, IOException {
        RequestBody body = RequestBody.create(MEDIA_XML, xmlBody);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = ReqExecute(request);
        if (!response.isSuccessful()) {
            throw new HttpException(response, "exception code:" + response.code());
        }
        return response.body().bytes();
    }

    /**
     * GET 同步方法
     *
     * @param baseUrl
     * @param queryParams
     * @return
     * @throws HttpException
     * @throws IOException
     */
    @Override
    public byte[] GET(String baseUrl, Map<String, String> queryParams) throws HttpException, IOException {
        //拼装param
        Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        for (Map.Entry<String, String> item : queryParams.entrySet()) {
            urlBuilder.addQueryParameter(item.getKey(), item.getValue());
        }
        HttpUrl httpUrl = urlBuilder.build();
        //发送请求
        Request request = new Request.Builder()
                .url(httpUrl.toString()).get()
                .build();
        Response response = ReqExecute(request);
        if (!response.isSuccessful()) {
            throw new HttpException(response, "exception code:" + response.code());
        }
        return response.body().bytes();
    }

    @Override
    public <T> T GET(String baseUrl, Map<String, String> queryParams, Class<T> clazz) throws HttpException, IOException {
        byte[] bytes = GET(baseUrl, queryParams);
        return JSON.parseObject(new String(bytes), clazz);
    }

    /**
     * POST同步方法
     *
     * @param baseUrl
     * @param bodyParams
     * @return
     * @throws HttpException
     * @throws IOException
     */
    @Override
    public byte[] POST(String baseUrl, Map<String, String> bodyParams) throws HttpException, IOException {
        //encode params
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> item : bodyParams.entrySet()) {
            bodyBuilder.addEncoded(item.getKey(), item.getValue());
        }
        FormBody formBody = bodyBuilder.build();
        //发送请求
        Request request = new Request.Builder()
                .url(baseUrl).post(formBody)
                .build();

        Response response = ReqExecute(request);
        if (!response.isSuccessful()) {
            throw new HttpException(response, "exception code:" + response.code());
        }
        return response.body().bytes();
    }

    @Override
    public <T> T POST(String baseUrl, Map<String, String> bodyParams, Class<T> clazz) throws HttpException, IOException {
        byte[] bytes = POST(baseUrl, bodyParams);

        return JSON.parseObject(new String(bytes), clazz);
    }

    /**
     * 请求方法
     *
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public Response ReqExecute(Request request) throws IOException {
        return ReqExecuteCall(request).execute();
    }

    /**
     * 构造CALL方法
     *
     * @param request
     * @return
     */
    @Override
    public Call ReqExecuteCall(Request request) {
        return httpClient.newCall(request);
    }


}
