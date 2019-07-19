/*
 * Copyright (C) 2016  HangZhou YuShi Technology Co.Ltd  Holdings Ltd. All rights reserved
 *
 * 本代码版权归杭州宇石科技所有，且受到相关的法律保护。
 * 没有经过版权所有者的书面同意，
 * 任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 *
 */
package com.jincou.wechat.utils.http.impl;

import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * HTTP客户端HTTP请求日志
 *
 * @author liuyi  2016年7月17日 下午7:15:30
 */
public class HttpLoggerInterceptor implements Interceptor {

    private Long startTime = 0L;
    private int reqId = 0;

    /**
     * 拦截生成日志
     * <p>
     * (non-Javadoc)
     *
     * @see Interceptor#intercept(Chain)
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        try {
            //请求前报文
            beforeRequest(request);
            response = chain.proceed(request);
        } finally {
            //返回后报文
            if (response != null) {
                return afterRequest(response);
            }
        }
        return response;
    }

    private void beforeRequest(Request request) {
        startTime = System.currentTimeMillis();

        String url = request.url().toString();

        reqId = request.hashCode();

        try {
            String content = null;
            if (request.body() != null) {
                final Request cloneReq = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                cloneReq.body().writeTo(buffer);
                content = buffer.readUtf8();
            }

            //SystemLogger.info(String.format("[http request,reqId=%s,url=%s,content=%s]", reqId,  url, content));
        } catch (final IOException e) {
            //SystemLogger.error(e, "http request error");
        }
    }

    private Response afterRequest(Response response) {
        String url = response.request().url().toString();
        Response.Builder builder = response.newBuilder();
        Response cloneRespone = builder.build();
        ResponseBody body = cloneRespone.body();

        //返回时间
        if (body != null) {
            MediaType mediaType = body.contentType();
            if (mediaType != null) {
                if (isText(mediaType)) {
                    String resp = null;
                    try {
                        resp = body.string();
                    } catch (IOException e) {
                        //SystemLogger.error("[HttpLoggerInterceptor error:]" + e);
                    }
                    body = ResponseBody.create(mediaType, resp);

                    Long durationTime = System.currentTimeMillis() - startTime;

                    Long useTime = TimeUnit.MILLISECONDS.toMillis(durationTime);

//                    SystemLogger.info(String.format("[http response:reqId:%s,url:%s,http code:%s,content:%s,time:%sms]",
//                            reqId, url, cloneRespone.code(), resp, useTime));
//                    response = response.newBuilder().body(body).build();
                }
            }
        }
        return response;
    }


    /**
     * 只监控json,xml,html类型
     *
     * @param mediaType
     * @return
     * @author liuyi 2016年7月18日
     */
    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml")
                    ) {
                return true;
            }
        }
        return false;
    }


}
