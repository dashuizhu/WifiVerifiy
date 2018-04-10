package com.verifywifi;

import android.support.annotation.IntDef;
import com.verifywifi.bean.StateBean;

/**
 * @author zhuj 2018/4/8 上午11:40.
 */
public class AppConstants {

  /**
   * 端口
   */
  public final static int SERVER_PORT = 56677;

  public static final String RXBUS_PUSH = "rx_push";
  public static final String RXBUS_STATE = "rx_state";


  @IntDef({ StateBean.CONNECT, StateBean.NONE, StateBean.WAIT })
  public @interface State {
  }

}
