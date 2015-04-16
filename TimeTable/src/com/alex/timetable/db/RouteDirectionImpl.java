package com.alex.timetable.db;

public class RouteDirectionImpl implements RouteDirection
{

	private int num;
	private int direction;
	private String name;
	
	@Override
	public int getRouteNum()
	{
		return num;
	}

	@Override
	public int getDirection()
	{
		return direction;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public void setRouteNum(int num)
	{
		this.num = num;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
}
