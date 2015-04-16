package com.alex.timetable.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alex.timetable.routesparser.EXTRoute;
import com.alex.timetable.routesparser.EXTStop;
import com.alex.timetable.routesparser.EXTStopTime;

public class DatabaseReader
{

	private static ExternalDbOpenHelper dbOpenHelper = null;
	private static SQLiteDatabase sqliteDB = null;
	
	public DatabaseReader(Context context)
	{
		if(dbOpenHelper == null || dbOpenHelper.getDb() == null || !dbOpenHelper.getDb().isOpen()) dbOpenHelper = new ExternalDbOpenHelper(context, "db.db");
		if(sqliteDB == null || !sqliteDB.isOpen()) sqliteDB = dbOpenHelper.openDataBase();
	}

//	public boolean isDbVersionActual() {
//		return dbOpenHelper.getDBModuleVersion().equals(getDbVersion());
//	}
	
	public void close() {
		sqliteDB.close();
		dbOpenHelper.close();
	}
	
	public void open() {
		sqliteDB = dbOpenHelper.openDataBase();
	}
	
	/**
	 * Краткий список маршрутов (без доп информации)
	 * @param context
	 * @return
	 */
	public ArrayList<Route> getRoutesFromDB(Context context)
	{
		ArrayList<Route> list = new ArrayList<Route>();
		try
		{
            String[] columnsToTake = { "_ID, NAME" };
            Cursor cursor = sqliteDB.query("routes", columnsToTake, null, null, null, null, null);

            boolean c = cursor.moveToFirst();

            while (c)
            {
            	Route oneRow = new RouteImpl();
            	oneRow.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_ID")));
            	oneRow.setName(cursor.getString(cursor.getColumnIndexOrThrow("NAME")));
            	list.add(oneRow);
            	c = cursor.moveToNext();
            }
            cursor.close();
		} catch (Exception e) {
			Log.e("TAG", "Failed to select Names.", e);
		}

		return list;
	}

	/**
	 * Берем расширеную информацию о маршрутах (записывается также дата последнего обновления маршрута и линки для обновления) 
	 * @param context
	 * @return
	 */
	public ArrayList<Route> getRoutesFromDB2(Context context) {
		ArrayList<Route> list = new ArrayList<Route>();
		try {
            String[] columnsToTake = { "_ID, NAME, UPDATELINKR, UPDATELINKS, LASTUPDATEDR, LASTUPDATEDS, HALFCOUNTSTOPS" };
            Cursor cursor = sqliteDB.query("routes", columnsToTake, null, null, null, null, null);
            boolean c = cursor.moveToFirst();
            
            int colId = cursor.getColumnIndexOrThrow("_ID");
            int colName = cursor.getColumnIndexOrThrow("NAME");
            int colLinkR = cursor.getColumnIndexOrThrow("UPDATELINKR");
            int colLinkS = cursor.getColumnIndexOrThrow("UPDATELINKS");
            int colUpdDateR = cursor.getColumnIndexOrThrow("LASTUPDATEDR");
            int colUpdDateS = cursor.getColumnIndexOrThrow("LASTUPDATEDS");
            int colHCS = cursor.getColumnIndexOrThrow("HALFCOUNTSTOPS");
            
            while (c) {
            	Route oneRow = new RouteImpl();
            	oneRow.setId(cursor.getInt(colId));
            	oneRow.setName(cursor.getString(colName));
            	oneRow.setUpdateLinkR(cursor.getString(colLinkR));
            	oneRow.setUpdateLinkS(cursor.getString(colLinkS));
            	oneRow.setLastUpdatedDateR(cursor.getString(colUpdDateR));
            	oneRow.setLastUpdatedDateS(cursor.getString(colUpdDateS));
            	oneRow.setHalfCountStops(cursor.getInt(colHCS));
            	list.add(oneRow);
            	c = cursor.moveToNext();
            }
            cursor.close();
		} catch (Exception e) {
			Log.e("TAG", "Failed to select Names.", e);
		}

		return list;
	}
	
