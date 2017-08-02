package com.easydo.activity;

import java.util.Calendar;

import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.layout.EasyDoTimePickerDialog;
import com.easydo.layout.TimePickerFragment;
import com.easydo.model.Journal;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

public class CreateJournalActivity extends JournalDetailBaseActivity implements
		OnClickListener {

	private Calendar curCalendar;
	private String curDateTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String date = intent.getStringExtra("date");
		curCalendar = DateTimeUtil.getCurrentCalendar();
		if (date == null) {
			curDateTime = DateTimeUtil.CalendarToString(curCalendar);
		} else {
			curDateTime = date + " " + DateTimeUtil.getCurrentTimeLongString();
		}

		titleMiddleTv.setText("写日志");
		titleMiddleTv.setTextSize(20);
		titleRightIb.setImageResource(R.drawable.done1_64);

		journalDateTv.setText(curDateTime.substring(0, 10));
		journalTimeTv.setText(curDateTime.substring(11, 16));

		journalContentEt.setTextColor(this.getResources().getColor(
				R.drawable.LightBlack));

		titleLeftIb.setOnClickListener(this);
		titleRightIb.setOnClickListener(this);

		journalSetDateTr.setOnClickListener(this);
		journalSetTimeTr.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.journal_set_date_tr:
			DatePickerFragment datePickerFrg = new DatePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = curCalendar;
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					return new EasyDoDatePickerDialog(getActivity(), this,
							year, month, day, "选择日志的日期",
							EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
				}

				@Override
				public void onDateSet(DatePicker view, int year, int month,
						int day) {
					String dateStr = DateTimeUtil.getDateString(year, month,
							day);
					journalDateTv.setText(dateStr);
				}
			};
			datePickerFrg.show(getFragmentManager(), "datePickerFrg");
			break;
		case R.id.journal_set_time_tr:
			TimePickerFragment timePickerFrg = new TimePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = curCalendar;
					int hour = c.get(Calendar.HOUR_OF_DAY);
					int minute = c.get(Calendar.MINUTE);

					return new EasyDoTimePickerDialog(getActivity(), this,
							hour, minute,
							DateFormat.is24HourFormat(getActivity()), "选择日志的时间");
				}

				@Override
				public void onTimeSet(android.widget.TimePicker view,
						int hourOfDay, int minute) {
					String timeStr = DateTimeUtil.getTimeString(hourOfDay,
							minute);
					journalTimeTv.setText(timeStr);
				};
			};
			timePickerFrg.show(getFragmentManager(), "timePickerFrg");
			break;
		case R.id.title_base_left_ib:
			finish();
			break;
		case R.id.title_base_right_ib:
			saveScheduleData();
			break;
		default:
			break;
		}
	}

	// 存储日志信息
	private void saveScheduleData() {
		String journalContent = journalContentEt.getText().toString();
		if (TextUtils.isEmpty(journalContent)) {
			ToastUtil.showShort(this, "日志的内容不能为空哦~");
			return;
		}

		Journal journal = new Journal();
		journal.setTitle("");
		journal.setContent(journalContent);
		journal.setCreateTime(journalDateTv.getText().toString() + " "
				+ journalTimeTv.getText().toString() + ":"
				+ DateTimeUtil.getCurrentSecondString());
		journal.setStatus(Journal.STATUS_NORMAL);

		easyDoDB.saveJournal(journal);

		// 弹出新建日程成功提示框
		ToastUtil.showShort(this, "保存日志成功！");

		// 用于返回数据给JournalActivity以便更新日志显示列表
		Intent intent = new Intent(this, JournalActivity.class);
		setResult(JournalActivity.CREATE_JOURNAL_REQUEST_CODE, intent);
		// 结束当前活动
		finish();
	}

}
