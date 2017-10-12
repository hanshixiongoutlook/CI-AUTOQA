package org.jenkinsci.plugins.client.build;

import com.offbytwo.jenkins.model.BuildResult;

public class BuildInfo {
	private int number;
	private String jobName;
	private BuildResult result = BuildResult.UNKNOWN;
	private boolean isBuilding = true;
	
	public boolean isBuilding() {
		return isBuilding;
	}

	public void setBuilding(boolean isBuilding) {
		this.isBuilding = isBuilding;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public BuildResult getResult() {
		return result;
	}

	public void setResult(BuildResult result) {
		this.result = result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		result = prime * result + number;
		result = prime * result
				+ ((this.result == null) ? 0 : this.result.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildInfo other = (BuildInfo) obj;
		if (isBuilding != other.isBuilding)
			return false;
		return true;
	}
}
