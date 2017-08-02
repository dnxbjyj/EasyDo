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

	// ǰһ���������������ʱ���ַ���
	private String formerDateTime;
	// ǰһ������������ճ������Ƿ�Ϊ��
	private boolean isFormerContentEmpty;
	// ǰһ����������ճ����ݣ�SimplyCreateScheduleDialog����������ݣ�
	private String formerContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʼ����һ���������������
		initData();

		// ��ʼ�������ؼ�
		titleMiddleTv.setText("�½��ճ�");
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

	// ��ʼ����һ���������������
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
							year, month, day, "ѡ���ճ̵�����",
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
							DateFormat.is24HourFormat(getActivity()), "ѡ���ճ̵�ʱ��");
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
							year, month, day, "ѡ���ظ��Ľ�ֹ����",
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

	// ���浱ǰҳ����ճ����ݵ����ݿ�
	private void saveScheduleData() {
		// ���ж��ճ̵������Ƿ�Ϊ��
		String scheduleContentStr = createScheduleContentEt.getText()
				.toString();
		if (scheduleContentStr == null || scheduleContentStr.length() <= 0) {
			ToastUtil.showShort(CreateScheduleActivity.this, "�ճ̵����ݲ���Ϊ��Ŷ~");
			return;
		}

		Schedule schedule = new Schedule();
		// ��ȡ��ǰ����ʹ�õ�scheduleBookId
		int scheduleBookId = systemConfig.getActivatedScheduleBookId();
		// ��ǰʱ�������-ʱ���ַ�����Ϊ�ճ̵Ĵ���ʱ��
		String createTime = DateTimeUtil.getCurrentDateTimeString();
		// �ճ̵�����(content)
		String content = scheduleContentStr;
		// �ճ̿�ʼ��ʱ��
		String startTime = creatScheduleDateTv.getText().toString() + " "
				+ creatScheduleTimeTv.getText().toString() + ":"
				+ DateTimeUtil.getDoubleNumString(startTimeSecond);
		startTimeSecond++;
		// ���ѷ�ʽ��������ƵĹ���alarmMode�պ���selectedAlarmModeIndex-1
		int alarmMode = selectedAlarmModeIndex - 1;

		// �ظ���ʽ�������ѷ�ʽͬ��
		int repeatMode = selectedRepeatModeIndex - 1;
		if (repeatMode != Schedule.REPEAT_MODE_OFF) {
			String repeatCutOffDate = createScheduleRepeatCutOffDateTv
					.getText().toString();
			// �ж��ճ̵��ظ���ֹ�����Ƿ�ȿ�ʼ���ڴ���������ھ���ʾ������Ϣ
			if (DateTimeUtil.compareDateString(repeatCutOffDate,
					creatScheduleDateTv.getText().toString()) <= 0) {
				ToastUtil.showShort(CreateScheduleActivity.this,
						"�ظ���ֹʱ��Ӧ�����ճ̿�ʼʱ���Ժ�������ѡ��");
				return;
			} else {
				schedule.setRepeatCutOffDate(repeatCutOffDate);
			}
		}
		// ����alarmMode��startTime����alarmTime
		String alarmTime = Schedule.calAlarmTime(alarmMode, startTime);
		// �ճ�״̬
		int status = Schedule.STATUS_UNFINISHED;
		// �ճ̱�ע
		String remark = createScheduleAddRemarkEt.getText().toString();
		// �ճ̱�ǩ
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

		// �����½��ճ̳ɹ���ʾ��
		ToastUtil.showShort(CreateScheduleActivity.this, "�½��ճ̳ɹ���");
		// �����ճ�����
		setScheduleAlarm(schedule);
		// �����ճ̵��ظ�����
		setScheduleRepeat(schedule);

		// ���ڷ������ݸ�ScheduleDisplayActivity�Ա�����ճ���ʾ�б�
		Intent intent = new Intent(CreateScheduleActivity.this,
				ScheduleActivity.class);
		setResult(ScheduleActivity.CREATE_SCHEDULE_REQUEST_CODE, intent);
		// ������ǰ�
		finish();
	}
}
