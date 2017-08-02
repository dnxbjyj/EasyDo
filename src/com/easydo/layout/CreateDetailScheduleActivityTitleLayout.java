package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class CreateDetailScheduleActivityTitleLayout extends BaseTitleLayout {

	public CreateDetailScheduleActivityTitleLayout(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		changeUI();
	}

	protected void changeUI() {
		titleLeftIb.setImageResource(R.drawable.back1_64);
	}

}
