package com.verifywifi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import com.hwangjr.rxbus.RxBus;
import com.verifywifi.agreement.CmdPackage;
import com.verifywifi.agreement.Encrypt;
import com.verifywifi.bean.StateBean;
import com.verifywifi.utils.AppUtils;
import com.verifywifi.utils.MyLog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/4/8 上午9:20.
 */
public class WifiServerService extends Service {

  private final String TAG = WifiServerService.class.getSimpleName();

  /**
   * 连接超时时间，单位毫秒
   */
  private final static int SO_TIME = 6000;
  /**
   * TCP客户端读取数据超时时间，单位毫秒
   */
  private final static int READ_TIME = 4000;
  /**
   * TCP客户端，读取数据连续4次无数据，断开客户端的连接，相当于9秒无数据，就T掉客户端
   */
  private final static int READ_COUNT = 4;

  /**
   * 最大连接数
   */
  private final static int MAX_CONNECT_CLIENT = 1;

  private final IBinder mBinder = new LocalBinder();

  /**
   * 是否工作中
   */
  private boolean mWork = true;
  /**
   * socket 服务端
   */
  private ServerSocket mServerSocket;

  private List<Socket> mSocketList = new ArrayList<>();

  /**
   * 连接状态
   */
  private int mWorkState;
  /**
   * 连接地址
   */
  private String mClientAddress;

  /**
   * 读取数据的线程
   */
  Disposable disposable;

  private Encrypt mEncrypt = new Encrypt();

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    Observable.just(this).observeOn(Schedulers.io()).subscribe(new Consumer<WifiServerService>() {
      @Override
      public void accept(WifiServerService wifiServerService) throws Exception {

        while (!AppUtils.isWifi(WifiServerService.this)) {
          Thread.sleep(1000);
        }
        startServer();
      }
    });
    mEncrypt.addOnParseListener(new Encrypt.OnParseListener() {
      @Override
      public void parse(byte type) {
        //添加解析结果， 解析一条数据，就回复发送一条数据
        writeAgreement(mSocketList.get(0), CmdPackage.receiveCMD(type));
      }
    });
    return mBinder;
  }

  /**
   * 获得工作状态
   */
  public int getWorkSate() {
    return mWorkState;
  }

  public class LocalBinder extends Binder {
    public WifiServerService getService() {
      return WifiServerService.this;
    }
  }

  /**
   * 开始服务器
   */
  private void startServer() {
    try {
      // 1、创建ServerSocket服务器套接字
      if (mServerSocket == null) {
        mServerSocket = new ServerSocket(AppConstants.SERVER_PORT);
        // 设置连接超时时间，不设置，则是一直阻塞等待
        mServerSocket.setSoTimeout(SO_TIME);
      }
      mWorkState = StateBean.WAIT;
      sendState();
      MyLog.w(TAG, "start work accept socket."
              + AppUtils.getLocalIpAddress(this)
              + ":"
              + AppConstants.SERVER_PORT);
      while (mWork) {
        //一直等待，连接
        try {

          if (mSocketList.size() >= MAX_CONNECT_CLIENT) {
            Thread.sleep(2000);
          } else {
            Socket socket = mServerSocket.accept();
            startRead(socket);
          }
        } catch (SocketTimeoutException e1) {
        } catch (InterruptedIOException e2) {

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      try {
        MyLog.e(TAG, " 服务端重启 ");
        mServerSocket.close();
      } catch (IOException eio) {
        eio.printStackTrace();
      }
    } finally {
      if (mWork) {
        mWorkState = StateBean.NONE;
        sendState();
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
        startServer();
      }
    }
  }

  /**
   * 开始读取数据
   */
  private void startRead(final Socket socket) {
    if (socket == null || !socket.isConnected()) {
      return;
    }
    mClientAddress = socket.getRemoteSocketAddress().toString();
    mSocketList.add(socket);
    MyLog.w(TAG, "connent success:  " + mClientAddress);
    disposable = Observable.just(socket).subscribe(new Consumer<Socket>() {
      @Override
      public void accept(Socket client) throws Exception {
        mWorkState = StateBean.CONNECT;
        sendState();
        // 设置读取流的超时时间，不设置，则是一直阻塞读取
        client.setSoTimeout(READ_TIME);
        // 3、获取输入流和输出流
        InputStream inputStream = client.getInputStream();

        byte[] buf = new byte[1024];
        int count = 0;
        while (client.isConnected()) {

          try {
            int length = inputStream.read(buf);
            if (length != -1) {
              //读取到数据 清零
              count = 0;
              mEncrypt.processDataCommand(buf, length);
            }
          } catch (SocketTimeoutException e) {
          }

          //读取数据次数，超时
          count++;
          if (count > READ_COUNT) {
            client.close();
            MyLog.w(TAG, " 未读取到数据 踢下线");
          }
        }
        mWorkState = StateBean.WAIT;
        sendState();
        if (client != null) {
          mSocketList.remove(client);
        }
        MyLog.w(TAG, "client disConnect " + mClientAddress);
        if (disposable != null && !disposable.isDisposed()) {
          disposable.dispose();
        }
      }
    }, new Consumer<Throwable>() {
      @Override
      public void accept(Throwable throwable) throws Exception {
        mWorkState = StateBean.WAIT;
        sendState();
        if (socket != null) {
          mSocketList.remove(socket);
          if (socket.isConnected()) {
            socket.close();
          }
        }
        MyLog.w(TAG, "client disConnect " + mClientAddress);
        if (disposable != null && !disposable.isDisposed()) {
          disposable.dispose();
        }
      }
    });
  }

  /**
   * 发送广播，通知状态变化
   */
  private void sendState() {
    RxBus.get().post(AppConstants.RXBUS_STATE, mWorkState);
  }

  /**
   * 获得当前的状态文字
   */
  public String getStateStr() {
    String str = "";
    switch (mWorkState) {
      case StateBean.NONE:
        str = "未开启";
        break;
      case StateBean.WAIT:
        String ip = AppUtils.getLocalIpAddress(this) + ":" + AppConstants.SERVER_PORT;
        Log.e(TAG, " ip:" + ip);
        str = ip + " 等待客户端连接";
        break;
      case StateBean.CONNECT:
        str = mClientAddress;
        break;
      default:
    }
    return str;
  }

  /**
   * 写数据
   */
  private void writeAgreement(Socket socket, byte[] cmd) {
    if (socket.isConnected()) {
      OutputStream out = null;
      try {
        out = socket.getOutputStream();
        out.write(Encrypt.packageCmd(cmd));
        out.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
