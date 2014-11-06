package com.yixian.biodatashare;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDataBaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "biodata";
	private static final String TABLE_AFFECTIVE = "affectivehealth";
	private static final String TABLE_TAGYOURPLACE = "tagyourplace";
	private static final int VERSION = 1;
	private static final String COLUMN_TIME = "time";
	private static final String COLUMN_LATITUDE = "latitude";
	private static final String COLUMN_LONGITUDE = "longitude";
	private static final String COLUMN_ACC = "acc";
	private static final String COLUMN_GSR = "gsr";
	private static final String COLUMN_TAG = "tag";

	private static final String DATABASE_TAG_CREATE = "create table "
			+ TABLE_TAGYOURPLACE + "(" + COLUMN_TIME + " long primary key, "
			+ COLUMN_TAG + " varchar, " + COLUMN_LATITUDE + " double, "
			+ COLUMN_LONGITUDE + " double " + " );";
	private static final String DATABASE_AFFECTIVE_CREATE = "create table "
			+ TABLE_AFFECTIVE + "(" + COLUMN_TIME + " long primary key, "
			+ COLUMN_ACC + " double, " + COLUMN_GSR + " double " + " );";

	public MyDataBaseHelper(Context context, String userName) {
		super(context, DATABASE_NAME + "_" + userName, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_TAG_CREATE);
		db.execSQL(DATABASE_AFFECTIVE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 Log.w(MyDataBaseHelper.class.getName(),
			        "Upgrading database from version " + oldVersion + " to "
			            + newVersion + ", which will destroy all old data");
			    db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGYOURPLACE);
			    db.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFECTIVE);
			    onCreate(db);
	}

}
