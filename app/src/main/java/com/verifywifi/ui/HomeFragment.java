package com.verifywifi.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.verifywifi.AppConstants;
import com.verifywifi.MyApplication;
import com.verifywifi.R;
import com.verifywifi.adapter.BaseRecyclerAdapter;
import com.verifywifi.adapter.SmartViewHolder;
import com.verifywifi.bean.DataBean;
import com.verifywifi.database.DataDao;
import com.verifywifi.utils.AppUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {

  private final String TAG = this.getClass().getSimpleName();

  /**
   * 缓冲刷新数据个数，  累计到一定数量，才刷新ui
   */
  private final int CACHE_SIZE = 50;

  /**
   * 单位秒，若数据暂停了，而在缓存队列中， 最后一个数据的时间，再延迟一定时间，直接显示
   */
  private final int DELAY_ADD_TIME = 2;

  /**
   * 最大显示个数
   */
  private final int LIST_MAX = 5000;
  /**
   * 达到最大显示个数时，  删除尾巴个数
   */
  private final int LIST_REMOVE_SIZE = 2000;

  @BindView(R.id.tv_content) TextView mTvContent;
  @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
  @BindView(R.id.refreshLayout) SmartRefreshLayout mRefreshLayout;

  private Unbinder mUnbinder;
  private List<DataBean> mBeanList = new ArrayList<>();
  private BaseRecyclerAdapter mAdapter;

  /**
   * 避免刷新太快， 累计一定数量数据，再添加刷新
   */
  private List<DataBean> mCacheList = new ArrayList<>();

  /**
   * 是否已经滑到顶
   */
  private boolean mIsTop = true;
  private LinearLayoutManager mLayoutManager;

  private Disposable mDelayAdd;

  public HomeFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
          Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    mUnbinder = ButterKnife.bind(this, view);
    initViews();
    RxBus.get().register(this);
    return view;
  }

  @Override
  public void onDestroyView() {
    mUnbinder.unbind();
    RxBus.get().unregister(this);
    super.onDestroyView();
  }

  //推送，有消息来了
  @Subscribe(thread = EventThread.IO, tags = @Tag(AppConstants.RXBUS_PUSH))
  public void onPushMessage(DataBean bean) {

    if (mAdapter.getData().size() < CACHE_SIZE) {
      Observable.just(bean)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Consumer<DataBean>() {
                @Override
                public void accept(DataBean dataBean) throws Exception {
                  mAdapter.getData().add(0, dataBean);
                  mAdapter.notifyItemInserted(0);
                  DataDao.saveOrUpdate(dataBean);
                  mRecyclerView.scrollToPosition(0);
                }
              });
    } else {

      synchronized (mCacheList) {
        mCacheList.add(0, bean);
      }
      if (mCacheList.size() >= CACHE_SIZE) {
        addCacheList();
      } else {
        startDelayaAdd();
      }
    }
  }

  @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(AppConstants.RXBUS_STATE))
  public void onPushConenct(Integer integer) {
    if (MyApplication.getApp().getWifiService() != null) {
      mTvContent.setText(MyApplication.getApp().getWifiService().getStateStr());
    }
  }

  @OnClick(R.id.tv_content)
  public void onScorllTop() {
    mRecyclerView.scrollToPosition(0);
    mIsTop = true;
  }

  private void initViews() {

    if (MyApplication.getApp().getWifiService() != null) {
      mTvContent.setText(MyApplication.getApp().getWifiService().getStateStr());
    }

    mRefreshLayout.setEnableRefresh(false);
    mRefreshLayout.setEnableLoadmore(true);
    mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
      @Override
      public void onLoadmore(RefreshLayout refreshlayout) {
        refreshlayout.setLoadmoreFinished(true);
      }
    });

    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    mLayoutManager = new LinearLayoutManager(getContext());
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
    ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setAddDuration(20);
    ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

    mAdapter = new BaseRecyclerAdapter<DataBean>(mBeanList, R.layout.recycler_home, this) {
      @Override
      protected void onBindViewHolder(SmartViewHolder holder, DataBean model, int position) {
        holder.text(R.id.tv_cmd, model.getCmd());
        holder.text(R.id.tv_time, AppUtils.timeFormat(model.getTime()));
      }
    };
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.getAdapter().notifyDataSetChanged();

    mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        Log.e(TAG, " scroll STATE : " + newState);
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
          case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
            int position = mLayoutManager.findFirstVisibleItemPosition();
            if (!mIsTop && position == 0) {
              mIsTop = true;
            } else if (mIsTop && position != 0) {
              mIsTop = false;
            }
            break;
          case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
          case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
            mIsTop = false;
            break;
        }
      }
    });
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

  }

  /**
   * 添加缓存的数据 到列表
   */
  private void addCacheList() {
    Observable.just(mCacheList)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<DataBean>>() {
              @Override
              public void accept(List<DataBean> dataBeans) throws Exception {
                long time1 = System.currentTimeMillis();
                synchronized (mCacheList) {
                  mAdapter.getData().addAll(0, dataBeans);
                  mAdapter.notifyItemRangeInserted(0, dataBeans.size());
                  DataDao.saveOrUpdate(mCacheList);
                  mCacheList.clear();
                  Log.e(TAG, "sync " + (System.currentTimeMillis() - time1));
                }
                //超过最大显示数据量时， 移除尾巴数据
                if (mAdapter.getData().size() > LIST_MAX) {
                  long time2 = System.currentTimeMillis();
                  mAdapter.removeHalf(LIST_REMOVE_SIZE);
                  Log.e(TAG, "delete " + (System.currentTimeMillis() - time2));
                }
                //是否自动刷新 ，滑到顶部
                if (mIsTop) {
                  mRecyclerView.scrollToPosition(0);
                }
              }
            });
  }

  /**
   * 避免缓存的数据丢失， 坐一个延迟添加显示
   */
  private void startDelayaAdd() {
    if (mDelayAdd != null && !mDelayAdd.isDisposed()) {
      mDelayAdd.dispose();
      mDelayAdd = null;
    }
    mDelayAdd = Observable.timer(DELAY_ADD_TIME, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
      @Override
      public void accept(Long aLong) throws Exception {
        addCacheList();
      }
    });
  }
}
