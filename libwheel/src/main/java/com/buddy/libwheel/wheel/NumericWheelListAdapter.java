/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.buddy.libwheel.wheel;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelListAdapter implements WheelAdapter {
	

	
	
	// format
	private String format;
	
//	public  int[] minuteItems = {0, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29
//						 ,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59};
//	
//	public  int[] items = {0,30,40,50,60,70,80,90,92,94,96,98,100,102,104,106,108,110,120,130,140,150,160};
	
//	private int[] nopreitems = {0,30,40,50,60,70,80,90,92,94,96,98,100,102,104,106,108,110,120,130,140,150,160};	
//	
//	private int[] nopreitemslow = {0,30,40,50,60,70,80,90,92,94,96,98,100,102,104,106};
//	
//	private int[] nopreitemshigh = {108,110,120,130,140,150,160};
//	private int[] preitems = {0,30,40,50,60,70,80,90,95,100,103,106,109,112,115,118};
	
	private int [] items;
	
	/**
	 * Default constructor
	 */
//	public NumericWheelListAdapter(int haspre) {//1有压力，2无压力，低温，3无压力高温      //4  DIYminute 0，5-60
//		if (haspre == 1) {
//			this.items = preitems;
//		}else {
//			if (haspre == 2) {//低温段
//				this.items = nopreitemslow;	
//			}else if (haspre == 3) {//高温段
//				this.items = nopreitemshigh;
//			}else if (haspre ==4) {
//				this.items = minuteItems;
//			}
//			else {
//				this.items = nopreitems;
//			} 
//			
//		}
//	}
	
	public NumericWheelListAdapter(int[] items) {
		this.items = items;
	}
	

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			//int value = minValue + index ;
			int value = items[index];
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return items.length;
	}
	
	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(items[items.length - 1]), Math.abs(items[0]));//Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (items[0] < 0) {
			maxLen++;
		}
		return maxLen;
	}
	
	@Override
	public int[] getItems() {
		return this.items;
	}
}
