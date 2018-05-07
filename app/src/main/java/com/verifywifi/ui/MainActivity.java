package com.verifywifi.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.verifywifi.R;
import com.verifywifi.utils.AppUtils;
import com.verifywifi.utils.MyLog;

/**
 * 主页
 *
 * @author zhuj 2018/4/11 上午10:16
 */
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

  private final static String TAG = MainActivity.class.getSimpleName();

  private final static String TAG_HOME = "home";
  private final static String TAG_SEARCH = "search";

  @BindView(R.id.layout_contains) LinearLayout mLayoutContains;
  @BindView(R.id.tabLayout) TabLayout mTabLayout;

  private HomeFragment mHomeFragment;
  private SearchFragment mSearchFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    if (savedInstanceState != null) {
      MyLog.e(TAG, " 这时候不为空  需要恢复FragmentManager 里保存的 fragment");
      mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(TAG_HOME);
      mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(TAG_SEARCH);
    }

    mTabLayout.addOnTabSelectedListener(this);
    initViews();
  }

  private void initViews() {
    onTabSelected(mTabLayout.getTabAt(0));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onStop() {
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    super.onStop();
  }

  @Override
  protected void onStart() {
    super.onStart();
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    if (!AppUtils.isWifi(this)) {
      Toast.makeText(this, "请先连接wifi", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onTabSelected(TabLayout.Tab tab) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    hideAllFragment(fragmentTransaction);
    showFragment(tab.getPosition(), fragmentTransaction);
    fragmentTransaction.commitAllowingStateLoss();
  }

  @Override
  public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override
  public void onTabReselected(TabLayout.Tab tab) {

  }

  private void showFragment(int position, FragmentTransaction fragmentTransaction) {
    switch (position) {
      case 0:
        //首页
        if (mHomeFragment == null) {
          mHomeFragment = new HomeFragment();
          fragmentTransaction.add(R.id.layout_contains, mHomeFragment, TAG_HOME);
        } else {
          fragmentTransaction.show(mHomeFragment);
        }
        break;
      case 1:
        //搜索
        if (mSearchFragment == null) {
          mSearchFragment = new SearchFragment();
          fragmentTransaction.add(R.id.layout_contains, mSearchFragment, TAG_SEARCH);
        } else {
          fragmentTransaction.show(mSearchFragment);
        }
        break;

      default:
    }
  }

  private void hideAllFragment(FragmentTransaction fragmentTransaction) {
    if (mHomeFragment != null && !mHomeFragment.isHidden()) {
      fragmentTransaction.hide(mHomeFragment);
    }
    if (mSearchFragment != null && !mSearchFragment.isHidden()) {
      fragmentTransaction.hide(mSearchFragment);
    }
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  @OnClick(R.id.btn_add)
  public void onViewClicked() {
    Intent intent = new Intent(this, InputActivity.class);
    startActivity(intent);
  }
}
