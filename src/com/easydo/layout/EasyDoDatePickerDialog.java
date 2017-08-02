package com.easydo.layout;

import java.util.Locale;

import com.easydo.util.ApplicationUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

public class EasyDoDatePickerDialog extends DatePickerDialog {
	// dialog的类型
	public static final int DIALOG_TYPE_WHOLE = 0;
	public static final int DIALOG_TYPE_YEAR_MONTH = 1;
	public static final int DIALOG_TYPE_MONTH_DAY = 2;
	
	// 标题
	private String mTilte;
	
	public EasyDoDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth, String title,int dialogType) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		mTilte = title;
		
		// 获取当前系统的语言
		Locale locale = context.getResources().getConfiguration().locale;
		String language = locale.getLanguage();

		switch (dialogType) {
		case DIALOG_TYPE_YEAR_MONTH:
			// 隐藏日选择栏
			if (language.endsWith("zh")) {
				((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
						.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
			} else {
				((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
						.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}

		this.setTitle(title);
		this.setButton2("取消", (OnClickListener) null);
		this.setButton("确定", this); // setButton和this参数组合表示这个按钮是确定按钮

	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
		this.setTitle(mTilte);
	}


}
