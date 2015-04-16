package com.alex.timetable;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.timetable.db.DatabaseReader;
import com.alex.timetable.db.Route;
import com.alex.timetable.db.RouteDirection;
import com.alex.timetable.db.RouteStation;

public class SettingsActivity extends Activity
{
	SettingsImpl settings = null;
	
	DatabaseReader dbReader = null;

	private TextView textViewRoute;
	private TextView textViewStations1;
	private TextView textViewStations2;
	private CheckBox checkViewReverseDirection;
	
	// список маршрутов
	protected ArrayList<Route> routes = new ArrayList<Route>();
	protected List<RouteStation> routeStations1 = new ArrayList<RouteStation>();
	protected List<RouteStation> routeStations2 = new ArrayList<RouteStation>();
	
	// направления маршрутов
	protected ArrayList<RouteDirection> routeDirections = new ArrayList<RouteDirection>();

	final int ROUTE_ITEMS = 1;
	final int STATIONS1_ITEMS = 2;
	final int STATIONS2_ITEMS = 3;

	private void reloadStations(Context context)
	{
        routeStations1 = dbReader.getStationsFromDB(context, settings.getActiveRouteIncremented(), 0);
        routeStations2 = dbReader.getStationsFromDB(context, settings.getActiveRouteIncremented(), 1);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		textViewRoute = (TextView) findViewById(R.id.textViewRoute);
		textViewStations1 = (TextView) findViewById(R.id.textViewStations1);
		textViewStations2 = (TextView) findViewById(R.id.textViewStations2);
		
		checkViewReverseDirection = (CheckBox) findViewById(R.id.SettingsCheckBox1);
		checkViewReverseDirection.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1)
			{
				settings.setIsReverseDirection(arg1);
			}
		}
		);
		
		dbReader = new DatabaseReader(getBaseContext());

		routes = dbReader.getRoutesFromDB(getBaseContext());
		
		if(settings == null) settings = new SettingsImpl(this);
		reloadStations(getBaseContext());
	
		setRouteText(settings.getActiveRouteIncremented());
		setStations1Text(settings.getActiveStations1Incremented());
		setStations2Text(settings.getActiveStations2Incremented());
		checkViewReverseDirection.setChecked(settings.isReverseDirection());
	}

	private void setRouteText(int activeRoute)
	{
		String routeText = "";
		for(Route rec:routes)
		{
			if(rec.getId().intValue() == activeRoute)
			{
				routeText = rec.getName();
				break;
			}
		}
		textViewRoute.setText("№ " + routeText);
	}

	protected Dialog onCreateDialog(int id)
	{
		List<String> str = new ArrayList<String>();
		AlertDialog.Builder adb = new AlertDialog.Builder(this);
		switch (id)
		{
			// массив
			case ROUTE_ITEMS:
				for(Route rec:routes) str.add(rec.getName());
				ArrayAdapter<String> routeAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, str);
				adb.setTitle(R.string.settingsRouteItems);
				adb.setSingleChoiceItems(routeAdapter, settings.getActiveRoute(), myClickListenerRoute);
			    adb.setPositiveButton(R.string.ok, myClickListenerRoute);
			    break;
			// адаптер
		    case STATIONS1_ITEMS:
		    	adb.setTitle(R.string.settingsDirectionItems);
		        for(RouteStation rec:routeStations1) str.add(rec.getName());
				ArrayAdapter<String> stations1Adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, str);
				adb.setSingleChoiceItems(stations1Adapter, settings.getActiveStations1(), myClickListenerStations1);
			    adb.setPositiveButton(R.string.ok, myClickListenerStations1);
			    break;
		    // курсор
		    case STATIONS2_ITEMS:
		    	adb.setTitle(R.string.settingsDirectionReverseItems);
		        for(RouteStation rec:routeStations2) str.add(rec.getName());
				ArrayAdapter<String> stations2Adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, str);
				adb.setSingleChoiceItems(stations2Adapter, settings.getActiveStations2(), myClickListenerStations2);
			    adb.setPositiveButton(R.string.ok, myClickListenerStations2);
			    break;
		    }

		    return adb.create();
		  }

	// обработчик нажатия на пункт списка диалога или кнопку
	OnClickListener myClickListenerRoute = new OnClickListener()
	{
		@SuppressWarnings("deprecation")
		public void onClick(DialogInterface dialog, int which)
		{
			ListView lv = ((AlertDialog) dialog).getListView();
			if (which == Dialog.BUTTON_POSITIVE)
			{
				settings.setActiveRoute(lv.getCheckedItemPosition());
				setRouteText(settings.getActiveRouteIncremented());
				reloadStations(getBaseContext());
				setStations1Text(settings.getActiveStations1Incremented());
				setStations2Text(settings.getActiveStations2Incremented());
				removeDialog(STATIONS1_ITEMS);
				removeDialog(STATIONS2_ITEMS);
			}
	    }
	};

	// обработчик нажатия на пункт списка диалога или кнопку
	OnClickListener myClickListenerStations1 = new OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			ListView lv = ((AlertDialog) dialog).getListView();
			if (which == Dialog.BUTTON_POSITIVE)
			{
				settings.setActiveStations1(lv.getCheckedItemPosition());
				setStations1Text(settings.getActiveStations1Incremented());
			}
	    }
	};
	
	// обработчик нажатия на пункт списка диалога или кнопку
	OnClickListener myClickListenerStations2 = new OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			ListView lv = ((AlertDialog) dialog).getListView();
			if (which == Dialog.BUTTON_POSITIVE)
			{
				settings.setActiveStations2(lv.getCheckedItemPosition());
				setStations2Text(settings.getActiveStations2Incremented());
			}
	    }
	};

	
	@SuppressWarnings("deprecation")
	public void onclick(View v)
	{
		switch (v.getId())
		{
			case R.id.buttonRoutes:
				showDialog(ROUTE_ITEMS);
				break;
			case R.id.buttonStations1:
				showDialog(STATIONS1_ITEMS);
				break;
			case R.id.buttonStations2:
				showDialog(STATIONS2_ITEMS);
				break;
			case R.id.buttonGoCheckUpdates: {
	    	    	Intent intent = new Intent(SettingsActivity.this, UpdateModuleOldActivity.class);
	    	    	startActivity(intent);
	    	    }
	    	    break;
			default:
				break;
		}
	}
	  
	protected void setStations1Text(int checkedItemPosition)
	{
		String stationText = "";
        for(RouteStation rec:routeStations1)
		{
			if(rec.getNumber() == checkedItemPosition)
			{
				stationText = rec.getName();
				break;
			}
		}
		textViewStations1.setText(stationText);
	}

	protected void setStations2Text(int checkedItemPosition)
	{
		String stationText = "";
        for(RouteStation rec:routeStations2)
		{
			if(rec.getNumber() == checkedItemPosition)
			{
				stationText = rec.getName();
				break;
			}
		}
		textViewStations2.setText(stationText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.settings, menu);
		return false;
	}

}
