package org.jenkinsci.plugins.han.entity;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BuildQueryEntity {
	String jobName;
	Integer buildNum;
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public Integer getBuildNum() {
		return buildNum;
	}
	public void setBuildNum(Integer buildNum) {
		this.buildNum = buildNum;
	}
	/**
	 * 
	 * @param queryStr : jobName1~buildNum,jobName2~buildNum
	 */
	public static List<BuildQueryEntity> getQuerys(String queryStr) {
		String[] array = queryStr.split(",");
		List<BuildQueryEntity> list = new ArrayList<BuildQueryEntity>();
		for (String obj: array) {
			String[] job = obj.split("~");
			BuildQueryEntity b = new BuildQueryEntity();
			b.setBuildNum(Integer.valueOf(job[1]));
			b.setJobName(job[0]);
			list.add(b);
		}
		return list;
	}
//	@Test
//	public void testGetQuerys() {
//		String querys = "[{\"jobName\":\"testJob\",\"buildNum\":12},{\"jobName\":\"aaa\",\"buildNum\":33}]";
//		getQuerys(querys);
//	}
}
