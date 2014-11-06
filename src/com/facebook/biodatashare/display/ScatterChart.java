package com.facebook.biodatashare.display;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.yixian.biodatashare.MyDataBaseHelper;
import com.yixian.biodatashare.TimeSelectionActivity;

public class ScatterChart {

	private Context context;
	private String time_string;
	private List<String> data_strings = new LinkedList<String>();
	private MyDataBaseHelper mDB;
	private String mUserName;
	private long timeFrom;
	HashMap<String, Long> mapTagName = new HashMap<String, Long>();

	public ScatterChart(Context context, List<String> y, long timeFrom,
			String x, String name) {

		this.context = context;
		this.data_strings = y;
		this.time_string = x;
		this.mUserName = name;
		this.timeFrom = timeFrom;
	}

	public GraphicalView getChartView() {

		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		XYMultipleSeriesRenderer mRender = new XYMultipleSeriesRenderer();

		System.out.println(data_strings.toString());

		for (String x_String : data_strings) {
			LinkedHashMap<Long, Double> dataMap = new LinkedHashMap<Long, Double>();
			GetData gd = new GetData(context, mUserName);
			dataMap = gd.getX_Data(x_String, timeFrom, time_string);
			XYSeries series = new XYSeries(x_String);
			XYSeriesRenderer render = new XYSeriesRenderer();
			if (dataMap.isEmpty()) {
				showDialog("this is no data during that time !!! Are you goning to download them? ");

			} else {

				Iterator iterator = dataMap.entrySet().iterator();

				while (iterator.hasNext()) {

					Entry entry = (Entry) iterator.next();

					long time = (long) entry.getKey();
					double data = (double) entry.getValue();

					System.out.println("Time :" + time + ", data: " + data);

					series.add(time, data);

					switch (x_String) {
					case "acc":
						render.setColor(Color.RED);
						break;
					case "gsr":
						render.setColor(Color.BLUE);
						break;
					case "longitude":
						render.setColor(Color.GREEN);
						break;
					case "latitude":
						render.setColor(Color.YELLOW);
						break;
					default:
						break;
					}
					render.setPointStyle(PointStyle.DIAMOND);
					render.setLineWidth(6);

				}

			}

			dataset.addSeries(series);

			mRender.addSeriesRenderer(render);
			
			
			ArrayList<String> tagNameList = new ArrayList<String>();
			tagNameList = gd.getTagList(timeFrom, time_string);

			if (tagNameList.isEmpty()) {

				Toast.makeText(context,
						"there is not GPS information during this time!!",
						Toast.LENGTH_LONG).show();
				mRender.setLabelsTextSize(15);

			}

			for (String s : tagNameList) {
				XYSeries _series = new XYSeries(x_String + "_" + s);
				XYSeriesRenderer _render = new XYSeriesRenderer();
				ArrayList<Long> tagTimeList = new ArrayList<Long>();
				tagTimeList = gd.getTagName(s);
				for (long time : tagTimeList) {
					double data_tag = dataMap.get(time);
					_series.add(time, data_tag);
					_render.setColor(Color.GREEN);
					_render.setPointStyle(PointStyle.DIAMOND);
					_render.setLineWidth(6);

				}

				dataset.addSeries(_series);
				mRender.addSeriesRenderer(_render);

			}

			mRender.setChartTitle("Scatter Chart");
			mRender.setChartTitleTextSize(30);
			mRender.setXTitle("X Value");
			mRender.setYTitle("Y Value");
			mRender.setZoomButtonsVisible(true);

			return ChartFactory.getScatterChartView(context, dataset, mRender);
		}
		return null;
	}

	private void showDialog(String message) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(context)
				.setTitle("Download the correspoding data!")
				.setMessage(message)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								// if press yes, the Asyctask will be
								// actived

								Intent intent = new Intent(context,
										TimeSelectionActivity.class);
								// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								context.startActivity(intent);

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