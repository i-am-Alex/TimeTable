package com.alex.timetable;


public class Globals {

	// Путь к папке с базами на устройстве
	private static String DB_PATH;
	// Имя файла с базой
	private static String DB_NAME = "db.db";
	
    public static void setDatabasePath(String _path) {
		DB_PATH = String.format("//data//data//%s//databases//", _path);
    }

	public static String getDataBasePathAndName() {
		return DB_PATH + DB_NAME;
	}
	
	public static String getDataBasePath() {
		return DB_PATH;
	}

	public static String getDataBaseName() {
		return DB_NAME;
	}

}
