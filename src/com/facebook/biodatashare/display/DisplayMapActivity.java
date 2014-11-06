package com.facebook.biodatashare.display;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.facebook.biodatashare.R;
import com.yixian.biodatashare.share.ScreenShot;
import com.yixian.biodatashare.share.ShareActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.yixian.biodatashare.TimeSelectionActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class DisplayMapActivity extends FragmentActivity {

	private static GoogleMap mMap;

	private Button btnTimeChanger;

	private LocationManager locationManager;

	private String mUserName;
	
	private final static int MIN_1 = 0;
	private final static int MIN_2 = 1;
	private final static int MIN_5 = 2;
	private final static int MIN_10 = 3;
	private final static int MIN_30 = 4;
	private final static int HOUR = 5;
	private final static int DAY = 6;
	private final static int MONTH = 7;
	private final static int YEAR = 8;

	private long timeFrom, timeTo, now;
	private int postion = 2;
	private int conditions = -1;
	private Bitmap bitMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapdisplay);
		setOverflowShowingAlways();

		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this
				.getBaseContext());

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();

		} else {
			initializeMap();

			btnTimeChanger.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String[] text = new String[] { "1min","2mins","5mins","10mins","30mins","1hours","Day", "Month", "Year" };

					if (btnTimeChanger.getText() == text[postion]
							|| btnTimeChanger.getText() == "Time") {

						++postion;

						if (postion == 9) {
							postion = 0;
						}

						btnTimeChanger.setText(text[postion]);

						refresh(text[postion]);

						timeUnit(postion);
					}
				}
			});

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting a fast fix with the last known location
			Location location = locationManager.getLastKnownLocation(provider);

			SharedPreferences pref = getSharedPreferences("UserName", 0);
			mUserName = pref.getString("USER_ID", "");

		}

	}

	private void initializeMap() {
		// TODO Auto-generated method stub
		//
		// FragmentManager myFM = getActivity().getSupportFragmentManager();
		// SupportMapFragment myMAPF = (SupportMapFragment) myFM
		// .findFragmentById(R.id.google_map);

		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.google_map);

		// Getting GoogleMap object from the fragment

		mMap = fm.getMap();

		mMap.setMyLocationEnabled(true);

		RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);

		rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_map) {
					mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				} else if (checkedId == R.id.rb_satellite) {
					mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				}
			}
		});

		btnTimeChanger = (Button) findViewById(R.id.btnTimechanger);
		btnTimeChanger.setText("Time");
		btnTimeChanger.setTextSize(12);
		btnTimeChanger.setHeight(13);
		btnTimeChanger.setWidth(20);

	}

	private void refresh(String string) {

		mMap.clear();

		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.google_map);

		// Getting GoogleMap object from the fragment
		mMap = fm.getMap();
		// Set the google Map view to Satellite view
		// mMap.setMapType(mMap.MAP_TYPE_NORMAL);
		// Enabling MyLocation Layer of Google Map
		mMap.setMyLocationEnabled(true);

		RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);

		rgViews.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_map) {
					mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				} else if (checkedId == R.id.rb_satellite) {
					mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				}
			}
		});

		btnTimeChanger = (Button) findViewById(R.id.btnTimechanger);
		btnTimeChanger.setText(string);
		btnTimeChanger.setTextSize(12);
		btnTimeChanger.setHeight(13);
		btnTimeChanger.setWidth(20);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// Creating a criteria object to retrieve provider
		Criteria criteria = new Criteria();

		// Getting the name of the best provider
		String provider = locationManager.getBestProvider(criteria, true);

		// Getting a fast fix with the last known location
		Location location = locationManager.getLastKnownLocation(provider);

		if (location != null) {
			String title = "I am here";
			drawMarker(location, title);
		}

	}

	private void timeUnit(int i) {
		switch (i) {
		
		case MIN_1 : 
			now = System.currentTimeMillis();
			timeFrom = now - 60000;
			DrawRoute(timeFrom, now);
			break;
		case MIN_2:
			now = System.currentTimeMillis();
			timeFrom = now - 120000;
			DrawRoute(timeFrom, now);
			break;
		case MIN_5:
			now = System.currentTimeMillis();
			timeFrom = now - 300000;
			DrawRoute(timeFrom, now);
			break;
		case MIN_10:
			now = System.currentTimeMillis();
			timeFrom = now - 600000;
			DrawRoute(timeFrom, now);
			break;
		case MIN_30:
			now = System.currentTimeMillis();
			timeFrom = now - 1800000;
			DrawRoute(timeFrom, now);
			break;
		case HOUR:	
			now = System.currentTimeMillis();
			timeFrom = now - 3600000;
			DrawRoute(timeFrom, now);
			break;
		
		case DAY:
			now = System.currentTimeMillis();
			timeFrom = now - 86400000;
			DrawRoute(timeFrom, now);
			break;
		case MONTH:
			now = System.currentTimeMillis();
			timeFrom = now - 2592000000L;
			DrawRoute(timeFrom, now);
			break;
		case YEAR:
			now = System.currentTimeMillis();
			timeFrom = now - 31560000000L;
			DrawRoute(timeFrom, now);
			break;
		default:
			now = System.currentTimeMillis();
			timeFrom = now - 31560000000L;
			DrawRoute(timeFrom, now);
			break;
		}

	}

	private void DrawRoute(long timeFrom, long timeTo) {
		// TODO Auto-generated method stub
		LocationInfo locationInfr = new LocationInfo(this, timeFrom, timeTo,
				mUserName);

		List<LatLng> routePoints = locationInfr.getLatLngPoints(timeFrom,
				timeTo);

		if (routePoints.isEmpty()) {

			String message = "this is no corresponding data during this time period, do you want to download the data?";

			showDialog(message);

		} else {

			System.out.println(routePoints.size());

			PolylineOptions options = new PolylineOptions().width(5)
					.color(Color.BLUE).geodesic(true);

			for (LatLng point : routePoints) {

				options.add(point);

			}

			Polyline polyline = mMap.addPolyline(options);

			// Polyline route = mMap.addPolyline(new PolylineOptions().width(25)
			// .color(Color.RED).geodesic(true));
			// route.setPoints(routePoints);

			for (LocationInfo l : locationInfr.getLocationInfoList()) {

				if (!l.getTag().equals("Unknown")) {

					mMap.addMarker(new MarkerOptions()
							.position(
									new LatLng(l.getLatitude(), l
											.getLongitude())).title(l.getTag())
							.snippet("Address"));

				}

			}

		}
	}

	private void showDialog(String message) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(DisplayMapActivity.this)
				.setTitle("Download the correspoding data!")
				.setMessage(message)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								// if press yes, the Asyctask will be
								// actived

								Intent intent = new Intent(
										DisplayMapActivity.this,
										TimeSelectionActivity.class);
								// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);

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

	private static void drawMarker(Location location, String title) {
		// For showing a move to my loction button
		// mMap.setMyLocationEnabled(true);
		// For dropping a marker at a point on the Map
		mMap.addMarker(new MarkerOptions()
				.position(
						new LatLng(location.getLatitude(), location
								.getLongitude())).title(title)
				.snippet("Home Address"));
		// For zooming automatically to the Dropped PIN Location

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
				}
			}
		}
		return super.onMenuOpened(featureId, menu);
	}

	private void setOverflowShowingAlways() {
		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			menuKeyField.setAccessible(true);
			menuKeyField.setBoolean(config, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_album:

			//showDialog();
			
			
			SnapshotReadyCallback callback = new SnapshotReadyCallback() {

				@Override
				public void onSnapshotReady(Bitmap snapshot) {
					// TODO Auto-generated method stub
					bitMap = snapshot;

					if (savePic(bitMap, "sdcard/screenshot_temp.png")) {
						Intent intent = new Intent(
								DisplayMapActivity.this,
								ShareActivity.class);
						startActivity(intent);

					}
					;

				}

			};

			mMap.snapshot(callback);

			// Toast.makeText(this, "Action Refresh selected",
			// Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
		return true;
	}

	public void showDialog() {
		final CharSequence[] items = { "Map", "Chart", "Both of them" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Which presentation will be shared on facebook?");
		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				switch (conditions) {
				case 0:
					SnapshotReadyCallback callback = new SnapshotReadyCallback() {

						@Override
						public void onSnapshotReady(Bitmap snapshot) {
							// TODO Auto-generated method stub
							bitMap = snapshot;

							if (savePic(bitMap, "sdcard/screenshot_temp.png")) {
								Intent intent = new Intent(
										DisplayMapActivity.this,
										ShareActivity.class);
								startActivity(intent);

							}
							;

						}

					};

					mMap.snapshot(callback);

					break;
				case 1:
					
					
				    Intent intent = new Intent(DisplayMapActivity.this, DisplayActivity.class);
				    ArrayList<String> list = new ArrayList<String>();
				    list.add("acc");
				    list.add("gsr");
			        Bundle b = new Bundle();
			        b.putStringArrayList("DataNameList", list); 
			        b.putLong("TimeFrom", timeFrom);
			        String Time_string = null;
			        switch (postion) {

					case MIN_1 : 
						Time_string = "1min";
						break;
					case MIN_2:
						Time_string = "2mins";
						break;
					case MIN_5:
						Time_string = "5mins";
						break;
					case MIN_10:
						Time_string = "10mins";
						break;
					case MIN_30:
						Time_string = "30mins";
						break;
					case HOUR:	
						Time_string = "1hours";
						break;
					
			    	case DAY:
			    		Time_string = "day";
						break;
					case MONTH:
						Time_string = "month";
						break;
					case YEAR:
						Time_string = "year";
						break;
					default:
						break;
					}
			        
			        b.putString("TimeString", Time_string);
			        b.putString("UserName", mUserName);
			        intent.putExtra("android.intent.extra.rssItem",b);
			        
			        startActivity(intent);
					
					
					
					break;

				case 2:

					break;

				default:
					break;
				}

			}
		});

		builder.setSingleChoiceItems(items, -1,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

						if ("Map".equals(items[which])) {

							conditions = 0;

						} else if ("Chart".equals(items[which])) {

							conditions = 1;
						} else if ("Both of them".equals(items[which])) {

							conditions = 2;
						}

					}
				});
		builder.show();

	}

	private static boolean savePic(Bitmap b, String strFileName) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(strFileName);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
				return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
