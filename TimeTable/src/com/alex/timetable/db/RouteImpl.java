package com.alex.timetable.db;


public class RouteImpl implements Route
{

	private Integer id;
	private String name;
	private String updateLinkR;
	private String updateLinkS;
	private String lastUpdatedDateR;
	private String lastUpdatedDateS;
	private int halfCountStops;
	
	@Override
	public Integer getId()
	{
		return id;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setId(Integer _id)
	{
		id = _id;
	}

	@Override
	public void setName(String _name)
	{
		name = _name;
	}

	public String getUpdateLinkR() {
		return updateLinkR;
	}

	public void setUpdateLinkR(String updateLinkR) {
		this.updateLinkR = updateLinkR;
	}

	public String getUpdateLinkS() {
		return updateLinkS;
	}

	public void setUpdateLinkS(String updateLinkS) {
		this.updateLinkS = updateLinkS;
	}

	public String getLastUpdatedDateR() {
		return lastUpdatedDateR;
	}

	public void setLastUpdatedDateR(String lastUpdatedDateR) {
		this.lastUpdatedDateR = lastUpdatedDateR;
	}

	public String getLastUpdatedDateS() {
		return lastUpdatedDateS;
	}

	public void setLastUpdatedDateS(String lastUpdatedDateS) {
		this.lastUpdatedDateS = lastUpdatedDateS;
	}

	public int getHalfCountStops() {
		return halfCountStops;
	}

	public void setHalfCountStops(int halfCountStops) {
		this.halfCountStops = halfCountStops;
	}

}

