package com.verifywifi.database;

import android.util.Log;
import com.verifywifi.bean.DataBean;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/4/8 下午5:47.
 */
public class DataDao extends RealmObject {

  private final static String TAG = DataDao.class.getSimpleName();

  @PrimaryKey private long time;
  private String name;

  public static void saveOrUpdate(final DataBean dataBean) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(castDao(dataBean));
      }
    });
  }

  public static void saveOrUpdate(final List<DataBean> list) {
    final long time1 = System.currentTimeMillis();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override public void execute(Realm realm) {
        List<DataDao> daoList = new ArrayList<>();
        for (DataBean bean : list) {
          daoList.add(castDao(bean));
        }
        realm.copyToRealmOrUpdate(daoList);

        Log.e(TAG, " save " + list.size() + " time: "  + (System.currentTimeMillis()- time1));
      }
    });
  }


  private static DataDao castDao(DataBean bean) {
    DataDao dao = new DataDao();
    dao.name = bean.getCmd();
    dao.time = bean.getTime();
    return dao;
  }

}
