package com.easydo.activity;

import java.util.Calendar;
import java.util.List;

import com.easydo.constant.GlobalConfig;
import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.layout.EasyDoTimePickerDialog;
import com.easydo.layout.TimePickerFragment;
import com.easydo.model.Schedule;
import com.easydo.service.ScheduleAlarmService;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.EditTextUtil;
import com.easydo.util.LogUtil;
import com.jiayongji.easydo.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 因CreateScheduleActivity和ScheduleDetailActivity两个活动的界面几乎完全一样，所以为了代码重用，
 * 抽象了这个基类来让它们继承
 *
 */

public class ScheduleDetailBaseActivity extends BaseActivity implements
		OnClickListener {
	// TableLayout，用于让EditText失去焦点
	protected View tableTl;

	// 标题栏左侧的Ib
	protected ImageButton titleLeftIb;
	// 标题栏中间的Tv
	protected TextView titleMiddleTv;
	// 标题栏的右侧Ib
	protected ImageButton titleRightIb;

	// 日程数据是否被修改
	boolean isChanged = false;

	// 日程输入栏
	protected LinearLayout contentInputLl;
	// 日程完成圆圈
	protected ImageView scheduleStatusImageIv;
	// 日程内容et
	protected EditText createScheduleContentEt;
	// 日程内容语音听写ib
	protected ImageButton createScheduleContentDictationIb;

	// 设置日程的标签
	protected TableRow createScheduleSetTagTr;
	// 显示日程标签的Tv
	protected TextView createScheduleTagTv;

	// 选择日程日期的tr
	protected TableRow createScheduleSetDateTr;
	// 显示选择的日期的tv
	protected TextView creatScheduleDateTv;

	// 选择日程时间的tr
	protected TableRow createScheduleSetTimeTr;
	// 显示选择的时间的tv
	protected TextView creatScheduleTimeTv;

	// 设置日程的提醒方式
	protected TableRow creatScheduleSetAlarmModeTr;
	// 显示日程的提醒方式
	protected TextView createScheduleAlarmModeTv;

	// 设置日程的重复模式
	protected TableRow createScheduleSetRepeatModeTr;
	// 显示日程的重复模式
	protected TextView createScheduleRepeatModeTv;

	// 日程的重复截止日期设置
	protected TableRow createScheduleSetRepeatCutOffDateTr;
	// 日程的重复截止日期显示
	protected TextView createScheduleRepeatCutOffDateTv;

	// 日程的备注输入
	protected EditText createScheduleAddRemarkEt;

	// 日程当前状态的显示
	protected TextView createScheduleStatusTv;

	// 设置日程的地点按钮
	protected ImageButton createScheduleSetLocationIb;

	// 当前选择的日程标签index
	protected int selectedTagIndex = Schedule.TAG_MATTERS;
	// 当前选择的日程提醒方式index
	protected int selectedAlarmModeIndex;
	// 当前选择的日程的重复模式的index
	protected int selectedRepeatModeIndex;

	// startTime的秒的值
	protected static int startTimeSecond = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_create_detail_schedule);

		// 让点击别处时备注Et失去焦点
		tableTl = findViewById(R.id.table_tl);
		tableTl.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				tableTl.setFocusable(true);
				tableTl.setFocusableInTouchMode(true);
				tableTl.requestFocus();
				return false;
			}
		});

		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		contentInputLl = (LinearLayout) findViewById(R.id.content_input_ll);

		scheduleStatusImageIv = (ImageView) findViewById(R.id.schedule_status_image_iv);
		createScheduleContentEt = (EditText) findViewById(R.id.create_schedule_content_et);
		createScheduleContentDictationIb = (ImageButton) findViewById(R.id.create_schedule_content_dictation_ib);

		createScheduleSetTagTr = (TableRow) findViewById(R.id.create_schedule_set_tag_tr);
		createScheduleTagTv = (TextView) findViewById(R.id.create_schedule_tag_tv);

		createScheduleSetDateTr = (TableRow) findViewById(R.id.create_schedule_set_date_tr);
		creatScheduleDateTv = (TextView) findViewById(R.id.create_schedule_date_tv);

		createScheduleSetTimeTr = (TableRow) findViewById(R.id.create_schedule_set_time_tr);
		creatScheduleTimeTv = (TextView) findViewById(R.id.create_schedule_time_tv);

		creatScheduleSetAlarmModeTr = (TableRow) findViewById(R.id.create_schedule_set_alarm_mode_tr);
		createScheduleAlarmModeTv = (TextView) findViewById(R.id.create_schedule_alarm_mode_tv);

		createScheduleSetRepeatModeTr = (TableRow) findViewById(R.id.create_schedule_set_repeat_mode_tr);
		createScheduleRepeatModeTv = (TextView) findViewById(R.id.create_schedule_repeat_mode_tv);

		createScheduleSetRepeatCutOffDateTr = (TableRow) findViewById(R.id.create_schedule_set_repeat_cut_off_date_tr);
		createScheduleRepeatCutOffDateTv = (TextView) findViewById(R.id.create_schedule_repeat_cut_off_date_tv);

		createScheduleAddRemarkEt = (EditText) findViewById(R.id.create_schedule_add_remark_et);

		createScheduleStatusTv = (TextView) findViewById(R.id.create_schedule_status_tv);

		createScheduleSetLocationIb = (ImageButton) findViewById(R.id.create_schedule_set_location_ib);

		createScheduleContentDictationIb.setOnClickListener(this);
		createScheduleSetTagTr.setOnClickListener(this);
		createScheduleSetDateTr.setOnClickListener(this);
		createScheduleSetTimeTr.setOnClickListener(this);
		creatScheduleSetAlarmModeTr.setOnClickListener(this);
		createScheduleSetRepeatModeTr.setOnClickListener(this);
		createScheduleSetRepeatCutOffDateTr.setOnClickListener(this);

		createScheduleSetLocationIb.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.create_schedule_content_dictation_ib:
			DictationUtil.showDictationDialog(this, new DictationListener() {

				@Override
				public void onDictationListener(String dictationResultStr) {
					EditTextUtil.insertText(createScheduleContentEt,
							dictationResultStr);
					createScheduleContentEt.requestFocus();
					isChanged = true;
				}
			});
			break;
		case R.id.create_schedule_set_tag_tr:
			showChooseTagDialog();
			break;
		case R.id.create_schedule_set_alarm_mode_tr:
			newSetAlarmModeDialog();
			break;
		case R.id.create_schedule_set_repeat_mode_tr:
			newSetRepeatModeDialog();
			break;
		case R.id.create_schedule_set_location_ib:

			break;
		default:
			break;
		}
	}

	// 弹出设置日程标签的窗口
	protected void showChooseTagDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScheduleDetailBaseActivity.this);

		dialog.setTitle("设置日程标签");
		dialog.setItems(Schedule.getTagTextArray(),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedTagIndex = which;
						createScheduleTagTv.setText(Schedule.getTagTextArray()[selectedTagIndex]);
						createScheduleContentEt
								.setTextColor(Schedule.TAG_COLOR_ARR[selectedTagIndex]);
					}
				});
		dialog.setCancelable(true);

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// 弹出设置日程提醒方式的窗口
	protected void newSetAlarmModeDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScheduleDetailBaseActivity.this);

		dialog.setTitle("选择日程的提醒方式");
		dialog.setSingleChoiceItems(Schedule.ALARM_MODE_STRING_ARRAY,
				selectedAlarmModeIndex, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedAlarmModeIndex = which;
					}
				});
		dialog.setCancelable(true);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				createScheduleAlarmModeTv
						.setText(Schedule.ALARM_MODE_STRING_ARRAY[selectedAlarmModeIndex]);
				isChanged = true;
			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// 弹出设置日程重复模式的窗口
	protected void newSetRepeatModeDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScheduleDetailBaseActivity.this);

		dialog.setTitle("选择日程的重复模式");
		dialog.setSingleChoiceItems(Schedule.REPEAT_MODE_STRING_ARRAY,
				selectedRepeatModeIndex, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedRepeatModeIndex = which;
					}
				});
		dialog.setCancelable(true);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 如果选择的重复模式不是“不重复”，那么让选择重复截止日期的Tr显示出来，并默认截止日期为从设置的日程开始时间算起10个重复周期之后的日期
				if (selectedRepeatModeIndex != 0) {
					createScheduleSetRepeatCutOffDateTr
							.setVisibility(View.VISIBLE);
					findViewById(R.id.repeat_cut_off_divider_v).setVisibility(
							View.VISIBLE);
					// 自动算出的默认截止日期（默认为GlobalConfig.DEFAULT_REPEAT_CYCLE_NUM个周期后的日期）
					// 注：每工作日重复不算周六周日
					// 当前设置的日程开始时间
					String selectedStartDate = creatScheduleDateTv.getText()
							.toString();
					createScheduleRepeatCutOffDateTv.setText(Schedule
							.getNCycleNumLaterDateString(selectedStartDate,
									selectedRepeatModeIndex - 1,
									GlobalConfig.DEFAULT_REPEAT_CYCLE_NUM));
				} else {
					createScheduleSetRepeatCutOffDateTr
							.setVisibility(View.GONE);
					findViewById(R.id.repeat_cut_off_divider_v).setVisibility(
							View.GONE);
				}

				createScheduleRepeatModeTv
						.setText(Schedule.REPEAT_MODE_STRING_ARRAY[selectedRepeatModeIndex]);
				isChanged = true;
			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// 根据日程的提醒方式设置提醒
	protected void setScheduleAlarm(Schedule schedule) {
		// 如果日程的提醒方式为不提醒，则什么都不做
		if (schedule.getAlarmMode() == Schedule.ALARM_MODE_OFF) {
			return;
		}

		Intent intent = new Intent(this, ScheduleAlarmService.class);
		intent.putExtra("schedule", schedule);
		startService(intent);
	}

	// 处理日程的重复创建
	protected void setScheduleRepeat(final Schedule schedule) {
		// 如果日程的重复模式为不重复，那么什么都不做
		if (schedule.getRepeatMode() == Schedule.REPEAT_MODE_OFF) {
			return;
		}

		// 在线程中创建重复模式日程
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		List<String> cycleTimeList = Schedule.getAllCycleDateTime(
				schedule.getStartTime(), schedule.getRepeatCutOffDate()
						+ " 23:59:59", schedule.getRepeatMode());
		for (int i = 0; i < cycleTimeList.size(); i++) {
			String newStartTime = cycleTimeList.get(i);

			Schedule newSchedule = new Schedule(schedule, newStartTime);
			int id = easyDoDB.saveSchedule(newSchedule);
			newSchedule.setId(id);
			setScheduleAlarm(newSchedule);
		}
		// }
		// }).start();
	}

}
