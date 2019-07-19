/*
 * Copyright (C) 2016  HangZhou YuShi Technology Co.Ltd  Holdings Ltd. All rights reserved
 *
 * 本代码版权归杭州宇石科技所有，且受到相关的法律保护。
 * 没有经过版权所有者的书面同意，
 * 任何其他个人或组织均不得以任何形式将本文件或本文件的部分代码用于其他商业用途。
 *
 */
package com.jincou.wechat.utils.http;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.httpclient.HttpException;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 *     HTTP客户端常用接口封装，简化操作，提升性能，后续支持注解
 *     参考RestTemplate
 * </p>
 * @author lvjunlong
 * @date 2019/7/19 下午2:42
 */
public interface HTTP {
	
	/**
	 * 
	 * GET同步方法
	 * 
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	byte[] GET(String url) throws HttpException,IOException;
	/**
	 * 
	 * GET同步方法
	 * 
	 * @param baseUrl
	 * @param queryParams
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	byte[] GET(String baseUrl, Map<String, String> queryParams) throws HttpException,IOException;


	<T> T GET(String baseUrl, Map<String, String> queryParams, Class<T> clazz) throws HttpException,IOException;

	/**
	 *
	 * POST同步方法
	 *
	 * @author wyq 2016年7月17日
	 * @param url
	 * @param xmlBody
	 * @return
	 * @throws IOException
	 */
	public  byte[] POSTXML(String url, String xmlBody) throws HttpException,IOException;


	/**
	 *
	 * POST同步方法
	 *
	 * @param url
	 * @param jsonBody
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	byte[] POST(String url, String jsonBody) throws HttpException,IOException;
	/**
	 *
	 *  POST同步方法
	 *
	 * @param baseUrl
	 * @param bodyParams
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	byte[] POST(String baseUrl, Map<String, String> bodyParams) throws HttpException,IOException;


	<T> T POST(String baseUrl, Map<String, String> bodyParams, Class<T> clazz) throws HttpException,IOException;

	Response ReqExecute(Request request) throws IOException;

	Call ReqExecuteCall(Request request);

}
