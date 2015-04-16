package com.alex.timetable.list;

public class StationStopItem
{
	private int Id;
	private String name;
	private int state;
	private String timeStop;
	
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

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		this.state = state;
	}

	public String getTimeStop()
	{
		return timeStop;
	}

	public void setTimeStop(String timeStop)
	{
		this.timeStop = timeStop;
	}

}
