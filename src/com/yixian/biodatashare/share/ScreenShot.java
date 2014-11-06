package com.yixian.biodatashare.share;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class ScreenShot {
	
	
	
	
	public static Bitmap takeScreenShot(Activity pActivity) {
		Bitmap bitmap = null;
		
		View view = pActivity.getWindow().getDecorView();
		view.getViewTreeObserver();
		view.setDrawingCacheEnabled(true);

	    //view.buildDrawingCache();

		bitmap = view.getDrawingCache();

		Rect frame = new Rect();

		view.getWindowVisibleDisplayFrame(frame);
		int stautsHeight = frame.top;
		Log.d("Yixian ", "the height is :"+stautsHeight);

		int width = pActivity.getWindowManager().getDefaultDisplay().getWidth();
		int height = pActivity.getWindowManager().getDefaultDisplay()
				.getHeight();
        
		bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height
				- stautsHeight);
		view.destroyDrawingCache();
		Log.d("Yixian ", "the size of bitmap is :"+ width*(height
				- stautsHeight)/1024);
		return bitmap;
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
	 public static boolean shoot(Activity a) {
	        return ScreenShot.savePic(ScreenShot.takeScreenShot(a), "sdcard/screenshot_temp.png");
	    }

}
