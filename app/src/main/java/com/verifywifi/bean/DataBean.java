package com.verifywifi.bean;

import com.verifywifi.utils.AppUtils;
import lombok.Data;

/**
 * @author zhuj 2018/4/8 上午11:42.
 */
@Data
public class DataBean {


  public final static int STATE_LOW = 0;
  public final static int STATE_NORMAL = 1;
  public final static int STATE_HIGH = 2;


  private long time;
  private String vin;
  private String name;
  /**
   * 数据节点id
   */
  private String nodeId;
  /**
   * 状态
   */
  private boolean state;
  /**
   * 螺栓号
   */
  private String bolt;

  /**
   * 扭矩
   */
  private int torque;

  /**
   * 扭矩状态
   */
  private int torqueState;

  /**
   * 角度值
   */
  private int angle;

  /**
   * 角度状态
   * 0偏低， 1正常， 2偏高
   */
  private int angleState;

  public String getTorquValue() {
    double value = torque / 100.0d;
    return AppUtils.doubleFormat(value);
  }
}
