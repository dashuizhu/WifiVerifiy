package com.verifywifi.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.WIFI_SERVICE;

/**
 * @author zhuj 2018/4/8 上午10:28.
 */
public class AppUtils {

  private final static SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final static DecimalFormat mDoubleFormat = new DecimalFormat("#.00");

  /**
   *
   * 获取WIFI下ip地址
   */
  public static String getLocalIpAddress(Context context) {
    WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    // 获取32位整型IP地址
    int ipAddress = wifiInfo.getIpAddress();

    //返回整型地址转换成“*.*.*.*”地址
    return String.format("%d.%d.%d.%d",
            (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
            (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
  }

  /**
   * make true current connect service is wifi
   * @param mContext
   * @return
   */
  public static boolean isWifi(Context mContext) {
    ConnectivityManager connectivityManager = (ConnectivityManager) mContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
    if (activeNetInfo != null
            && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
      return true;
    }
    return false;
  }

  /**
   * yy-MM-dd HH:mm:ss
   * @param time
   * @return
   */
  public static String timeFormat(long time) {
    return mFormat.format(new Date(time));
  }


  public static String doubleFormat(double d) {
    return mDoubleFormat.format(d);
  }
}
