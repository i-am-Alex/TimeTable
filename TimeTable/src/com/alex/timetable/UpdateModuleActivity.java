package com.alex.timetable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alex.timetable.db.DatabaseReader;
import com.alex.timetable.routesparser.EXTGivenParams;
import com.alex.timetable.routesparser.EXTRoute;
import com.alex.timetable.routesparser.EXTStop;
import com.alex.timetable.routesparser.EXTStopTime;
import com.alex.timetable.routesparser.HTMLParser;

public class UpdateModuleActivity extends Activity {

	private Button updateButton;
	
	private DatabaseReader dbReader;

	ProgressDialog progressDialog;
	
	private TextView textViewShed;
	private TextView textViewUpdOk;
	
	private boolean isDownloadSuccess;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_module);

		textViewShed = (TextView) findViewById(R.id.textViewUpdTextShed);
		textViewUpdOk = (TextView) findViewById(R.id.textViewUpdTextUpdOk);

		final EXTGivenParams params = new EXTGivenParams();
		Intent intent = getIntent();
		params.setUrlStr(intent.getStringExtra("link"));
		params.setRouteId(intent.getIntExtra("routeid", 0));
		params.setHalfStopsCount(intent.getIntExtra("halfstopscount", 0));
		params.setIsWorkDays(intent.getIntExtra("isworkdays", 0));
		
		textViewShed.setText("Для маршрута: " + getIntent().getStringExtra("name") + "(" + (params.getIsWorkDays()>0?"Рабочие":"Выходные") + " дни)");
		textViewUpdOk.setVisibility(View.INVISIBLE);
		isDownloadSuccess = false;
		
		updateButton = (Button) findViewById(R.id.buttUpdateShedule);
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkUpdateVersionFile(params);
			}
		});

		dbReader = new DatabaseReader(getBaseContext());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_module, menu);
		return true;
	}

	private void checkUpdateVersionFile(EXTGivenParams params) {
		progressDialog = new ProgressDialog(this);

		new AsyncTask<EXTGivenParams, Integer, Void>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progressDialog.setMessage("Загрузка обновления ...");
				progressDialog.setCancelable(true);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(EXTGivenParams... paramsArray) {
				HttpURLConnection urlConnection;
				InputStream is;
				EXTGivenParams params = paramsArray[0];
				try {
					URL url = new URL(params.getUrlStr());
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setDoOutput(false);
					int status = urlConnection.getResponseCode();
					if (status >= HttpStatus.SC_BAD_REQUEST)
						is = urlConnection.getErrorStream();
					else
						is = urlConnection.getInputStream();
					BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is, "windows-1251"), 4096);

					publishProgress(1, 100);
					String inputLine = "";
					StringBuilder websiteContent = new StringBuilder();
					while ((inputLine = bufferReader.readLine()) != null) {
						websiteContent.append(inputLine);
					}
					final String tmpStr = websiteContent.toString();
					bufferReader.close();
					urlConnection.disconnect();
					is.close();
					publishProgress(2, 100);

					HTMLParser parser = new HTMLParser();
					EXTRoute route = parser.parse(tmpStr, params.getRouteId(), "37", params.getHalfStopsCount(), (params.getIsWorkDays() > 0));
					
					publishProgress(3, 100);
					dbReader.deleteRouteByStr("delete from stationtimes where route=" + params.getRouteId() + " and isworkday=" + params.getIsWorkDays());
					publishProgress(4, 100);
					int i = 0;
					if (route != null) {
						SQLiteDatabase db = dbReader.getWritableDatabase();
						db.beginTransaction();

						for (EXTStop stop : route.getStopList()) {
							for (EXTStopTime stopTime : stop.getTimeList()) {
								if (stopTime.getPosNum() > 0 && !"".equals(stopTime.getStationTime().trim())) {
									ContentValues cv = new ContentValues();
									cv.put("route", route.getRouteId());
									cv.put("direction", stopTime.getFirstDirection());
									cv.put("station", stopTime.getPosNum());
									cv.put("isworkday", route.isWorkDays());
									cv.put("time", stopTime.getStationTime());
									db.insert("stationtimes", null, cv);
								}
							}
							publishProgress(i, route.getStopList().size());
							i = i + 1;
						}
						ContentValues data=new ContentValues();
						if(route.isWorkDays() > 0) {
							data.put("LASTUPDATEDR", route.getLastUpdateDate().trim());
						} else {
							data.put("LASTUPDATEDS", route.getLastUpdateDate().trim());
						}
						db.update("routes", data, "_ID=" + route.getRouteId(), null);
						db.setTransactionSuccessful();
						db.endTransaction();
						db.close();
					}
					isDownloadSuccess = true;
					return null;
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			// обновляем progressDialog
			protected void onProgressUpdate(Integer... values) {
				progressDialog.setProgress((int) ((values[0] / (float) values[1]) * 100));
			};

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				// закрываем прогресс
				progressDialog.hide();
				if(isDownloadSuccess) textViewUpdOk.setVisibility(View.VISIBLE);
			}
		}.execute(params);

	}

}
