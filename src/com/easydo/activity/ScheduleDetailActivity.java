package com.easydo.activity;

import java.util.Calendar;
import java.util.List;

import com.easydo.constant.AlarmAndRepeatFlag;
import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.layout.EasyDoTimePickerDialog;
import com.easydo.layout.TimePickerFragment;
import com.easydo.model.Schedule;
import com.easydo.receiver.ScheduleAlarmReceiver;
import com.easydo.service.ScheduleAlarmService;
import com.easydo.util.AlarmManagerUtil;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.EditTextUtil;
import com.easydo.util.InputMethodUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 日程详细信息查看及编辑
 *
 */

public class ScheduleDetailActivity extends ScheduleDetailBaseActivity
		implements OnClickListener {
	private Window mWin;

	// 前一个活动传递过来的日程对象
	private Schedule formerSchedule;

	// 新的Schedule
	private Schedule newSchedule;

	// 关闭重复的Tr
	private TableRow finishRepeatTr;
	private TextView finishRepeatTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 锁屏状态下可以唤醒屏幕
		mWin = getWindow();
		mWin.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		mWin.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		initData();

		// 初始化界面
		titleMiddleTv.setText("日程详细信息");
		titleMiddleTv.setTextSize(20);
		titleRightIb.setImageResource(R.drawable.edit1_64);

		createScheduleContentEt
				.setTextColor(Schedule.TAG_COLOR_ARR[formerSchedule.getTag()]);

		scheduleStatusImageIv.setVisibility(View.VISIBLE);
		if (formerSchedule.getStatus() == Schedule.STATUS_UNFINISHED) {
			scheduleStatusImageIv
					.setImageResource(R.drawable.schedule_status_not_done);
			scheduleStatusImageIv.setTag("NOT_DONE");
		} else {
			scheduleStatusImageIv
					.setImageResource(R.drawable.schedule_status_done);
			scheduleStatusImageIv.setTag("DONE");
		}
		createScheduleContentEt.setText(formerSchedule.getContent());

		createScheduleTagTv.setText(Schedule.getTagTextArray()[formerSchedule
				.getTag()]);
		selectedTagIndex = formerSchedule.getTag();

		creatScheduleDateTv.setText(formerSchedule.getStartTime().substring(0,
				10));
		creatScheduleTimeTv.setText(formerSchedule.getStartTime().substring(11,
				16));
		createScheduleAlarmModeTv
				.setText(Schedule.ALARM_MODE_STRING_ARRAY[formerSchedule
						.getAlarmMode() + 1]);
		createScheduleRepeatModeTv
				.setText(Schedule.REPEAT_MODE_STRING_ARRAY[formerSchedule
						.getRepeatMode() + 1]);
		finishRepeatTr = (TableRow) findViewById(R.id.finish_repeat_tr);
		if (formerSchedule.getRepeatMode() != Schedule.REPEAT_MODE_OFF) {
			createScheduleSetRepeatCutOffDateTr.setVisibility(View.VISIBLE);
			finishRepeatTr.setVisibility(View.VISIBLE);
			findViewById(R.id.repeat_cut_off_divider_v).setVisibility(
					View.VISIBLE);
			findViewById(R.id.finish_repeat_divider_v).setVisibility(
					View.VISIBLE);
			createScheduleRepeatCutOffDateTv.setText(formerSchedule
					.getRepeatCutOffDate());
		}
		finishRepeatTr.setOnClickListener(this);

		createScheduleAddRemarkEt.setText(formerSchedule.getRemark());
		createScheduleStatusTv
				.setText(Schedule.STATUS_STRING_ARRAY[formerSchedule
						.getStatus() + 1]);
		createScheduleStatusTv.setTextColor(getResources().getColor(
				R.drawable.TextDefault));

		setEnabled(false);

		scheduleStatusImageIv.setOnClickListener(this);
		titleLeftIb.setOnClickListener(this);
		titleRightIb.setOnClickListener(this);

	}

	// 初始化前一个活动传递过来的日程数据
	private void initData() {
		formerSchedule = getIntent().getParcelableExtra("schedule_data");
		selectedAlarmModeIndex = formerSchedule.getAlarmMode() + 1;
		selectedRepeatModeIndex = formerSchedule.getRepeatMode() + 1;
	}

	private void setEnabled(boolean isEnabled) {
		if (isEnabled) {
			// createScheduleContentEt.setEnabled(true);
			createScheduleContentEt.setFocusable(true);
			createScheduleContentEt.setFocusableInTouchMode(true);
			createScheduleContentEt.requestFocus();
			createScheduleContentEt.requestFocusFromTouch();
			createScheduleContentEt
					.setTextColor(Schedule.TAG_COLOR_ARR[selectedTagIndex]);
			createScheduleContentEt.setSelection(createScheduleContentEt
					.getText().toString().length());

			createScheduleContentDictationIb.setClickable(true);

			createScheduleSetTagTr.setEnabled(true);
			createScheduleTagTv.setTextColor(getResources().getColor(
					R.drawable.TextBlack));

			// 如果日程重复模式是不重复的话
			if (formerSchedule.getRepeatMode() == Schedule.REPEAT_MODE_OFF) {
				createScheduleSetDateTr.setEnabled(true);
				createScheduleSetTimeTr.setEnabled(true);

				creatScheduleDateTv.setTextColor(getResources().getColor(
						R.drawable.TextBlack));
				creatScheduleTimeTv.setTextColor(getResources().getColor(
						R.drawable.TextBlack));
			}
			if (finishRepeatTr.getTag() == null) {
				finishRepeatTr.setEnabled(true);
				((TextView) findViewById(R.id.finish_repeat_tv))
						.setTextColor(getResources().getColor(
								R.drawable.TextBlack));
			}

			creatScheduleSetAlarmModeTr.setEnabled(true);
			createScheduleAlarmModeTv.setTextColor(getResources().getColor(
					R.drawable.TextBlack));

			createScheduleAddRemarkEt.setEnabled(true);
			createScheduleSetLocationIb.setClickable(true);
		} else {
			// createScheduleContentEt.setEnabled(false);
			createScheduleContentEt.setFocusableInTouchMode(false);
			createScheduleContentEt.setFocusable(false);
			createScheduleContentEt.clearFocus();
			createScheduleContentEt.setOnLongClickListener(null);
			InputMethodUtil.closeInputMethod(ScheduleDetailActivity.this);

			createScheduleContentDictationIb.setClickable(false);

			createScheduleSetTagTr.setEnabled(false);
			createScheduleTagTv.setTextColor(getResources().getColor(
					R.drawable.TextDefault));

			createScheduleSetDateTr.setEnabled(false);
			createScheduleSetTimeTr.setEnabled(false);
			createScheduleSetRepeatCutOffDateTr.setEnabled(false);
			createScheduleSetRepeatModeTr.setEnabled(false);

			creatScheduleDateTv.setTextColor(getResources().getColor(
					R.drawable.TextDefault));
			creatScheduleTimeTv.setTextColor(getResources().getColor(
					R.drawable.TextDefault));
			createScheduleRepeatModeTv.setTextColor(getResources().getColor(
					R.drawable.TextDefault));
			createScheduleRepeatCutOffDateTv.setTextColor(getResources()
					.getColor(R.drawable.TextDefault));
			finishRepeatTr.setEnabled(false);
			((TextView) findViewById(R.id.finish_repeat_tv))
					.setTextColor(getResources().getColor(
							R.drawable.TextDefault));

			creatScheduleSetAlarmModeTr.setEnabled(false);
			createScheduleAlarmModeTv.setTextColor(getResources().getColor(
					R.drawable.TextDefault));
			createScheduleAddRemarkEt.setEnabled(false);
			createScheduleSetLocationIb.setClickable(false);
		}
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
							.StringToGregorianCalendar(formerSchedule
									.getStartTime());
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
					isChanged = true;
				}
			};
			datePickerFrg.show(getFragmentManager(), "datePickerFrg");
			break;
		case R.id.create_schedule_set_time_tr:
			TimePickerFragment timePickerFrg = new TimePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil
							.StringToGregorianCalendar(formerSchedule
									.getStartTime());
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
					isChanged = true;
				};
			};
			timePickerFrg.show(getFragmentManager(), "timePickerFrg");
			break;
		case R.id.finish_repeat_tr:
			dealWithFinishRepeat(formerSchedule);
			break;
		case R.id.title_base_left_ib:
			testIsChanged();
			// 如果当前是可编辑状态，说明还没有保存修改的数据
			if (titleRightIb.getTag() != null
					&& titleRightIb.getTag().equals("enabled") && isChanged) {
				ToastUtil.showShort(ScheduleDetailActivity.this, "请保存修改再返回！");
				return;
			}
			setResult(ScheduleActivity.SCHEDULE_DETAIL_REQUEST_CODE);
			// 结束当前活动
			finish();

			break;
		case R.id.title_base_right_ib:
			if (titleRightIb.getTag() == null
					|| titleRightIb.getTag().equals("not_enabled")) {
				titleRightIb.setTag("enabled");
				titleRightIb.setImageResource(R.drawable.done1_64);
				setEnabled(true);
			} else if (titleRightIb.getTag().equals("enabled")) {
				// 先判断日程content是否为空
				if (TextUtils.isEmpty(createScheduleContentEt.getText()
						.toString())) {
					ToastUtil.showShort(ScheduleDetailActivity.this,
							"日程的内容不能为空哦~");
					return;
				}

				testIsChanged();

				// 判断content是否修改
				if (isChanged) {
					saveScheduleData();
				}
				titleRightIb.setTag("not_enabled");
				titleRightIb.setImageResource(R.drawable.edit1_64);
				setEnabled(false);
			}
			break;
		case R.id.schedule_status_image_iv:
			String tag = (String) scheduleStatusImageIv.getTag();
			if (tag.equals("NOT_DONE")) {
				easyDoDB.updateDB("Schedule", "status",
						Schedule.STATUS_FINISHED + "", "id",
						formerSchedule.getId() + "");
				createScheduleStatusTv
						.setText(Schedule.STATUS_STRING_ARRAY[Schedule.STATUS_FINISHED + 1]);
				scheduleStatusImageIv
						.setImageResource(R.drawable.schedule_status_done);
				scheduleStatusImageIv.setTag("DONE");
				ToastUtil
						.showShort(ScheduleDetailActivity.this, "完成了一件事情，你真棒！");
			} else if (tag.equals("DONE")) {
				easyDoDB.updateDB("Schedule", "status",
						Schedule.STATUS_UNFINISHED + "", "id",
						formerSchedule.getId() + "");
				createScheduleStatusTv
						.setText(Schedule.STATUS_STRING_ARRAY[Schedule.STATUS_UNFINISHED + 1]);
				scheduleStatusImageIv
						.setImageResource(R.drawable.schedule_status_not_done);
				scheduleStatusImageIv.setTag("NOT_DONE");
				ToastUtil
						.showShort(ScheduleDetailActivity.this, "这件事情还需要继续努力哦");
			}
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
		if (TextUtils.isEmpty(scheduleContentStr)) {
			ToastUtil.showShort(ScheduleDetailActivity.this, "日程的内容不能为空哦~");
			return;
		}

		Schedule newSchedule = new Schedule();
		// 获取当前正在使用的scheduleBookId
		int scheduleBookId = systemConfig.getActivatedScheduleBookId();

		// 当前时间的日期-时间字符串作为日程的创建时间
		String createTime = formerSchedule.getCreateTime();
		// 日程的内容(content)
		String content = scheduleContentStr;
		// 日程开始的时间
		String startTime = creatScheduleDateTv.getText().toString() + " "
				+ creatScheduleTimeTv.getText().toString() + ":"
				+ DateTimeUtil.getDoubleNumString(startTimeSecond);
		startTimeSecond++;
		// 提醒方式，按照设计的规则，alarmMode刚好是selectedAlarmModeIndex-1
		int alarmMode = selectedAlarmModeIndex - 1;
		// 如果提醒方式发生了变化，那么同批重复创建的所有日程的提醒方式都要跟着改变
		if (alarmMode != formerSchedule.getAlarmMode()) {
			List<Integer> idsList = easyDoDB
					.getSameCreateTimeScheduleId(formerSchedule.getCreateTime());
			for (int id : idsList) {
				easyDoDB.updateDB(Schedule.TABLE_NAME, "alarm_mode", alarmMode
						+ "", "id", id + "");
				Schedule toSetSchedule = easyDoDB.loadScheduleById(id);
				toSetSchedule.setAlarmMode(alarmMode);
				// 分两种情况：
				if (alarmMode == Schedule.ALARM_MODE_OFF) {
					// 关闭该日程的定时提醒（如果提醒模式不为关闭的话）
					AlarmManagerUtil.cancelAlarmBroadcast(
							ScheduleDetailActivity.this, id,
							ScheduleAlarmReceiver.class);
				} else {
					String alarmTime = Schedule.calAlarmTime(alarmMode,
							toSetSchedule.getStartTime());
					toSetSchedule.setAlarmTime(alarmTime);
					easyDoDB.updateDB(Schedule.TABLE_NAME, "alarm_time",
							alarmTime, "id", id + "");
					setScheduleAlarm(toSetSchedule);
				}
			}

		}

		// 重复方式，与提醒方式同理
		int repeatMode = selectedRepeatModeIndex - 1;
		if (repeatMode != Schedule.REPEAT_MODE_OFF) {
			String repeatCutOffDate = createScheduleRepeatCutOffDateTv
					.getText().toString();
			// 判断日程的重复截止日期是否比开始日期大，如果不大于就提示错误信息
			if (DateTimeUtil.compareDateString(repeatCutOffDate,
					creatScheduleDateTv.getText().toString()) <= 0) {
				ToastUtil.showShort(ScheduleDetailActivity.this,
						"重复截止时间应该在日程开始时间以后，请重新选择");
				return;
			} else {
				newSchedule.setRepeatCutOffDate(repeatCutOffDate);
			}
		}

		// 根据alarmMode和startTime计算alarmTime
		String alarmTime = Schedule.calAlarmTime(alarmMode, startTime);

		// 日程状态
		int status = formerSchedule.getStatus();
		String tag = (String) scheduleStatusImageIv.getTag();
		if (tag.equals("NOT_DONE")) {
			status = Schedule.STATUS_UNFINISHED;
		} else if (tag.equals("DONE")) {
			status = Schedule.STATUS_FINISHED;
		}

		// 日程备注
		String remark = createScheduleAddRemarkEt.getText().toString();

		// 日程标签
		int scheduleTag = selectedTagIndex;
		// 判断标签是否发生改变
		if (scheduleTag != formerSchedule.getTag()) {
			List<Integer> idList = easyDoDB
					.getSameCreateTimeScheduleId(formerSchedule.getCreateTime());
			easyDoDB.updateDB(Schedule.TABLE_NAME, "tag", scheduleTag + "",
					idList);
		}

		newSchedule.setId(formerSchedule.getId());
		newSchedule.setScheduleBookId(scheduleBookId);
		newSchedule.setCreateTime(createTime);
		newSchedule.setContent(content);
		newSchedule.setStartTime(startTime);
		newSchedule.setAlarmMode(alarmMode);
		newSchedule.setRepeatMode(repeatMode);
		newSchedule.setAlarmTime(alarmTime);
		newSchedule.setStatus(status);
		newSchedule.setRemark(remark);
		newSchedule.setTag(scheduleTag);

		easyDoDB.updateScheduleById(formerSchedule.getId(), newSchedule);

		ToastUtil.showShort(ScheduleDetailActivity.this, "修改日程信息成功！");

		setScheduleAlarm(newSchedule);
	}

	// “关闭重复”按钮点击事件，处理所有的有相同创建日期的日程的关闭重复的操作
	// 删除这些日程当中开始时间在当前日程开始时间之后的所有日程，并把这之前的那些日程的重复截止日期设定为当前日程的开始日期
	private void dealWithFinishRepeat(Schedule schedule) {
		// 同一批重复创建的日程的id，它们有相同的创建时间
		List<Integer> sameBatchSchedulesIdList = easyDoDB
				.getSameCreateTimeScheduleId(schedule.getCreateTime());

		// 把sameBatchSchedulesIdList中当前schedule之后的所有日程都删掉
		int lastIndex = sameBatchSchedulesIdList.indexOf(schedule.getId());
		for (int i = lastIndex + 1; i < sameBatchSchedulesIdList.size(); i++) {
			int id = sameBatchSchedulesIdList.get(i);
			easyDoDB.deleteRecord(Schedule.TABLE_NAME, id + "");

			if (formerSchedule.getAlarmMode() != Schedule.ALARM_MODE_OFF) {
				AlarmManagerUtil.cancelAlarmBroadcast(
						ScheduleDetailActivity.this, id,
						ScheduleAlarmReceiver.class);
			}
		}
		// 把sameBatchSchedulesIdList中当前schedule之前的所有日程的重复截止日期都设置为当前schedule的开始日期
		for (int i = 0; i <= lastIndex; i++) {
			int id = sameBatchSchedulesIdList.get(i);
			easyDoDB.updateDB(Schedule.TABLE_NAME, "repeat_cut_off_date",
					schedule.getStartTime().substring(0, 10), "id", id + "");
		}

		createScheduleRepeatCutOffDateTv.setText(schedule.getStartTime()
				.substring(0, 10));
		finishRepeatTr.setTag("finished");
		finishRepeatTr.setEnabled(false);
		((TextView) findViewById(R.id.finish_repeat_tv)).setText("重复已关闭");
		((TextView) findViewById(R.id.finish_repeat_tv))
				.setTextColor(getResources().getColor(R.drawable.TextDefault));
		ToastUtil.showLong(ScheduleDetailActivity.this,
				"关闭重复成功，当前日程之后的所有重复创建的日程已删除！");

	}

	@Override
	public void onBackPressed() {
		testIsChanged();
		// 如果当前是可编辑状态，说明还没有保存修改的数据
		if (titleRightIb.getTag() != null
				&& titleRightIb.getTag().equals("enabled") && isChanged) {
			ToastUtil.showShort(ScheduleDetailActivity.this, "请保存修改再返回！");
			return;
		}
		setResult(ScheduleActivity.SCHEDULE_DETAIL_REQUEST_CODE);
		// 结束当前活动
		finish();

		super.onBackPressed();
	}

	// 测试当前有没有修改未保存
	private void testIsChanged() {
		String content = createScheduleContentEt.getText().toString();
		String remark = createScheduleAddRemarkEt.getText().toString();

		if (selectedTagIndex != formerSchedule.getTag()) {
			isChanged = true;
			return;
		}

		if (TextUtils.isEmpty(content)
				|| (TextUtils.isEmpty(remark) && !TextUtils
						.isEmpty(formerSchedule.getRemark()))) {
			isChanged = true;
			return;
		}

		if (!TextUtils.isEmpty(content)
				&& !content.equals(formerSchedule.getContent())) {
			isChanged = true;
			return;
		}

		if (!TextUtils.isEmpty(remark)
				&& !remark.equals(formerSchedule.getRemark())) {
			isChanged = true;
			return;
		}
	}
}
