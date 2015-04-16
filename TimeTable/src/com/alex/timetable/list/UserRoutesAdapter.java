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

public class UserRoutesAdapter extends ArrayAdapter<UserRouteItem> {
	
	private final Activity activity;
	//
	private final ArrayList<UserRouteItem> entries;

	public UserRoutesAdapter(final Activity a, final int textViewResourceId, final ArrayList<UserRouteItem> _entries) {
		super(a, textViewResourceId, _entries);
		this.entries = _entries;
		activity = a;
	}
	
	@SuppressWarnings("unchecked")
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		ViewHolder holderDate;
		List<ViewHolder> lst = null;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) activity .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.routesheduleitem, parent, false);
			holder = new ViewHolder();
			holderDate = new ViewHolder();
			// инициализируем нашу разметку
			holder.textView = (TextView) v.findViewById(R.id.TextView1);
			holderDate.textView = (TextView) v.findViewById(R.id.TextView2);
			lst = new ArrayList<ViewHolder>();
			lst.add(holder);
			lst.add(holderDate);
			v.setTag(lst);
		} else {
			lst = (List<ViewHolder>) v.getTag();
			holder = (ViewHolder) lst.get(0);
			holderDate = (ViewHolder) lst.get(1);
		}
		UserRouteItem userFB = entries.get(position);
		if (userFB != null) {
			// получаем текст из массива
			holder.textView.setText(" Маршрут: " + userFB.getName() + " (" + (userFB.getIsWorkDays()>0?"Рабочие":"Выходные") + ")");
			// а здесь нам надо расчитать время
			holderDate.textView.setText("Последнее обновление: " + userFB.getLastUpdatedDate());
		}
		return v;
	}

	// для быстроты вынесли в отдельный класс
	class ViewHolder {
		public TextView textView;
	}

}
