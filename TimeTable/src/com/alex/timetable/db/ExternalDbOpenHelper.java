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
	// ������ ����� ��, �������� ����� � �� � ���� version_db
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
		// �������� ������ ���� � ����� ��� ������ ����������
		openDataBase();
	}

	// ������� ����, ���� ��� �� �������
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

	// �������� ������������� ���� ������
	private boolean checkDataBase()
	{
		SQLiteDatabase checkDb = null;
		Cursor cursor = null;
		try {
			checkDb = SQLiteDatabase.openDatabase(Globals.getDataBasePathAndName(), null, SQLiteDatabase.OPEN_READONLY);
			
			// �������� ������ ��
            String[] columnsToTake = { "VERSION_DB" };
			cursor = checkDb.query("dbversion1", columnsToTake, null, null, null, null, null);
            cursor.moveToFirst();
			int loadedVersionDB = cursor.getInt(cursor.getColumnIndexOrThrow("VERSION_DBN"));
			// ���� ������ �� � �������� ������, ��� ��������� - �������������� �
			if(needVersionDB > loadedVersionDB) return false;
            return (checkDb != null);// && (needVersionDB.equals(loadedVersionDB));
		} catch (SQLException e) {
			Log.e(this.getClass().toString(), "Error while checking db");
		}
		finally
		{
			// ������� �� ����� ������ ��������, ��� ������ �����������
			if(cursor != null) cursor.close();
			if (checkDb != null) checkDb.close();
		}
		return false;
	}

	// ����� ����������� ����
	private void copyDataBase() throws IOException
	{
		// ��������� ����� ��� ������ �� ��� ��������� ���� ��
		// �������� � assets
		InputStream externalDbStream = context.getAssets().open(Globals.getDataBaseName());

		// ���� � ��� ��������� ������ ���� � ��������
		String outFileName = Globals.getDataBasePathAndName();

		// ������ �������� ����� ��� ������ � ��� �� ��������
		OutputStream localDbStream = new FileOutputStream(outFileName);

		// ����������, �����������
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = externalDbStream.read(buffer)) > 0)
		{
			localDbStream.write(buffer, 0, bytesRead);
		}
		// �� ����� �������� ���������� (���������) � ������� ������
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
