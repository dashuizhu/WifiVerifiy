package com.verifywifi.ui.presenter;

import com.verifywifi.bean.DataBean;
import com.verifywifi.database.DataDao;
import com.verifywifi.ui.BasePresenter;
import com.verifywifi.ui.IBaseView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/4/9 上午11:09.
 */
public class DatePresenter extends BasePresenter {

  public final static int PAGE_SIZE = 200;

  private IBaseView mBaseView;

  public DatePresenter(IBaseView baseView) {
    mBaseView = baseView;
  }

  /**
   * 显示进度条
   * @param startLong
   * @param endTime
   */
  public void queryListShowProgress(long startLong, long endTime) {
    mBaseView.showProgress();
    queryList(startLong, endTime);
  }

  public void queryList(final long startlong, final long endlong) {
    Observable.create(new ObservableOnSubscribe<List<DataBean>>() {
      @Override
      public void subscribe(ObservableEmitter<List<DataBean>> emitter) throws Exception {
        RealmResults<DataDao> results = DataDao.queryList(startlong, endlong);

        List<DataBean> list = new ArrayList<>();
        if (results == null || !results.isValid()) {
          emitter.onNext(list);
          return;
        }
        int count = 0;
        //因为是倒序查询的， add(0
        for (DataDao dao : results) {
          list.add( dao.castBean());
          count++;
          if (count >= PAGE_SIZE) {
            break;
          }
        }
        emitter.onNext(list);
      }
    })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<List<DataBean>>() {
              @Override
              public void onSubscribe(Disposable d) {
                addDisposable(d);
              }

              @Override
              public void onNext(List<DataBean> dataBeans) {
                mBaseView.hideProgress();
                mBaseView.onSuccess(dataBeans);
              }

              @Override
              public void onError(Throwable e) {
                mBaseView.onError(e);
              }

              @Override
              public void onComplete() {

              }
            });
  }
}
