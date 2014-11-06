package com.facebook.biodatashare.display;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.content.Context;
import android.database.Cursor;

import com.yixian.biodatashare.MyDataBaseHelper;

public class GetData {
	private MyDataBaseHelper mDB;
	private String mUserName;
	private final static String TABLE_TAGYOURPLACE = "tagyourplace";
	private final static String TABLE_AffectiveHealthy = "affectivehealth";
	private long timeTo;
	private Context context;

	public GetData(Context context, String name) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.mUserName = name;
	}

	public LinkedHashMap<Long, Double> getX_Data(String dataType, long time,
			String timeperiod) {

		String tableName = "";
		String[] columns = null;

		switch (dataType) {

		case "longitude":
			tableName = TABLE_TAGYOURPLACE;
			columns = new String[] { "time", "longitude" };

			break;
		case "latitude":
			tableName = TABLE_TAGYOURPLACE;
			columns = new String[] { "time", "latitude" };

			break;
		case "acc":
			tableName = TABLE_AffectiveHealthy;
			columns = new String[] { "time", "acc" };

			break;

		case "gsr":
			tableName = TABLE_AffectiveHealthy;
			columns = new String[] { "time", "gsr" };

			break;

		default:
			break;
		}

		switch (timeperiod) {
		case "1min":
			timeTo = time + 60000;
			break;
		case "2mins":
			timeTo = time + 120000;
			break;

		case "5mins":
			timeTo = time + 300000;
			break;

		case "10mins":
			timeTo = time + 600000;
			break;

		case "30mins":
			timeTo = time + 1800000;
			break;

		case "1hours":
			timeTo = time + 3600000;
			break;
		default:
			break;
		}

		String whereClause = "time >= ? AND time <= ?";

		String[] whereArgs = new String[] { "" + time, "" + timeTo };

		String orderBy = "time";

		mDB = new MyDataBaseHelper(context, mUserName);

		Cursor c = mDB.getReadableDatabase().query(tableName, columns,
				whereClause, whereArgs, null, null, orderBy);

		LinkedHashMap<Long, Double> map = new LinkedHashMap<Long, Double>();

		while (c.moveToNext()) {

			long time_data = c.getLong(0);
			double data = c.getDouble(1);
			map.put(time_data, data);

		}
		mDB.close();

		return map;
	}

	public ArrayList<Long> getTagName(String name) {

		String[] columns = new String[] { "time", "tag" };

		String whereClause = "tag = ?";

		String[] whereArgs = new String[] { name };
		
		String orderBy = "tag";
		

		mDB = new MyDataBaseHelper(context, mUserName);

		Cursor c = mDB.getReadableDatabase().query(TABLE_TAGYOURPLACE, columns,
				whereClause, whereArgs, null, null, orderBy);

		ArrayList<Long> timeList = new ArrayList<Long>();

		while (c.moveToNext()) {
			long time_data = c.getLong(0);
			String tag_name = c.getString(1);
			timeList.add(time_data);

		}
		return timeList;
	}

	public LinkedHashMap<Long, String> getTagMap(long time, String timeperiod) {

		switch (timeperiod) {
		case "1min":
			timeTo = time + 60000;
			break;
		case "2mins":
			timeTo = time + 120000;
			break;

		case "5mins":
			timeTo = time + 300000;
			break;

		case "10mins":
			timeTo = time + 600000;
			break;

		case "30mins":
			timeTo = time + 1800000;
			break;

		case "1hours":
			timeTo = time + 3600000;
			break;
		default:
			break;
		}

		String[] columns = new String[] { "time", "tag" };

		String whereClause = "time >= ? AND time <= ?";

		String[] whereArgs = new String[] { "" + time, "" + timeTo };

		String orderBy = "time";

		mDB = new MyDataBaseHelper(context, mUserName);

		Cursor c = mDB.getReadableDatabase().query(TABLE_TAGYOURPLACE, columns,
				whereClause, whereArgs, null, null, orderBy);

		if (c == null) {

			return null;

		} else {

			LinkedHashMap<Long, String> tagMap = new LinkedHashMap<Long, String>();

			while (c.moveToNext()) {
				long time_data = c.getLong(0);
				String tag_name = c.getString(1);
				tagMap.put(time_data, tag_name);

			}
			return tagMap;
		}
	}
	public ArrayList<String> getTagList(long time, String timeperiod){
		
		
		switch (timeperiod) {
		case "1min":
			timeTo = time + 60000;
			break;
		case "2mins":
			timeTo = time + 120000;
			break;

		case "5mins":
			timeTo = time + 300000;
			break;

		case "10mins":
			timeTo = time + 600000;
			break;

		case "30mins":
			timeTo = time + 1800000;
			break;

		case "1hours":
			timeTo = time + 3600000;
			break;
		default:
			break;
		}

		String[] columns = new String[] { "tag" };

		String whereClause = "time >= ? AND time <= ? AND tag != ?";

		String[] whereArgs = new String[] { "" + time, "" + timeTo,"" + "Unknown"};

		String groupBy = "tag";

		String orderBy = "time";
		
		//String having  = "count(*)>1" ;
		
		
		mDB = new MyDataBaseHelper(context, mUserName);

		Cursor c = mDB.getReadableDatabase().query(TABLE_TAGYOURPLACE, columns,
				whereClause, whereArgs, groupBy, null, orderBy);

		if (c == null) {

			return null;

		} else {

			ArrayList<String> tagList = new ArrayList<String>();

			while (c.moveToNext()) {
				String tag_name = c.getString(0);
				tagList.add(tag_name);

			}
			return tagList;
		}
		
	}
	
	
}
