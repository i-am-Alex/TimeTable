package com.alex.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsImpl
{
	// это будет именем файла настроек
	public static final String APP_PREFERENCES = "mysettings";
	public static final String APP_PREFERENCES_ACTIVEROUTE = "activeRoute";
	public static final String APP_PREFERENCES_ACTIVESTATIONSDIRECTION1 = "activeStationsDirection1";
	public static final String APP_PREFERENCES_ACTIVESTATIONSDIRECTION2 = "activeStationsDirection2";
	private static final String APP_PREFERENCES_ISREVERSEDIRECTION = "reversedirection";
	
	static SharedPreferences mSettings;

	public SettingsImpl(Activity activity)
	{
        if(mSettings == null) mSettings = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
	}
	
	public int getActiveRoute()
	{
		if (mSettings.contains(APP_PREFERENCES_ACTIVEROUTE))
		{
			// Получаем число из настроек
			return mSettings.getInt(APP_PREFERENCES_ACTIVEROUTE, 0);
		}

		return 0;
	}

	public int getActiveRouteIncremented()
	{
		return getActiveRoute() + 1;
	}

	public void setActiveRoute(int checkedItemPosition)
	{
		if(checkedItemPosition < 0) checkedItemPosition = 0;
		Editor editor = mSettings.edit();
		editor.putInt(APP_PREFERENCES_ACTIVEROUTE, checkedItemPosition);
		editor.commit();
	}

	public int getActiveStations1()
	{
		if (mSettings.contains(APP_PREFERENCES_ACTIVESTATIONSDIRECTION1))
		{
			// Получаем число из настроек
			return mSettings.getInt(APP_PREFERENCES_ACTIVESTATIONSDIRECTION1, 0);
		}

		return 0;
	}

	public int getActiveStations2()
	{
		if (mSettings.contains(APP_PREFERENCES_ACTIVESTATIONSDIRECTION2))
		{
			// Получаем число из настроек
			return mSettings.getInt(APP_PREFERENCES_ACTIVESTATIONSDIRECTION2, 0);
		}

		return 0;
	}

	public void setActiveStations1(int checkedItemPosition)
	{
		if(checkedItemPosition < 0) checkedItemPosition = 0;
		Editor editor = mSettings.edit();
		editor.putInt(APP_PREFERENCES_ACTIVESTATIONSDIRECTION1, checkedItemPosition);
		editor.commit();
	}
	
	public void setActiveStations2(int checkedItemPosition)
	{
		if(checkedItemPosition < 0) checkedItemPosition = 0;
		Editor editor = mSettings.edit();
		editor.putInt(APP_PREFERENCES_ACTIVESTATIONSDIRECTION2, checkedItemPosition);
		editor.commit();
	}

	public int getActiveStations1Incremented()
	{
		return getActiveStations1() + 1;
	}

	public int getActiveStations2Incremented()
	{
		return getActiveStations2() + 1;
	}

	public boolean isReverseDirection()
	{
		if (mSettings.contains(APP_PREFERENCES_ISREVERSEDIRECTION))
		{
			// Получаем число из настроек
			return mSettings.getBoolean(APP_PREFERENCES_ISREVERSEDIRECTION, false);
		}
		return false;
	}

	public void setIsReverseDirection(boolean bool)
	{
		Editor editor = mSettings.edit();
		editor.putBoolean(APP_PREFERENCES_ISREVERSEDIRECTION, bool);
		editor.commit();
	}
}
