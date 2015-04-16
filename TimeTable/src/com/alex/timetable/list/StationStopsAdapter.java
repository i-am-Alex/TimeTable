package com.alex.timetable.list;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alex.timetable.R;

public class StationStopsAdapter extends ArrayAdapter<StationStopItem> { // унаследовали от нашего класса UserFB

	private final Activity activity;
	//
	private final ArrayList<StationStopItem> entries;

        // конструктор класса, принимает активность, листвью и массив данных
	public StationStopsAdapter(final Activity a, final int textViewResourceId, final ArrayList<StationStopItem> entries)
	{
		super(a, textViewResourceId, entries);
		this.entries = entries;
		activity = a;
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{

		View v = convertView;
		ViewHolder holder;
		ViewHolder holderTime;
		List<ViewHolder> lst = null;
		if (v == null)
		{
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.stationsstopslist, parent, false);
			holder = new ViewHolder();
			holderTime = new ViewHolder();
			// инициализируем нашу разметку
			holder.textView = (TextView) v.findViewById(R.id.TextView1);
			holderTime.textView = (TextView) v.findViewById(R.id.TextView2);
			lst = new ArrayList<ViewHolder>();
			lst.add(holder);
			lst.add(holderTime);
			v.setTag(lst);
		}
		else
		{
			lst = (List<ViewHolder>) v.getTag();
			holder = (ViewHolder) lst.get(0);
			holderTime = (ViewHolder) lst.get(1);
		}
		StationStopItem userFB = entries.get(position);
		if (userFB != null)
		{
			// получаем текст из массива
			holder.textView.setText(userFB.getName());
			// а здесь нам надо расчитать время
			holderTime.textView.setText(userFB.getTimeStop());

		}
		
		if(userFB.getState() == 1)
		{
            // цвет выбранного элемента
			holder.textView.setBackgroundResource(R.drawable.list_selector_active);
			holder.textView.setTextColor(0xFFFFFFFF);
			holderTime.textView.setBackgroundResource(R.drawable.list_selector_active);
		}
		else
		{
			holder.textView.setBackgroundResource(R.drawable.list_selector_inactive);
			holderTime.textView.setBackgroundResource(R.drawable.list_selector_inactive);
			holder.textView.setTextColor(0xFF000000);
		}
		return v;
	}

	// для быстроты вынесли в отдельный класс
	private static class ViewHolder
	{
		public TextView textView;
	}

}
