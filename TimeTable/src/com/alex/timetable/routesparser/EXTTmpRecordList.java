package com.alex.timetable.routesparser;

import java.util.ArrayList;
import java.util.List;

public class EXTTmpRecordList {
	
	private List<EXTTmpRecord> items;

	public List<EXTTmpRecord> getItems() {
		if(items == null) items = new ArrayList<EXTTmpRecord>();
		return items;
	}

	public void setItems(List<EXTTmpRecord> items) {
		this.items = items;
	}

}
