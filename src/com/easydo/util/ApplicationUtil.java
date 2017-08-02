package com.easydo.util;

/**
 * 获取全局Context的工具类
 */

import android.app.Application;
import android.content.Context;

public class ApplicationUtil extends Application {

	private static Context context;

	@Override
	public void onCreate() {
		context = getApplicationContext();
	}

	public static Context getContext() {
		return context;
	}
}
