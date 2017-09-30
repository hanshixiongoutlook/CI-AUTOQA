package org.jenkinsci.plugins.client.build;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.jenkinsci.plugins.client.CIApplication;
import org.jenkinsci.plugins.client.CIConfigure;
import org.junit.Test;
/**
 * 
 * <p>Description: []</p>
 * Created on 2017年9月30日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class JenkinsHelper {
	/*
	 * 1. 获取构建号
	 * 
	 * 
	 */
	
	/** Jenkins 地址 */
	private static final String BUILD_URL = "http://10.95.121.180:8084/jenkins/job/TestDemo/buildWithParameters";
	
	
	@Test
	public void test() throws HttpException, IOException {
		this.runBuild(new File(CIConfigure.DATA_PATH + "patch.diff"));
	}
	
	public void runbuild() throws HttpException, IOException {
		this.runBuild(CIApplication.patchHelper.getPatchFile());
	}
	public void runBuild(File patch) throws HttpException, IOException {
		String returnStr = null;
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000); // 请求超时
		client.getHttpConnectionManager().getParams().setSoTimeout(3000); // 读取超时
		URL url = new URL(BUILD_URL);
		PostMethod httpPost = new PostMethod(BUILD_URL);
		
		
		Part[] parts = { new FilePart(patch.getName(), patch) };
		httpPost.setRequestEntity(new MultipartRequestEntity(parts, httpPost.getParams()));
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		int status = client.executeMethod(httpPost);
		byte[] result = httpPost.getResponseBody();
		returnStr = new String(result, "UTF-8");
		System.out.println(returnStr);
	}
	
	private File getPatchFile() {
		return new File(CIConfigure.DATA_PATH + "patch.diff");
	}
	
	private int getNextBuildNumber() {
		return 0;
	}
	
	public void getRunJobs() {
		
	}
	
}
