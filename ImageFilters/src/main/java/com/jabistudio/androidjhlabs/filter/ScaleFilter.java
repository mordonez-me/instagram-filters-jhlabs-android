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

/**
 * Scales an image using the area-averaging algorithm, which can't be done with AffineTransformOp.
 */
public class ScaleFilter {

	private int width;
	private int height;

    /**
     * Construct a ScaleFilter.
     */
	public ScaleFilter() {
		this(32, 32);
	}

    /**
     * Construct a ScaleFilter.
     * @param width the width to scale to
     * @param height the height to scale to
     */
	public ScaleFilter( int width, int height ) {
		this.width = width;
		this.height = height;
	}

    public int[] filter( int[] src, int w, int h) {
        int[] dst = new int[width * height];
        
        Bitmap srcBitmap = Bitmap.createBitmap(src, w, h, Bitmap.Config.ARGB_8888);
        Bitmap dstBitmap = Bitmap.createScaledBitmap(srcBitmap, width, height, false);
        dstBitmap.getPixels(dst, 0, width, 0, 0, width, height);
        
        srcBitmap.recycle();
        dstBitmap.recycle();
        
        return dst;
    }

	public String toString() {
		return "Distort/Scale";
	}

}
