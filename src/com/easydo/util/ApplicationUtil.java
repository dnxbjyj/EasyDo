package com.easydo.util;

/**
 * ��ȡȫ��Context�Ĺ�����
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
