package com.verifywifi.agreement;

import com.hwangjr.rxbus.RxBus;
import com.verifywifi.AppConstants;
import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.MyHexUtils;
import com.verifywifi.utils.MyLog;

/**
 * 数据解析
 *
 * @author zhuj 2018/4/8 上午10:01.
 */
public class CmdParse {

  private final static String TAG = CmdParse.class.getSimpleName();
  static int count;

  public static void parse(byte[] buff, int length) {
    String str = MyHexUtils.buffer2String(buff, length);
    MyLog.w(TAG, "parse: " + str);
    count++;

    DataBean bean = new DataBean();
    bean.setCmd(" " + MyHexUtils.buffer2String(buff, length));
    bean.setTime(System.currentTimeMillis());
    RxBus.get().post(AppConstants.RXBUS_PUSH, bean);
  }
}
