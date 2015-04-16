package com.alex.timetable.list;

public class UserRouteItem {
	
	private String name;
	private int routeId;
	private String updateLink;
	private int isWorkDays;
	private int halfCountStops;
	private String lastUpdatedDate;
	
	public UserRouteItem(Integer _id, String _name, String _updateLink, int _halfCountStops, String _lastUpdatedDate, int _workDays) {
		name = _name;
		routeId = _id;
		updateLink = _updateLink;
		halfCountStops = _halfCountStops;
		isWorkDays = _workDays;
		lastUpdatedDate = _lastUpdatedDate;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getRouteId() {
		return routeId;
	}
	
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	
	public String getUpdateLink() {
		return updateLink;
	}
	
	public void setUpdateLink(String updateLink) {
		this.updateLink = updateLink;
	}
	
	public int getIsWorkDays() {
		return isWorkDays;
	}
	
	public void setIsWorkDays(int isWorkDays) {
		this.isWorkDays = isWorkDays;
	}
	
	public int getHalfCountStops() {
		return halfCountStops;
	}
	
	public void setHalfCountStops(int halfCountStops) {
		this.halfCountStops = halfCountStops;
	}

	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

}
