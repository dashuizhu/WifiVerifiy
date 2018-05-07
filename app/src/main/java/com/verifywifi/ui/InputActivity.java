package com.verifywifi.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.buddy.libwheel.WheelDateTimeActivity;
import com.hwangjr.rxbus.RxBus;
import com.verifywifi.AppConstants;
import com.verifywifi.R;
import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.DateUtils;
import com.verifywifi.utils.ToastUtils;
import java.util.Date;

/**
 * 手动添加数据
 *
 * @author zhuj 2018/5/7 上午10:44
 */
public class InputActivity extends Activity {

  private final int ACTIVITY_TIME = 11;

  @BindView(R.id.btn_time) Button mBtnTime;
  @BindView(R.id.et_name) EditText mEtName;
  @BindView(R.id.et_vin) EditText mEtVin;
  @BindView(R.id.et_torque) EditText mEtTorque;
  @BindView(R.id.et_angle) EditText mEtAngle;
  @BindView(R.id.et_nodeId) EditText mEtNodeId;
  @BindView(R.id.et_bolt) EditText mEtBolt;
  @BindView(R.id.spinner_torque) Spinner mSpinnerTorque;
  @BindView(R.id.spinner_angle) Spinner mSpinnerAngle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input);
    ButterKnife.bind(this);
    setTitle("添加数据");

    initViews();
  }

  private void initViews() {
    Date date = new Date();
    mBtnTime.setText(DateUtils.getDateString(date));

    mSpinnerAngle.setSelection(1);
    mSpinnerTorque.setSelection(1);

    mSpinnerTorque.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int resColor = getTextColor(position);
        mEtTorque.setTextColor(resColor);
        TextView tv = (TextView) view;
        tv.setTextColor(resColor);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    mSpinnerAngle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int resColor = getTextColor(position);
        mEtAngle.setTextColor(resColor);
        TextView tv = (TextView) view;
        tv.setTextColor(resColor);
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  @OnClick(R.id.tv_back)
  public void onBack() {
    finish();
  }

  @OnClick(R.id.btn_time)
  public void onTimeClicked() {
    String startTime = mBtnTime.getText().toString();
    startActivityForResult(getDateTimeIntent(startTime), ACTIVITY_TIME);
  }

  @OnClick(R.id.tv_confirm)
  public void onViewClicked() {
    String name = mEtName.getText().toString().trim();
    String vin = mEtVin.getText().toString().trim();
    String torque = mEtTorque.getText().toString().trim();
    String angle = mEtAngle.getText().toString().trim();
    String nodeId = mEtNodeId.getText().toString().trim();
    String bolt = mEtBolt.getText().toString().trim();

    if (TextUtils.isEmpty(name)) {
      ToastUtils.showToast("名字不可为空");
      mEtName.requestFocus();
      return;
    }
    if (TextUtils.isEmpty(vin)) {
      ToastUtils.showToast("VIN不可为空");
      mEtVin.requestFocus();
      return;
    }
    if (TextUtils.isEmpty(nodeId)) {
      ToastUtils.showToast("id不可为空");
      mEtNodeId.requestFocus();
      return;
    }
    //if (TextUtils.isEmpty(vin)) {
    //  ToastUtils.showToast("VIN不可为空");
    //  return;
    //}
    if (TextUtils.isEmpty(torque)) {
      ToastUtils.showToast("扭矩不可为空");
      mEtTorque.requestFocus();
      return;
    }
    if (TextUtils.isEmpty(angle)) {
      ToastUtils.showToast("角度不可为空");
      mEtAngle.requestFocus();
      return;
    }

    double torqueDou;
    try {
      torqueDou = Double.parseDouble(torque);
    } catch (Exception e) {
      ToastUtils.showToast("扭矩值不正确");
      mEtTorque.setText("");
      mEtTorque.requestFocus();
      e.printStackTrace();
      return;
    }

    int angleInt;
    try {
      angleInt = Integer.parseInt(angle);
    } catch (Exception e) {
      ToastUtils.showToast("角度值不正确");
      mEtAngle.setText("");
      mEtAngle.requestFocus();
      e.printStackTrace();
      return;
    }

    DataBean bean = new DataBean();
    bean.setName(name);
    bean.setVin(vin);
    bean.setAngle(angleInt);
    bean.setNodeId(nodeId);
    bean.setBolt(bolt);

    int torqueInt = (int) (torqueDou * 100);
    bean.setTorque(torqueInt);

    String date = mBtnTime.getText().toString();
    bean.setTime(DateUtils.getTimeMill(date));

    int torqueState = mSpinnerTorque.getSelectedItemPosition();
    int angleState = mSpinnerAngle.getSelectedItemPosition();
    bean.setTorqueState(torqueState);
    bean.setAngleState(angleState);

    bean.setState(torqueState == DataBean.STATE_NORMAL && angleState == DataBean.STATE_NORMAL);

    RxBus.get().post(AppConstants.RXBUS_PUSH, bean);
    finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    if (requestCode == ACTIVITY_TIME) {
      mBtnTime.setText(getStrByDate(data));
    }
  }

  private Intent getDateTimeIntent(String str) {
    int[] time = DateUtils.getDate(str);
    Intent intent = new Intent(this, WheelDateTimeActivity.class);
    intent.putExtra(WheelDateTimeActivity.KEY_RECYCLER_YEAR, time[0]);
    intent.putExtra(WheelDateTimeActivity.KEY_RECYCLER_MONTH, time[1]);
    intent.putExtra(WheelDateTimeActivity.KEY_RECYCLER_DAY, time[2]);
    intent.putExtra(WheelDateTimeActivity.KEY_RECYCLER_HOUR, time[3]);
    intent.putExtra(WheelDateTimeActivity.KEY_RECYCLER_MINUTE, time[4]);
    intent.putExtra(WheelDateTimeActivity.KEY_RECYCLER_SECOND, time[5]);
    return intent;
  }

  private String getStrByDate(Intent data) {
    int year = data.getIntExtra(WheelDateTimeActivity.KEY_RECYCLER_YEAR, 2018);
    int month = data.getIntExtra(WheelDateTimeActivity.KEY_RECYCLER_MONTH, 1);
    int day = data.getIntExtra(WheelDateTimeActivity.KEY_RECYCLER_DAY, 1);
    int hour = data.getIntExtra(WheelDateTimeActivity.KEY_RECYCLER_HOUR, 1);
    int minute = data.getIntExtra(WheelDateTimeActivity.KEY_RECYCLER_MINUTE, 1);
    int second = data.getIntExtra(WheelDateTimeActivity.KEY_RECYCLER_SECOND, 1);

    String str =
            String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);
    return str;
  }

  private int getTextColor(int state) {
    int color;
    switch (state) {
      case DataBean.STATE_HIGH:
        //偏高
        color = R.color.red;
        break;
      case DataBean.STATE_LOW:
        //偏低
        color = R.color.low_blue;
        break;
      case DataBean.STATE_NORMAL:
      default:
        //正常
        color = R.color.text_light_gray;
        break;
    }
    return ContextCompat.getColor(this, color);
  }
}
