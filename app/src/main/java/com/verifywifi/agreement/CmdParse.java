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

  private final static byte CMD_DATA = 0x01;

  private final static String TAG = CmdParse.class.getSimpleName();

  public static void parse(byte[] buff) {
    String str = MyHexUtils.buffer2String(buff);
    MyLog.w(TAG, "parse: " + str);

    switch (buff[0]) {
      case CMD_DATA:
        DataBean bean = new DataBean();
        bean.setCmd(" " + MyHexUtils.buffer2String(buff));
        bean.setTime(System.currentTimeMillis());
        RxBus.get().post(AppConstants.RXBUS_PUSH, bean);
        break;
    }

  }
}
