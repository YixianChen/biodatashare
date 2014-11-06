package com.facebook.biodatashare.display;


import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;
import com.yixian.biodatashare.MyDataBaseHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

public class LocationInfo {
	


	private Context context;
	private long timeFrom, timeTo;
	private MyDataBaseHelper mDB;
	private double longitude, latitude;
	private String tag;
	private String mUserName;

	private static final String TABLE_TAGYOURPLACE = "tagyourplace";
	private List<LocationInfo> listLoction = new ArrayList<LocationInfo>();

	public LocationInfo(Context context, long timeFrom, long timeTo, String userName) {

		this.context = context;
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
		this.mUserName = userName;

	}

	public LocationInfo(Cursor c) {

		longitude = c.getLong(0);
		latitude = c.getLong(1);
		tag = c.getString(2);
	}

	public List<LatLng> getLatLngPoints(long timeFrom, long timeTo) {

		List<LatLng> list = new ArrayList<LatLng>();

		String[] columns = new String[] { "longitude", "latitude", "tag" };

		String whereClause = "time >= ? AND time <= ?";

		String[] whereArgs = new String[] { "" + timeFrom, "" + timeTo };

		String orderBy = "time";
		
		mDB = new MyDataBaseHelper(context,mUserName);

		Cursor c = mDB.getReadableDatabase().query(TABLE_TAGYOURPLACE, columns,
				whereClause, whereArgs, null, null, orderBy);

		while (c.moveToNext()) {
			LocationInfo el = new LocationInfo(c);

			listLoction.add(el);

			LatLng mapPoints = new LatLng(el.latitude, el.latitude);

			list.add(mapPoints);
		}
		mDB.close();
		return list;
	}
	
	public double getLongitude(){
		return longitude;
	}
	public double getLatitude(){
		return latitude;
	}
	public String getTag(){
		return tag;
	}
	public List<LocationInfo> getLocationInfoList(){
		return listLoction;
		
		
	}

}
