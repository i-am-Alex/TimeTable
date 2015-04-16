package com.alex.timetable;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.alex.timetable.db.DatabaseReader;

public class UpdateModuleOldActivity extends Activity {

	private Button updateButton; 
	
	private final String updateVersionLink = "https://www.dropbox.com/s/l0850ww5vlfu9iw/version?dl=1";
	private final String updateVersionDb = "https://www.dropbox.com/s/s21ths6vdmp09mn/db.db?dl=1";

	private TextView textView;
	private TextView textViewNoNeVers;
	private Button buttonUpdateDB;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_module_old);
		
		textView = (TextView) findViewById(R.id.textViewUpdateAvaiable);
		textViewNoNeVers = (TextView) findViewById(R.id.textViewNoNewVersionDB);
		
		updateButton = (Button) findViewById(R.id.buttonCheckUpdate);
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				checkUpdateVersionFile(updateVersionLink);
			}
		});
		
		buttonUpdateDB = (Button) findViewById(R.id.buttonUpdateDB);
		buttonUpdateDB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				downloadUpdatedDBFile(updateVersionDb);
			}
		});
		
		buttonUpdateDB.setVisibility(View.INVISIBLE);
		textView.setVisibility(View.INVISIBLE);
		textViewNoNeVers.setVisibility(View.INVISIBLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_module, menu);
		return true;
	}

	private void checkUpdateVersionFile(String url) {
		final ProgressDialog progressDialog = new ProgressDialog(this);
		 
		new AsyncTask<String, Integer, File>() {
			@Override
			protected void onPreExecute() {
				progressDialog.setMessage("Проверка ...");
				progressDialog.setCancelable(true);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
			}
			 
			@Override
			protected File doInBackground(String... params) {
				URL url;
				HttpURLConnection urlConnection;
				InputStream is;
				try {
					url = new URL(params[0]);
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setDoOutput(false);
					int status = urlConnection.getResponseCode();
					if(status >= HttpStatus.SC_BAD_REQUEST)
					    is = urlConnection.getErrorStream();
					else
					    is = urlConnection.getInputStream();
					is = urlConnection.getInputStream();
					BufferedInputStream bis = new BufferedInputStream(is);

					final ByteArrayBuffer baf = new ByteArrayBuffer(10);
					int current = 0;
					while ((current = bis.read()) != -1) {
						baf.append((byte) current);
					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String tmpStr = new String(baf.buffer());
							tmpStr = tmpStr.trim();
							DatabaseReader dbReader = new DatabaseReader(getBaseContext());
							String ver = dbReader.getDbVersion();
							if(tmpStr != null && !"".equals(tmpStr) && !ver.equals(tmpStr)) {
								buttonUpdateDB.setVisibility(View.VISIBLE);
								textView.setVisibility(View.VISIBLE);
								textViewNoNeVers.setVisibility(View.INVISIBLE);
							} else {
								buttonUpdateDB.setVisibility(View.INVISIBLE);
								textView.setVisibility(View.INVISIBLE);
								textViewNoNeVers.setVisibility(View.VISIBLE);
							}
						}
					});

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
			protected void onPostExecute(File file) {
				// закрываем прогресс и удаляем временный файл
				progressDialog.hide();
				if(file != null && file.exists()) file.delete();
			}
		}.execute(url);
	}

	private void downloadUpdatedDBFile(String url) {
		final ProgressDialog progressDialog = new ProgressDialog(this);
		 
		new AsyncTask<String, Integer, File>() {
			@Override
			protected void onPreExecute() {
				progressDialog.setMessage("Загрузка обновления ...");
				progressDialog.setCancelable(true);
				progressDialog.setMax(100);
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
			}
			 
			@Override
			protected File doInBackground(String... params) {
				URL url;
				HttpURLConnection urlConnection;
				InputStream inputStream;
				int totalSize;
				int downloadedSize;
				byte[] buffer;
				int bufferLength;
				File file = null;

				try {
					url = new URL(params[0]);
					urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setDoOutput(false);
/*					int status = urlConnection.getResponseCode();
					if(status >= HttpStatus.SC_BAD_REQUEST)
					    is = urlConnection.getErrorStream();
					else
					    is = urlConnection.getInputStream();
*/
					DatabaseReader dbReader = new DatabaseReader(getBaseContext());
					dbReader.close();

					// Путь к уже созданной пустой базе в андроиде
					String outFileName = Globals.getDataBasePathAndName();
					// Теперь создадим поток для записи в эту БД побайтно
					OutputStream localDbStream = new FileOutputStream(outFileName);
					inputStream = urlConnection.getInputStream();
					totalSize = urlConnection.getContentLength();
					downloadedSize = 0;

					buffer = new byte[1024];
					bufferLength = 0;
			 
					// читаем со входа и пишем в выход, 
					// с каждой итерацией публикуем прогресс
					while ((bufferLength = inputStream.read(buffer)) > 0) {
						localDbStream.write(buffer, 0, bufferLength);
						downloadedSize += bufferLength;
						publishProgress(downloadedSize, totalSize);
					}
			 
					localDbStream.close();
					inputStream.close();
			 
					dbReader = new DatabaseReader(getBaseContext());
					
					return file;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
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
			protected void onPostExecute(File file) {
				// закрываем прогресс и удаляем временный файл
				progressDialog.hide();
				if(file != null && file.exists()) file.delete();
			}
		}.execute(url);
	}
	
}
