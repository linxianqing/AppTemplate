package demo.com.apptemplate;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Application类是最先执行
 * 使用场景,初始化,日志.
 */
public class InitApplication extends Application {
  private static Context context;

  @Override
  public void onCreate() {
    super.onCreate();
    context = this;// 全局上下文
    init();                              //初始化
  }

  public static Context getContext() {
    return context;
  }

  /**
   * 保存对象
   */
  public boolean saveObject(Serializable ser, String file) {
    FileOutputStream fos = null;
    ObjectOutputStream oos = null;
    try {
      fos = openFileOutput(file, MODE_PRIVATE);
      oos = new ObjectOutputStream(fos);
      oos.writeObject(ser);
      oos.flush();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        oos.close();
      } catch (Exception e) {
      }
      try {
        fos.close();
      } catch (Exception e) {
      }
    }
  }

  public void delFileData(String file) {
    File data = getFileStreamPath(file);
    data.delete();
  }

  /**
   * 读取对象.
   */
  public Serializable readObject(String file) {
    if (!isExistDataCache(file)) {
      return null;
    }
    FileInputStream fis = null;
    ObjectInputStream ois = null;
    try {
      fis = openFileInput(file);
      ois = new ObjectInputStream(fis);
      return (Serializable) ois.readObject();
    } catch (FileNotFoundException e) {
    } catch (Exception e) {
      e.printStackTrace();
      // 反序列化失败 - 删除缓存文件
      if (e instanceof InvalidClassException) {
        File data = getFileStreamPath(file);
        data.delete();
      }
    } finally {
      try {
        ois.close();
      } catch (Exception e) {
      }
      try {
        fis.close();
      } catch (Exception e) {
      }
    }
    return null;
  }

  /**
   * 判断缓存是否存在
   */
  private boolean isExistDataCache(String cachefile) {
    boolean exist = false;
    File data = getFileStreamPath(cachefile);
    if (data.exists()) {
      exist = true;
    }
    return exist;
  }

  /**
   * 初始化.
   */
  private void init() {
    //    开发模式开启日志
    if (BuildConfig.IS_DEBUG) {
      initLog();  //初始化日志
      initARouterLog(); //初始化路由框架日志
      initHttpLog();    //初始化网络日志拦截器
    }
//    Utils.init(context);                    //android工具类初始化
//    ARouter.init(this);   //初始化路由
//    ZXingLibrary.initDisplayOpinion(this);  //初始化二维码
  }

  private void initHttpLog() {
//    HttpUtil.getLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
  }

  private void initARouterLog() {
//    ARouter.openLog();     // 打印日志
//    ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
  }

  private void initLog() {
    FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // (可选择的) 是否显示线程信息. 默认：显示
            .methodCount(0)         // (可选择的) 决定打印多少行（每一行代表一个方法）默认：2
//                    .methodOffset(7)        // (可选择的) 隐藏内部抵消方法调用. Default 5
//                .logStrategy() // (可选择的) 更改日志打印策略. Default LogCat
            .tag("log")   // (可选择的) 全局标签 . Default PRETTY_LOGGER
            .build();
    Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    Logger.i("jaja");
    Logger.d("www");
  }
}
