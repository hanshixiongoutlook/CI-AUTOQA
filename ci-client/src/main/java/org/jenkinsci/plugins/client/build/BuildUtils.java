package org.jenkinsci.plugins.client.build;

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpException;
import org.jenkinsci.plugins.client.CIConfigure;
import org.jenkinsci.plugins.client.utils.HttpClientUtils;

public class BuildUtils {
	
	//http://10.95.121.181:8081/jenkins/build-monitor/loadBuild?querys=Test~1,Test2~4
	public static BuildInfo getBuild(String jobName,int number) throws HttpException, IOException {
		String buildInfoUrl = CIConfigure.getInstance().getJenkins().getRootUrl() + "/build-monitor/loadBuild?querys=" + jobName + "~"+number;
		
		// [{"buildNum":1,"building":false,"color":"ABORTED","jobName":"Test","progress":100}]
		String result = HttpClientUtils.httpGet(buildInfoUrl);
		JSONArray array = JSONArray.fromObject(result);
		if ( array.size()==0 ) {
			return null;
		}
		
		BuildInfo build = new BuildInfo();
		JSONObject json = JSONObject.fromObject(array.get(0));
		
		build.setBuilding(json.getBoolean("building"));
		build.setJobName(jobName);
		build.setNumber(number);
		if ( !build.isBuilding() ) {
			build.setColor(Color.valueOf(json.getString("color")));
		}
		return build;
		
	}
	
	// http://10.95.121.181:8081/jenkins/job/Test/3/stop
	public static void stopBuild(String jobName, int number) {
		String stopUrl = CIConfigure.getInstance().getJenkins().getRootUrl() + "/job/" + jobName + "/"+number+"/stop";
		
		// [{"buildNum":1,"building":false,"color":"ABORTED","jobName":"Test","progress":100}]
		try {
			String result = HttpClientUtils.httpPost(stopUrl);
			System.out.println(result);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
