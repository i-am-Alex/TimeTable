package com.alex.timetable.db;

public interface Route 
{
	Integer getId();
	
	String getName();
	
	void setId(Integer _id);
	
	void setName(String _name);
	
	String getUpdateLinkR();
	
	String getUpdateLinkS();
	
	void setUpdateLinkR(String updateLinkR);
	
	void setUpdateLinkS(String updateLinkS);

	String getLastUpdatedDateR();
	
	String getLastUpdatedDateS();
	
	void setLastUpdatedDateR(String _date);
	
	void setLastUpdatedDateS(String _date);
	
	int getHalfCountStops();

	void setHalfCountStops(int _int);
	
}
