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
 * ��CreateScheduleActivity��ScheduleDetailActivity������Ľ��漸����ȫһ��������Ϊ�˴������ã�
 * ��������������������Ǽ̳�
 *
 */

public class ScheduleDetailBaseActivity extends BaseActivity implements
		OnClickListener {
	// TableLayout��������EditTextʧȥ����
	protected View tableTl;

	// ����������Ib
	protected ImageButton titleLeftIb;
	// �������м��Tv
	protected TextView titleMiddleTv;
	// ���������Ҳ�Ib
	protected ImageButton titleRightIb;

	// �ճ������Ƿ��޸�
	boolean isChanged = false;

	// �ճ�������
	protected LinearLayout contentInputLl;
	// �ճ����ԲȦ
	protected ImageView scheduleStatusImageIv;
	// �ճ�����et
	protected EditText createScheduleContentEt;
	// �ճ�����������дib
	protected ImageButton createScheduleContentDictationIb;

	// �����ճ̵ı�ǩ
	protected TableRow createScheduleSetTagTr;
	// ��ʾ�ճ̱�ǩ��Tv
	protected TextView createScheduleTagTv;

	// ѡ���ճ����ڵ�tr
	protected TableRow createScheduleSetDateTr;
	// ��ʾѡ������ڵ�tv
	protected TextView creatScheduleDateTv;

	// ѡ���ճ�ʱ���tr
	protected TableRow createScheduleSetTimeTr;
	// ��ʾѡ���ʱ���tv
	protected TextView creatScheduleTimeTv;

	// �����ճ̵����ѷ�ʽ
	protected TableRow creatScheduleSetAlarmModeTr;
	// ��ʾ�ճ̵����ѷ�ʽ
	protected TextView createScheduleAlarmModeTv;

	// �����ճ̵��ظ�ģʽ
	protected TableRow createScheduleSetRepeatModeTr;
	// ��ʾ�ճ̵��ظ�ģʽ
	protected TextView createScheduleRepeatModeTv;

	// �ճ̵��ظ���ֹ��������
	protected TableRow createScheduleSetRepeatCutOffDateTr;
	// �ճ̵��ظ���ֹ������ʾ
	protected TextView createScheduleRepeatCutOffDateTv;

	// �ճ̵ı�ע����
	protected EditText createScheduleAddRemarkEt;

	// �ճ̵�ǰ״̬����ʾ
	protected TextView createScheduleStatusTv;

	// �����ճ̵ĵص㰴ť
	protected ImageButton createScheduleSetLocationIb;

	// ��ǰѡ����ճ̱�ǩindex
	protected int selectedTagIndex = Schedule.TAG_MATTERS;
	// ��ǰѡ����ճ����ѷ�ʽindex
	protected int selectedAlarmModeIndex;
	// ��ǰѡ����ճ̵��ظ�ģʽ��index
	protected int selectedRepeatModeIndex;

	// startTime�����ֵ
	protected static int startTimeSecond = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_create_detail_schedule);

		// �õ����ʱ��עEtʧȥ����
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

	// ���������ճ̱�ǩ�Ĵ���
	protected void showChooseTagDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScheduleDetailBaseActivity.this);

		dialog.setTitle("�����ճ̱�ǩ");
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

		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// ���������ճ����ѷ�ʽ�Ĵ���
	protected void newSetAlarmModeDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScheduleDetailBaseActivity.this);

		dialog.setTitle("ѡ���ճ̵����ѷ�ʽ");
		dialog.setSingleChoiceItems(Schedule.ALARM_MODE_STRING_ARRAY,
				selectedAlarmModeIndex, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedAlarmModeIndex = which;
					}
				});
		dialog.setCancelable(true);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				createScheduleAlarmModeTv
						.setText(Schedule.ALARM_MODE_STRING_ARRAY[selectedAlarmModeIndex]);
				isChanged = true;
			}
		});

		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// ���������ճ��ظ�ģʽ�Ĵ���
	protected void newSetRepeatModeDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ScheduleDetailBaseActivity.this);

		dialog.setTitle("ѡ���ճ̵��ظ�ģʽ");
		dialog.setSingleChoiceItems(Schedule.REPEAT_MODE_STRING_ARRAY,
				selectedRepeatModeIndex, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedRepeatModeIndex = which;
					}
				});
		dialog.setCancelable(true);
		dialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// ���ѡ����ظ�ģʽ���ǡ����ظ�������ô��ѡ���ظ���ֹ���ڵ�Tr��ʾ��������Ĭ�Ͻ�ֹ����Ϊ�����õ��ճ̿�ʼʱ������10���ظ�����֮�������
				if (selectedRepeatModeIndex != 0) {
					createScheduleSetRepeatCutOffDateTr
							.setVisibility(View.VISIBLE);
					findViewById(R.id.repeat_cut_off_divider_v).setVisibility(
							View.VISIBLE);
					// �Զ������Ĭ�Ͻ�ֹ���ڣ�Ĭ��ΪGlobalConfig.DEFAULT_REPEAT_CYCLE_NUM�����ں�����ڣ�
					// ע��ÿ�������ظ�������������
					// ��ǰ���õ��ճ̿�ʼʱ��
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

		dialog.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	// �����ճ̵����ѷ�ʽ��������
	protected void setScheduleAlarm(Schedule schedule) {
		// ����ճ̵����ѷ�ʽΪ�����ѣ���ʲô������
		if (schedule.getAlarmMode() == Schedule.ALARM_MODE_OFF) {
			return;
		}

		Intent intent = new Intent(this, ScheduleAlarmService.class);
		intent.putExtra("schedule", schedule);
		startService(intent);
	}

	// �����ճ̵��ظ�����
	protected void setScheduleRepeat(final Schedule schedule) {
		// ����ճ̵��ظ�ģʽΪ���ظ�����ôʲô������
		if (schedule.getRepeatMode() == Schedule.REPEAT_MODE_OFF) {
			return;
		}

		// ���߳��д����ظ�ģʽ�ճ�
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
