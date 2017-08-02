package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;

public class ScheduleActivityTitleLayout extends BaseTitleLayout {

	public ScheduleActivityTitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		changeUI();
	}

	protected void changeUI() {
		titleLeftIb.setImageResource(R.drawable.drawer1_64);
		titleRightIb.setImageResource(R.drawable.add3_64);
	}

}
