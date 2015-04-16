package com.alex.timetable;

import java.util.ArrayList;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.timetable.db.DatabaseReader;
import com.alex.timetable.db.Route;

public class AboutActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		//DatabaseReader dbReader = new DatabaseReader(getBaseContext());

		//TextView textViewVersionDB = (TextView) findViewById(R.id.textViewVersionDB);
		//textViewVersionDB.setText("Версия БД: " + dbReader.getDbVersion());

		TextView textViewVersionProgram = (TextView) findViewById(R.id.textViewVersionProgram);
		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		textViewVersionProgram.setText("Версия приложения: " + versionName);
		
/*		ListView listViewCrD = (ListView) findViewById(R.id.listViewCreationDate);

		ArrayList<Route> routes = dbReader.getRoutesFromDB(getBaseContext());

		ArrayList<String> strList = new ArrayList<String>();
		for(Route route:routes)
		{
			strList.add("Маршрут " + route.getName());
			strList.add(" дата создания для рабочих дней: " + route.getCreationWorkDate());
			strList.add(" дата создания для выходных дней: " + route.getCreationWeekDate());
		}
        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strList);
        // устанавливаем адаптер списку
        listViewCrD.setAdapter(adapter);
*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.about, menu);
		return false;
	}

}
