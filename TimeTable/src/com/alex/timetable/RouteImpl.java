package com.alex.timetable;

public class RouteImpl implements Route
{

	private Integer id;
	private String name;
	private String creationWorkDate;
	private String creationWeekDate;
	
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

	@Override
	public String getCreationWorkDate() {
		return creationWorkDate;
	}

	@Override
	public String getCreationWeekDate() {
		return creationWeekDate;
	}

	@Override
	public void setCreationWorkDate(String _creationWorkDate) {
		creationWorkDate = _creationWorkDate;
	}

	@Override
	public void setCreationWeekDate(String _creationWeekDate) {
		creationWeekDate = _creationWeekDate;		
	}

}
