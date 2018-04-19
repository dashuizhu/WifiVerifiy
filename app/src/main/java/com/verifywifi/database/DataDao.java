package com.verifywifi.database;

import com.verifywifi.bean.DataBean;
import com.verifywifi.utils.MyLog;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.PrimaryKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/4/8 下午5:47.
 */
public class DataDao extends RealmObject {

  private final static String TAG = DataDao.class.getSimpleName();

  private final static String COLUMN_TIME = "time";

  @PrimaryKey private long time;
  private String name;

  public static void saveOrUpdate(final DataBean dataBean) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.copyToRealmOrUpdate(castDao(dataBean));
      }
    });
  }

  public static void saveOrUpdate(final List<DataBean> list) {
    final long time1 = System.currentTimeMillis();
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        List<DataDao> daoList = new ArrayList<>();
        for (DataBean bean : list) {
          daoList.add(castDao(bean));
        }
        realm.copyToRealmOrUpdate(daoList);

        MyLog.e(TAG, " save " + list.size() + " time: " + (System.currentTimeMillis() - time1));
      }
    });
  }

  private static DataDao castDao(DataBean bean) {
    DataDao dao = new DataDao();
    dao.name = bean.getCmd();
    dao.time = bean.getTime();
    return dao;
  }

  public DataBean castBean() {
    DataBean bean =new DataBean();
    bean.setTime(this.time);
    bean.setCmd(this.name);
    return bean;
  }

  /**
   * 查询所有记录,倒序查
   */
  public static RealmResults<DataDao> queryList(long startTime, long endTime) {
    Realm realm = Realm.getDefaultInstance();
    RealmResults<DataDao> results;
    realm.beginTransaction();
    results = realm.where(DataDao.class)
            .greaterThan(COLUMN_TIME, startTime)
            .lessThan(COLUMN_TIME, endTime)
            .findAllSorted(COLUMN_TIME, Sort.ASCENDING);
    realm.commitTransaction();
    return results;
  }

  /**
   * 删除指定时间之前 的数据
   * @param time
   */
  public static void cleanOld(long time) {
    Realm realm = Realm.getDefaultInstance();
    RealmResults<DataDao> results;
    realm.beginTransaction();
    results = realm.where(DataDao.class)
            .lessThan(COLUMN_TIME, time)
            .findAll();
    if (results!=null && results.isValid()) {
      results.deleteAllFromRealm();
    }
    realm.commitTransaction();
  }
}
