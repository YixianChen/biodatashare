package com.yixian.biodatashare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.facebook.biodatashare.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter {
	Activity activity;
	List<Integer> mImageSource;
	List<Boolean> mChecked;  
	 //List<Data> listData; 
	 private static LayoutInflater inflater=null;
	 ArrayList<HashMap<String,Object>> data;
	 //HashMap<Integer,View> map = new HashMap<Integer,View>(); 
	   public DataAdapter(Activity a,ArrayList<HashMap<String,Object>> list){  
		   data=list;
           //listPerson = list;  
		   activity = a;
		   inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           mChecked = new ArrayList<Boolean>();  
           for(int i=0;i<list.size();i++){  
               mChecked.add(false);  
           }  
           
       } 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi=convertView;
		 if(convertView==null)
	            vi = inflater.inflate(R.layout.datalistitem, null);

	        TextView title = (TextView)vi.findViewById(R.id.dataname); // title
	        TextView discription = (TextView)vi.findViewById(R.id.discription); // discription of this data
	        CheckBox checkBox = (CheckBox)vi.findViewById(R.id.check); // checkbox
	        ImageView pic=(ImageView)vi.findViewById(R.id.list_image); //
	        
	        
	        HashMap<String, Object> data_row = new HashMap<String, Object>();
	        data_row = data.get(position);
	        title.setText((CharSequence) data_row.get("Title"));
	        discription.setText((CharSequence)data_row.get("Description"));
	        pic.setImageResource((Integer) data_row.get("Image"));
	        final int p = position;
	        checkBox.setOnClickListener(new View.OnClickListener() {  
                
                @Override  
                public void onClick(View v) {  
                    CheckBox cb = (CheckBox)v;  
                    mChecked.set(p, cb.isChecked()); 
                }  
            }); 
	        
		return vi;
	}
	
	

}
