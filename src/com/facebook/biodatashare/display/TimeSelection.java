package com.facebook.biodatashare.display;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.achartengine.GraphicalView;

import com.facebook.biodatashare.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeSelection extends Activity {


	private Button btnConfirm, btnDateChanger, btnTimeChanger;
	private TextView tvDateDisplay, tvTimeDisPlay;
	private Spinner spinner_X;
	private MultiSelectionSpinner spinner_Y;
	private List<String> list ;
	private String Time_string;
	private int days_current, month_current, year_current, hours_current,
			minutes_current;
	private HashMap<String, Integer> timeMap = new HashMap<String, Integer>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.timedataselection);
		
		
		String[] array = { "acc", "gsr"};

		spinner_Y = (MultiSelectionSpinner)findViewById(R.id.mySpinner1);

		spinner_Y.setItems(array);

		spinner_X = (Spinner) findViewById(R.id.mySpinner2);

		ArrayAdapter<String> spinnerYAxizArrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_dropdown_item,
				getResources().getStringArray(R.array.spinner_datalist));

		spinner_X.setAdapter(spinnerYAxizArrayAdapter);

		Calendar c = Calendar.getInstance();
		days_current = c.get(Calendar.DAY_OF_MONTH);
		month_current = c.get(Calendar.MONTH);
		year_current = c.get(Calendar.YEAR);

		hours_current = c.get(Calendar.HOUR_OF_DAY);
		minutes_current = c.get(Calendar.MINUTE);

		tvDateDisplay = (TextView)findViewById(R.id.tv_dateselection);
		tvDateDisplay.setText(year_current + "-" + String.valueOf(month_current+1) + "-"
				+ days_current);

		tvTimeDisPlay = (TextView) findViewById(R.id.tv_timeselection);
		tvTimeDisPlay.setText(hours_current + ":" + minutes_current);
		
		timeMap.put("year", year_current);
		timeMap.put("month", month_current);
		timeMap.put("day", days_current);
		timeMap.put("hour", hours_current);
		timeMap.put("min", minutes_current);

		btnDateChanger = (Button) findViewById(R.id.btnchartdatechager);
		
		

		btnDateChanger.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s  = tvDateDisplay.getText().toString();
				String[] date =s.split("-");
				DatePickerDialog picker = new DatePickerDialog(TimeSelection.this,
						new OnDateSetListener() {
					
							@Override
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tvDateDisplay.setText(year + "-" + String.valueOf(monthOfYear+1)
										+ "-" + dayOfMonth);
								timeMap.remove("year");
								timeMap.remove("month");
								timeMap.remove("day");
								timeMap.put("year", year);
								timeMap.put("month", monthOfYear);
								timeMap.put("day", dayOfMonth);
								

							}
						}, Integer.parseInt(date[0]), Integer.parseInt(date[1])-1, Integer.parseInt(date[2]));
				picker.show();
			}

		});

		btnTimeChanger = (Button) findViewById(R.id.btncharttimechager);

		btnTimeChanger.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String s  = tvTimeDisPlay.getText().toString();
				String[] time =s.split(":");
				TimePickerDialog pickerDialog = new TimePickerDialog(
						TimeSelection.this, new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								tvTimeDisPlay.setText(hourOfDay + ":" + minute);
								timeMap.remove("hour");
								timeMap.remove("min");
								timeMap.put("hour", hourOfDay);
								timeMap.put("min", minute);

							}
						}, Integer.parseInt(time[0]), Integer.parseInt(time[1]), true);
				pickerDialog.show();

			}
		});

		btnConfirm = (Button) findViewById(R.id.btnchartcomfirm);

		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (spinner_Y.getSelectedStrings().size() == 0
						|| spinner_Y.getSelectedItem() == null) {
					Toast.makeText(TimeSelection.this,
							"you have to select at least one data!!!",
							Toast.LENGTH_LONG).show();
				} else {
					String string = spinner_Y.getSelectedItemsAsString();

					Time_string = spinner_X.getSelectedItem().toString();

					Toast.makeText(TimeSelection.this, string, Toast.LENGTH_LONG)
							.show();

					SharedPreferences pref = TimeSelection.this
							.getSharedPreferences("UserName", 0);
					String mUserName = pref.getString("USER_ID", "");

					long timeFrom = convertToMilliseconds(timeMap);
					
					
				     Intent intent = new Intent(TimeSelection.this, DisplayActivity.class);
				     
				        Bundle b = new Bundle();
				        b.putStringArrayList("DataNameList", new ArrayList<String>(spinner_Y.getSelectedStrings())); 
				        b.putLong("TimeFrom", timeFrom);
				        b.putString("TimeString", Time_string);
				        b.putString("UserName", mUserName);
				        intent.putExtra("android.intent.extra.rssItem",b);
				        
				        startActivity(intent);
					
				}
			}
		});
		
		
		
		
		
	}
	
	private long convertToMilliseconds(HashMap<String, Integer> time) {

		if (time == null) {

			Toast.makeText(
					TimeSelection.this,
					"you have to select one valid start time for displaying the exist data",
					Toast.LENGTH_LONG).show();

			return 0L;
		} else {
			int year = time.get("year");
			int month = time.get("month");
			int day = time.get("day");
			int hour = time.get("hour");
			int mins = time.get("min");
			
			System.out.println("year:"+ year + "month: "+ month + "day: "+ day + "hour: " +hour+"min: "+ mins);
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month, day, hour, mins);
			
			
			long milliseconds = calendar.getTimeInMillis();
			
			System.out.println(milliseconds);
			

			return milliseconds;
		}
	}


}
