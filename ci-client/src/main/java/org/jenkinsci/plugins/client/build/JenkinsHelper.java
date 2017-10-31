package org.jenkinsci.plugins.client.build;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.jenkinsci.plugins.client.CIApplication;
import org.jenkinsci.plugins.client.CIConfigure;
import org.jenkinsci.plugins.client.windows.CIMonitorWindow;
import org.jenkinsci.plugins.client.windows.MessageWindow;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
/**
 * 
 * <p>Description: []</p>
 * Created on 2017年9月30日
 * @author  <a href="mailto: hanshixiong@camelotchina.com">韩士雄</a>
 * @version 1.0 
 * Copyright (c) 2017 北京柯莱特科技有限公司 交付部
 */
public class JenkinsHelper {
	private static JenkinsHelper _instance;
	/** Jenkins服务地址 */
	private static final String JENKINS_URL = CIConfigure.getInstance().getJenkins().getRootUrl();
	/** 任务监控地址 */
	private static final String MONITOR_URL = JENKINS_URL + "build-monitor/?querys=";
	private static JenkinsServer jenkins;
	/** 构建任务列表 */
	private static final List<BuildInfo> builds = new ArrayList<BuildInfo>();
	private JenkinsHelper(){}
	
	public static JenkinsHelper getInstance() {
		if ( _instance==null ) {
			try {
				jenkins = new JenkinsServer(new URI(JENKINS_URL));
			} catch (URISyntaxException e) {
				e.printStackTrace();
				MessageWindow.info(MessageWindow.TitleEnum.ERROR, "Jenkin 连接失败", MessageWindow.ExitEnum.FAILURE);
			}
			_instance = new JenkinsHelper();
		}
		return _instance;
	}
	/**
	 * 
	 * <p>Discription:[构建所有任务]</p>
	 * Created on 2017年10月11日
	 * @author[hanshixiong]
	 */
	public JenkinsHelper buildAll() {
		String[] buildJobs = CIApplication.mainArguments.getJobs();
		//http://10.95.121.180:8084/jenkins/build-monitor/?querys=TestDemo~17,TestDemo~13
		String params = "";
		// 触发所有构建
		for ( int i=1; i<=buildJobs.length; i++ ) {
			String job = buildJobs[i-1];
			int buildNumber = this.runbuild(job);
			params += job+"~"+buildNumber;
			if ( i<buildJobs.length) {
				params += ",";
			}
		}
		String openUrl = MONITOR_URL+params;
		// 启动浏览器加载构建进度
		this.openBrowser(openUrl);
		// 启动构建监控
		boolean isSuccess = this.monitor();
		
		if ( isSuccess ) {
			MessageWindow.info(MessageWindow.TitleEnum.SUCCESS, "构建成功", MessageWindow.ExitEnum.NORMAL);
		} else {
			MessageWindow.info(MessageWindow.TitleEnum.FAILURE, "构建失败", MessageWindow.ExitEnum.FAILURE);
		}
		return this;
	}
	/**
	 * 
	 * <p>Discription:[启动一个线程展示构建进度]</p>
	 * Created on 2017年10月11日
	 * @param url
	 * @author[hanshixiong]
	 */
	private void openBrowser(final String url) {
		System.out.println("[Info] url..." + url);
		Thread browserThread = new Thread(new Runnable(){
			public void run() {
				CIMonitorWindow.getInstance().open(url);
			}
		});
		browserThread.start();
	}
	/**
	 * 
	 * <p>Discription:[监控构建状态]</p>
	 * Created on 2017年10月11日
	 * @throws IOException 
	 * @throws InterruptedException 
	 * @author[hanshixiong]
	 */
	private boolean monitor() {
		BuildInfo building = new BuildInfo();
		boolean isBuildSuccess = true;
		int retryCounter = 0;
		int retryThreshold = 5; // 重试阈值 
		while (builds.contains(building)) {
			try {
				Thread.sleep(1000);
				for ( BuildInfo info: builds ) {
					JobWithDetails jobObj = jenkins.getJob(info.getJobName());
					Build build = jobObj.getBuildByNumber(info.getNumber());
					if ( build==null ) {
						continue;
					}
					BuildWithDetails buildDetail = build.details();
					info.setResult(buildDetail.getResult());
					info.setBuilding(buildDetail.isBuilding());
					// 构建已结束 且 构建不成功，标记为失败
					if ( !buildDetail.isBuilding() && buildDetail.getResult()!=BuildResult.SUCCESS ) {
						isBuildSuccess = false;
					}
				}
			} catch (Exception e) {
				retryCounter ++;
				if (retryCounter>retryThreshold) { // 失败次数过多时，退出程序
					MessageWindow.info(MessageWindow.TitleEnum.ERROR, "获取构建进度失败", MessageWindow.ExitEnum.FAILURE);
				}
			}
		}
		return isBuildSuccess;
	}

