package com.easydo.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class BaseLinearLayout extends LinearLayout {

	// ���ڻ�ȡContext
	protected Context mContext;

	public BaseLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

}
