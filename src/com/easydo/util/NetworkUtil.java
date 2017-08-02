package com.easydo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * �������йصĲ����Ĺ�����
 *
 */

public class NetworkUtil {
	// �жϵ�ǰ�����Ƿ����
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
