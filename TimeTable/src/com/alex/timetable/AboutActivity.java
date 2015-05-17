package com.alex.timetable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class AboutActivity extends Activity {

	private String linkStr = "<a href=\"https://www.dropbox.com/s/ghql18zygnp2ij6/TimeTable.apk?dl=1\">Ссылка на скачивание приложения</a>";
	
	private String app_version_link = "https://www.dropbox.com/s/mqloolkao47y3lo/app_version?dl=1";

	final static String DEBUG_TAG = "+++ImageDownloader+++";
	final int PROGRESS_DLG_ID = 786;
	
	private String versionName = "";
	
	private TextView textNewVersAvaliable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		TextView textViewVersionProgram = (TextView) findViewById(R.id.textViewVersionProgram);
		
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		textViewVersionProgram.setText("Версия приложения: " + versionName);
		
		TextView textLinkStr = (TextView) findViewById(R.id.textViewLinkStr);
		textLinkStr.setText(Html.fromHtml(linkStr));
		textLinkStr.setMovementMethod(LinkMovementMethod.getInstance());
		
		textNewVersAvaliable = (TextView) findViewById(R.id.textViewAboutNewVersAvaliable);
		
		new DownloadImageTask().execute(app_version_link);
		
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.about, menu);
		return false;
	}
	
    class DownloadImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //publishProgress(new Void[]{});
            
            String url = "";
            if( params.length > 0 ) url = params[0];		    	
           
            InputStream input = null;
            
            try {
            	URL urlConn = new URL(url);
                input = urlConn.openStream();
            }
            catch (MalformedURLException e) {
            	Log.d(DEBUG_TAG,"Oops, Something wrong with URL...");
            	e.printStackTrace();
            }
            catch (IOException e) {
            	Log.d(DEBUG_TAG,"Oops, Something wrong with inpur stream...");
                e.printStackTrace();
            }
            
            StringBuffer vers = new StringBuffer();
            int i;
			try {
				i = input.read();
				while(i > 0) {
					vers.append(Character.toChars(i));
					i = input.read();
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return vers.toString().trim();
        }
        
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //showDialog(PROGRESS_DLG_ID);
        }
        
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //dismissDialog(PROGRESS_DLG_ID);
            //image.setImageBitmap(result);
            if(versionName != null && !versionName.equals(result)) {
            	textNewVersAvaliable.setText("Доступна новая версия (" + result + ").");
            	textNewVersAvaliable.setTextColor(0xff0000ff);
            } else {
            	textNewVersAvaliable.setText("Установлена последняя версия приложения.");
            }
        }
    }

}
