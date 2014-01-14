/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.jabistudio.androidjhlabs.filter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;

/**
 * A filter which produces motion blur the faster, but lower-quality way.
 */
public class MotionBlurOp {

    private float centreX = 0.5f, centreY = 0.5f;
    private float distance;
    private float angle;
    private float rotation;
    private float zoom;

    /**
     * Construct a MotionBlurOp.
     */
    public MotionBlurOp() {
	}
	
    /**
     * Construct a MotionBlurOp.
     * @param distance the distance of blur.
     * @param angle the angle of blur.
     * @param rotation the angle of rotation.
     * @param zoom the zoom factor.
     */
	public MotionBlurOp( float distance, float angle, float rotation, float zoom ) {
        this.distance = distance;
        this.angle = angle;
        this.rotation = rotation;
        this.zoom = zoom;
    }
    
	/**
     * Specifies the angle of blur.
     * @param angle the angle of blur.
     * @angle
     * @see #getAngle
     */
	public void setAngle( float angle ) {
		this.angle = angle;
	}

	/**
     * Returns the angle of blur.
     * @return the angle of blur.
     * @see #setAngle
     */
	public float getAngle() {
		return angle;
	}
	
	/**
     * Set the distance of blur.
     * @param distance the distance of blur.
     * @see #getDistance
     */
	public void setDistance( float distance ) {
		this.distance = distance;
	}

	/**
     * Get the distance of blur.
     * @return the distance of blur.
     * @see #setDistance
     */
	public float getDistance() {
		return distance;
	}
	
	/**
     * Set the blur rotation.
     * @param rotation the angle of rotation.
     * @see #getRotation
     */
	public void setRotation( float rotation ) {
		this.rotation = rotation;
	}

	/**
     * Get the blur rotation.
     * @return the angle of rotation.
     * @see #setRotation
     */
	public float getRotation() {
		return rotation;
	}
	
	/**
     * Set the blur zoom.
     * @param zoom the zoom factor.
     * @see #getZoom
     */
	public void setZoom( float zoom ) {
		this.zoom = zoom;
	}

	/**
     * Get the blur zoom.
     * @return the zoom factor.
     * @see #setZoom
     */
	public float getZoom() {
		return zoom;
	}
	
	/**
	 * Set the centre of the effect in the X direction as a proportion of the image size.
	 * @param centreX the center
     * @see #getCentreX
	 */
	public void setCentreX( float centreX ) {
		this.centreX = centreX;
	}

	/**
	 * Get the centre of the effect in the X direction as a proportion of the image size.
	 * @return the center
     * @see #setCentreX
	 */
	public float getCentreX() {
		return centreX;
	}
	
	/**
	 * Set the centre of the effect in the Y direction as a proportion of the image size.
	 * @param centreY the center
     * @see #getCentreY
	 */
	public void setCentreY( float centreY ) {
		this.centreY = centreY;
	}

	/**
	 * Get the centre of the effect in the Y direction as a proportion of the image size.
	 * @return the center
     * @see #setCentreY
	 */
	public float getCentreY() {
		return centreY;
	}
	
	/**
	 * Set the centre of the effect as a proportion of the image size.
	 * @param centre the center
     * @see #getCentre
	 */
	public void setCentre( float centerX, float centerY ) {
		this.centreX = centerX;
		this.centreY = centerY;
	}
	
    private int log2( int n ) {
        int m = 1;
        int log2n = 0;

        while (m < n) {
            m *= 2;
            log2n++;
        }
        return log2n;
    }
    /**
     * TODO 문제가 있음 Bitmap 해제를 해줄것
     * @param src
     * @param w
     * @param h
     * @return
     */
    public int[] filter( int[] src ,int w, int h ) {
        int dst[] = new int[w*h];
        Bitmap srcBitmap = Bitmap.createBitmap(src, w, h, Bitmap.Config.ARGB_8888);
        Bitmap tSrcBitmap = Bitmap.createBitmap(src, w, h, Bitmap.Config.ARGB_8888);
        Bitmap dstBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        
        float cx = (float)w * centreX;
        float cy = (float)h * centreY;
        float imageRadius = (float)Math.sqrt( cx*cx + cy*cy );
        float translateX = (float)(distance * Math.cos( angle ));
        float translateY = (float)(distance * -Math.sin( angle ));
        float scale = zoom;
        float rotate = rotation;
        float maxDistance = distance + Math.abs(rotation*imageRadius) + zoom*imageRadius;
        int steps = log2((int)maxDistance);
        
		translateX /= maxDistance;
		translateY /= maxDistance;
		scale /= maxDistance;
		rotate /= maxDistance;
		
        if ( steps == 0 ) {
            dstBitmap.getPixels(dst, 0, w, 0, 0, w, h);
            srcBitmap.recycle();
            tSrcBitmap.recycle();
            dstBitmap.recycle();
            return dst;
        }
        
        Bitmap tmpBitmap = srcBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap ti = null;
        Paint p = new Paint();
        p.setAlpha(128);
        p.setAntiAlias(true);
        p.setXfermode(new PorterDuffXfermode(Mode.SRC_OVER));
        
        for ( int i = 0; i < steps; i++ ) {
            Canvas c = new Canvas(dstBitmap);

            c.translate( cx+translateX, cy+translateY );
            // The .0001 works round a bug on Windows where drawImage throws an ArrayIndexOutofBoundException
            c.scale((float)(1.0001+scale), (float)(1.0001+scale), 0.5f, 0.5f);
            c.scale( (float)(1.0001+scale), (float)(1.0001+scale) );  
            if ( rotation != 0 ){
                c.rotate( rotate );
            }
            c.translate( -cx, -cy );
            c.drawBitmap(tSrcBitmap, 0, 0, p);
            ti = dstBitmap;
            dstBitmap = tmpBitmap;
            tmpBitmap = ti;
            tSrcBitmap = dstBitmap;

            translateX *= 2;
            translateY *= 2;
            scale *= 2;
            rotate *= 2;
        }
        dstBitmap.getPixels(dst, 0, w, 0, 0, w, h);
        
        if(ti != null){
            ti.recycle();
        }
        srcBitmap.recycle();
        tSrcBitmap.recycle();
        tmpBitmap.recycle();
        dstBitmap.recycle();
        
        return dst;
    }
    
	public String toString() {
		return "Blur/Faster Motion Blur...";
	}
}
