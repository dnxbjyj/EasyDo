package com.easydo.layout;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.TimePicker;

public class EasyDoTimePickerDialog extends TimePickerDialog {

	// ����
	private String mTilte;

	public EasyDoTimePickerDialog(Context context, OnTimeSetListener callBack,
			int hourOfDay, int minute, boolean is24HourView, String title) {
		super(context, callBack, hourOfDay, minute, is24HourView);
		mTilte = title;

		this.setTitle(title);
		this.setButton2("ȡ��", (OnClickListener) null);
		this.setButton("ȷ��", this); // setButton��this������ϱ�ʾ�����ť��ȷ����ť
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		super.onTimeChanged(view, hourOfDay, minute);
		this.setTitle(mTilte);
	}

}
