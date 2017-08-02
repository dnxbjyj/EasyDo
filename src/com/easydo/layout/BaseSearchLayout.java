package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public class BaseSearchLayout extends BaseLinearLayout {

	public BaseSearchLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.search_bar, this);
	}

}
