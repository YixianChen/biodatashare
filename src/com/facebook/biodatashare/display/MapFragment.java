package com.facebook.biodatashare.display;

import java.util.List;

import com.facebook.biodatashare.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.yixian.biodatashare.DataSelectActivity;
import com.yixian.biodatashare.Download;
import com.yixian.biodatashare.TimeSelectionActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.TimeFormatException;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MapFragment extends Fragment {

	private static GoogleMap mMap;
	private MapView mapView;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private String mUserName;
	private final static int DAY = 0;
	private final static int MONTH = 1;
	private final static int YEAR = 2;
	private long timeFrom, timeTo, now;
	private Button btnTimeChanger;
	private int postion = 2;
	private View view;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);
		
		view = inflater.inflate(R.layout.mapdisplay, container, false);

		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity().getBaseContext());

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
													// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
					getActivity(), requestCode);
			dialog.show();

		} else { // Google Play Services are available
			
			view = initializeMap(view);
			
			
			System.out.println("11111111111111111111111111111111111111111111111111");
			
			//timeUnit(2);
			
			
//			btnTimeChanger.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					String[] text = new String[] { "Day", "Month", "Year" };
//
//					if (btnTimeChanger.getText() == text[postion] || btnTimeChanger.getText() == "Time") {
//
//						++postion;
//
//						if (postion == 3) {
//							postion = 0;
//						}
//
//						btnTimeChanger.setText(text[postion]);
//
//						refresh(view, text[postion]);
//
//						timeUnit(postion);
//
//					}
//
//				}
//			});

			// Getting reference to the SupportMapFragment of activity_main.xml

			locationManager = (LocationManager) getActivity().getSystemService(
					Context.LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting a fast fix with the last known location
			Location location = locationManager.getLastKnownLocation(provider);
//
//			mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//
//				@Override
//				public void onMyLocationChange(Location location) {
//					// TODO Auto-generated method stub
//					// drawMarker(location);
//
//					// mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
//					// LatLng(location.getLatitude(),
//					// location.getLongitude()), 12.0f));
//				}
//
//			});

			locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					// redraw the marker when get location update.
					// Toast.makeText(getApplicationContext(),
					// "Location Updated", Toast.LENGTH_SHORT).show();
					String title = "I am here";
					drawMarker(location, title);

					// LatLng currentPosition = new
					// LatLng(location.getLatitude(),
					// location.getLongitude());
					// move the camera to the position
					// googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
					// Zoom in the Google Map
					// googleMap.animateCamera(CameraUpdateFactory.zoomTo(1));
					// add a marker to a map
					// mMap.addMarker(new MarkerOptions()
					// .position(currentPosition)
					// .snippet("Lat:" + location.getLatitude() + " Long:"+
					// location.getLongitude())
					// .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon21))
					// .title("I am Here!!!"));
					//

					// Toast.makeText(getActivity().getBaseContext(),
					// "Location Updated", Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onProviderDisabled(String arg0) {
					// print "Currently GPS is Disabled";
					Toast.makeText(getActivity().getApplicationContext(),
							"Currently GPS is Disabled", Toast.LENGTH_SHORT)
							.show();

				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					// print "GPS got Enabled";
					Toast.makeText(getActivity().getApplicationContext(),
							"GPS got Enabled", Toast.LENGTH_SHORT).show();

				}

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub

				}
			};

			SharedPreferences pref = getActivity().getSharedPreferences(
					"UserName", 0);
			mUserName = pref.getString("USER_ID", "");
//
//			if (location != null) {
//				String title = "I am here";
//				drawMarker(location, title);
//			}

			// locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
			// 60*1000, 0, locationListener);
		}

		// timeUnit(2);

	
		fl.addView(view);
		
		return fl;
	}

	
	
	private View initializeMap(View v) {
		// TODO Auto-generated method stub
//		
//		FragmentManager myFM = getActivity().getSupportFragmentManager();
//		SupportMapFragment myMAPF = (SupportMapFragment) myFM
//                .findFragmentById(R.id.google_map);
		
		SupportMapFragment fm = (SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.google_map);
		
		// Getting GoogleMap object from the fragment
		
		mMap = fm.getMap();
		
		mMap.setMyLocationEnabled(true);

		RadioGroup rgViews = (RadioGroup) v.findViewById(R.id.rg_views);

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

		btnTimeChanger = (Button) v.findViewById(R.id.btnTimechanger);
		btnTimeChanger.setText("Time");
		btnTimeChanger.setTextSize(12);
		btnTimeChanger.setHeight(13);
		btnTimeChanger.setWidth(20);


		return v;

	}

	private View refresh(View view, String string) {

		mMap.clear();

		SupportMapFragment fm = (SupportMapFragment) getActivity()
				.getSupportFragmentManager().findFragmentById(R.id.google_map);

		// Getting GoogleMap object from the fragment
		mMap = fm.getMap();
		// Set the google Map view to Satellite view
		// mMap.setMapType(mMap.MAP_TYPE_NORMAL);
		// Enabling MyLocation Layer of Google Map
		mMap.setMyLocationEnabled(true);

		RadioGroup rgViews = (RadioGroup) view.findViewById(R.id.rg_views);

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

		btnTimeChanger = (Button) view.findViewById(R.id.btnTimechanger);
		btnTimeChanger.setText(string);
		btnTimeChanger.setTextSize(12);
		btnTimeChanger.setHeight(13);
		btnTimeChanger.setWidth(20);
		

		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

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

		return view;
	}

	private void timeUnit(int i) {
		switch (i) {
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
			System.out.println("2222222222222222222222222222222222222222222222222");
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
		LocationInfo locationInfr = new LocationInfo(this.getActivity(),
				timeFrom, timeTo, mUserName);
		
		System.out.println("333333333333333333333333333333333333333333333333");
		List<LatLng> routePoints = locationInfr.getLatLngPoints(timeFrom,
				timeTo);
		
		System.out.println("4444444444444444444444444444444444444444444444");
		if (routePoints.isEmpty()) {

			String message = "this is no corresponding data during this time period, do you want to download the data?";

			showDialog(message);
			
		} else {

			System.out.println(routePoints.size());
			
			PolylineOptions options = new PolylineOptions().width(5).color(Color.RED).geodesic(true);
			
			for(LatLng point:routePoints){
				
				options.add(point);
				
				
			}
			Polyline polyline = mMap.addPolyline(options);

//			Polyline route = mMap.addPolyline(new PolylineOptions().width(25)
//					.color(Color.RED).geodesic(true));
//			route.setPoints(routePoints);
			

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
		new AlertDialog.Builder(getActivity())
				.setTitle("Download the correspoding data!")
				.setMessage(message)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								// if press yes, the Asyctask will be
								// actived

								Intent intent = new Intent(getActivity(),
										TimeSelectionActivity.class);
								// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								getActivity().startActivity(intent);

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
	


	
	
	
	
}