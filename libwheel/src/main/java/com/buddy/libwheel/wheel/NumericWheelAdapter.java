/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.buddy.libwheel.wheel;

/**
 * Numeric Wheel adapter.
 */
public class NumericWheelAdapter implements WheelAdapter {

  /** The default min value */
  public static final int DEFAULT_MAX_VALUE = 40;

  /** The default max value */
  private static final int DEFAULT_MIN_VALUE = 0;

  // Values
  private int minValue;
  private int maxValue;

  // format
  private String format;

  private int step = 1;

  /**
   * Default constructor
   */
  public NumericWheelAdapter() {
    this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
  }

  /**
   * Constructor
   *
   * @param minValue the wheel min value
   * @param maxValue the wheel max value
   */
  public NumericWheelAdapter(int minValue, int maxValue) {
    this(minValue, maxValue, null);
    if (maxValue > 9) {
      format = "%02d";
    } else {
      format = "%d";
    }
  }

  public NumericWheelAdapter(int minValue, int maxValue, int step) {
    this(minValue, maxValue, null);
    if (step == 0) {
      step = 1;
    }
    this.step = step;
  }

  /**
   * Constructor
   *
   * @param minValue the wheel min value
   * @param maxValue the wheel max value
   * @param format the format string
   */
  public NumericWheelAdapter(int minValue, int maxValue, String format) {
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.format = format;
  }

  @Override public String getItem(int index) {
    if (index >= 0 && index < getItemsCount()) {
      //int value = minValue + index ;
      int value = minValue + index * step;
      return format != null ? String.format(format, value) : Integer.toString(value);
    }
    return null;
  }

  @Override public int getItemsCount() {
    int i = step;
    if (step == 0) {
      i = 1;
    }
    return (maxValue - minValue) / i + 1;
  }

  @Override public int getMaximumLength() {
    int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
    int maxLen = Integer.toString(max).length();
    if (minValue < 0) {
      maxLen++;
    }
    return maxLen;
  }

  /* (non-Javadoc)
   * @see cookerbjx.view.wheel.WheelAdapter#getItems()
   */
  @Override public int[] getItems() {

    int[] array = new int[getItemsCount()];
    for (int i=0; i<array.length; i++) {
      array[i] = minValue + i * step;
    }
    return array;
  }

  public void setRange(int i, int maxDay) {
    minValue = i;
    maxValue = maxDay;
  }
}
