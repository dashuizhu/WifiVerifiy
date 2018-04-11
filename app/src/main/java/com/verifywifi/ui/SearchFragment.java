package com.verifywifi.ui;

import android.app.Activity;
import android.content.Intent;
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
import com.buddy.libwheel.WheelDateTimeActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.verifywifi.R;
import com.verifywifi.adapter.DataAdapter;
import com.verifywifi.bean.DataBean;
import com.verifywifi.ui.presenter.DatePresenter;
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

  /**
   * 获取结束时间
   */
  private static final int ACTIVITY_END_TIME = 11;
  private static final int ACTIVITY_START_TIME = 12;

  @BindView(R.id.btn_time_start) Button mBtnTimeStart;
  @BindView(R.id.btn_time_end) Button mBtnTimeEnd;
  @BindView(R.id.recyclerView) FastScrollRecyclerView mRecyclerView;
  @BindView(R.id.refreshLayout) SmartRefreshLayout mRefreshLayout;
  @BindView(R.id.btn_date_start) Button mBtnDateStart;
  @BindView(R.id.btn_date_end) Button mBtnDateEnd;
  @BindView(R.id.btn_query) Button mBtnQuery;

  private Unbinder mUnbinder;
  private DataAdapter mAdapter;

  private DatePresenter mPresenter;

  private long mQueryEndTime;

  public SearchFragment() {
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

    mAdapter = new DataAdapter(new ArrayList<DataBean>());
    //mAdapter.setOnItemClickListener(this);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.getAdapter().notifyDataSetChanged();

    Date date = new Date();
    mBtnDateStart.setText(DateUtils.getDateString(date));
    mBtnDateEnd.setText(DateUtils.getDateString(date));
  }

  @OnClick({ R.id.btn_date_start, R.id.btn_date_end })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_date_start:
        String startTime = mBtnDateStart.getText().toString();
        startActivityForResult(getDateTimeIntent(startTime), ACTIVITY_START_TIME);
        break;
      case R.id.btn_date_end:
        String endTime = mBtnDateEnd.getText().toString();
        startActivityForResult(getDateTimeIntent(endTime), ACTIVITY_END_TIME);
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
      mAdapter.getData().addAll(list);
      mAdapter.notifyItemRangeInserted(oldIndex, list.size());

      if (list.size() < DatePresenter.PAGE_SIZE) {
        mRefreshLayout.setLoadmoreFinished(true);
      }
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode != Activity.RESULT_OK) {
      return;
    }
    switch (requestCode) {
      case ACTIVITY_END_TIME:
        mBtnDateEnd.setText(getStrByDate(data));
        break;
      case ACTIVITY_START_TIME:
        mBtnDateStart.setText(getStrByDate(data));
        break;
      default:
    }
  }

  private Intent getDateTimeIntent(String str) {
    int[] time = DateUtils.getDate(str);
    Intent intent = new Intent(getContext(), WheelDateTimeActivity.class);
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
}
