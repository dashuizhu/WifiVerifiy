package com.verifywifi.ui;

import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import com.hwangjr.rxbus.RxBus;
import com.verifywifi.view.MyProgressDialog;

/**
 * @author zhuj 2018/4/9 上午10:52.
 */
public class BaseFragment extends Fragment implements IBaseView {

  private Toast mToast;

  private MyProgressDialog mProgressDialog;

  private boolean isRxRegister;

  @Override
  public void showProgress() {
    if (mProgressDialog == null) {
      synchronized (this) {
        if (mProgressDialog == null) {
          mProgressDialog = new MyProgressDialog(getContext());
        }
      }
    }
    if (!mProgressDialog.isShowing()) {
      mProgressDialog.show();
    }
  }

  @Override
  public void hideProgress() {
    if (mProgressDialog != null) {
      if (mProgressDialog.isShowing()) {
        mProgressDialog.dismiss();
      }
    }
  }

  @Override
  public void onError(Throwable e) {
    hideProgress();
    e.printStackTrace();
    if (getActivity() == null || getActivity().isFinishing()) {
      return;
    }
  }

  @Override
  public void onLoadError(Throwable e) {
    onError(e);
  }

  @Override
  public void onSuccess(Object successObj) {

  }

  @Override
  public void onDestroy() {
    hideProgress();
    unRegisterRxBus();
    super.onDestroy();
  }

  protected void registerRxBus() {
    if (!isRxRegister) {
      synchronized (this) {
        if (!isRxRegister) {
          RxBus.get().register(this);
          isRxRegister = true;
        }
      }
    }
  }

  protected void unRegisterRxBus() {
    if (isRxRegister) {
      synchronized (this) {
        if (isRxRegister) {
          RxBus.get().unregister(this);
          isRxRegister = false;
        }
      }
    }
  }

  public void showToast(String str) {
    if (mToast == null) {
      mToast = Toast.makeText(getContext(), str, Toast.LENGTH_LONG);
    }
    mToast.setText(str);
    mToast.setDuration(Toast.LENGTH_LONG);
    mToast.show();
  }

  public void showToast(@StringRes int resId) {
    showToast(getString(resId));
  }
}
