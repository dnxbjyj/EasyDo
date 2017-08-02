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
 * �ճ���ϸ��Ϣ�鿴���༭
 *
 */

public class ScheduleDetailActivity extends ScheduleDetailBaseActivity
		implements OnClickListener {
	private Window mWin;

	// ǰһ������ݹ������ճ̶���
	private Schedule formerSchedule;

	// �µ�Schedule
	private Schedule newSchedule;

	// �ر��ظ���Tr
	private TableRow finishRepeatTr;
	private TextView finishRepeatTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ����״̬�¿��Ի�����Ļ
		mWin = getWindow();
		mWin.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		mWin.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		initData();

		// ��ʼ������
		titleMiddleTv.setText("�ճ���ϸ��Ϣ");
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

	// ��ʼ��ǰһ������ݹ������ճ�����
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

			// ����ճ��ظ�ģʽ�ǲ��ظ��Ļ�
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
							year, month, day, "ѡ���ճ̵�����",
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
							DateFormat.is24HourFormat(getActivity()), "ѡ���ճ̵�ʱ��");
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
			// �����ǰ�ǿɱ༭״̬��˵����û�б����޸ĵ�����
			if (titleRightIb.getTag() != null
					&& titleRightIb.getTag().equals("enabled") && isChanged) {
				ToastUtil.showShort(ScheduleDetailActivity.this, "�뱣���޸��ٷ��أ�");
				return;
			}
			setResult(ScheduleActivity.SCHEDULE_DETAIL_REQUEST_CODE);
			// ������ǰ�
			finish();

			break;
		case R.id.title_base_right_ib:
			if (titleRightIb.getTag() == null
					|| titleRightIb.getTag().equals("not_enabled")) {
				titleRightIb.setTag("enabled");
				titleRightIb.setImageResource(R.drawable.done1_64);
				setEnabled(true);
			} else if (titleRightIb.getTag().equals("enabled")) {
				// ���ж��ճ�content�Ƿ�Ϊ��
				if (TextUtils.isEmpty(createScheduleContentEt.getText()
						.toString())) {
					ToastUtil.showShort(ScheduleDetailActivity.this,
							"�ճ̵����ݲ���Ϊ��Ŷ~");
					return;
				}

				testIsChanged();

				// �ж�content�Ƿ��޸�
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
						.showShort(ScheduleDetailActivity.this, "�����һ�����飬�������");
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
						.showShort(ScheduleDetailActivity.this, "������黹��Ҫ����Ŭ��Ŷ");
			}
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
		if (TextUtils.isEmpty(scheduleContentStr)) {
			ToastUtil.showShort(ScheduleDetailActivity.this, "�ճ̵����ݲ���Ϊ��Ŷ~");
			return;
		}

		Schedule newSchedule = new Schedule();
		// ��ȡ��ǰ����ʹ�õ�scheduleBookId
		int scheduleBookId = systemConfig.getActivatedScheduleBookId();

		// ��ǰʱ�������-ʱ���ַ�����Ϊ�ճ̵Ĵ���ʱ��
		String createTime = formerSchedule.getCreateTime();
		// �ճ̵�����(content)
		String content = scheduleContentStr;
		// �ճ̿�ʼ��ʱ��
		String startTime = creatScheduleDateTv.getText().toString() + " "
				+ creatScheduleTimeTv.getText().toString() + ":"
				+ DateTimeUtil.getDoubleNumString(startTimeSecond);
		startTimeSecond++;
		// ���ѷ�ʽ��������ƵĹ���alarmMode�պ���selectedAlarmModeIndex-1
		int alarmMode = selectedAlarmModeIndex - 1;
		// ������ѷ�ʽ�����˱仯����ôͬ���ظ������������ճ̵����ѷ�ʽ��Ҫ���Ÿı�
		if (alarmMode != formerSchedule.getAlarmMode()) {
			List<Integer> idsList = easyDoDB
					.getSameCreateTimeScheduleId(formerSchedule.getCreateTime());
			for (int id : idsList) {
				easyDoDB.updateDB(Schedule.TABLE_NAME, "alarm_mode", alarmMode
						+ "", "id", id + "");
				Schedule toSetSchedule = easyDoDB.loadScheduleById(id);
				toSetSchedule.setAlarmMode(alarmMode);
				// �����������
				if (alarmMode == Schedule.ALARM_MODE_OFF) {
					// �رո��ճ̵Ķ�ʱ���ѣ��������ģʽ��Ϊ�رյĻ���
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

		// �ظ���ʽ�������ѷ�ʽͬ��
		int repeatMode = selectedRepeatModeIndex - 1;
		if (repeatMode != Schedule.REPEAT_MODE_OFF) {
			String repeatCutOffDate = createScheduleRepeatCutOffDateTv
					.getText().toString();
			// �ж��ճ̵��ظ���ֹ�����Ƿ�ȿ�ʼ���ڴ���������ھ���ʾ������Ϣ
			if (DateTimeUtil.compareDateString(repeatCutOffDate,
					creatScheduleDateTv.getText().toString()) <= 0) {
				ToastUtil.showShort(ScheduleDetailActivity.this,
						"�ظ���ֹʱ��Ӧ�����ճ̿�ʼʱ���Ժ�������ѡ��");
				return;
			} else {
				newSchedule.setRepeatCutOffDate(repeatCutOffDate);
			}
		}

		// ����alarmMode��startTime����alarmTime
		String alarmTime = Schedule.calAlarmTime(alarmMode, startTime);

		// �ճ�״̬
		int status = formerSchedule.getStatus();
		String tag = (String) scheduleStatusImageIv.getTag();
		if (tag.equals("NOT_DONE")) {
			status = Schedule.STATUS_UNFINISHED;
		} else if (tag.equals("DONE")) {
			status = Schedule.STATUS_FINISHED;
		}

		// �ճ̱�ע
		String remark = createScheduleAddRemarkEt.getText().toString();

		// �ճ̱�ǩ
		int scheduleTag = selectedTagIndex;
		// �жϱ�ǩ�Ƿ����ı�
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

		ToastUtil.showShort(ScheduleDetailActivity.this, "�޸��ճ���Ϣ�ɹ���");

		setScheduleAlarm(newSchedule);
	}

	// ���ر��ظ�����ť����¼����������е�����ͬ�������ڵ��ճ̵Ĺر��ظ��Ĳ���
	// ɾ����Щ�ճ̵��п�ʼʱ���ڵ�ǰ�ճ̿�ʼʱ��֮��������ճ̣�������֮ǰ����Щ�ճ̵��ظ���ֹ�����趨Ϊ��ǰ�ճ̵Ŀ�ʼ����
	private void dealWithFinishRepeat(Schedule schedule) {
		// ͬһ���ظ��������ճ̵�id����������ͬ�Ĵ���ʱ��
		List<Integer> sameBatchSchedulesIdList = easyDoDB
				.getSameCreateTimeScheduleId(schedule.getCreateTime());

		// ��sameBatchSchedulesIdList�е�ǰschedule֮��������ճ̶�ɾ��
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
		// ��sameBatchSchedulesIdList�е�ǰschedule֮ǰ�������ճ̵��ظ���ֹ���ڶ�����Ϊ��ǰschedule�Ŀ�ʼ����
		for (int i = 0; i <= lastIndex; i++) {
			int id = sameBatchSchedulesIdList.get(i);
			easyDoDB.updateDB(Schedule.TABLE_NAME, "repeat_cut_off_date",
					schedule.getStartTime().substring(0, 10), "id", id + "");
		}

		createScheduleRepeatCutOffDateTv.setText(schedule.getStartTime()
				.substring(0, 10));
		finishRepeatTr.setTag("finished");
		finishRepeatTr.setEnabled(false);
		((TextView) findViewById(R.id.finish_repeat_tv)).setText("�ظ��ѹر�");
		((TextView) findViewById(R.id.finish_repeat_tv))
				.setTextColor(getResources().getColor(R.drawable.TextDefault));
		ToastUtil.showLong(ScheduleDetailActivity.this,
				"�ر��ظ��ɹ�����ǰ�ճ�֮��������ظ��������ճ���ɾ����");

	}

	@Override
	public void onBackPressed() {
		testIsChanged();
		// �����ǰ�ǿɱ༭״̬��˵����û�б����޸ĵ�����
		if (titleRightIb.getTag() != null
				&& titleRightIb.getTag().equals("enabled") && isChanged) {
			ToastUtil.showShort(ScheduleDetailActivity.this, "�뱣���޸��ٷ��أ�");
			return;
		}
		setResult(ScheduleActivity.SCHEDULE_DETAIL_REQUEST_CODE);
		// ������ǰ�
		finish();

		super.onBackPressed();
	}

	// ���Ե�ǰ��û���޸�δ����
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
