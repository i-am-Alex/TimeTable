package com.alex.timetable.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**  ласс создающий, удал€ющий и редактирующий базу */
public class DatabaseOpenHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "db.db";
	private static final int DATABASE_VERSION = 1;
	private static final String DEBUG_TAG = DatabaseOpenHelper.class.getSimpleName();
	private static final boolean LOGV = false;

	public DatabaseOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * ”даление всех таблиц из базы
	 * 
	 * @param db
	 *            - object of SQLiteDatabase
	 */
	public void dropTables(SQLiteDatabase db)
	{
		if (LOGV) Log.d(DEBUG_TAG, "onDropTables called");
		//db.execSQL("DROP TABLE IF EXISTS " + Names.TABLE_NAME);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		if (LOGV) Log.v(DEBUG_TAG, "onCreate()");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		Log.d(DEBUG_TAG, "onUpgrade called");
	}
}
