package com.alex.timetable.list;

public class UserStationsList
{
	private int Id;
	private String name;
	private int route;
	private int direction;
	
	public int getId()
	{
		return Id;
	}
	
	public void setId(int id)
	{
		Id = id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public int getRoute()
	{
		return route;
	}

	public void setRoute(int route)
	{
		this.route = route;
	}

	public int getDirection()
	{
		return direction;
	}

	public void setDirection(int direction)
	{
		this.direction = direction;
	}
	
}
