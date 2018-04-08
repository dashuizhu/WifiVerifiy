package com.verifywifi.agreement;

import android.util.Log;
import com.hwangjr.rxbus.RxBus;
import com.verifywifi.AppConstants;
import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.Myhex;

/**
 * 数据解析
 *
 * @author zhuj 2018/4/8 上午10:01.
 */
public class CmdParse {

  private final static String TAG = CmdParse.class.getSimpleName();
  static int count;

  public static void parse(byte[] buff, int length) {
    String str = Myhex.buffer2String(buff, length);
    Log.w(TAG, "parse: " + str);
    count++;

    DataBean bean = new DataBean();
    bean.setCmd(" " + count);
    bean.setTime(System.currentTimeMillis());
    RxBus.get().post(AppConstants.RXBUS_PUSH, bean);
  }
}
