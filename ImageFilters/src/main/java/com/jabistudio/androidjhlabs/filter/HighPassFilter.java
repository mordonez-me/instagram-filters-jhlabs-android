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
 * A filter which adds Gaussian blur to an image, producing a glowing effect.
 * @author Jerry Huxtable
 */
public class HighPassFilter extends GaussianFilter {
	
	public HighPassFilter() {
		radius = 10;
	}
	
    public int[] filter( int[] src ,int w, int h) {
        int width = w;
        int height = h;

        int[] inPixels = new int[width*height];
        int[] originalPixels = new int[width*height];
        int[] outPixels = new int[width*height];
        
        inPixels = src;
        for(int i=0;i<src.length;++i){
        	originalPixels[i] = src[i];
        }
        
		if ( radius > 0 ) {
			convolveAndTranspose(kernel, inPixels, outPixels, width, height, alpha, alpha && premultiplyAlpha, false, CLAMP_EDGES);
			convolveAndTranspose(kernel, outPixels, inPixels, height, width, alpha, false, alpha && premultiplyAlpha, CLAMP_EDGES);
		}

		outPixels = originalPixels;

		int index = 0;
		for ( int y = 0; y < height; y++ ) {
			for ( int x = 0; x < width; x++ ) {
				int rgb1 = outPixels[index];
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;

				int rgb2 = inPixels[index];
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;

				r1 = (r1 + 255-r2) / 2;
				g1 = (g1 + 255-g2) / 2;
				b1 = (b1 + 255-b2) / 2;

				inPixels[index] = (rgb1 & 0xff000000) | (r1 << 16) | (g1 << 8) | b1;
				index++;
			}
		}

        return inPixels;
    }

	public String toString() {
		return "Blur/High Pass...";
	}
}
