package com.alex.timetable.routesparser;

import java.util.ArrayList;
import java.util.List;


public class EXTRoute {

	private List<EXTStop> stopList;
	private String routeNumber;
	private String lastUpdateDate;
	private int isWorkDays;
	private int idInDb;
	
	public List<EXTStop> getStopList() {
		if(stopList == null) stopList = new ArrayList<EXTStop>();
		return stopList;
	}
	
	public void setStopList(List<EXTStop> stopList) {
		this.stopList = stopList;
	}
	
	public String getRouteNumber() {
		return routeNumber;
	}
	
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}
	
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public int isWorkDays() {
		return isWorkDays;
	}
	
	public void setWorkDays(int isWorkDays) {
		this.isWorkDays = isWorkDays;
	}

	public void addStopIfTimeListNotNull(EXTStop stop) {
		if(stop != null && stop.getTimeList() != null) getStopList().add(stop);
	}

	public void setRouteId(int i) {
		idInDb = i;
	}
	
	public int getRouteId() {
		return idInDb;
	}
	
}
