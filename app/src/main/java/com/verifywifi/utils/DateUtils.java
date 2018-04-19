package com.verifywifi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhuj 2018/4/9 上午11:06.
 */
public class DateUtils {

  private final static SimpleDateFormat sSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private final static SimpleDateFormat sSDF2 = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");

  /**
   *
   * @param time yyyy-MM-dd HH:mm
   * @return
   */
  public static long getTimeMill(String time) {
    Date date;
    try {
      date = sSDF.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
      date = new Date();
    }
    return date.getTime();
  }

  public static long getTimeMillByMill(String time) {
    Date date;
    try {
      date = sSDF2.parse(time);
    } catch (ParseException e) {
      e.printStackTrace();
      date = new Date();
    }
    return date.getTime();
  }


  /**
   * 年月日时分
   * @param startTime
   * @return
   */
  public static int[] getDate(String startTime) {
    int[] buff = new int[6];
    Date date;
    try {
      date = sSDF.parse(startTime);
    } catch (ParseException e) {
      e.printStackTrace();
      date = new Date();
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    buff[0] = calendar.get(Calendar.YEAR);
    buff[1] = calendar.get(Calendar.MONTH)+1;
    buff[2] = calendar.get(Calendar.DAY_OF_MONTH);
    buff[3] = calendar.get(Calendar.HOUR_OF_DAY);
    buff[4] = calendar.get(Calendar.MINUTE);
    buff[5] = calendar.get(Calendar.SECOND);
    return buff;
  }

  public static String getDateString(Date date) {
    return sSDF.format(date);
  }
}
