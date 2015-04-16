package com.alex.timetable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.alex.timetable.db.DatabaseReader;
import com.alex.timetable.db.Route;
import com.alex.timetable.db.RouteDirection;
import com.alex.timetable.db.RouteStation;
import com.alex.timetable.db.RouteStationStopItem;
import com.alex.timetable.list.StationStopItem;
import com.alex.timetable.list.StationStopsAdapter;
import com.alex.timetable.list.UserStationsAdapter;
import com.alex.timetable.list.UserStationsList;
import com.alex.toggleview.view.Toggle;
import com.alex.toggleview.view.Toggle.OnStateChangeListener;

public class MainActivity extends Activity
{
	enum MenuItems { About, Shedule, Options, Exit }
	
	private DatabaseReader dbReader = null;
	
	SettingsImpl settings = null;
			
	// список маршрутов
	protected ArrayList<Route> routes = new ArrayList<Route>();

	// направления маршрутов
	protected ArrayList<RouteDirection> routeDirections = new ArrayList<RouteDirection>();

	private RadioGroup radiogroup;
	private ListView listView1;
	private ListView listView2;
	
    Toggle toggle;
	ToggleButton toggleWorkDays;
	
	private boolean toggleWorkDaysChecked = false;
	
	private int activeRoute = 1;
	private int activeDirection = 0;
	private int defActiveRoute = 1;
	private int defActiveStations1 = 1;
	private int defActiveStations2 = 1;
	private boolean isReverseDirection = false;
	
    private UserStationsAdapter stationsAdapter;
    private ArrayList<UserStationsList> fetch = new ArrayList<UserStationsList>();

    // Вызывается в начале "активного" состояния.
    @Override
    public void onResume()
    {
        super.onResume();
        // Возобновите все приостановленные обновления UI,
        // потоки или процессы, которые были "заморожены",
        // когда данный объект был неактивным.

    	loadParameters();
    	
    	setToggleWorkDaysParams();

        // утром едем в город, вечером из города
        activeDirection = getActiveDirection();
        
        SetActiveRadioButton(activeRoute);
        
        SetActiveDirection(activeDirection);
    }

    private void SetActiveRadioButton(int activeRoute2)
    {
    	RadioButton butt = (RadioButton) radiogroup.findViewById(activeRoute);
    	if(butt != null)
    	{
    		butt.setChecked(true);
    	}
    	else
    	{
        	butt = (RadioButton) radiogroup.findViewById(1);
        	if(butt != null) butt.setChecked(true);
    	}    	
	}

    private void setToggleWorkDaysParams()
    {
        // утром едем в город, вечером из города
        Calendar current_Calendar = Calendar.getInstance();// Сегодняшняя дата

        current_Calendar.setFirstDayOfWeek(Calendar.MONDAY);
        if(current_Calendar.get(Calendar.DAY_OF_WEEK) > 1 && current_Calendar.get(Calendar.DAY_OF_WEEK) < 7) 
        {
        	toggleWorkDaysChecked = true;
        }
        toggleWorkDays.setChecked(toggleWorkDaysChecked);
    	
    }
    
	@Override
    protected void onRestart() 
    {
    	super.onRestart();
    	
    	loadParameters();
    	
    	setToggleWorkDaysParams();

        // утром едем в город, вечером из города
        activeDirection = getActiveDirection();
    }
    
	private void createAllComponents()
	{
        listView1 = (ListView) findViewById(R.id.ListView1);
        listView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Связываемся с ListView
        listView2 = (ListView) findViewById(R.id.ListView2);
        listView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
        toggleWorkDays = (ToggleButton)findViewById(R.id.WorkDaysChecker);
        toggleWorkDays.setOnCheckedChangeListener(new OnCheckedChangeListener()	{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				toggleWorkDaysChecked = isChecked;
				ReloadListStations(activeRoute, activeDirection);
			}
			
		});
        
