package com.yixian.biodatashare;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.facebook.biodatashare.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimeSelectionActivity extends Activity {
	
	private EditText showDate_From;
	private Button pickDate_From;
	private EditText showTime_From;
	private Button pickTime_From;
	private EditText showDate_To;
	private Button pickDate_To;
	private EditText showTime_To;
	private Button pickTime_To;
	private TextView tv;
	private Button btnConfrim, btnDisplay;
	StringBuilder data_from, data_to, time_from, time_to;
	GregorianCalendar gregCal_From, gregCal_To;

	private static final int SHOW_DATAPICK_FROM = 0;
	private static final int DATE_DIALOG_ID_FROM = 1;
	private static final int SHOW_TIMEPICK_FROM = 2;
	private static final int TIME_DIALOG_ID_FROM = 3;
	private static final int SHOW_DATAPICK_TO = 4;
	private static final int DATE_DIALOG_ID_TO = 5;
	private static final int SHOW_TIMEPICK_TO = 6;
	private static final int TIME_DIALOG_ID_TO = 7;

	private int mYear_FROM, mYear_TO;
	private int mMonth_FROM, mMonth_TO;
	private int mDay_FROM, mDay_TO;
	private int mHour_FROM, mHour_TO;
	private int mMinute_FROM, mMinute_TO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeselection);

		initViews();

		setDateTime();
		setTimeOfDay();
	}

	private void initViews() {
		showDate_From = (EditText) findViewById(R.id.etdatefrom);
		pickDate_From = (Button) findViewById(R.id.pickdatefrom);
		showTime_From = (EditText) findViewById(R.id.ettimefrom);
		pickTime_From = (Button) findViewById(R.id.picktimefrom);
		showDate_To = (EditText) findViewById(R.id.etdateto);
		pickDate_To = (Button) findViewById(R.id.pickdateto);
		showTime_To = (EditText) findViewById(R.id.ettimeto);
		pickTime_To = (Button) findViewById(R.id.picktimeto);
		btnDisplay = (Button) findViewById(R.id.displaytime);
		btnConfrim = (Button) findViewById(R.id.confrimtime);
		tv = (TextView) findViewById(R.id.tvtime);

		pickDate_From.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                
				Message msg = new Message();
				if (pickDate_From.equals((Button) v)) {

					msg.what = TimeSelectionActivity.SHOW_DATAPICK_FROM;
				}
				TimeSelectionActivity.this.dateandtimeHandler.sendMessage(msg);
			}

		});

		pickTime_From.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
                
				Message msg = new Message();
				if (pickTime_From.equals((Button) v)) {
					msg.what = TimeSelectionActivity.SHOW_TIMEPICK_FROM;
				}
				TimeSelectionActivity.this.dateandtimeHandler.sendMessage(msg);

			}
		});

		pickDate_To.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Message msg = new Message();
				if (pickDate_To.equals((Button) v)) {
					msg.what = TimeSelectionActivity.SHOW_DATAPICK_TO;
				}
				TimeSelectionActivity.this.dateandtimeHandler.sendMessage(msg);
			}
		});

		pickTime_To.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Message msg = new Message();
				if (pickTime_To.equals((Button) v)) {
					msg.what = TimeSelectionActivity.SHOW_TIMEPICK_TO;
				}
				TimeSelectionActivity.this.dateandtimeHandler.sendMessage(msg);
			}

		});
		btnDisplay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String s = Represent();
				tv.setText(s);

			}

			private String Represent() {
				// TODO Auto-generated method stub
				String s = "Do you want to download the data From " + data_from
						+ " " + time_from + " to " + data_to + " " + time_to
						+ "?";
				return s;
			}
		});
		btnConfrim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (endBiggerThanStart()) {
					long unixTime_From = gregCal_From.getTimeInMillis()/1000;
					long unixTime_To = gregCal_To.getTimeInMillis()/1000;
					
					System.out.println(unixTime_From+" sssssssss "+unixTime_To);
					
					Intent intent = new Intent(TimeSelectionActivity.this,
							DataSelectActivity.class);
					Bundle bl = new Bundle();
					bl.putLong("From", unixTime_From);
					bl.putLong("To", unixTime_To);
					intent.putExtras(bl);
					// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				} else
					Toast.makeText(TimeSelectionActivity.this,
							"End time should bigger than start time!!!!!",
							Toast.LENGTH_LONG).show();
			}
		});

	}

	protected boolean endBiggerThanStart() {
		// TODO Auto-generated method stub
		gregCal_From = new GregorianCalendar(mYear_FROM,
				mMonth_FROM, mDay_FROM, mHour_FROM, mMinute_FROM,0);

		gregCal_To = new GregorianCalendar(mYear_TO,
				mMonth_TO, mDay_TO, mHour_TO, mMinute_TO,0);
		if (gregCal_To.compareTo(gregCal_From) > 0) {
			return true;
		}
		return false;
	}

	private void setDateTime() {
		final Calendar c = Calendar.getInstance();

		mYear_FROM = c.get(Calendar.YEAR);
		mMonth_FROM = c.get(Calendar.MONTH);
		mDay_FROM = c.get(Calendar.DAY_OF_MONTH);

		mYear_TO = c.get(Calendar.YEAR);
		mMonth_TO = c.get(Calendar.MONTH);
		mDay_TO = c.get(Calendar.DAY_OF_MONTH);

		updateDateDisplay1();
		updateDateDisplay2();
	}

	private void setTimeOfDay() {
		final Calendar c = Calendar.getInstance();
		mHour_FROM = c.get(Calendar.HOUR_OF_DAY);
		mMinute_FROM = c.get(Calendar.MINUTE);
		mHour_TO = c.get(Calendar.HOUR_OF_DAY);
		mMinute_TO = c.get(Calendar.MINUTE);

		updateTimeDisplay1();
		updateTimeDisplay2();
	}

	private void updateDateDisplay2() {
		// TODO Auto-generated method stub

		data_to = new StringBuilder()
				.append(mYear_TO)
				.append("-")
				.append((mMonth_TO + 1) < 10 ? "0" + (mMonth_TO + 1)
						: (mMonth_TO + 1)).append("-")
				.append((mDay_TO < 10) ? "0" + mDay_TO : mDay_TO);
		showDate_To.setText(data_to);
	}

	private void updateDateDisplay1() {
		data_from = new StringBuilder()
				.append(mYear_FROM)
				.append("-")
				.append((mMonth_FROM + 1) < 10 ? "0" + (mMonth_FROM + 1)
						: (mMonth_FROM + 1)).append("-")
				.append((mDay_FROM < 10) ? "0" + mDay_FROM : mDay_FROM);
		showDate_From.setText(data_from);

	}

	private void updateTimeDisplay2() {
		// TODO Auto-generated method stub
		time_to = new StringBuilder().append(mHour_TO).append(":")
				.append((mMinute_TO < 10) ? "0" + mMinute_TO : mMinute_TO);
		showTime_To.setText(time_to);
	}

	private void updateTimeDisplay1() {
		// TODO Auto-generated method stub
		time_from = new StringBuilder()
				.append(mHour_FROM)
				.append(":")
				.append((mMinute_FROM < 10) ? "0" + mMinute_FROM : mMinute_FROM);
		showTime_From.setText(time_from);

	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour_FROM = hourOfDay;
			mMinute_FROM = minute;

			updateTimeDisplay1();
		}
	};
	private TimePickerDialog.OnTimeSetListener mTimeSetListener2 = new TimePickerDialog.OnTimeSetListener() {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour_TO = hourOfDay;
			mMinute_TO = minute;

			updateTimeDisplay2();
		}
	};

	private DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear_FROM = year;
			mMonth_FROM = monthOfYear;
			mDay_FROM = dayOfMonth;

			updateDateDisplay1();
		}
	};
	private DatePickerDialog.OnDateSetListener mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear_TO = year;
			mMonth_TO = monthOfYear;
			mDay_TO = dayOfMonth;

			updateDateDisplay2();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID_FROM:
			return new DatePickerDialog(this, mDateSetListener1, mYear_FROM,
					mMonth_FROM, mDay_FROM);
		case TIME_DIALOG_ID_FROM:
			return new TimePickerDialog(this, mTimeSetListener1, mHour_FROM,
					mMinute_FROM, true);
		case DATE_DIALOG_ID_TO:
			return new DatePickerDialog(this, mDateSetListener2, mYear_TO,
					mMonth_TO, mDay_TO);
		case TIME_DIALOG_ID_TO:
			return new TimePickerDialog(this, mTimeSetListener2, mHour_TO,
					mMinute_TO, true);
		}

		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID_FROM:
			((DatePickerDialog) dialog).updateDate(mYear_FROM, mMonth_FROM,
					mDay_FROM);
			break;
		case DATE_DIALOG_ID_TO:
			((DatePickerDialog) dialog)
					.updateDate(mYear_TO, mMonth_TO, mDay_TO);
			break;
		case TIME_DIALOG_ID_FROM:
			((TimePickerDialog) dialog).updateTime(mHour_FROM, mMinute_FROM);
			break;
		case TIME_DIALOG_ID_TO:
			((TimePickerDialog) dialog).updateTime(mHour_TO, mMinute_TO);
			break;
		}
	}

	Handler dateandtimeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TimeSelectionActivity.SHOW_DATAPICK_FROM:
				showDialog(DATE_DIALOG_ID_FROM);
				break;
			case TimeSelectionActivity.SHOW_TIMEPICK_FROM:
				showDialog(TIME_DIALOG_ID_FROM);
				break;
			case TimeSelectionActivity.SHOW_DATAPICK_TO:
				showDialog(DATE_DIALOG_ID_TO);
				break;
			case TimeSelectionActivity.SHOW_TIMEPICK_TO:
				showDialog(TIME_DIALOG_ID_TO);
				break;
			}
		}

	};

}
