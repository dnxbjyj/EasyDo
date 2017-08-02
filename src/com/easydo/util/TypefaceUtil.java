package com.easydo.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.widget.TextView;

/**
 * ������ز���������
 * 
 *
 */
public class TypefaceUtil {
	// ������
	private Context mContext;
	private Typeface mTypeface;

	/**
	 * ���ttfPathΪ"default"����ômTypeface��ΪϵͳĬ��ֵ
	 * 
	 * @param context
	 * @param ttfPath
	 */

	public TypefaceUtil(Context context, String ttfPath) {
		mContext = context;
		mTypeface = getTypefaceFromTTF(ttfPath);
	}

	/**
	 * ��ttf�ļ�����Typeface����
	 * 
	 * @ttfPath "fonts/XXX.ttf"
	 */
	public Typeface getTypefaceFromTTF(String ttfPath) {

		if (ttfPath == null || ttfPath.equals("default")) {
			return Typeface.DEFAULT;
		} else {
			return Typeface.createFromAsset(mContext.getAssets(), ttfPath);
		}
	}

	/**
	 * ����TextView������ ע��EditTextҲ�Ǽ̳���TextView��
	 * 
	 * @tv TextView����
	 * @ttfPath ttf�ļ�·��
	 * @isBold �Ƿ�Ӵ�����
	 */
	public void setTypeface(TextView tv, boolean isBold) {
		tv.setTypeface(mTypeface);
		setBold(tv, isBold);
	}

	/**
	 * ��������Ӵ�
	 */
	public void setBold(TextView tv, boolean isBold) {
		TextPaint tp = tv.getPaint();
		tp.setFakeBoldText(isBold);
	}

	/**
	 * ����TextView������ΪϵͳĬ������
	 * 
	 */
	public void setDefaultTypeFace(TextView tv, boolean isBold) {
		tv.setTypeface(Typeface.DEFAULT);
		setBold(tv, isBold);
	}

	/**
	 * ���õ�ǰ���߶��������
	 * 
	 */
	public void setmTypeface(String ttfPath) {
		mTypeface = getTypefaceFromTTF(ttfPath);
	}

}
