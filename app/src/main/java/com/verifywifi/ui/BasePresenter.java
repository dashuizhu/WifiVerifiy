package com.verifywifi.ui;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author zhuj 2018/4/9 上午11:34.
 */
public class BasePresenter {

  private CompositeDisposable mDisposables;

  protected void addDisposable(Disposable disposable) {
    if (disposable == null) {
      return;
    }
    if (mDisposables == null) {
      mDisposables = new CompositeDisposable();
    }
    mDisposables.add(disposable);
  }

  protected void dispose(Disposable disposable) {
    if (mDisposables != null) {
      mDisposables.delete(disposable);
    }
  }

  protected void destroy() {
    if (mDisposables != null) {
      mDisposables.clear();
    }
  }
}
