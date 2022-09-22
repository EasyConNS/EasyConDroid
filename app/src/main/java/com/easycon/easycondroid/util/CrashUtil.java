package com.easycon.easycondroid.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author yanjun.zhao
 * @time 2021/1/11 4:44 PM
 * @desc
 */
public class CrashUtil implements Thread.UncaughtExceptionHandler {

    private static final Format FORMAT = new SimpleDateFormat("MM-dd HH-mm-ss", Locale.getDefault());
    private Context mContext;
    private static CrashUtil INSTANCE = new CrashUtil();
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;
    private String deviceInfo;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashUtil() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashUtil getInstance() {
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

        //自定义设备信息
        PackageInfo pi = null;
        String versionName = "";
        int versionCode = 0;

        try {
            pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            if (pi != null) {
                versionName = pi.versionName;
                versionCode = pi.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        deviceInfo = "versionName: " + versionName + "\n"
                + "versionCode: " + versionCode + "\n"
                + "androidSDK: " + Build.VERSION.RELEASE + "\n"  // 系统版本
                + "DeviceModel: " + Build.MANUFACTURER + " " + Build.MODEL + "\n"// 设备型号
                + "\n";
    }

    /**
     * 打印当前线程堆栈，保存到本地文件
     */
    public void print(Throwable throwable) {
        String fileName = getFileDir(mContext) + FORMAT.format(System.currentTimeMillis()) + ".txt";
        if (createOrExistsFile(fileName)) {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new FileWriter(fileName, false));
                pw.write(deviceInfo);  //写入设备信息
                throwable.printStackTrace(pw);
            } catch (IOException ioException) {

            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        }
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     */
    private static boolean createOrExistsFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.isFile();
        }
        if (!createOrExistsDir(file.getParentFile())) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 获取堆栈日志存储目录
     *
     * @param context
     * @return
     */
    private static String getFileDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && context.getExternalCacheDir() != null) {
            return context.getExternalCacheDir() + File.separator + "crash" + File.separator;
        } else {
            return context.getCacheDir() + File.separator + "crash" + File.separator;
        }
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

        //优先处理自己的逻辑，把crash日志存起来
        print(e);

        if (defaultUncaughtExceptionHandler != null) {
            //如果原来的 Thread 有自己的 handler , 就把 crash 传递下去,
            //比如：如果集成了bugly , 那就传给bugly 处理
            defaultUncaughtExceptionHandler.uncaughtException(t, e);
        } else {

        }
    }
}
