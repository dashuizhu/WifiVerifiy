package com.verifywifi.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qqtheme.framework.picker.DateTimePicker;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.verifywifi.R;
import com.verifywifi.adapter.BaseRecyclerAdapter;
import com.verifywifi.adapter.SmartViewHolder;
import com.verifywifi.bean.DataBean;
import com.verifywifi.ui.presenter.DatePresenter;
import com.verifywifi.utils.AppUtils;
import com.verifywifi.utils.DateUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * 搜索
 *
 * @author zhuj 2018/4/9 上午10:13
 */
public class SearchFragment extends BaseFragment {

  @BindView(R.id.btn_time_start) Button mBtnTimeStart;
  @BindView(R.id.btn_time_end) Button mBtnTimeEnd;
  @BindView(R.id.recyclerView) FastScrollRecyclerView mRecyclerView;
  @BindView(R.id.refreshLayout) SmartRefreshLayout mRefreshLayout;
  @BindView(R.id.btn_date_start) Button mBtnDateStart;
  @BindView(R.id.btn_date_end) Button mBtnDateEnd;
  @BindView(R.id.btn_query) Button mBtnQuery;

  private Unbinder mUnbinder;
  private BaseRecyclerAdapter<DataBean> mAdapter;
  private List<DataBean> mBeanList = new ArrayList<>();

  private DatePresenter mPresenter;

  private long mQueryEndTime;

  public SearchFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_search, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    initViews();
    return view;
  }

  @Override
  public void onDestroyView() {
    mUnbinder.unbind();
    super.onDestroyView();
  }

  private void initViews() {

    mPresenter = new DatePresenter(this);

    mRefreshLayout.setEnableRefresh(false);
    mRefreshLayout.setEnableLoadmore(true);
    mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
      @Override
      public void onLoadmore(RefreshLayout refreshlayout) {
        if (mAdapter.getData() == null || mAdapter.getData().size() == 0) {
          return;
        }
        long time = mAdapter.getData().get(mAdapter.getData().size() - 1).getTime();
        mPresenter.queryList(time, mQueryEndTime);
      }
    });

    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
    ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    mAdapter = new BaseRecyclerAdapter<DataBean>(mBeanList, R.layout.recycler_home, null) {
      @Override
      protected void onBindViewHolder(SmartViewHolder holder, DataBean model, int position) {
        holder.text(R.id.tv_cmd, model.getCmd());
        holder.text(R.id.tv_time, AppUtils.timeFormat(model.getTime()));
      }
    };
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.getAdapter().notifyDataSetChanged();

    //mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    //  @Override
    //  public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    //    super.onScrollStateChanged(recyclerView, newState);
    //    switch (newState) {
    //      //空闲状态
    //      case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
    //        int position = mLayoutManager.findFirstVisibleItemPosition();
    //        if (!mIsTop && position == 0) {
    //          mIsTop = true;
    //        } else if (mIsTop && position != 0) {
    //          mIsTop = false;
    //        }
    //        break;
    //      //滚动状态
    //      case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
    //        //触摸后滚动
    //      case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
    //        mIsTop = false;
    //        break;
    //      default:
    //    }
    //  }
    //});

    Date date = new Date();
    mBtnDateStart.setText(DateUtils.getDateString(date));
    mBtnDateEnd.setText(DateUtils.getDateString(date));
  }

  @OnClick({ R.id.btn_date_start, R.id.btn_time_start, R.id.btn_date_end, R.id.btn_time_end })
  public void onViewClicked(View view) {
    DateTimePicker picker;
    int[] time;
    switch (view.getId()) {
      case R.id.btn_date_start:
        picker = new DateTimePicker(getActivity(), DateTimePicker.HOUR_24);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setTimeRangeEnd(0, 0);
        picker.setTimeRangeEnd(23, 59);
        picker.setCycleDisable(false);
        picker.setTopLineColor(0x99FF0000);
        //picker.setLabelTextColor(0xFFFF0000);
        picker.setDividerColor(0xFFFF0000);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
          @Override
          public void onDateTimePicked(String year, String month, String day, String hour,
                  String minute) {
            //showToast(year + "-" + month + "-" + day + " " + hour + ":" + minute);
            mBtnDateStart.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
          }
        });

        String startTime = mBtnDateStart.getText().toString();
        time = DateUtils.getDate(startTime);
        picker.setSelectedItem(time[0], time[1], time[2], time[3], time[4]);

        picker.show();

        break;
      case R.id.btn_time_start:
        break;
      case R.id.btn_date_end:
        picker = new DateTimePicker(getActivity(), DateTimePicker.HOUR_24);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setTimeRangeEnd(0, 0);
        picker.setTimeRangeEnd(23, 59);
        picker.setTopLineColor(0x99FF0000);
        picker.setCycleDisable(false);
        picker.setDividerColor(0xFFFF0000);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
          @Override
          public void onDateTimePicked(String year, String month, String day, String hour,
                  String minute) {
            //showToast(year + "-" + month + "-" + day + " " + hour + ":" + minute);
            mBtnDateEnd.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute);
          }
        });

        String endTime = mBtnDateEnd.getText().toString();
        time = DateUtils.getDate(endTime);
        picker.setSelectedItem(time[0], time[1], time[2], time[3], time[4]);

        picker.show();
        break;
      case R.id.btn_time_end:
        break;
      default:
    }
  }

  @OnClick(R.id.btn_query)
  public void onViewClicked() {
    String startTime = mBtnDateStart.getText().toString();
    String endTime = mBtnDateEnd.getText().toString();
    long startlong = DateUtils.getTimeMill(startTime);
    long endlong = DateUtils.getTimeMill(endTime);

    if (startlong >= endlong) {
      showToast(R.string.toast_time_start_big_end);
      return;
    }
    mQueryEndTime = endlong;
    mAdapter.getData().clear();
    mAdapter.notifyDataSetChanged();
    mRefreshLayout.setLoadmoreFinished(false);
    mPresenter.queryListShowProgress(startlong, endlong);
  }

  @Override
  public void hideProgress() {
    super.hideProgress();
    if (mRefreshLayout != null) {
      mRefreshLayout.finishLoadmore();
    }
  }

  @Override
  public void onSuccess(Object successObj) {
    super.onSuccess(successObj);
    if (successObj instanceof List) {
      int oldIndex = mAdapter.getData().size();
      List<DataBean> list = (List<DataBean>) successObj;
      mBeanList.addAll(list);
      mAdapter.getData().addAll(list);
      mAdapter.notifyItemRangeInserted(oldIndex, list.size());

      if (list.size() < DatePresenter.PAGE_SIZE) {
        mRefreshLayout.setLoadmoreFinished(true);
      }
    }
  }
}