	/**
	 * 
	 * <p>Discription:[触发远程构建]</p>
	 * Created on 2017年10月11日
	 * @param jobName job名
	 * @return 返回构建号
	 * @author[hanshixiong]
	 */
	public int runbuild(String jobName) {
		int nextNum = -1;
		try {
			BuildInfo info = new BuildInfo();
			JobWithDetails jobObj = jenkins.getJob(jobName);
			nextNum = jobObj.getNextBuildNumber();
			String postUrl = this.getPostUrl(jobName);
			// 触发构建
			this.postFileBuild(postUrl, CIApplication.patchHelper.getPatchFile());
			info.setJobName(jobName);
			info.setNumber(nextNum);
			builds.add(info);
		} catch (Exception e) {
			e.printStackTrace();
			MessageWindow.info(MessageWindow.TitleEnum.ERROR, "构建触发失败", MessageWindow.ExitEnum.FAILURE);
		}
		return nextNum;
	}
	/**
	 * 
	 * <p>Discription:[停止触发的所有构建]</p>
	 * Created on 2017年10月12日
	 * @author[hanshixiong]
	 */
	public void stopAll() {
		for ( BuildInfo info: builds ) {
			try {
				JobWithDetails jobObj = jenkins.getJob(info.getJobName());
				Build build = jobObj.getBuildByNumber(info.getNumber());
				System.out.println("[Info] stop " + build + info.getJobName() + "#" + info.getNumber());
				
				if ( build==null ) {
					continue;
				}
				BuildWithDetails buildDetail = build.details();
				// 取消构建中的任务
				if ( buildDetail.isBuilding() ) {
					buildDetail.Stop();
				}
			} catch (Exception e) {
				// 该异常可不处理
				e.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * <p>Discription:[拼接构建触发url]</p>
	 * Created on 2017年10月11日
	 * @param jobName 执行构建的job名
	 * @return
	 * @author[hanshixiong]
	 */
	private String getPostUrl(String jobName) {
		String postUrl = JENKINS_URL + "job/" + jobName + "/buildWithParameters";
		return postUrl;
	}
	/**
	 * 
	 * <p>Discription:[触发构建]</p>
	 * Created on 2017年10月11日
	 * @param url 触发构建url
	 * @param patch patch文件
	 * @throws HttpException
	 * @throws IOException
	 * @author[hanshixiong]
	 */
	private void postFileBuild(String url, File patch) throws HttpException, IOException {
		String returnStr = null;
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000); // 请求超时
		client.getHttpConnectionManager().getParams().setSoTimeout(3000); // 读取超时
		PostMethod httpPost = new PostMethod(url);
		
		
		Part[] parts = { new FilePart(patch.getName(), patch) };
		httpPost.setRequestEntity(new MultipartRequestEntity(parts, httpPost.getParams()));
		client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		int status = client.executeMethod(httpPost);
		byte[] result = httpPost.getResponseBody();
		returnStr = new String(result, "UTF-8");
		System.out.println(returnStr);
	}
}
