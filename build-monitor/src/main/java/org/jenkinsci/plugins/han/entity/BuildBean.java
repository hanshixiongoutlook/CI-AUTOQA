package org.jenkinsci.plugins.han.entity;

public class BuildBean {
	String jobName;
	int buildNum;
	int progress;
	boolean isBuilding;
	String color;
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public int getBuildNum() {
		return buildNum;
	}
	public void setBuildNum(int buildNum) {
		this.buildNum = buildNum;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public boolean isBuilding() {
		return isBuilding;
	}
	public void setBuilding(boolean isBuilding) {
		this.isBuilding = isBuilding;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
