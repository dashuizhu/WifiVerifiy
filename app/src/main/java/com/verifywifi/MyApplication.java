package com.verifywifi;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
//import com.crashlytics.android.Crashlytics;
import com.hwangjr.rxbus.RxBus;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
//import com.umeng.commonsdk.UMConfigure;
import com.verifywifi.database.BuddyRealm;
//import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * @author zhuj 2018/4/8 上午9:28.
 */
public class MyApplication extends Application {

  private WifiServerService mWifiServerService;
  private static MyApplication sApp;

  @Override
  public void onCreate() {
    super.onCreate();

    sApp = this;
    Intent intent = new Intent(this, WifiServerService.class);
    bindService(intent, mConnection, BIND_AUTO_CREATE);

    Realm.init(this);
    BuddyRealm.setDefaultRealmForUser("wifiSystem");

    //UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);

    //Fabric.with(this, new Crashlytics());

    //UMConfigure.setLogEnabled(true);
  }

  public WifiServerService getWifiService() {
    return mWifiServerService;
  }

  public static MyApplication getApp() {
    return sApp;
  }

  ServiceConnection mConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mWifiServerService = ((WifiServerService.LocalBinder) service).getService();

      //mconnect 过程又延迟，service已经存在，并且rxbus通知了homeFragment， 而这里service还是为null
      // 到这里再通知一次，HomeFragment刷新UI
      RxBus.get().post(AppConstants.RXBUS_STATE, mWifiServerService.getWorkSate());
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mWifiServerService = null;
    }
  };

  static {
    ClassicsFooter.REFRESH_FOOTER_ALLLOADED = "没有更多了";
    //设置全局的Header构建器
    SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
      @Override
      public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
        return new ClassicsHeader(context).setTextSizeTime(13)
                .setTextSizeTitle(14)
                .setSpinnerStyle(SpinnerStyle.Translate);
      }
    });
    //设置全局的Footer构建器
    SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
      @Override
      public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
        //指定为经典Footer，默认是 BallPulseFooter
        return new ClassicsFooter(context).setTextSizeTitle(14)
                .setSpinnerStyle(SpinnerStyle.Translate);
      }
    });
  }
}
