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

	// ��ǰѡ���ʱ���Calendars
	private Calendar mCurCalendar;
	// һ�ܵĵ�һ�죬ȡֵ��Ϊ��Calendar.SUNDAY,Calendar.MONDAY...���߸�ֵ
	private int mFirstDayOfWeek;

	// ��ߵ����ǰ�ť
	private ImageButton weekChooseMinusIb;
	// �м���ʾ���ڶε�Tv
	private TextView weekChooseDisplayTv;
	// �ұߵ����ǰ�ť
	private ImageButton weekChoosePlusIb;

	public WeekChooseBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.week_choose_bar, this);

		// ���趨��һΪһ��ĵ�һ��,֮���ٵ����ݿ��ж�̬��ȡ
		mFirstDayOfWeek = Calendar.MONDAY;

		initUI();
	}

	// ��ʼ��
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

	// ���ü����¼�
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
								year, month, day, "ѡ������",
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

	// ��ȡ��ǰѡ����������ڵ��ܵ�һ���Calendar
	public Calendar getFirstDayCalendarOfWeek() {
		return DateTimeUtil.getFirstDayCalendarOfWeek(mCurCalendar,
				mFirstDayOfWeek);
	}

	// ��ȡ��ǰѡ����������ڵ������һ���Calendar
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
