package com.verifywifi.agreement;

import com.hwangjr.rxbus.RxBus;
import com.verifywifi.AppConstants;
import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.DateUtils;
import com.verifywifi.utils.MyHexUtils;
import com.verifywifi.utils.MyLog;

/**
 * 数据解析
 *
 * @author zhuj 2018/4/8 上午10:01.
 */
public class CmdParse {

  private final static byte CMD_DATA_F = 0x46;
  private final static byte CMD_DATA_D = 0x4d;

  private final static String TAG = CmdParse.class.getSimpleName();

  public static void parse(byte[] buff) {
    //String str = MyHexUtils.buffer2String(buff);
    //MyLog.w(TAG, "parse: " + str);

    byte[] buffer;
    String str = null;
    int value;
    switch (buff[0]) {
      case CMD_DATA_F:
      case CMD_DATA_D:
        DataBean bean = new DataBean();

        //name 25字节
        buffer = new byte[25];
        System.arraycopy(buff, 1, buffer, 0, buffer.length);
        String name = MyHexUtils.buffer2String(buffer);
        bean.setName(name);

        //VIN PF25 ， PM 40
        buffer = new byte[25];
        System.arraycopy(buff, 26, buffer, 0, buffer.length);
        String vin = MyHexUtils.buffer2String(buffer);
        bean.setVin(vin);

        //时间 yyyy-MM-dd:HH:mm:ss
        buffer = new byte[19];
        System.arraycopy(buff, 51, buffer, 0, buffer.length);
        str = MyHexUtils.buffer2String(buffer);
        long time = DateUtils.getTimeMillByMill(str);
        bean.setTime(time);

        //id号
        buffer = new byte[10];
        System.arraycopy(buff, 70, buffer, 0, buffer.length);
        str = MyHexUtils.buffer2String(buffer);
        bean.setId(str);

        //状态
        byte state= buff[82];
        bean.setState(state);

        buffer = new byte[6];
        System.arraycopy(buff, 83, buffer, 0, buffer.length);
        str = MyHexUtils.buffer2String(buffer);
        value = Integer.parseInt(str);
        bean.setTorque(value);

        byte torqueState = buff[89];
        bean.setTorqueState(torqueState);

        buffer = new byte[5];
        System.arraycopy(buff, 94, buffer, 0, buffer.length);
        str = MyHexUtils.buffer2String(buffer);
        value = Integer.parseInt(str);
        bean.setAngle(value);


        byte angleState = buff[104];
        bean.setAngleState(angleState);

        bean.setCmd(" " + MyHexUtils.buffer2String(buff));
        bean.setTime(System.currentTimeMillis());
        RxBus.get().post(AppConstants.RXBUS_PUSH, bean);
        break;

      default:
    }

  }
}
