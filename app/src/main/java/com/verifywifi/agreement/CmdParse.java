package com.verifywifi.agreement;

import com.hwangjr.rxbus.RxBus;
import com.verifywifi.AppConstants;
import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.DateUtils;
import com.verifywifi.utils.MyHexUtils;

/**
 * 数据解析
 *
 * @author zhuj 2018/4/8 上午10:01.
 */
public class CmdParse {

  private final static byte CMD_DATA_F = 0x46;
  private final static byte CMD_DATA_M = 0x4d;

  private final static String TAG = CmdParse.class.getSimpleName();

  public static void parse(byte[] buff) {
    String str2 = MyHexUtils.buffer2String(buff);
    //MyLog.w(TAG, "parse: " + str);

    DataBean bean = null;
    try {
      switch (buff[0]) {
        case CMD_DATA_F:
          bean = parseF(buff);
          break;
        case CMD_DATA_M:
          bean = parseM(buff);
          break;

        default:
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (bean != null) {
      RxBus.get().post(AppConstants.RXBUS_PUSH, bean);
    }
  }

  /**
   * 解析
   */
  private static DataBean parseF(byte[] buff) {
    byte[] buffer;
    String str = null;
    int value;
    int startIndex = 1;

    DataBean bean = new DataBean();
    //name 25字节
    buffer = new byte[25];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    String name = new String(buffer).trim();
    bean.setName(name);
    startIndex += buffer.length;

    //VIN PF25 ， PM 40
    buffer = new byte[25];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    String vin = new String(buffer).trim();
    bean.setVin(vin);
    startIndex += buffer.length;

    //时间 yyyy-MM-dd:HH:mm:ss
    buffer = new byte[19];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer).trim();
    long time = DateUtils.getTimeMillByMill(str);
    bean.setTime(time);
    startIndex += buffer.length;

    //id号
    buffer = new byte[10];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer);
    bean.setNodeId(str);
    startIndex += buffer.length;

    //螺栓号
    buffer = new byte[2];
    buffer[0] = buff[startIndex];
    buffer[1] = buff[startIndex + 1];
    str = new String(buffer);
    bean.setBolt(str);
    startIndex += 2;

    //状态
    bean.setState(buff[startIndex] == 0x31);
    startIndex += 1;

    //扭矩值
    buffer = new byte[6];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer);
    value = Integer.parseInt(str);
    bean.setTorque(value);
    startIndex += buffer.length;

    byte torqueState = buff[startIndex];
    // 0x30 0x31 0x32 分别代表 0、1、2
    bean.setTorqueState(torqueState - 0x30);
    startIndex += 1;

    buffer = new byte[5];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer);
    value = Integer.parseInt(str);
    bean.setAngle(value);
    startIndex += buffer.length;

    byte angleState = buff[startIndex];
    // 0x30 0x31 0x32 分别代表 0、1、2
    bean.setAngleState(angleState - 0x30);
    startIndex += 1;
    return bean;
  }

  /**
   * 解析
   */
  private static DataBean parseM(byte[] buff) {
    byte[] buffer;
    String str = null;
    int value;
    int startIndex = 1;

    DataBean bean = new DataBean();
    //name 25字节
    buffer = new byte[25];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    String name = new String(buffer).trim();
    bean.setName(name);
    startIndex += buffer.length;

    //VIN PF25 ， PM 40
    buffer = new byte[40];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    String vin = new String(buffer).trim();
    bean.setVin(vin);
    startIndex += buffer.length;

    //时间 yyyy-MM-dd:HH:mm:ss
    buffer = new byte[19];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer).trim();
    long time = DateUtils.getTimeMillByMill(str);
    bean.setTime(time);
    startIndex += buffer.length;

    //id号
    buffer = new byte[10];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer);
    bean.setNodeId(str);
    startIndex += buffer.length;

    //螺栓号
    buffer = new byte[2];
    buffer[0] = buff[startIndex];
    buffer[1] = buff[startIndex + 1];
    str = new String(buffer);
    bean.setBolt(str);
    startIndex += 2;

    //状态
    bean.setState(buff[startIndex] == 0x31);
    startIndex += 1;

    //扭矩值
    buffer = new byte[7];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer);
    value = Integer.parseInt(str);
    bean.setTorque(value);
    startIndex += buffer.length;

    byte torqueState = buff[startIndex];
    // 0x30 0x31 0x32 分别代表 0、1、2
    bean.setTorqueState(torqueState - 0x30);
    startIndex += 1;

    buffer = new byte[7];
    System.arraycopy(buff, startIndex, buffer, 0, buffer.length);
    str = new String(buffer);
    value = Integer.parseInt(str);
    bean.setAngle(value);
    startIndex += buffer.length;

    byte angleState = buff[startIndex];
    // 0x30 0x31 0x32 分别代表 0、1、2
    bean.setAngleState(angleState - 0x30);
    startIndex += 1;
    return bean;
  }
}
