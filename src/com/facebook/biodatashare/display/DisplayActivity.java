package com.facebook.biodatashare.display;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Toast;

import com.facebook.biodatashare.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.yixian.biodatashare.share.ScreenShot;
import com.yixian.biodatashare.share.ShareActivity;



public class DisplayActivity extends FragmentActivity {

	
	 
	    private MapFragment mapFragment;  
	  
	    /** 
	     * MapFragment 
	     */  
	    private LineChartFragment lineChartFragment;  
	  
	    /** 
	     * DataAnalysisFragment 
	     */  
	    private BarChartFragment barChartFragment; 
	    
	    
	    private ScatterChartFragment scatterChartFragment; 
	    
	    private PieChartFragment pieChartFragment; 
	  
	    /** 
	     *  The instance of PagerSlidingTabStrip 
	     */  
	    private PagerSlidingTabStrip tabs;  
	  
	    /** 
	     * Obtain the metrics of the screen 
	     */  
	    private DisplayMetrics dm;  
	    
	    public static FragmentManager fragmentManager;
	   
	

	    
	    private Bundle bundle;
	  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.display); 
	        
	        Intent intent = getIntent(); 
	        
	        
	        if(intent!=null){
	        	bundle = intent 
	        	            .getBundleExtra("android.intent.extra.rssItem");
	        	
	        }
	       
	        
	       
	        
	        fragmentManager = getSupportFragmentManager();
	        setOverflowShowingAlways();  
	        dm = getResources().getDisplayMetrics();  
	        ViewPager pager = (ViewPager) findViewById(R.id.pager);  
	        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);  
	        pager.setAdapter(new MyPagerAdapter(fragmentManager));  
	        tabs.setViewPager(pager);  
	        setTabsValue();  
	        
	     
	        
	        
	    }
	  
	    /** 
	     * 对PagerSlidingTabStrip的各项属性进行赋值。 
	     */  
	    private void setTabsValue() {  
	         
	        tabs.setShouldExpand(true);  
	       
	        tabs.setDividerColor(Color.TRANSPARENT);  
	        
	        tabs.setUnderlineHeight((int) TypedValue.applyDimension(  
	                TypedValue.COMPLEX_UNIT_DIP, 1, dm));  
	       
	        tabs.setIndicatorHeight((int) TypedValue.applyDimension(  
	                TypedValue.COMPLEX_UNIT_DIP, 4, dm));  
	        
	        tabs.setTextSize((int) TypedValue.applyDimension(  
	                TypedValue.COMPLEX_UNIT_SP, 16, dm));  
	        
	        tabs.setIndicatorColor(Color.parseColor("#45c01a"));  
	       
	        tabs.setSelectedTextColor(Color.parseColor("#45c01a"));  
	        
	        tabs.setTabBackground(0);  
	    }  
	  
	    public class MyPagerAdapter extends FragmentPagerAdapter {  
	  
	        public MyPagerAdapter(FragmentManager fm) {  
	            super(fm);  
	        }  
	  
	        private final String[] titles = { "Line Chart", "Bar Chart","Scatter Chart"};  
	  
	        @Override  
	        public CharSequence getPageTitle(int position) {  
	            return titles[position];  
	        }  
	  
	        @Override  
	        public int getCount() {  
	            return titles.length;  
	        }  
	  
	        @Override  
	        public Fragment getItem(int position) {  
	            switch (position) {  
	            case 0:  
	                if (lineChartFragment == null) {  
	                	lineChartFragment = new LineChartFragment();
	                	lineChartFragment.setArguments(bundle);
	                }  
	                return lineChartFragment;  
	            case 1:  
	                if (barChartFragment == null) {  
	                	barChartFragment = new BarChartFragment();
	                	barChartFragment.setArguments(bundle);
	                }  
	                return barChartFragment;  
	            case 2:
	            	 if (scatterChartFragment == null) {  
	            		 scatterChartFragment = new ScatterChartFragment();
	            		 scatterChartFragment.setArguments(bundle);
		                }  
		                return scatterChartFragment;
	            
	            default:  
	                return null;  
	            }  
	        }  
	  
	    }  
	    
	    
	    public void showDialog()
	    {
	    	final CharSequence[] items={"Map","Chart","Both of them"};
	    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
	    	builder.setTitle("Which presentation will be shared on facebook?");
	    	builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					
					
				}
			});
	    	
	     	builder.setSingleChoiceItems(items,-1, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					
					if("Map".equals(items[which]))
					{
					
					}
					else if("Chart".equals(items[which]))
					{
						
						}
					else if("Both of them".equals(items[which]))
					{
						
						}
					
				}
			});
	    	builder.show();
	    
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
        
           // Toast.makeText(this, "Action Refresh selected", Toast.LENGTH_SHORT).show();
        	
        	if(ScreenShot.shoot(DisplayActivity.this)){
        		
        		Toast.makeText(this, "Screenshot successful!!", Toast.LENGTH_SHORT).show();
            	
        		
        		Intent intent = new Intent(
						DisplayActivity.this,
						ShareActivity.class);
				startActivity(intent);
        		
        	}else{
        		
        		Toast.makeText(this, "Screenshot failed!!", Toast.LENGTH_SHORT).show();
        		
        	}
        	
        	
            break;
         
        default:
            break;
    }
    return true;
	}


	
}
