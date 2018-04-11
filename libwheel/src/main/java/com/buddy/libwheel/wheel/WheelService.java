package com.buddy.libwheel.wheel;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class WheelService {
	
	
//
//
//	/**
//	 * @param context
//	 * @param max
//	 * @param min
//	 * @param select
//	 * @param name
//	 * @param textSize
//	 * @param density  屏幕的密度 。 1920* 1080的密度是  3.0，
//	 * @return
//	 */
//	public static WheelView getBinInt(Context context,int max,int min,int select,String name,int textSize, float density) {
//		WheelView minus = new WheelView(context);
//
//		//设置字体最大值，
//		Log.v("wheelService" ,"字体大小"+ textSize +" " + density );
//		int maxSize = 20 + (int) (20*density);
//		if(textSize>maxSize) textSize=maxSize;
//
//		minus.setTextSize(textSize);
//		minus.setAdapter(new NumericWheelAdapter(min, max));
//		minus.setLabel(name);
//		minus.setCurrentItem(select);
//		minus.setCyclic(true);
//		minus.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//
//		return minus;
//	}
//
//	public static WheelView getBinInt(Context context,int max,int min,int select,String name,int textSize) {
//		WheelView minus = new WheelView(context);
//		if(textSize>55) textSize=55;
//		minus.setTextSize(textSize);
//		minus.setAdapter(new NumericWheelAdapter(min, max));
//		minus.setLabel(name);
//		minus.setCurrentItem(select);
//		minus.setCyclic(true);
//		minus.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//
//		return minus;
//	}
//
//
//	public static WheelView getBinIntFormat(Context context,int max,int min,int select,String name,int textSize,String format) {
//
//		WheelView minus = new WheelView(context);
//		if(textSize>55) textSize=55;
//		minus.setTextSize(textSize);
//		minus.setAdapter(new NumericWheelAdapter(min, max,format));
//		minus.setLabel(name);
//		minus.setCurrentItem(select);
//		minus.setCyclic(true);
//		minus.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//
//		return minus;
//	}
//
//
//	public static WheelView getBinIntStep(Context context,int max,int min,int select,String name,int textSize,int step ) {
//
//		WheelView minus = new WheelView(context);
//		if(textSize>55) textSize=55;
//		minus.setTextSize(textSize);
//		minus.setAdapter(new NumericWheelAdapter(min, max,step));
//		minus.setLabel(name);
//		minus.setCurrentItem(select);
//		minus.setCyclic(true);
//		minus.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//
//		return minus;
//	}
//
//
////	public static WheelView getBinIntList(Context context, int select,int haspre,String name,int textSize) {
////
////		WheelView minus = new WheelView(context);
////		if(textSize>55) textSize=55;
////		minus.setTextSize(textSize);
////		minus.setAdapter(new NumericWheelListAdapter(haspre));
////		minus.setLabel(name);
////		minus.setCyclic(true);
////		minus.setCurrentItem(getindex(select,minus.getAdapter().getItems()));
////		minus.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
////
////		return minus;
////	}
//
//
//
//
//	private static int getindex(int taget,int[] items) {
//		// TODO Auto-generated method stub
//		for (int i = 0; i < items.length; i++) {
//			if( taget == items[i]){
//				return i;
//			}
//
//		}
//		return 0;
//	}
//
////	public static WheelView getBinInt(Context context,int max,int min,int select,String name) {
////
////		WheelView minus = new WheelView(context);
////
////		minus.setAdapter(new NumericWheelAdapter(min, max));
////		minus.setLabel(name);
////		minus.setCurrentItem(select);
////		minus.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
////
////		return minus;
////	}
//
//	public static WheelView getBinString(Context context,String[] strs,int select,String name, int textSize, float density) {
//		WheelView minus = new WheelView(context);
//
//		//设置字体最大值，
//		Log.v("wheelService" ,"字体大小"+ textSize +" " + density );
//		int maxSize = 20 + (int) (20*density);
//		if(textSize>maxSize) textSize=maxSize;
//
//		minus.setTextSize(textSize);
//		minus.setAdapter(new ArrayWheelAdapter<String>(strs));
//		minus.setLabel(name);
//		minus.setCurrentItem(select);
//		return minus;
//	}
//	public static void updateData(WheelView temp_WheelView, int temp,
//			int minTemp, int maxTemp) {
//		temp_WheelView.setAdapter(new NumericWheelAdapter(minTemp,maxTemp));
//		temp_WheelView.setCurrentItem(temp);
//	}
//
//	public static void updateDataStep(WheelView temp_WheelView, int temp,
//			int minTemp, int maxTemp,int step ) {
//		temp_WheelView.setAdapter(new NumericWheelAdapter(minTemp,maxTemp,step));
//		temp_WheelView.setCurrentItem(temp);
//	}
//
////	public static void updateDataList(WheelView temp_WheelView, int temp,
////			int haspre ) {
////		temp_WheelView.setAdapter(new NumericWheelListAdapter(haspre));
////
////		temp_WheelView.setCurrentItem(getindex(temp, temp_WheelView.getAdapter().getItems()));
////	}
	
}
