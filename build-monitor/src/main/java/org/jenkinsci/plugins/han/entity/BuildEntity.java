package org.jenkinsci.plugins.han.entity;

import static hudson.Util.filter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hudson.model.Executor;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.Jenkins;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BuildEntity {
	private static final Logger LOG = LoggerFactory.getLogger(BuildEntity.class);
	private final Jenkins _jenkins = Jenkins.getInstance();
	/**
	 * 批量查询构建状态
	 * @param querys
	 * @return
	 */
	public List<BuildBean> getBuilds(List<BuildQueryEntity> querys) {
		List<BuildBean> result = new ArrayList<BuildBean>();
		if ( querys == null ) {
			return result;
		}
		for (BuildQueryEntity q: querys) {
			BuildBean b = this.getBuild(q);
			if ( b!=null ) {
				result.add(b);
			}
		}
		return result;
	}
	/**
	 * 获取构建状态
	 * @param jobName
	 * @param buildNum
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BuildBean getBuild(BuildQueryEntity query) {
		if ( query==null || query.getBuildNum()==null || query.getBuildNum()==null ) {
			LOG.error("参数错误：" + JSONObject.fromObject(query).toString());
			return null;
		}
		Job<?,?> job = this.findJob(query.getJobName());
		if ( job==null ) {
			LOG.error("Job不存在：" + JSONObject.fromObject(query).toString());
			return null;
		}
		Run build = job.getBuildByNumber(query.getBuildNum());
		if ( build==null ) {
			LOG.error("Build不存在：" + JSONObject.fromObject(query).toString());
			return null;
		}
		Executor executor = build.getExecutor();
		
		BuildBean b = new BuildBean();
		b.setJobName(query.getJobName());
		b.setBuildNum(query.getBuildNum());
		if ( executor != null ) { // 任务构建中
			b.setProgress(executor.getProgress());
			b.setBuilding(true);
		} else { // 任务构建结束
			b.setProgress(100);
			b.setBuilding(false);
			b.setColor(job.getBuildByNumber(query.getBuildNum()).getIconColor().name());
		}
		LOG.info(JSONObject.fromObject(b).toString());
		return b;
	}
	/**
	 * 查找目标job
	 * @param jobName
	 * @return
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	private Job<?,?> findJob(String jobName) {
		List<Job<?, ?>> jobList = new ArrayList(filter(_jenkins.getAllItems(), Job.class));
		Job<?, ?> destJob = null; 
		for (Job<?, ?> job: jobList) {
			if ( job.getName().equals(jobName) ) {
				destJob = job;
			}
		}
		return destJob;
		
	}
}
