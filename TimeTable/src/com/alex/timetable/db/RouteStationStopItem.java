package com.alex.timetable.db;

public class RouteStationStopItem
{
	private int Id;
	private String stopTime;

	
	public int getId()
	{
		return Id;
	}

	public void setId(int id)
	{
		Id = id;
	}

	public String getStopTime()
	{
		return stopTime;
	}

	public void setStopTime(String stopTime)
	{
		this.stopTime = stopTime;
	}
	
}
