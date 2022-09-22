package com.easycon.easycondroid;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.crashreport.CrashReport;
import com.easycon.easycondroid.util.CrashUtil;


/**
 * Application
 */

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		//初始化自定义 crash 捕捉
		CrashUtil.getInstance().init(this);

		//初始化 bugly
		CrashReport.initCrashReport(this, "8e456ea54c", true);
		/**
		 * Log日志初始化
		 * 可参考https://github.com/orhanobut/logger 使用
		 */
		Logger.addLogAdapter(new AndroidLogAdapter());

	}
}
