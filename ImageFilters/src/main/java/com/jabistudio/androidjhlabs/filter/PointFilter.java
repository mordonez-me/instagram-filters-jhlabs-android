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

/**
 * An abstract superclass for point filters. The interface is the same as the old RGBImageFilter.
 */
public abstract class PointFilter {
	private int width;
	private int height;
	
	protected boolean canFilterIndexColorModel = false;

    public int[] filter( int[] src ,int w, int h) {
    	width = w;
    	height = h;

        setDimensions( width, height);
        
		int[] inPixels = new int[width];
		int[] outPixels = new int[width * height];
		
        for ( int y = 0; y < height; y++ ) {
        	int index = 0;
        	for(int i=(y*width);i<((y*width) + width);++i){
        		inPixels[index] = src[i];
        		index++;
        	}
			
			for ( int x = 0; x < width; x++ ){
				inPixels[x] = filterRGB( x, y, inPixels[x] );
			}			
			
			index = 0;
			for(int i=(y*width);i<((y*width) + width);++i){
				outPixels[i] = inPixels[index];
        		index++;
        	}			
        }

        return outPixels;
    }

	public void setDimensions(int w, int h) {
		width = w;
    	height = h;
	}

	public abstract int filterRGB(int x, int y, int rgb);
}
