package com.alex.timetable.db;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alex.timetable.Globals;

public class ExternalDbOpenHelper extends SQLiteOpenHelper
{
	// Версия самой БД, записано также в БД в поле version_db
	private final int needVersionDB = 1;
	
	public SQLiteDatabase database;
	public final Context context;

	public SQLiteDatabase getDb()
	{
		return database;
	}

	public ExternalDbOpenHelper(Context context, String databaseName)
	{
		super(context, databaseName, null, 1);
		this.context = context;
		// Составим полный путь к базам для вашего приложения
		openDataBase();
	}

	// Создаст базу, если она не создана
	public void createDataBase()
	{
		boolean dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.e(this.getClass().toString(), "Copying error");
				throw new Error("Error copying database!");
			}
		} else {
			Log.i(this.getClass().toString(), "Database already exists");
		}
	}

	// Проверка существования базы данных
	private boolean checkDataBase()
	{
		SQLiteDatabase checkDb = null;
		Cursor cursor = null;
		try {
			checkDb = SQLiteDatabase.openDatabase(Globals.getDataBasePathAndName(), null, SQLiteDatabase.OPEN_READONLY);
			
			// проверка версии БД
            String[] columnsToTake = { "VERSION_DB" };
			cursor = checkDb.query("dbversion1", columnsToTake, null, null, null, null, null);
            cursor.moveToFirst();
			int loadedVersionDB = cursor.getInt(cursor.getColumnIndexOrThrow("VERSION_DBN"));
			// если версия БД в телефоне меньше, чем требуемая - перезаписываем её
			if(needVersionDB > loadedVersionDB) return false;
            return (checkDb != null);// && (needVersionDB.equals(loadedVersionDB));
		} catch (SQLException e) {
			Log.e(this.getClass().toString(), "Error while checking db");
		}
		finally
		{
			// Андроид не любит утечки ресурсов, все должно закрываться
			if(cursor != null) cursor.close();
			if (checkDb != null) checkDb.close();
		}
		return false;
	}

	// Метод копирования базы
	private void copyDataBase() throws IOException
	{
		// Открываем поток для чтения из уже созданной нами БД
		// источник в assets
		InputStream externalDbStream = context.getAssets().open(Globals.getDataBaseName());

		// Путь к уже созданной пустой базе в андроиде
		String outFileName = Globals.getDataBasePathAndName();

		// Теперь создадим поток для записи в эту БД побайтно
		OutputStream localDbStream = new FileOutputStream(outFileName);

		// Собственно, копирование
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = externalDbStream.read(buffer)) > 0)
		{
			localDbStream.write(buffer, 0, bytesRead);
		}
		// Мы будем хорошими мальчиками (девочками) и закроем потоки
		localDbStream.close();
		externalDbStream.close();
	}

	public SQLiteDatabase openDataBase() throws SQLException {
		String path = Globals.getDataBasePathAndName();
		if (database == null) {
			createDataBase();
			database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		}
		return database;
	}

	@Override
	public synchronized void close() {
		if (database != null) {
			database.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}

//	public Object getDBModuleVersion() {
//		return needVersionDB;
//	}
}
