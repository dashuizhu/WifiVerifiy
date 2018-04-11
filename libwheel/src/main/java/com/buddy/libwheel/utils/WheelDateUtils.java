package com.buddy.libwheel.utils;

/**
 * @author zhuj 2018/4/11 上午11:45.
 */
public class WheelDateUtils {

  /**
   * 获得指定时期的  单月最大 日
   * @param year
   * @param month
   * @return
   */
  public static int getMaxDayByMonth(int year, int month) {
    int maxDay = 31;
    switch (month) {
      case 2:
        if (year % 100 != 0 && year % 4 == 0) {
          maxDay = 29;
        } else {
          maxDay = 28;
        }
        break;
      case 4:
      case 6:
      case 9:
      case 11:
        maxDay = 30;
        break;
    }
    return maxDay;
  }

}
