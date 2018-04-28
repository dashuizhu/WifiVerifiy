package com.verifywifi.adapter;

import android.view.View;
import com.verifywifi.R;
import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.AppUtils;
import java.util.Collection;

/**
 * @author zhuj 2018/4/11 下午1:51.
 */
public class DataAdapter extends BaseRecyclerAdapter<DataBean> {

  /**
   * 全部显示
   */
  private final int FILTER_ALL = 0;
  /**
   * 只显示不良 ，偏高和偏低 都算不良
   */
  private final int FILTER_BAD = 1;
  /**
   * 偏高
   */
  private final int FILTER_HIGH = 2;
  /**
   * 偏低
   */
  private final int FILTER_LOW = 3;

  /**
   * 0全部， 1不良， 2 高  3低
   */
  private int angleStateFilter, torqueStateFilter;

  public DataAdapter(Collection<DataBean> collection) {
    super(collection, R.layout.recycler_home);
  }

  @Override
  protected void onBindViewHolder(SmartViewHolder holder, DataBean model, int position) {
    //holder.text(R.id.tv_state, model.get());
    holder.text(R.id.tv_name, model.getName());
    holder.text(R.id.tv_vin, model.getVin());
    holder.text(R.id.tv_time, AppUtils.timeFormat(model.getTime()));
    holder.text(R.id.tv_angle, "" + model.getAngle());
    holder.text(R.id.tv_torque, model.getTorquValue());

    holder.text(R.id.tv_state, model.isState() ? "通过" : "未通过");
    holder.textColorId(R.id.tv_state, model.isState() ? R.color.text_light : R.color.orange);

    holder.textColorId(R.id.tv_angle, getColor(model.getAngleState()));
    holder.textColorId(R.id.tv_torque, getColor(model.getTorqueState()));

    boolean isAngleShow = isFilter(angleStateFilter, model.getAngleState());
    boolean isTorqueShow = isFilter(torqueStateFilter, model.getTorqueState());

    boolean isShow = isAngleShow && isTorqueShow;
    holder.itemView.findViewById(R.id.layout_data).setVisibility(isShow ? View.VISIBLE : View.GONE);
  }

  private int getColor(int state) {
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
    return color;
  }

  private boolean isFilter(int filter, int state) {
    boolean isShow = true;
    switch (filter) {
      case FILTER_ALL:
        break;
      case FILTER_BAD:
        isShow = (DataBean.STATE_HIGH == state || DataBean.STATE_LOW == state);
        break;
      case FILTER_HIGH:
        isShow = (DataBean.STATE_HIGH == state);
        break;
      case FILTER_LOW:
        isShow = (DataBean.STATE_LOW == state);
        break;
      default:
    }
    return isShow;
  }

  public void setTorqueFilter(int torqueFilter) {
    torqueStateFilter = torqueFilter;
    notifyDataSetChanged();
  }

  public void setAngleFilter(int angleFilter) {
    angleStateFilter = angleFilter;
    notifyDataSetChanged();
  }
}
