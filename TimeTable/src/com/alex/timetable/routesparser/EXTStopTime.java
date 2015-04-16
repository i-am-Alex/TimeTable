package com.alex.timetable.routesparser;

public class EXTStopTime {

	private int posNum = 0;
	private String stationTime = "";
	private int firstDirection = 1;

	public int getPosNum() {
		return posNum;
	}

	public void setPosNum(int posNum) {
		this.posNum = posNum;
	}

	public String getStationTime() {
		return stationTime;
	}

	public void setStationTime(String stationTime) {
		this.stationTime = stationTime;
	}

	public int getFirstDirection() {
		return firstDirection;
	}

	public void setFirstDirection(int firstDirection) {
		this.firstDirection = firstDirection;
	}

}
