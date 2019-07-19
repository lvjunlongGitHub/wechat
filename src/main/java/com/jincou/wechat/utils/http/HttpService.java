package com.jincou.wechat.utils.http;

import com.jincou.wechat.utils.common.CommonResponse;
import com.jincou.wechat.utils.common.OperationResult;

import java.util.Map;

/**
 * @author lvjunlong
 * @date 2019/7/19 下午2:42
 */
public interface HttpService {

    <T extends CommonResponse> T call(String service, String methodName, Map<String, Object> params, Class<T> clazz);

    /**
     * 远程调用接口
     * @param url
     * @param uri
     * @param params
     * @param clazz
     * @param <T>
     * @return
     */
    <T> OperationResult<T> callRemote(String url, String uri, Map<String, Object> params, Class<T> clazz);

}
