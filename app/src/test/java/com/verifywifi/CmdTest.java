package com.verifywifi;

import com.verifywifi.agreement.CmdParse;
import com.verifywifi.utils.MyHexUtils;
import org.junit.Test;

/**
 * @author zhuj 2018/4/19 上午10:10.
 */
public class CmdTest {

  @Test
  public void test() {
    String cmd =
            "CC 5C 7F 46 33 4c 5a 20 57 53 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 48 42 33 39 37 33 32 34 2D 30 30 43 4E 32 30 2D 30 30 30 42 30 32 20 20 20 31 39 39 30 2D 30 31 2D 30 31 3A 30 30 3A 34 33 3A 34 32 20 20 20 20 20 20 30 30 31 32 33 34 35 36 37 38 20 20 30 30 31 35 31 32 30 32 30 30 30 31 30 31 CD";
    byte[] bb = MyHexUtils.hexStringToByte(cmd);

    byte[] b2 = new byte[bb.length-3];
    System.arraycopy(bb, 3, b2, 0, b2.length);

    CmdParse.parse(b2);
  }
}
