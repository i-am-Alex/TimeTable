package com.alex.timetable;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alex.timetable.db.DatabaseReader;
import com.alex.timetable.db.Route;
import com.alex.timetable.list.UserRouteItem;
import com.alex.timetable.list.UserRoutesAdapter;

public class SheduleUpdateListActivity extends Activity {

	private DatabaseReader dbReader;

	private ListView listView;
	
	private UserRoutesAdapter routesAdapter;
	
	@Override
	protected void onRestart() {
		super.onRestart();
		refreshList();
	}
	 
	public void refreshList() {
		if(dbReader == null) dbReader = new DatabaseReader(getBaseContext());
		
		ArrayList<UserRouteItem> list = new ArrayList<UserRouteItem>();
		ArrayList<Route> routeList = dbReader.getRoutesFromDB2(getBaseContext());
		for(Route route:routeList) {
			UserRouteItem item = new UserRouteItem(route.getId(), route.getName(), route.getUpdateLinkR(), route.getHalfCountStops(), route.getLastUpdatedDateR(), 1);
			list.add(item);
			item = new UserRouteItem(route.getId(), route.getName(), route.getUpdateLinkS(), route.getHalfCountStops(), route.getLastUpdatedDateS(), 0);
			list.add(item);
		}
		
        // создаем адаптер
        routesAdapter = new UserRoutesAdapter(SheduleUpdateListActivity.this, R.id.ListViewShedulesUpdate, list);
        
        // устанавливаем адаптер списку
        listView.setAdapter(routesAdapter);
		
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    	{
    		@SuppressLint("NewApi")
			@Override
    		public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
    			listView.setItemChecked(position, true);
    			UserRouteItem item = (UserRouteItem) parent.getItemAtPosition(position);
    			
        	    Intent intent = new Intent(SheduleUpdateListActivity.this, UpdateModuleActivity.class);
        	    intent.putExtra("name", item.getName());
        	    intent.putExtra("link", item.getUpdateLink());
        	    intent.putExtra("routeid", item.getRouteId());
        	    intent.putExtra("halfstopscount", item.getHalfCountStops());
        	    intent.putExtra("isworkdays", item.getIsWorkDays());

        	    startActivity(intent);
    		}
    	});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_shedule_list);

		listView = (ListView) findViewById(R.id.ListViewShedulesUpdate);

		refreshList();
		//dbReader = new DatabaseReader(getBaseContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.update_module, menu);
		return true;
	}

}
