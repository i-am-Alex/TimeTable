package com.alex.timetable.routesparser;

public class EXTGivenParams {
	
	private int routeId;
	private int isWorkDays;
	private int halfStopsCount;
	private String urlStr;
	
	public int getRouteId() {
		return routeId;
	}
	
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	
	public int getIsWorkDays() {
		return isWorkDays;
	}
	
	public void setIsWorkDays(int isWorkDays) {
		this.isWorkDays = isWorkDays;
	}
	
	public int getHalfStopsCount() {
		return halfStopsCount;
	}
	
	public void setHalfStopsCount(int halfStopsCount) {
		this.halfStopsCount = halfStopsCount;
	}
	
	public String getUrlStr() {
		return urlStr;
	}
	
	public void setUrlStr(String urlStr) {
		this.urlStr = urlStr;
	}

}