        toggle = (Toggle)findViewById( R.id.on_off_toggle_view );
        toggle.setOnStateChangeListener( new OnStateChangeListener()
        {
			
			@Override
			public void stateChanged()
			{
				SetActiveDirection(toggle.isLeftOptionSelected()?0:1);
			}
		});
    
	}
	
	private void loadParameters()
	{
		if(settings == null) settings = new SettingsImpl(this);
		
		defActiveRoute = settings.getActiveRouteIncremented();
		defActiveStations1 =  settings.getActiveStations1();
		defActiveStations2 =  settings.getActiveStations2();
		activeRoute = defActiveRoute;
		isReverseDirection = settings.isReverseDirection();
	}

	public int getActiveDirection()
	{
        Calendar current_Calendar = Calendar.getInstance();
        int direction = (current_Calendar.get(Calendar.HOUR_OF_DAY) > 12)?0:1;
        // реверс направления
        if(isReverseDirection) direction = 1 - direction;
        return direction;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		String packageName = getBaseContext().getPackageName();
        Globals.setDatabasePath(packageName);
        
        loadParameters();
        
        dbReader = new DatabaseReader(getBaseContext());

        routes = dbReader.getRoutesFromDB(getBaseContext());

        // если версия БД устарела. для себя напоминалка
        //if(!dbReader.isDbVersionActual()) {
        ///	TextView text = (TextView) findViewById(R.id.label1);
        //	text.setText("Wrong DB Version");
        //}

        createAllComponents();
        
        // утром едем в город, вечером из города
        activeDirection = getActiveDirection();
        setToggleWorkDaysParams();

        CreateRadioButtons();
    }

	@Override
    public void onStop()
    {
    	super.onStop();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	menu.add(0, MenuItems.About.ordinal(), 0, "О программе");
    	menu.add(0, MenuItems.Shedule.ordinal(), 1, "Маршруты");
    	menu.add(0, MenuItems.Options.ordinal(), 2, "Настройки");
    	menu.add(0, MenuItems.Exit.ordinal(), 3, "Выход");
   	      
    	return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
    	int intemId = item.getItemId();
    	
    	// Покажем закладку о программе
    	if(intemId == MenuItems.About.ordinal()) {
    	    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
    	    startActivity(intent);
        }
 
    	// Покажем закладку с расписаниями
    	if(intemId == MenuItems.Shedule.ordinal()) {
    	    Intent intent = new Intent(MainActivity.this, SheduleUpdateListActivity.class);
    	    startActivity(intent);
        }
    	
        // Покажем закладку настроек
    	if(intemId == MenuItems.Options.ordinal()) {
    	    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
    	    startActivity(intent);
        }

        // Выход из программы
    	if(intemId == MenuItems.Exit.ordinal()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    	
        return super.onOptionsItemSelected(item);
    }
    
    private void SetActiveDirection(int direction)
    {
    	activeDirection = direction;
    	
    	toggle.setLeftOptionSelected(activeDirection == 0);
    	
		ReloadListStations(activeRoute, activeDirection);
    }
    
    private void CreateRadioButtons()
    {
    	radiogroup = (RadioGroup)findViewById(R.id.RadioGroup1); 
        
    	for(Route route:routes)
    	{
    		RadioButton newRadioButton = new RadioButton(getBaseContext()); 
    		newRadioButton.setId(route.getId());
    		newRadioButton.setText(route.getName());
    		newRadioButton.setTextColor(0xff000000);
    		radiogroup.addView(newRadioButton);
    	}

    	// обработка переключения состояния переключателя
    	radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
    	{
    		@Override
    		public void onCheckedChanged(RadioGroup arg0, int id) {
    			activeRoute = id;
    			ReloadRouteDirections(id);
            }});

    }
    
    private void ReloadRouteDirections(int id)
    {
    	routeDirections = dbReader.getRouteDirectionsFromDB(getBaseContext(), id);
    	for(RouteDirection rec:routeDirections) {
    		switch (rec.getDirection()) {
    			case 0:	toggle.setLeftText(rec.getName()); break;
    			case 1: toggle.setRightText(rec.getName()); break;
    		}
    	}
    	
    	ReloadListStations(activeRoute, activeDirection);
    }
    
    private void ReloadListStations(int route, int direction)
    {
        // может в будущем пригодиться еще полные классы
        List<RouteStation> routeStations = dbReader.getStationsFromDB(getBaseContext(), route, direction);
        
        fetch.clear();
        
        for(RouteStation rec:routeStations) 
        {
        	UserStationsList item = new UserStationsList();
        	item.setId(rec.getId());
        	item.setName(rec.getName());
        	item.setRoute(route);
        	item.setDirection(direction);
        	fetch.add(item);
        }
        
        // создаем адаптер
        stationsAdapter = new UserStationsAdapter(MainActivity.this, R.id.ListView1, fetch);
 
        // устанавливаем адаптер списку
        listView1.setAdapter(stationsAdapter);
        
        // номер станции по умолчанию
        int numStation = 0;
        
        if(activeRoute == defActiveRoute)
        {
        	if(direction == 0) numStation = defActiveStations1; else numStation = defActiveStations2;
        }

        if(fetch.size() > numStation) listView1.setItemChecked(numStation, true);
        
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        	{
        		@SuppressLint("NewApi")
				@Override
        		public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
        		{
        			listView1.setItemChecked(position, true);
        			UserStationsList item = (UserStationsList) parent.getItemAtPosition(position);
        			OnStationsListClick(item.getId(), item.getDirection(), item.getRoute());
        		}
        	});
        
        if (routeStations != null && routeStations.size() > numStation)
        {
        	RouteStation item = (RouteStation) routeStations.get(numStation);
        	OnStationsListClick(item.getId(), item.getDirection(), item.getRoute());
        	
        }
        else OnStationsListClick(0, 0, 0);
    }


	protected void OnStationsListClick(long stationId, int direction, int route)
	{
        int workDays = toggleWorkDays.isChecked()?1:0;
		// может в будущем пригодиться еще полные классы
        List<RouteStationStopItem> routeStationStops = dbReader.getStationStopsFromDB(getBaseContext(), route, direction, stationId, workDays);
        
        ArrayList<StationStopItem> lst = new ArrayList<StationStopItem>();
        
        String nowTime = String.format("%1$tH.%1$tM", Calendar.getInstance());
        int count = 0;
        int activeIndex = 0;
        
        Calendar c = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        
        boolean added = false;
        for(RouteStationStopItem rec:routeStationStops) {
        	String timeStop = " ";
        	int hour = 0;
        	int minute = 0;
        	String[] time = rec.getStopTime().split("\\.");
        	try {
        		hour = Integer.valueOf(time[0]);
        		minute = Integer.valueOf(time[1]);
        	}
        	catch (Exception e) {
        		
        	}
        	c2.set(Calendar.HOUR_OF_DAY, hour);
        	c2.set(Calendar.MINUTE, minute);
        	
        	long milis = (c2.get(Calendar.HOUR_OF_DAY)*60 + c2.get(Calendar.MINUTE)) - (c.get(Calendar.HOUR_OF_DAY)*60 + c.get(Calendar.MINUTE));
        	
        	if(milis < 60 && milis > -30) timeStop = String.valueOf(milis) + " мин.";
        		
        	StationStopItem item = new StationStopItem();
			item.setName(rec.getStopTime());
			item.setTimeStop(timeStop);
			item.setId(count);
        	if(nowTime.compareTo(rec.getStopTime()) <= 0 && !added)
        	{
				item.setState(1);
    			added = true;
    			activeIndex = count;
        	}
    		lst.add(item);
        	count = count + 1;
        }
 
        // создаем адаптер
        StationStopsAdapter stationsStopsAdapter = new StationStopsAdapter(MainActivity.this, R.id.ListView2, lst);
 
        listView2.setAdapter(stationsStopsAdapter);
        
        listView2.setSelection((activeIndex>0)?activeIndex - 1:activeIndex);
        //listView2.smoothScrollToPosition(10);
		
	}
}
