package com.verifywifi.agreement;

/**
 * @author zhuj 2018/4/11 上午9:38.
 */
public class CmdPackage {

  /**
   * 回复协议
   */
  public static byte[] receiveCMD(byte type) {
    byte[] buff = new byte[1];
    buff[0] = type;
    return buff;
  }
}
