package com.alex.timetable;

public interface Route 
{
	Integer getId();
	String getName();
	void setId(Integer _id);
	void setName(String _name);
	String getCreationWorkDate();
	String getCreationWeekDate();
	void setCreationWorkDate(String _creationWorkDate);
	void setCreationWeekDate(String _creationWeekDate);
}
