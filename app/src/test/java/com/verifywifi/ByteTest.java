package com.verifywifi;

import com.verifywifi.utils.MyHexUtils;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import rx.functions.Action1;

/**
 * @author zhuj 2018/4/10 下午4:25.
 */
public class ByteTest {

  @Test
  public void testByte() {
    byte[] busrc = new byte[] { 0x01, 0x02, 0x03, 0x04 };

    byte[] budet = new byte[3];

    budet [2] = (byte) -2;

    //System.arraycopy(busrc, 1, budet, 0, 2);
    //System.out.println(MyHexUtils.buffer2String(busrc));
    System.out.println(MyHexUtils.buffer2String(budet));
    //testSync(5);
    //rx.Observable.timer(1, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
    //  @Override
    //  public void call(Long aLong) {
    //    testSync(4);
    //  }
    //});
    //
    //rx.Observable.interval(500, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
    //  @Override
    //  public void call(Long aLong) {
    //    if (aLong ==5) {
    //      long time1 = System.currentTimeMillis();
    //      test();
    //      System.out.println("test start " + (System.currentTimeMillis() - time1) );
    //    }
    //  }
    //});
    //
    //try {
    //  Thread.sleep(5000);
    //} catch (InterruptedException e) {
    //  e.printStackTrace();
    //}
  }

  private synchronized void testSync(int count) {
    count--;
    System.out.println(" now " + count);
    long time1 = System.currentTimeMillis();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        test();
      }
    }
    System.out.println(" now over" + (System.currentTimeMillis() - time1));
  }

  private void test() {
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
