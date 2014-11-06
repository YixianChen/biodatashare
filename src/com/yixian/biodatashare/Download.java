package com.yixian.biodatashare;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.facebook.biodatashare.display.DisplayActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.widget.Toast;

public class Download extends AsyncTask<String, Integer, String> {

	private String userID;
	private long timeFrom, timeTo;
	private Context context;
	private ProgressDialog simpleWaitDialog;
	private ArrayList<String> titles;
	private static final String DATADIR = "/Android/data/biodata/files/";
	private String API_URL;
	private boolean externalStorageAvailable = false;
	private boolean externalStorageWriteable = false;
	private static final String TABLE_AFFECTIVE = "affectivehealth";
	private static final String TABLE_TAGYOURPLACE = "tagyourplace";
	private int progressBarStatus = 0;
	int status_todownload = 0;
	int status_progress = 0;
	private boolean mRun = true;
	private String message = "";

	public Download(Context context, ArrayList<String> titles, String userName,
			Long timeFrom, Long timeTo, String URL) {
		// TODO Auto-generated constructor stub

		this.context = context;
		this.titles = titles;
		this.userID = userName;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.API_URL = URL;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		int n;
		String JsonObj = null;
		try {
			JsonObj = requestCount(titles);

			JSONObject jsonObj = new JSONObject(JsonObj);

			n = jsonObj.getInt("count_data");

			progressBarStatus = 0;

			status_todownload = n;

			Log.d("total number ", Integer.toString(n));
			// Toast.makeText(ctx, "total number:" + Integer.toString(n) ,
			// Toast.LENGTH_LONG).show();
			int i = 0;
			String jsonString = "";
			while (i < n) {
				jsonString = download(titles);
				// String jsonString_GSR = download("GSR");
				writeIntoFile(jsonString);
				// parseData(jsonString);
				if (jsonString == "[]" || jsonString == "[[]]")
					message = onTaskCompleted("empty");

				System.out.println(jsonString);
				if (jsonString.length() > 0) {
					i += jsonString.length();
					status_progress = i;

					// System.out.println(titles.toString());
					int condition = judgeCondition();

					switch (parseData(jsonString, condition)) {
					case 1:
						System.out
								.println("data was stored into database succesfully!!!");
						message = onTaskCompleted("successful");
						break;
					case 2:
						System.out
								.println("The data cannot be stored into database!!");
						message = onTaskCompleted("fail");
					case 3:
						System.out
								.println("data has been existed in the phone!!!");
						message = onTaskCompleted("exist");
					}

				} else
					break;
			}
			mRun = false;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = onTaskCompleted("error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = onTaskCompleted("error");
		}

		return null;
	}

	@Override
	protected void onPreExecute() {
		simpleWaitDialog = ProgressDialog.show(context, "Wait",
				"Downloading Data");
	}

	@Override
	protected void onPostExecute(String result) {
		simpleWaitDialog.dismiss();
		
		Toast.makeText(context, "Finish downloading", Toast.LENGTH_LONG).show();
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		Intent intent = new Intent(context,
				DisplayActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); 
		context.startActivity(intent);
		
	}

	private int judgeCondition() {
		// TODO Auto-generated method stub

		if (this.titles.contains("gsr") == false
				&& this.titles.contains("acc") == false)
			return 1;
		if (this.titles.contains("longitude") == false
				&& this.titles.contains("latitude") == false
				&& this.titles.contains("tag") == false)
			return 2;
		else {
			return 3;
		}

	}

	private int parseData(String jsonString, int condition) {
		// TODO Auto-generated method stub

		JsonReader reader = new JsonReader(new StringReader(jsonString));
		try {

			MyDataBaseHelper dbHelper = null;
			SQLiteDatabase db = null;
			switch (condition) {
			case 1:
				reader.beginArray();
				dbHelper = new MyDataBaseHelper(context, userID);
				db = dbHelper.getWritableDatabase();
				while (reader.hasNext()) {
					reader.beginObject();
					Long time = -1L;
					double latitude = 0;
					double longitude = 0;
					String tag = "";
					while (reader.hasNext()) {
						String name = reader.nextName();

						if (name.equals("time")) {
							time = reader.nextLong();

							time *= 1000;

						} else if (name.equals("latitude")) {
							latitude = reader.nextDouble();

						} else if (name.equals("longitude")) {
							longitude = reader.nextDouble();
						} else if (name.equals("tag")) {
							tag = reader.nextString();
						} else
							reader.skipValue();
					}

					reader.endObject();

					ContentValues cv_tag = new ContentValues();
					cv_tag.put("time", time);
					cv_tag.put("latitude", latitude);
					cv_tag.put("longitude", longitude);
					cv_tag.put("tag", tag);

					// db.insert(TABLE_TAGYOURPLACE, null, cv_tag);
					long nameID = db.insertWithOnConflict(TABLE_TAGYOURPLACE,
							null, cv_tag, SQLiteDatabase.CONFLICT_IGNORE);

				}
				reader.endArray();
				db.close();
				return 1;
				

			case 2:
				reader.beginArray();
				dbHelper = new MyDataBaseHelper(context, userID);
				db = dbHelper.getWritableDatabase();
				int count = 0;
				while (reader.hasNext()) {
					reader.beginObject();
					Long time = 0L;
					double gsr = 0;
					double acc = 0;

					System.out.println(reader.toString());

					while (reader.hasNext()) {
						String name = reader.nextName();

						if (name.equals("time")) {
							time = reader.nextLong();

						} else if (name.equals("acc")
								&& reader.peek() != JsonToken.NULL) {

							acc = reader.nextDouble();

						} else if (name.equals("gsr")
								&& reader.peek() != JsonToken.NULL) {
							gsr = reader.nextDouble();

						} else
							reader.skipValue();
					}

					reader.endObject();
					count++;
					System.out.println(count);
					ContentValues cv_aff = new ContentValues();
					cv_aff.put("time", time);
					cv_aff.put("acc", acc);
					cv_aff.put("gsr", gsr);

					// db.insert(TABLE_AFFECTIVE, null, cv_aff);
					long nameID =db.insertWithOnConflict(TABLE_AFFECTIVE, null, cv_aff,
							SQLiteDatabase.CONFLICT_IGNORE);

				}
				reader.endArray();
				db.close();
				return 1;
			case 3:
				reader.beginArray();
				while (reader.hasNext()) {
					reader.beginArray();
					dbHelper = new MyDataBaseHelper(context, userID);
					db = dbHelper.getWritableDatabase();
					while (reader.hasNext()) {
						reader.beginObject();
						Long timeMill = 0L;
						double latitude = 0;
						double longitude = 0;
						String tag = "";
						double acc = 0;
						double gsr = 0;
						while (reader.hasNext()) {
							String name = reader.nextName();

							if (name.equals("time")) {
								String time = reader.nextString();
								if (time.length() == 10) {
									timeMill = Long.parseLong(time);
									timeMill *= 1000;
								} else
									timeMill = Long.parseLong(time);

							} else if (name.equals("latitude")) {
								latitude = reader.nextDouble();

							} else if (name.equals("longitude")) {
								longitude = reader.nextDouble();
							} else if (name.equals("tag")) {
								tag = reader.nextString();
							} else if (name.equals("acc")
									&& reader.peek() != JsonToken.NULL) {
								acc = reader.nextDouble();

							} else if (name.equals("acc")
									&& reader.peek() != JsonToken.NULL) {
								gsr = reader.nextDouble();

							} else
								reader.skipValue();
						}

						reader.endObject();

						ContentValues cv_aff = new ContentValues();
						cv_aff.put("time", timeMill);
						cv_aff.put("acc", acc);
						cv_aff.put("gsr", gsr);

						// db.insert(TABLE_AFFECTIVE, null, cv_aff);
						long nameID_AFF =db.insertWithOnConflict(TABLE_AFFECTIVE, null, cv_aff,
								SQLiteDatabase.CONFLICT_IGNORE);

						ContentValues cv_tag = new ContentValues();
						cv_tag.put("time", timeMill);
						cv_tag.put("latitude", latitude);
						cv_tag.put("longitude", longitude);
						cv_tag.put("tag", tag);

						// db.insert(TABLE_TAGYOURPLACE, null, cv_tag);
						long nameID = db.insertWithOnConflict(TABLE_TAGYOURPLACE, null,
								cv_tag, SQLiteDatabase.CONFLICT_IGNORE);

					}
					reader.endArray();

				}
				reader.endArray();
				db.close();

				return 1;

			default:
				return 2;
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 2;

		} finally {

			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void updateExternalStorageState() {
		boolean preWritable = externalStorageWriteable;
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			externalStorageAvailable = externalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			externalStorageAvailable = true;
			externalStorageWriteable = false;
		} else {
			externalStorageAvailable = externalStorageWriteable = false;
		}
		if (preWritable != externalStorageWriteable) {
			if (externalStorageWriteable) {
				// Toast.makeText(ctx,
				// "External storage available", Toast.LENGTH_LONG)
				// .show();
			}

		} else {
			// Toast.makeText(ctx,
			// "External storage available", Toast.LENGTH_LONG)
			// .show();
		}
	}

	private void writeIntoFile(String jsonString) {
		// TODO Auto-generated method stub
		updateExternalStorageState();
		if (externalStorageWriteable) {
			File extDir = Environment.getExternalStorageDirectory();
			File fDir = new File(extDir + DATADIR);
			fDir.mkdirs();
			Date date = new Date();
			try {
				FileWriter fos = new FileWriter(fDir + "/" + date.toString()
						+ ".txt");

				fos.write(jsonString);

				if (fos != null)
					fos.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private String requestCount(ArrayList<String> titlesName)
			throws IOException {
		Connection client = Jsoup.connect(API_URL);
		client.data("cmd", "count");

		for (String title : titlesName) {
			client.data("titles[]", toJsonTitle(title));
			System.out.println(toJsonTitle(title));
		}

		client.data("since", Long.toString(timeFrom));
		client.data("before", Long.toString(timeTo));
		client.data("user", userID);
		System.out.println(userID);
		client.method(Connection.Method.GET);

		Connection.Response response = client.execute();
		Log.d("database error ", response.body());
		return response.body();

	}

	private String toJsonTitle(String t) {

		return "{\"title\":\"" + t + "\"}";
	}

	private String download(ArrayList<String> titlesName) throws IOException {

		Connection client = Jsoup.connect(API_URL);
		client.data("cmd", "data");

		for (String title : titlesName) {
			client.data("titles[]", toJsonTitle(title));
			System.out.println(toJsonTitle(title));
		}

	
		client.data("since", Long.toString(timeFrom));
		client.data("before", Long.toString(timeTo));
		client.data("user", userID);

		client.method(Connection.Method.GET);

		Connection.Response response = client.execute();
		Log.d("database error ", response.body());
		return response.body();

	}

	public String onTaskCompleted(String string) {
		// TODO Auto-generated method stub
		String presentation = "";
		if (string.equals("successful")) {

			presentation = "data was stored into database succesfully!!!";

		} else if (string.equals("fail")) {
			presentation = "The data cannot be stored into database!!";

		} else if (string.equals("error")) {
			presentation = "There are some problem to insert into database, please check the network!! ";

		} else if (string.equals("empty")) {
			presentation = "There is no data in the database of server!! please upload data firstly! ";

		} else if (string.equals("exist")) {
			presentation = " the data have already exist, don't need to download again!!! ";
		}
		return presentation;

	}

}