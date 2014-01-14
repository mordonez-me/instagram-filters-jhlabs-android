package com.jabistudio.androidjhlabs.filter.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class AndroidUtils {
	/**
	 * dp to px
	 * @param dip
	 * @param context
	 * @return
	 */
	public static int dipTopx(float dip, Context context) {
	    int num = (int) TypedValue.applyDimension(
		    TypedValue.COMPLEX_UNIT_DIP, dip, 
		    context.getResources().getDisplayMetrics());
	    return num;
	}
	/**
	 * px to dp
	 * @param px
	 * @param context
	 * @return
	 */
	public static float pxTodip(int px, Context context) {
		float num = px / context.getResources().getDisplayMetrics().density;
	    return num;
	}
	/**
	 * Drawable을 Bitmap으로 바꾸어 준다.
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable){
	    final int width = drawable.getIntrinsicWidth();
	    final int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
	}
	
	public static int[] drawableToIntArray(Drawable drawable){
	    Bitmap bitmap = AndroidUtils.drawableToBitmap(drawable);
        
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        
        int[] colors = new int[bitmapWidth *  bitmapHeight];
        bitmap.getPixels(colors, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        
        return colors;
	}
	
	public static int[] bitmapToIntArray(Bitmap bitmap){
	    final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        
        int[] colors = new int[bitmapWidth *  bitmapHeight];
        bitmap.getPixels(colors, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
        
        return colors;
	}
	
	  /** Get Bitmap's Width **/
	 public static int getBitmapOfWidth(Resources res, int id){ 
	    try {
	        BitmapFactory.Options options = new BitmapFactory.Options(); 
	        options.inJustDecodeBounds = true; 
	        BitmapFactory.decodeResource(res, id, options);
	        return options.outWidth; 
	    } catch(Exception e) {
	        return 0; 
	    } 
	 } 
	   
	 /** Get Bitmap's height **/
	 public static int getBitmapOfHeight(Resources res, int id){ 
	    try { 
	        BitmapFactory.Options options = new BitmapFactory.Options(); 
	        options.inJustDecodeBounds = true; 
	        BitmapFactory.decodeResource(res, id, options);
	        return options.outHeight; 
	    } catch(Exception e) { 
	        return 0; 
	   } 
	 }
}
