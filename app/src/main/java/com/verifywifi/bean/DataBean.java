package com.verifywifi.bean;

import lombok.Data;

/**
 * @author zhuj 2018/4/8 上午11:42.
 */
@Data
public class DataBean {

  private long time;
  private String cmd;
  private String vin;
  private String name;
  /**
   * 数据id
   */
  private String id;
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
   */
  private int angleState;

  public double getTorquValue() {
    return  torque / 100.00d;
  }

}
