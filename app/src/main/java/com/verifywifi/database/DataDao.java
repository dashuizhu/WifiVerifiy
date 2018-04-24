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
import java.util.UUID;

/**
 * @author zhuj 2018/4/8 下午5:47.
 */
public class DataDao extends RealmObject {

  private final static String TAG = DataDao.class.getSimpleName();

  private final static String COLUMN_TIME = "time";

  @PrimaryKey
  private String id;
  private String nodeId;
  private String vin;
  private String name;
  private long time;
  private int angle;
  private int angleState;
  private int torqueState;
  private int torque;
  private String bolt;
  private boolean state;

  public static void saveOrUpdate(final DataBean dataBean) {
    Realm realm = Realm.getDefaultInstance();
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        DataDao dao = castDao(dataBean);
        dao.id = UUID.randomUUID().toString();
        realm.copyToRealmOrUpdate(dao);
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
        DataDao dao;
        for (DataBean bean : list) {
          dao = castDao(bean);
          dao.id = UUID.randomUUID().toString();
          daoList.add(dao);
        }
        realm.copyToRealmOrUpdate(daoList);

        MyLog.e(TAG, " save " + list.size() + " time: " + (System.currentTimeMillis() - time1));
      }
    });
  }

  private static DataDao castDao(DataBean bean) {
    DataDao dao = new DataDao();
    dao.name = bean.getName();
    dao.time = bean.getTime();
    dao.nodeId = bean.getNodeId();
    dao.angleState = bean.getAngleState();
    dao.angle = bean.getAngle();
    dao.torque = bean.getTorque();
    dao.torqueState = bean.getTorqueState();
    dao.state = bean.isState();
    dao.bolt = bean.getBolt();
    dao.vin = bean.getVin();
    return dao;
  }

  public DataBean castBean() {
    DataBean bean = new DataBean();
    bean.setNodeId(this.nodeId);
    bean.setVin(this.vin);
    bean.setTime(this.time);
    bean.setName(this.name);
    bean.setAngle(this.angle);
    bean.setAngleState(this.angleState);
    bean.setTorque(this.torque);
    bean.setTorqueState(torqueState);
    bean.setBolt(this.bolt);
    bean.setState(this.state);

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
   */
  public static void cleanOld(long time) {
    Realm realm = Realm.getDefaultInstance();
    RealmResults<DataDao> results;
    realm.beginTransaction();
    results = realm.where(DataDao.class).lessThan(COLUMN_TIME, time).findAll();
    if (results != null && results.isValid()) {
      results.deleteAllFromRealm();
    }
    realm.commitTransaction();
  }
}
