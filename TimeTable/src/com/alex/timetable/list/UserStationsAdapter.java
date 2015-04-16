package com.alex.timetable.list;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.timetable.R;

public class UserStationsAdapter extends ArrayAdapter<UserStationsList> { // ������������ �� ������ ������ UserFB

	private final Activity activity;
	//
	private final ArrayList<UserStationsList> entries;

        // ����������� ������, ��������� ����������, ������� � ������ ������
	public UserStationsAdapter(final Activity a, final int textViewResourceId, final ArrayList<UserStationsList> entries)
	{
		super(a, textViewResourceId, entries);
		this.entries = entries;
		activity = a;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{

		View v = convertView;
		ViewHolder holder;
		if (v == null)
		{
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.stationslist, parent, false);
			holder = new ViewHolder();
			// �������������� ���� ��������
			holder.textView = (TextView) v.findViewById(R.id.TextView1);
			v.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) v.getTag();
		}
		UserStationsList userFB = entries.get(position);
		if (userFB != null)
		{
			// �������� ����� �� �������
			holder.textView.setText(" " + userFB.getName());
		}
		
		final ListView lv = (ListView) parent;
		if(position == lv.getCheckedItemPosition())
		{
            // ���� ���������� ��������
			//holder.textView.setBackgroundColor(0xFF0000FF);
			holder.textView.setBackgroundResource(R.drawable.list_selector_active);
			holder.textView.setTextColor(0xFFFFFFFF);
		}
		else
		{
			// ������ ��������, ��� �������� ������ state_pressed
			//holder.textView.setBackgroundResource(R.color.white);
			holder.textView.setBackgroundResource(R.drawable.list_selector_inactive);
			holder.textView.setTextColor(0xFF000000);
		}
		return v;
	}

	// ��� �������� ������� � ��������� �����
	private static class ViewHolder
	{
		public TextView textView;
	}

}
