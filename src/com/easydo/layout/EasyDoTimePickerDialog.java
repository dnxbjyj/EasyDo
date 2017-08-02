package com.easydo.layout;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.TimePicker;

public class EasyDoTimePickerDialog extends TimePickerDialog {

	// 标题
	private String mTilte;

	public EasyDoTimePickerDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView, String title) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		mTilte = title;

		this.setTitle(title);
		this.setButton2("取消", (OnClickListener) null);
		this.setButton("确定", this); // setButton和this参数组合表示这个按钮是确定按钮
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		super.onTimeChanged(view, hourOfDay, minute);
		this.setTitle(mTilte);
	}

}
