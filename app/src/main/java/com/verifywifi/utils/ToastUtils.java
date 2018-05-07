package com.verifywifi.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;
import com.verifywifi.MyApplication;

/**
 * @author zhuj 2018/5/7 上午10:50.
 */
public class ToastUtils {

  private static volatile Toast mToast;

  private static Toast getInstance() {
    if (mToast == null) {
      synchronized (ToastUtils.class) {
        if (mToast == null) {
          mToast = Toast.makeText(MyApplication.getApp(), "", Toast.LENGTH_LONG);
        }
      }
    }
    return mToast;
  }

  public static void showToast(String str) {
    getInstance();
    mToast.setText(str);
    mToast.setDuration(Toast.LENGTH_LONG);
    mToast.show();
  }

  public static void showToast(@StringRes int resId) {
    showToast(MyApplication.getApp().getString(resId));
  }
}
