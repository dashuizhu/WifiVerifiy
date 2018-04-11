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
    holder.text(R.id.tv_cmd, model.getCmd());
    holder.text(R.id.tv_time, AppUtils.timeFormat(model.getTime()));
  }
}
