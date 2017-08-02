package com.easydo.activity;

import java.util.Calendar;

import com.easydo.constant.AlarmAndRepeatFlag;
import com.easydo.constant.GlobalConfig;
import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.layout.EasyDoTimePickerDialog;
import com.easydo.layout.TimePickerFragment;
import com.easydo.model.Schedule;
import com.easydo.model.SystemConfig;
import com.easydo.service.ScheduleAlarmService;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.iflytek.speech.UtilityConfig;
import com.jiayongji.easydo.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CreateScheduleActivity extends ScheduleDetailBaseActivity
		implements OnClickListener {

	// 前一个活动传过来的日期时间字符串
	private String formerDateTime;
	// 前一个活动传过来的日程内容是否为空
	private boolean isFormerContentEmpty;
	// 前一个活动传来的日程内容（SimplyCreateScheduleDialog的输入的内容）
	private String formerContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化上一个活动传过来的数据
		initData();

		// 初始化各个控件
		titleMiddleTv.setText("新建日程");
		titleMiddleTv.setTextSize(20);
		titleRightIb.setImageResource(R.drawable.done1_64);

		if (!isFormerContentEmpty) {
			createScheduleContentEt.setText(formerContent);
		}
		createScheduleContentEt.setTextColor(Schedule.TAG_MATTERS_COLOR);

		createScheduleTagTv.setText(Schedule.TAG_MATTERS_TEXT);
		creatScheduleDateTv.setText(formerDateTime.substring(0, 10));
		creatScheduleTimeTv.setText(formerDateTime.substring(11, 16));
		createScheduleAlarmModeTv.setText(Schedule.ALARM_MODE_STRING_ARRAY[0]);
		createScheduleRepeatModeTv
				.setText(Schedule.REPEAT_MODE_STRING_ARRAY[0]);
		createScheduleStatusTv.setText(Schedule.STATUS_STRING_ARRAY[1]);

		titleLeftIb.setOnClickListener(this);
		titleRightIb.setOnClickListener(this);

	}

	// 初始化上一个活动传过来的数据
	private void initData() {
		Intent intent = getIntent();
		formerDateTime = intent.getStringExtra("datetime_data");
		isFormerContentEmpty = intent.getBooleanExtra("is_content_empty", true);
		formerContent = intent.getStringExtra("content");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.create_schedule_set_date_tr:
			DatePickerFragment datePickerFrg = new DatePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil
							.StringToGregorianCalendar(formerDateTime);
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					return new EasyDoDatePickerDialog(getActivity(), this,
							year, month, day, "选择日程的日期",
							EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
				}

				@Override
				public void onDateSet(DatePicker view, int year, int month,
						int day) {
					String dateStr = DateTimeUtil.getDateString(year, month,
							day);
					creatScheduleDateTv.setText(dateStr);
				}
			};
			datePickerFrg.show(getFragmentManager(), "datePickerFrg");
			break;
		case R.id.create_schedule_set_time_tr:
			TimePickerFragment timePickerFrg = new TimePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil
							.StringToGregorianCalendar(formerDateTime);
					int hour = c.get(Calendar.HOUR_OF_DAY);
					int minute = c.get(Calendar.MINUTE);

					return new EasyDoTimePickerDialog(getActivity(), this,
							hour, minute,
							DateFormat.is24HourFormat(getActivity()), "选择日程的时间");
				}

				@Override
				public void onTimeSet(android.widget.TimePicker view,
						int hourOfDay, int minute) {
					String timeStr = DateTimeUtil.getTimeString(hourOfDay,
							minute);
					creatScheduleTimeTv.setText(timeStr);
				};
			};
			timePickerFrg.show(getFragmentManager(), "timePickerFrg");
			break;
		case R.id.create_schedule_set_repeat_cut_off_date_tr:
			DatePickerFragment datePickerFrg2 = new DatePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil
							.StringToGregorianCalendar(Schedule
									.getNCycleNumLaterDateString(
											formerDateTime,
											selectedRepeatModeIndex - 1,
											GlobalConfig.DEFAULT_REPEAT_CYCLE_NUM)
									+ " 00:00:00");
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					return new EasyDoDatePickerDialog(getActivity(), this,
							year, month, day, "选择重复的截止日期",
							EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
				}

				@Override
				public void onDateSet(DatePicker view, int year, int month,
						int day) {
					String dateStr = DateTimeUtil.getDateString(year, month,
							day);
					createScheduleRepeatCutOffDateTv.setText(dateStr);
				}
			};
			datePickerFrg2.show(getFragmentManager(), "datePickerFrg");
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

	// 保存当前页面的日程数据到数据库
	private void saveScheduleData() {
		// 先判断日程的内容是否为空
		String scheduleContentStr = createScheduleContentEt.getText()
				.toString();
		if (scheduleContentStr == null || scheduleContentStr.length() <= 0) {
			ToastUtil.showShort(CreateScheduleActivity.this, "日程的内容不能为空哦~");
			return;
		}

		Schedule schedule = new Schedule();
		// 获取当前正在使用的scheduleBookId
		int scheduleBookId = systemConfig.getActivatedScheduleBookId();
		// 当前时间的日期-时间字符串作为日程的创建时间
		String createTime = DateTimeUtil.getCurrentDateTimeString();
		// 日程的内容(content)
		String content = scheduleContentStr;
		// 日程开始的时间
		String startTime = creatScheduleDateTv.getText().toString() + " "
				+ creatScheduleTimeTv.getText().toString() + ":"
				+ DateTimeUtil.getDoubleNumString(startTimeSecond);
		startTimeSecond++;
		// 提醒方式，按照设计的规则，alarmMode刚好是selectedAlarmModeIndex-1
		int alarmMode = selectedAlarmModeIndex - 1;

		// 重复方式，与提醒方式同理
		int repeatMode = selectedRepeatModeIndex - 1;
		if (repeatMode != Schedule.REPEAT_MODE_OFF) {
			String repeatCutOffDate = createScheduleRepeatCutOffDateTv
					.getText().toString();
			// 判断日程的重复截止日期是否比开始日期大，如果不大于就提示错误信息
			if (DateTimeUtil.compareDateString(repeatCutOffDate,
					creatScheduleDateTv.getText().toString()) <= 0) {
				ToastUtil.showShort(CreateScheduleActivity.this,
						"重复截止时间应该在日程开始时间以后，请重新选择");
				return;
			} else {
				schedule.setRepeatCutOffDate(repeatCutOffDate);
			}
		}
		// 根据alarmMode和startTime计算alarmTime
		String alarmTime = Schedule.calAlarmTime(alarmMode, startTime);
		// 日程状态
		int status = Schedule.STATUS_UNFINISHED;
		// 日程备注
		String remark = createScheduleAddRemarkEt.getText().toString();
		// 日程标签
		int tag = selectedTagIndex;

		schedule.setScheduleBookId(scheduleBookId);
		schedule.setCreateTime(createTime);
		schedule.setContent(content);
		schedule.setStartTime(startTime);
		schedule.setAlarmMode(alarmMode);
		schedule.setRepeatMode(repeatMode);
		schedule.setAlarmTime(alarmTime);
		schedule.setStatus(status);
		schedule.setRemark(remark);
		schedule.setTag(tag);

		int id = easyDoDB.saveSchedule(schedule);
		schedule.setId(id);

		// 弹出新建日程成功提示框
		ToastUtil.showShort(CreateScheduleActivity.this, "新建日程成功！");
		// 设置日程提醒
		setScheduleAlarm(schedule);
		// 处理日程的重复创建
		setScheduleRepeat(schedule);

		// 用于返回数据给ScheduleDisplayActivity以便更新日程显示列表
		Intent intent = new Intent(CreateScheduleActivity.this,
				ScheduleActivity.class);
		setResult(ScheduleActivity.CREATE_SCHEDULE_REQUEST_CODE, intent);
		// 结束当前活动
		finish();
	}
}
