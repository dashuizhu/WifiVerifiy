package com.verifywifi.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.verifywifi.R;
import com.verifywifi.utils.AppUtils;

public class MainActivity extends AppCompatActivity {

  private final static String TAG_HOME = "home";
  private final static String TAG = MainActivity.class.getSimpleName();

  @BindView(R.id.layout_contains) LinearLayout mLayoutContains;

  private HomeFragment mHomeFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    if (savedInstanceState != null) {
      Log.e(TAG, " 这时候不为空  需要恢复FragmentManager 里保存的 fragment");
      mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
    }

    initViews();

    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
  }

  private void initViews() {

    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    if (mHomeFragment == null) {
      mHomeFragment = new HomeFragment();
      transaction.add(R.id.layout_contains, mHomeFragment, TAG_HOME);
    } else {
      transaction.show(mHomeFragment);
    }
    transaction.commitAllowingStateLoss();
  }

  @Override
  protected void onDestroy() {
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    super.onDestroy();
  }

  @Override
  protected void onStart() {
    if (!AppUtils.isWifi(this)) {
      Toast.makeText(this, "请先连接wifi", Toast.LENGTH_LONG).show();
    }
    super.onStart();
  }
}
