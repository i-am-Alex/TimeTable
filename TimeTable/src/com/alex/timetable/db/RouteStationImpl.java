package com.alex.timetable.db;

public class RouteStationImpl implements RouteStation
{

	private int id;
	private int route;
	private int direction;
	private int number;
	private String name;
	
	@Override
	public int getRoute()
	{
		return route;
	}

	@Override
	public int getDirection()
	{
		return direction;
	}

	@Override
	public int getNumber()
	{
		return number;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setRoute(int _route)
	{
		route = _route;
	}

	@Override
	public void setDirection(int _direction)
	{
		direction = _direction;
	}

	@Override
	public void setNumber(int _number)
	{
		number = _number;
	}

	@Override
	public void setName(String _name) 
	{
		name = _name;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public void setId(int _id)
	{
		id = _id;
	}

}
