package com.verifywifi.bean;

import android.support.annotation.IntDef;
import com.verifywifi.AppConstants;
import lombok.Data;

/**
 * @author zhuj 2018/4/8 下午2:29.
 */
@Data
public class StateBean {

  public static final int NONE = 0;
  public static final int WAIT = 1;
  public static final int CONNECT = 2;

  //private @AppConstants.State int state;
  //private String content;
  //
  //private StateBean() {}
  //
  //public StateBean(@AppConstants.State int state) {
  //  this.state = state;
  //}
  //
  //public StateBean(@AppConstants.State int state, String str) {
  //  this.state = state;
  //  content = str;
  //}
  //
  //public String getStateStr() {
  //  String str = "";
  //  switch (state) {
  //    case NONE:
  //      str = "未开启";
  //      break;
  //    case WAIT:
  //      str = "等待连接";
  //      break;
  //    case CONNECT:
  //      str = content;
  //      break;
  //  }
  //  return str;
  //}
}
