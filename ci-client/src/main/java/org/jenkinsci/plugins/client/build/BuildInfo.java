package org.jenkinsci.plugins.client.build;


public class BuildInfo {
	private int number;
	private String jobName;
	private boolean isBuilding = true;
	private Color color;
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

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
	public boolean isSuccess() {
		if ( this.isBuilding ) {
			return false;
		}
		if (Color.ABORTED.name().equals(color.name())) {
			return false;
		} 
		if (Color.RED.name().equals(color.name())) {
			return false;
		} 
		return true;
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
