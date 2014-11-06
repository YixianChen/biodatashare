package com.yixian.biodatashare;

import java.util.ArrayList;
import java.util.List;

import com.facebook.biodatashare.R;



public class DataCreator{
	
	
	List<String> title= new ArrayList<String>();
	List<String> discription= new ArrayList<String>();
	List<Integer> image= new ArrayList<Integer>(); 
	
	String acc= "acc";
	String gsr = "gsr";
	String longitude = "longitude";
	String latitude= "latitude";
	String tagName="tag";
	String accDescription="Accelerometer data shows acceleration";
	String gsrDescription="Galvanic skin response, a method to understand various types of activity in certain parts of the body";
	String longitudeDescription="The longitude of the correspoding location";
	String latitudeDescription="The latitude of the correspoding location";
	String tagNameDescription="The name of tagged place";
	
	int ACC_PIC = R.drawable.acc;
	int GSR_PIC = R.drawable.gsr;
	int TAG_PIC = R.drawable.tag;
	int LONGTITUDE_PIC = R.drawable.longitude;
	int LATITUDE_PIC= R.drawable.latitude;
	
	DataCreator(){
		
		title.add(acc);
		title.add(gsr);
		title.add(longitude);
		title.add(latitude);
		title.add(tagName);
		
		
		discription.add(accDescription);
		discription.add(gsrDescription);
		discription.add(longitudeDescription);
		discription.add(latitudeDescription);
		discription.add(tagNameDescription);
		
		image.add(ACC_PIC);
		image.add(GSR_PIC);
		image.add(LONGTITUDE_PIC);
		image.add(LATITUDE_PIC);
		image.add(TAG_PIC);
		
		
		
	}
	

}