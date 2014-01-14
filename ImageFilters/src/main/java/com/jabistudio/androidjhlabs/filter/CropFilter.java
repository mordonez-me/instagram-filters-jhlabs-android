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

import com.jabistudio.androidjhlabs.filter.util.PixelUtils;

/**
 * A filter which crops an image to a given rectangle.
 */
public class CropFilter {

	private int x;
	private int y;
	private int width;
	private int height;

    /**
     * Construct a CropFilter.
     */
	public CropFilter() {
		this(0, 0, 32, 32);
	}

    /**
     * Construct a CropFilter.
     * @param x the left edge of the crop rectangle
     * @param y the top edge of the crop rectangle
     * @param width the width of the crop rectangle
     * @param height the height of the crop rectangle
     */
	public CropFilter(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

    /**
     * Set the left edge of the crop rectangle.
     * @param x the left edge of the crop rectangle
     * @see #getX
     */
	public void setX(int x) {
		this.x = x;
	}

    /**
     * Get the left edge of the crop rectangle.
     * @return the left edge of the crop rectangle
     * @see #setX
     */
	public int getX() {
		return x;
	}

    /**
     * Set the top edge of the crop rectangle.
     * @param y the top edge of the crop rectangle
     * @see #getY
     */
	public void setY(int y) {
		this.y = y;
	}

    /**
     * Get the top edge of the crop rectangle.
     * @return the top edge of the crop rectangle
     * @see #setY
     */
	public int getY() {
		return y;
	}

    /**
     * Set the width of the crop rectangle.
     * @param width the width of the crop rectangle
     * @see #getWidth
     */
	public void setWidth(int width) {
		this.width = width;
	}

    /**
     * Get the width of the crop rectangle.
     * @return the width of the crop rectangle
     * @see #setWidth
     */
	public int getWidth() {
		return width;
	}

    /**
     * Set the height of the crop rectangle.
     * @param height the height of the crop rectangle
     * @see #getHeight
     */
	public void setHeight(int height) {
		this.height = height;
	}

    /**
     * Get the height of the crop rectangle.
     * @return the height of the crop rectangle
     * @see #setHeight
     */
	public int getHeight() {
		return height;
	}

	public int[] filter( int[] src ,int w, int h ) {
        int[] dst = new int[width * height];
      
        Bitmap srcBitmap = Bitmap.createBitmap(src, 0, w, w, h, Bitmap.Config.ARGB_8888);
        Bitmap dstBitmap = Bitmap.createBitmap(srcBitmap, x, y, w - x, h - y);
        dstBitmap = Bitmap.createScaledBitmap(dstBitmap, width, height, false);
        
        dstBitmap.getPixels(dst, 0, width, 0, 0, width, height);
        return dst;
    }

	public String toString() {
		return "Distort/Crop";
	}
}
