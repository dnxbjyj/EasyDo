package com.easydo.layout;

import java.util.Calendar;

import com.easydo.util.DateTimeUtil;
import com.easydo.util.LogUtil;
import com.jiayongji.easydo.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

public class WeekChooseBarLayout extends BaseLinearLayout {

	// 当前选择的时间的Calendars
	private Calendar mCurCalendar;
	// 一周的第一天，取值可为：Calendar.SUNDAY,Calendar.MONDAY...等七个值
	private int mFirstDayOfWeek;

	// 左边的三角按钮
	private ImageButton weekChooseMinusIb;
	// 中间显示日期段的Tv
	private TextView weekChooseDisplayTv;
	// 右边的三角按钮
	private ImageButton weekChoosePlusIb;

	public WeekChooseBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.week_choose_bar, this);

		// 先设定周一为一天的第一天,之后再到数据库中动态读取
		mFirstDayOfWeek = Calendar.MONDAY;

		initUI();
	}

	// 初始化
	private void initUI() {
		mCurCalendar = DateTimeUtil.getCurrentCalendar();

		weekChooseMinusIb = (ImageButton) findViewById(R.id.week_choose_minus_ib);
		weekChooseDisplayTv = (TextView) findViewById(R.id.week_choose_display_tv);
		weekChooseDisplayTv.setText(DateTimeUtil.getWeekString(mCurCalendar,
				mFirstDayOfWeek));
		weekChoosePlusIb = (ImageButton) findViewById(R.id.week_choose_plus_ib);
	}

	public interface WeekChooseBarListener {
		public void onMinusClick();

		public void onDisplaySetDateClick();

		public void onPlusClick();
	}

	// 设置监听事件
	public void setOnChooseBarListener(final Context context,
			final WeekChooseBarListener listener) {
		weekChooseMinusIb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurCalendar.add(Calendar.DAY_OF_MONTH, -7);
				weekChooseDisplayTv.setText(DateTimeUtil.getWeekString(
						mCurCalendar, mFirstDayOfWeek));
				listener.onMinusClick();
			}
		});

		weekChoosePlusIb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCurCalendar.add(Calendar.DAY_OF_MONTH, 7);
				weekChooseDisplayTv.setText(DateTimeUtil.getWeekString(
						mCurCalendar, mFirstDayOfWeek));
				listener.onPlusClick();
			}
		});

		weekChooseDisplayTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePickerFrg = new DatePickerFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						final Calendar c = mCurCalendar;
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH);
						int day = c.get(Calendar.DAY_OF_MONTH);
						return new EasyDoDatePickerDialog(getActivity(), this,
								year, month, day, "选择日期",
								EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
					}

					@Override
					public void onDateSet(DatePicker view, int year, int month,
							int day) {
						mCurCalendar = DateTimeUtil.getCalendar(year, month,
								day);
						weekChooseDisplayTv.setText(DateTimeUtil.getWeekString(
								mCurCalendar, mFirstDayOfWeek));

						listener.onDisplaySetDateClick();
					}
				};
				datePickerFrg.show(((Activity) mContext).getFragmentManager(),
						"datePickerFrg");
			}
		});
	}

	public int getmFirstDayOfWeek() {
		return mFirstDayOfWeek;
	}

	public void setmFirstDayOfWeek(int mFirstDayOfWeek) {
		this.mFirstDayOfWeek = mFirstDayOfWeek;
	}

	// 获取当前选择的日期所在的周第一天的Calendar
	public Calendar getFirstDayCalendarOfWeek() {
		return DateTimeUtil.getFirstDayCalendarOfWeek(mCurCalendar,
				mFirstDayOfWeek);
	}

	// 获取当前选择的日期所在的周最后一天的Calendar
	public Calendar getLastDayCalendarOfWeek() {
		return DateTimeUtil.getLastDayCalendarOfWeek(mCurCalendar,
				mFirstDayOfWeek);
	}

	public Calendar getmCurCalendar() {
		return mCurCalendar;
	}

	public void setmCurCalendar(Calendar mCurCalendar) {
		this.mCurCalendar = mCurCalendar;
	}

	public void setDisplayText(Calendar c) {
		weekChooseDisplayTv.setText(DateTimeUtil.getWeekString(c,
				mFirstDayOfWeek));
	}
}
