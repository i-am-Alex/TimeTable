package com.alex.timetable.db;

public interface RouteStation 
{
	int getId();
	int getRoute();
	int getDirection();
	int getNumber();
	String getName();
	
	void setId(int _id);
	void setRoute(int _route);
	void setDirection(int _direction);
	void setNumber(int _number);
	void setName(String _name);
}
