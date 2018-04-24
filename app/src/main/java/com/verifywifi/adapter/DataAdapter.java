package com.verifywifi.adapter;

import com.verifywifi.R;
import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.AppUtils;
import java.util.Collection;

/**
 * @author zhuj 2018/4/11 下午1:51.
 */
public class DataAdapter extends BaseRecyclerAdapter<DataBean> {

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
    holder.text(R.id.tv_torque,  model.getTorquValue());

    holder.text(R.id.tv_state, model.isState() ? "通过" : "未通过");
    holder.textColorId(R.id.tv_state, model.isState() ? R.color.text_light : R.color.orange);

    holder.textColorId(R.id.tv_angle, getColor(model.getAngleState()));
    holder.textColorId(R.id.tv_torque, getColor(model.getTorqueState()));
  }

  private int getColor(int state) {
    int color;
    switch (state) {
      case 2:
        color = R.color.red;
        break;
      case 1:
        color = R.color.orange;
        break;
      case 0:
      default:
        //正常
        color = R.color.text_light_gray;
        break;
    }
    return color;
  }
}
