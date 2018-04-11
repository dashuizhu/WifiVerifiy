package com.buddy.libwheel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import com.buddy.libwheel.utils.WheelDateUtils;
import com.buddy.libwheel.wheel.NumericWheelAdapter;
import com.buddy.libwheel.wheel.OnWheelScrollListener;
import com.buddy.libwheel.wheel.WheelView;
import java.util.Calendar;

/**
 * 类型必传， 年月日看对应类型
 * {@link #KEY_RECYCLER_YEAR}   年
 * {@link #KEY_RECYCLER_MONTH}  月
 * {@link #KEY_RECYCLER_DAY}    日
 * {@link #KEY_RECYCLER_TYPE} 类型 对应上面三种值
 *
 */
public class WheelDateActivity extends Activity implements View.OnClickListener {

  //显示年月
  public final static int RECYCLER_ALL = 0;
  //显示年月日
  public final static int RECYCLER_YEAR = 1;
  //显示日
  public final static int RECYCLER_MONTH = 2;

  public static final String KEY_RECYCLER_TYPE = "type";
  public static final String KEY_RECYCLER_YEAR = "year";
  public static final String KEY_RECYCLER_MONTH = "month";
  public static final String KEY_RECYCLER_DAY = "day";

  private int MAX_YEAR = 2020;
  private int MIN_YEAR = 1920;

  private WheelView mWheelViewDay;
  private WheelView mWheelViewYear;
  private WheelView mWheelViewMonth;
  private int mYear, mMonth, mDay;
  private int mRecyclerType;
  private NumericWheelAdapter mDayAdapter;
  //年月滑动变化监听
  private OnWheelScrollListener mWheelScrollListener;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wheel_date);
    getWindow().setGravity(Gravity.BOTTOM);
    getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    initViews();
    initWheel();
  }

  private void initViews() {

    mWheelViewDay = findViewById(R.id.wheelView_day);
    mWheelViewMonth = findViewById(R.id.wheelView_month);
    mWheelViewYear = findViewById(R.id.wheelView_year);

    findViewById(R.id.tv_cancel).setOnClickListener(this);
    findViewById(R.id.tv_confrim).setOnClickListener(this);

    Calendar calendar = Calendar.getInstance();
    //默认4岁
    int nowYear = calendar.get(Calendar.YEAR);
    int nowMonth = calendar.get(Calendar.MONTH);
    int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
    mRecyclerType = getIntent().getIntExtra(KEY_RECYCLER_TYPE, RECYCLER_ALL);
    mYear = getIntent().getIntExtra(KEY_RECYCLER_YEAR, nowYear);
    mMonth = getIntent().getIntExtra(KEY_RECYCLER_MONTH, nowMonth);
    mDay = getIntent().getIntExtra(KEY_RECYCLER_DAY, nowDay);

    if (mMonth > 12 || mMonth < 0) {
      mMonth = nowMonth;
    }
    if (mDay <= 0 || mDay > 31) {
      mDay = nowDay;
    }
    MAX_YEAR = nowYear + 5;
    MIN_YEAR = nowYear;
    if (mYear > MAX_YEAR || mYear < MIN_YEAR) {
      mYear = nowYear;
    }
  }

  private void initWheel() {
    switch (mRecyclerType) {
      case RECYCLER_YEAR:
        mWheelViewYear.setVisibility(View.GONE);
        break;
      case RECYCLER_ALL:
        mWheelViewYear.setVisibility(View.VISIBLE);
        mWheelViewDay.setVisibility(View.VISIBLE);
        break;
      case RECYCLER_MONTH:
        mWheelViewYear.setVisibility(View.GONE);
        mWheelViewMonth.setVisibility(View.GONE);
        break;
      default:
    }

    initWheelDay(mYear, mMonth);

    mWheelViewYear.setAdapter(new NumericWheelAdapter(MIN_YEAR, MAX_YEAR));
    mWheelViewYear.setCyclic(true);
    mWheelViewYear.setCurrentItem(mYear - MIN_YEAR);

    mWheelViewMonth.setAdapter(new NumericWheelAdapter(1, 12));
    mWheelViewMonth.setCyclic(true);
    mWheelViewMonth.setCurrentItem(mMonth - 1);

    mWheelScrollListener = new OnWheelScrollListener() {
      @Override
      public void onScrollingStarted(WheelView wheel) {

      }

      @Override
      public void onScrollingFinished(WheelView wheel) {
        int year = Integer.valueOf(mWheelViewYear.getCurrentValue());
        int month = Integer.valueOf(mWheelViewMonth.getCurrentValue());
        //重新获取 日的最大值
        initWheelDay(year, month);
      }
    };
    mWheelViewMonth.addScrollingListener(mWheelScrollListener);
    mWheelViewYear.addScrollingListener(mWheelScrollListener);
  }

  /**
   * 根据年月 刷新日
   */
  private void initWheelDay(int year, int month) {
    int maxDay = 31;
    //只选日的话， 最大日跟月份有关系
    if (mRecyclerType == RECYCLER_ALL) {
      maxDay = WheelDateUtils.getMaxDayByMonth(year, month);
    } else if (mRecyclerType == RECYCLER_YEAR) {
      //如果只是选 月日， 二月要算29
      maxDay = WheelDateUtils.getMaxDayByMonth(2016, month);
    }
    if (mDayAdapter == null) {
      //初始化 日期控件
      mDayAdapter = new NumericWheelAdapter(1, maxDay);
      if (mDay > maxDay) {
        mDay = maxDay;
      }
      mWheelViewDay.setAdapter(mDayAdapter);
      mWheelViewDay.setCurrentItem(mDay - 1);
      mWheelViewDay.setCyclic(true);
    } else if (maxDay != mDayAdapter.getItemsCount()) {//当前日期范围，跟之前选中的 有变化
      //下表从0开始
      int nowDay = mWheelViewDay.getCurrentItem() + 1;
      //重新设置范围
      mDayAdapter.setRange(1, maxDay);
      //避免最大值溢出
      if (nowDay > maxDay) {
        mWheelViewDay.setCurrentItem(maxDay - 1);
      }
      //刷新
      mWheelViewDay.notifyDateSetChange();
    }
  }

  @Override
  public void onClick(View v) {
    int i = v.getId();
    if (i == R.id.tv_cancel) {
      finish();
    } else if (i == R.id.tv_confrim) {
      Intent intent = getIntent();
      int year = Integer.parseInt(mWheelViewYear.getCurrentValue());
      int month = Integer.parseInt(mWheelViewMonth.getCurrentValue());
      int day = Integer.parseInt(mWheelViewDay.getCurrentValue());
      intent.putExtra(KEY_RECYCLER_YEAR, year);
      intent.putExtra(KEY_RECYCLER_MONTH, month);
      intent.putExtra(KEY_RECYCLER_DAY, day);
      setResult(RESULT_OK, intent);
      finish();
    }
  }
}
