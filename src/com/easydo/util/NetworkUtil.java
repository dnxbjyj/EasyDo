package com.easydo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 和网络有关的操作的工具类
 *
 */

public class NetworkUtil {
	// 判断当前网络是否可用
	public static boolean isNetworkAvailabel() {
		ConnectivityManager mgr = (ConnectivityManager) ApplicationUtil
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = mgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
}