	public ArrayList<RouteDirection> getRouteDirectionsFromDB(Context baseContext, int id)
	{
		ArrayList<RouteDirection> list = new ArrayList<RouteDirection>();
		try
		{
            String[] columnsToTake = { "_ID, routeid, direction, name" };
            Cursor cursor = sqliteDB.query("routedirection", columnsToTake, "routeid=" + String.valueOf(id), null, null, null, "_ID");

            boolean c = cursor.moveToFirst();

            while (c)
            {
            	RouteDirection oneRow = new RouteDirectionImpl();
            	oneRow.setRouteNum(cursor.getInt(cursor.getColumnIndexOrThrow("routeid")));
            	oneRow.setDirection(cursor.getInt(cursor.getColumnIndexOrThrow("direction")));
            	oneRow.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            	list.add(oneRow);
            	c = cursor.moveToNext();
            }
            cursor.close();
		} catch (Exception e) {
			Log.e("TAG", "Failed to select Names.", e);
		}
		return list;
	}

	public List<RouteStation> getStationsFromDB(Context baseContext, int route, int direction)
	{
		List<RouteStation> list = new ArrayList<RouteStation>();

		try
		{
            String[] columnsToTake = { "_ID, route, direction, number, name" };
            Cursor cursor = sqliteDB.query("stations", columnsToTake,
            		  "route=" + String.valueOf(route) + " and direction=" + String.valueOf(direction),
            		  null, null, null, "number");

            boolean c = cursor.moveToFirst(); 

            while (c)
            {
            	RouteStation oneRow = new RouteStationImpl();
            	oneRow.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_ID")));
            	oneRow.setRoute(cursor.getInt(cursor.getColumnIndexOrThrow("route")));
            	oneRow.setDirection(cursor.getInt(cursor.getColumnIndexOrThrow("direction")));
            	oneRow.setNumber(cursor.getInt(cursor.getColumnIndexOrThrow("number")));
            	oneRow.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            	list.add(oneRow);
            	c = cursor.moveToNext();
            }
            cursor.close();
		} catch (Exception e) {
			Log.e("TAG", "Failed to select Names.", e);
		}

		return list;
	}

	public List<RouteStationStopItem> getStationStopsFromDB(Context baseContext, int route, int direction, long stationId, int workDays)
	{
		List<RouteStationStopItem> list = new ArrayList<RouteStationStopItem>();

		try
		{
            String[] columnsToTake = { "_ID, time, route, direction, isworkday, station" };
            Cursor cursor = sqliteDB.query("stationtimes", columnsToTake,
            		  "route=" + String.valueOf(route) + " and direction=" + String.valueOf(direction) +
            		  " and isworkday=" + String.valueOf(workDays)+ " and station=" + String.valueOf(stationId),
            		  null, null, null, "time");

            boolean c = cursor.moveToFirst(); 

            int col1 = cursor.getColumnIndexOrThrow("_ID");
            int col2 = cursor.getColumnIndexOrThrow("time");
            		
            while (c) {
            	RouteStationStopItem oneRow = new RouteStationStopItem();
            	oneRow.setId(cursor.getInt(col1));
            	oneRow.setStopTime(cursor.getString(col2));
            	list.add(oneRow);
            	c = cursor.moveToNext();
            }
            cursor.close();
		} catch (Exception e) {
			Log.e("TAG", "Failed to select Names.", e);
		}

		return list;
	}

	public String getDbVersion()
	{
		Cursor cursor = null;
		try 
		{
			// проверка версии БД
			String[] columnsToTake = { "VERSION" };
			cursor = sqliteDB.query("dbversion", columnsToTake, null, null, null, null, null);
			cursor.moveToFirst();
			return cursor.getString(cursor.getColumnIndexOrThrow("VERSION"));
		} catch (SQLException e) {
			Log.e(this.getClass().toString(), "Error while checking db");
		}
		finally {
			// Андроид не любит утечки ресурсов, все должно закрываться
			if(cursor != null) cursor.close();
		}
		return "";
	}

	public void deleteRouteByStr(String deleteStr) {
		sqliteDB.execSQL(deleteStr);
	}

	public void executeSQLStr(String sqlStr) {
		sqliteDB.execSQL(sqlStr);
	}

	public SQLiteDatabase getWritableDatabase() {
		return dbOpenHelper.getWritableDatabase();
	}

}
