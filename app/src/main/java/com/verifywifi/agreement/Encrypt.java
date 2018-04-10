package com.verifywifi.agreement;

import com.verifywifi.utils.MyByteUtils;
import com.verifywifi.utils.MyHexUtils;
import com.verifywifi.utils.MyLog;

/**
 * @author zhuj 2018/4/10 下午3:33.
 */
public class Encrypt {

  private final static String TAG = Encrypt.class.getSimpleName();

  public static final byte CMD_HEAD = (byte) 0xCC;
  public static final byte CMD_END = (byte) 0xCD;

  /**
   * 当前缓存的数据长度， 收到的字节个数， 在构成一条协议数据时 清0
   */
  private int data_index;

  /**
   * 在构成一条协议数据时 清空
   */
  private byte[] data_command = new byte[1024];

  /**
   * 将收到的数据 截取符合协议的子集
   */
  public synchronized void processDataCommand(byte[] command, int length) {
    if (command == null || length == 0) {
      return;
    }
    MyLog.d(TAG, "收到数据~:" + MyHexUtils.buffer2String(command, length));
    //解析数据达到缓存值时处理
    if (data_index + length >= 1024) {
      byte[] newArray = new byte[100];
      //将最后100个字节数据，复制到最前面， 从头开始累计字节
      System.arraycopy(data_command, data_index - 100, newArray, 0, 100);
      data_command = new byte[1024];
      System.arraycopy(newArray, 0, data_command, 0, 100);
      data_index = 100;
    }

    //将收到的cmd 数据， 复制到缓存的数组data_command ，接在最后面
    System.arraycopy(command, 0, data_command, data_index, length);
    data_index += length;
    //最短协议长度
    if (data_index < 3) {
      return;
    }
    //协议首字母，下标位置
    int headIndex = 0;

    for (int index = 0; index < data_index; index++) {
      if (data_command[index] == CMD_HEAD) { // 收到头码
        headIndex = index;
      } else if (data_command[index] == CMD_END) {

        int cmdLength = MyByteUtils.byteToInt(data_command[headIndex + 1]);
        //+3 是因为 包头 、长度、 校验位、包尾、
        if (index - headIndex == cmdLength + 3) {
          //+3 +1
          processData(data_command, headIndex, cmdLength + 4);

          //把剩余没解析完的数据，加入缓存池，清空原来数据
          byte[] otherBuff = new byte[data_index - (index + 1)];
          System.arraycopy(data_command, index + 1, otherBuff, 0, otherBuff.length);

          data_index = 0;
          data_command = new byte[1024];
          processDataCommand(otherBuff, otherBuff.length);

          //另外一种写法
          //int oldDataIndex = data_index;
          ////记录新的数据长度
          //data_index = oldDataIndex - (index + 1);
          ////清除后面的缓存无效数据
          //for (int i = data_index; i < oldDataIndex; i++) {
          //  data_command[i] = 0;
          //}
          //MyLog.w(TAG, data_index
          //        + " after : "
          //        + MyHexUtils.buffer2String(data_command, data_index + 20));
          break;
        }
      }
    }
  }

  private void processData(byte[] buffer, int start, int length) {
    byte[] bbb = new byte[length];
    System.arraycopy(buffer, start, bbb, 0, bbb.length);
    CmdParse.parse(bbb, length);
  }
}
