package com.facebook.biodatashare.display;

import java.util.ArrayList;

import org.achartengine.GraphicalView;

import com.facebook.biodatashare.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;

public class LineChartFragment extends Fragment {

	private View view;

	private ArrayList<String> dataNameList = new ArrayList<String>();

	private long timeFrom;

	private String time_string, mUserName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		dataNameList = getArguments().getStringArrayList("DataNameList");
		timeFrom = getArguments().getLong("TimeFrom");
		time_string = getArguments().getString("TimeString");
		mUserName = getArguments().getString("UserName");
		
		
		System.out.println("timeFrom:"+timeFrom);

		view = inflater.inflate(R.layout.chartdisplay, container, false);
		if (timeFrom != 0L) {

			LineChart lineChart = new LineChart(getActivity(), dataNameList,
					timeFrom, time_string, mUserName);
			GraphicalView gView = lineChart.getChartView();
		
			LinearLayout layout = (LinearLayout) view
					.findViewById(R.id.chartdisplayer);
			layout.addView(gView);

		}

		fl.addView(view);
		return fl;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
		
		
		
		
		
	}

}
