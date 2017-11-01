package org.jenkinsci.plugins.client.utils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpClientUtils {
	
	public static String httpGet(String url) throws HttpException, IOException {
		String returnStr = null;
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000); // 请求超时
		client.getHttpConnectionManager().getParams().setSoTimeout(3000); // 读取超时
		GetMethod httpGet = new GetMethod(url);
		client.executeMethod(httpGet);
		byte[] result = httpGet.getResponseBody();
		returnStr = new String(result, "UTF-8");
		return returnStr;
	}
	
	public static String httpPost(String url) throws HttpException, IOException {
		String returnStr = null;
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000); // 请求超时
		client.getHttpConnectionManager().getParams().setSoTimeout(3000); // 读取超时
		PostMethod httpPost = new PostMethod(url);
		client.executeMethod(httpPost);
		byte[] result = httpPost.getResponseBody();
		returnStr = new String(result, "UTF-8");
		return returnStr;
	}

}
