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

import android.graphics.Color;
import android.util.Log;

/**
 * Edge detection by difference of Gaussians.
 * @author Jerry Huxtable
 */
public class DoGFilter {

	private float radius1 = 1;
	private float radius2 = 2;
    private boolean normalize = true;
    private boolean invert;
	
	public DoGFilter() {
	}
	
	/**
	 * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will take.
	 * @param radius the radius of the blur in pixels.
     * @min-value 0
     * @max-value 100+
     * @see #getRadius
	 */
	public void setRadius1(float radius1) {
		this.radius1 = radius1;
	}
	
	/**
	 * Get the radius of the kernel.
	 * @return the radius
     * @see #setRadius
	 */
	public float getRadius1() {
		return radius1;
	}

	/**
	 * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will take.
	 * @param radius the radius of the blur in pixels.
     * @min-value 0
     * @max-value 100+
     * @see #getRadius
	 */
	public void setRadius2(float radius2) {
		this.radius2 = radius2;
	}
	
	/**
	 * Get the radius of the kernel.
	 * @return the radius
     * @see #setRadius
	 */
	public float getRadius2() {
		return radius2;
	}
	
    public void setNormalize( boolean normalize ) {
        this.normalize = normalize;
    }
    
    public boolean getNormalize() {
        return normalize;
    }
    
    public void setInvert( boolean invert ) {
        this.invert = invert;
    }
    
    public boolean getInvert() {
        return invert;
    }
    
    public int[] filter( int[] src, int width, int height) {
    	
    	int[] originalSrc = new int[width * height];
    	for(int i=0;i<src.length;++i){
    		originalSrc[i] = src[i];
    	}
    	
        int[] image1 = new int[width * height];
        BoxBlurFilter filter1 = new BoxBlurFilter( radius1, radius1, 3 );
        image1 = filter1.filter( originalSrc, width, height );
        int[] image2 = new int[width * height];
        BoxBlurFilter filter2 = new BoxBlurFilter( radius2, radius2, 3 );
        image2 = filter2.filter( src, width, height );
        
        image2 = compose(width,height,image1, image2, 1.0f);

        if ( normalize && radius1 != radius2 ) {
            int[] pixels = null;
            int max = 0;
            for ( int y = 0; y < height; y++ ) {
                //pixels = getRGB( image2, 0, y, width, 1, pixels );
            	pixels = getLineRGB(image2,y,width,pixels);
                for ( int x = 0; x < width; x++ ) {
                    int rgb = pixels[x];
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    if ( r > max )
                        max = r;
                    if ( g > max )
                        max = g;
                    if ( b > max )
                        max = b;
                }
            }

            for ( int y = 0; y < height; y++ ) {
                //pixels = getRGB( image2, 0, y, width, 1, pixels );
                pixels = getLineRGB(image2,y,width,pixels);
                for ( int x = 0; x < width; x++ ) {
                    int rgb = pixels[x];
                    int r = (rgb >> 16) & 0xff;
                    int g = (rgb >> 8) & 0xff;
                    int b = rgb & 0xff;
                    if(max != 0){
                    	r = r * 255 / max;
                        g = g * 255 / max;
                        b = b * 255 / max;
                        pixels[x] = (rgb & 0xff000000) | (r << 16) | (g << 8) | b;
                    }
                }
                //setRGB( image2, 0, y, width, 1, pixels );
                setLineRGB(image2,y,width,pixels);
            }
        }

        if ( invert )
            image2 = new InvertFilter().filter( image2, width, height);

        return image2;
    }
    private int[] getLineRGB(int[] src, int y, int width, int[] pixel){
    	pixel = null;
    	pixel = new int[width];
    	for(int i = 0;i < width;++i){
    		pixel[i] = src[(width * y) + i];
    	}
    	return pixel;
    }
    private void setLineRGB(int[] src, int y, int width, int[] pixel){
    	for(int i = 0;i < width;++i){
    		src[(width * y) + i] = pixel[i];
    	}
    }
    
    private int[] getRasterLineRGB(int[] src, int y, int width, int[] pixel){
    	pixel = null;
    	pixel = new int[width * 4];
    	int j = width * y;
    	for(int i = 0;i < width * 4;i += 4){
    		pixel[i] = Color.alpha(src[j]);
    		pixel[i + 1] = Color.red(src[j]);
    		pixel[i + 2] = Color.green(src[j]);
    		pixel[i + 3] = Color.blue(src[j]);
    		j++;
    	}
    	return pixel;
    }
    private void setRasterLineRGB(int[] dst, int y, int width, int[] pixel){
    	int j = width * y;
    	for(int i = 0;i < width * 4;i += 4){
    		dst[j] = Color.argb(pixel[i], pixel[i + 1], pixel[i + 2], pixel[i + 3]);
    		j++;
    	}
    }
    
    public int[] compose(int w, int h, int[] src, int[] dst, float alpha ){
    	int[] dstOut = new int[src.length];
        int y0 = 0;
        int y1 = y0 + h;
        int[] srcpixel = null;
        int[] dstpixel = null;
    	for ( int y = y0; y < y1; y++ ) {
    		srcpixel = getRasterLineRGB(src, y, w, srcpixel );
    		dstpixel = getRasterLineRGB(dst, y, w, dstpixel );
            composeRGB( srcpixel, dstpixel, alpha );
            setRasterLineRGB(dstOut, y, w, dstpixel);
        }
    	return dstOut;
    }
    public void composeRGB( int[] src, int[] dst, float alpha ) {
        int w = src.length;
        for ( int i = 0; i < w; i += 4 ) {
        	int sa = src[i];
            int dia = dst[i];
            int sr = src[i+1];
            int dir = dst[i+1];
            int sg = src[i+2];
            int dig = dst[i+2];
            int sb = src[i+3];
            int dib = dst[i+3];
            int dor, dog, dob;

            dor = dir - sr;
            if ( dor < 0 )
                dor = 0;
            dog = dig - sg;
            if ( dog < 0 )
                dog = 0;
            dob = dib - sb;
            if ( dob < 0 )
                dob = 0;

            float a = alpha*sa/255f;
            float ac = 1-a;

            dst[i] = (int)(sa*alpha + dia*ac);
            dst[i+1] = (int)(a*dor + ac*dir);
            dst[i+2] = (int)(a*dog + ac*dig);
            dst[i+3] = (int)(a*dob + ac*dib);
        }
    }
    
	public String toString() {
		return "Edges/Difference of Gaussians...";
	}
}
