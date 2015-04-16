package com.alex.timetable;

public interface RouteDirection
{

	int getRouteNum();
	int getDirection();
	String getName();

	void setRouteNum(int id);
	void setDirection(int direction);
	void setName(String name);
}
