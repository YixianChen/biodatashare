package com.yixian.biodatashare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.facebook.biodatashare.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class DataSelectActivity extends Activity {
	ListView list;
	DataAdapter adapter;
	Button btnComfirm;
	ArrayList<HashMap<String, Object>> dataList = new ArrayList<HashMap<String, Object>>();
	String mUserName;
	Long timeFrom, timeTo;
	ArrayList<String> titleList = new ArrayList<String>();
	private static final String API_URL = "http://newaffectivehealth.mobilelifecentre.org/api.php";
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataselect);
		btnComfirm = (Button) findViewById(R.id.btnconfirmdata);
		context = getApplicationContext();
		Intent intent = getIntent();
		timeFrom = intent.getLongExtra("From", 0);
		timeTo = intent.getLongExtra("To", 0);

		DataCreator datas = new DataCreator();

		for (int i = 0; i < datas.title.size(); i++) {
			// creating new HashMap
			HashMap<String, Object> map = new HashMap<String, Object>();

			map.put("Title", datas.title.get(i));
			map.put("Description", datas.discription.get(i));
			map.put("Image", datas.image.get(i));

			// adding HashList to ArrayList
			dataList.add(map);
		}

		list = (ListView) findViewById(R.id.lvdata);
		adapter = new DataAdapter(this, dataList);
		list.setAdapter(adapter);
		btnComfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ArrayList<String> list = new ArrayList<String>();
				titleList.clear();
				for (int i = 0; i < dataList.size(); i++) {
					if (adapter.mChecked.get(i) == true) {
						String title = (String) dataList.get(i).get("Title");
						titleList.add(title);
					}

				}

				String content = "Are you make sure you want to download the data: ";
				for (String t : titleList) {
					content += t + " ";
				}

				// final String[] titles = list.toArray(new
				// String[list.size()]);
				if(titleList.isEmpty()){
					Toast.makeText(DataSelectActivity.this,
							"You have to select at least one type of data!", Toast.LENGTH_SHORT)
							.show();
					
				}
				else{
				new AlertDialog.Builder(DataSelectActivity.this)
						.setTitle("Download")
						.setMessage(content + "?")
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialoginterface,
											int i) {
										
										
										SharedPreferences pref = getSharedPreferences("UserName", 0);
										mUserName = pref.getString("USER_ID", "");
										
										Download download = new Download(DataSelectActivity.this, titleList, mUserName, timeFrom, timeTo, API_URL);
										
										download.execute();
										
										
									}

								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).show();
				}

			}

		});

	}

//	private class Download extends AsyncTask<String, Integer, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//			// TODO Auto-generated method stub
//			startDownload(titleList, params[0]);
//			return null;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			simpleWaitDialog = ProgressDialog.show(DataSelectActivity.this,
//					"Wait", "Downloading Data");
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			simpleWaitDialog.dismiss();
//			Toast.makeText(DataSelectActivity.this,
//					"Finish downloading", Toast.LENGTH_LONG)
//					.show();
//		}
//
//	}
//
//	private int startDownload(ArrayList<String> list, String URL) {
//
//		if (mDownloader != null) {
//			mDownloader.stop();
//			return 0;
//		}
//
//		SharedPreferences pref = getSharedPreferences("UserName", 0);
//		mUserName = pref.getString("USER_ID", "");
//
//		mDownloader = new DataDownloader(list, context,
//				mUserName, timeFrom, timeTo, URL);
//		mDownloader.start(new DataDownloader.Listener() {
//
//			@Override
//			public void downloaderStopped(String reason) {
//				mDownloader.stop();
//				mDownloader = null;
//				Log.d("TAG", "Data upload stopped because of : " + reason);
//
//			}
//
//			@Override
//			public void downloaderIssue(String msg) {
//				/*
//				 * if (mUIListener!=null) { output(msg); } else {
//				 * mNotification.setLatestEventInfo(HRVService.this,
//				 * "Uploading Issue", msg, mPendingIntent); }
//				 */
//			}
//		});
//		return 0;
//	}

}
