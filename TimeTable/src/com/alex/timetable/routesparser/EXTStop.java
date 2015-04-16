package com.alex.timetable.routesparser;

import java.util.ArrayList;
import java.util.List;


public class EXTStop {

	private List<EXTStopTime> timeList;

	public List<EXTStopTime> getTimeList() {
		if(timeList == null) timeList = new ArrayList<EXTStopTime>();
		return timeList;
	}

	public void setTimeList(List<EXTStopTime> timeList) {
		this.timeList = timeList;
	}

	
	
}
