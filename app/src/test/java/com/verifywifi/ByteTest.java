package com.verifywifi;

import com.verifywifi.utils.MyHexUtils;
import org.junit.Test;

/**
 * @author zhuj 2018/4/10 下午4:25.
 */
public class ByteTest {

  @Test
  public void testByte() {
    byte[] busrc  = new byte[] {0x01, 0x02, 0x03, 0x04};

    byte[] budet = new byte[3];

    System.arraycopy(busrc, 1, budet, 0, 2);
    System.out.println(MyHexUtils.buffer2String(busrc));
    System.out.println(MyHexUtils.buffer2String(budet));
    testSync(5);
  }

  private synchronized  void testSync(int count) {
    count --;
    System.out.println(" now " + count);
    if (count>0) {
      for (int i= 0; i<count; i++) {
        if (i ==3) {
          testSync(count);
          break;
        }
      }
    }
  }
}
