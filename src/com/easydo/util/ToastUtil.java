package com.easydo.util;

/**
 * Toast��ʾ��ʾ������
 * 
 */

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

	// ��ʱ����ʾToast��Ϣ
	public static void showShort(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	// ��ʱ����ʾToast��Ϣ
	public static void showLong(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_LONG).show();
	}

}
