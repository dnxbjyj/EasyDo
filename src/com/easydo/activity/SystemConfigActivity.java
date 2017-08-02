package com.easydo.activity;

import com.easydo.constant.GlobalConfig;
import com.easydo.layout.BaseTitleLayout;
import com.easydo.layout.ChooseTypefaceLayout;
import com.easydo.layout.ChooseTypefaceLayout.onTypefaceChooseListener;
import com.easydo.layout.SlideSwitch;
import com.easydo.layout.SlideSwitch.SlideSwitchListener;
import com.easydo.model.SystemConfig;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.easydo.util.TypefaceUtil;
import com.jiayongji.easydo.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

public class SystemConfigActivity extends BaseActivity implements
		OnClickListener {
	// �������Ŀؼ�
	private ImageButton titleLeftIb;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// �ճ�ʱ�����Ƿ���ʾ����ʱ��Ŀ��ذ�ť
	private SlideSwitch showScheduleDetailTimeSwitch;

	// �ճ�����ʱ�Ƿ�����
	private SlideSwitch scheduelAlarmVoiceSwitch;
	// �ճ�����ʱ�Ƿ���
	private SlideSwitch scheduelAlarmVibrateSwitch;

	// �ճ��Ƿ���ʾ��ǩ��ɫ����
	private SlideSwitch showScheduleColorfulSwitch;

	// �ճ���������Tr
	private TableRow showScheduleTypefaceTr;
	// ��־��������Tr
	private TableRow showJournalTypefaceTr;
	// ѡ����ճ�����
	private TextView selectedScheduleTypefaceTv;
	// ѡ�����־����
	private TextView selectedJournalTypefaceTv;

	// �������ݱ��ݹ���
	private TableRow localBackupTr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_system_config);

		systemConfig = easyDoDB.loadSystemConfig(null, null);

		/**
		 * �������ĳ�ʼ��
		 */
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleLeftIb.setImageResource(R.drawable.back1_64);
		titleLeftIb.setOnClickListener(this);

		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleMiddleTv.setText("����");
		titleMiddleTv.setTextSize(20);

		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);
		titleRightIb.setImageResource(R.drawable.write2_64);
		titleRightIb.setVisibility(View.INVISIBLE);

		/**
		 * ��ѡ���������γ�ʼ��
		 */
		/**
		 * �ճ������Ƿ��������
		 */
		scheduelAlarmVoiceSwitch = (SlideSwitch) findViewById(R.id.scheduel_alarm_voice_switch);
		scheduelAlarmVibrateSwitch = (SlideSwitch) findViewById(R.id.scheduel_alarm_vibrate_switch);
		switch (systemConfig.getScheduleAlarmWay()) {
		case SystemConfig.SCHEDULE_ALARM_WAY_OFF:
			scheduelAlarmVoiceSwitch.setOff();
			scheduelAlarmVibrateSwitch.setOff();
			break;
		case SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE_BELL:
			scheduelAlarmVoiceSwitch.setOn();
			scheduelAlarmVibrateSwitch.setOn();
			break;
		case SystemConfig.SCHEDULE_ALARM_WAY_BELL:
			scheduelAlarmVoiceSwitch.setOn();
			scheduelAlarmVibrateSwitch.setOff();
			break;
		case SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE:
			scheduelAlarmVoiceSwitch.setOff();
			scheduelAlarmVibrateSwitch.setOn();
			break;
		default:
			break;
		}

		scheduelAlarmVoiceSwitch.setOnSlideSwitchListener(
				SystemConfigActivity.this, new SlideSwitchListener() {

					@Override
					public void onClick(boolean isOn) {
						int alarmWay;
						if (isOn) {
							scheduelAlarmVoiceSwitch.setOn();

							if (scheduelAlarmVibrateSwitch.isOn()) {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE_BELL;
							} else {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_BELL;
							}
						} else {
							scheduelAlarmVoiceSwitch.setOff();

							if (scheduelAlarmVibrateSwitch.isOn()) {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE;
							} else {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_OFF;
							}
						}

						easyDoDB.updateDB(SystemConfig.TABLE_NAME,
								"schedule_alarm_way", alarmWay + "", "id", "1");
						systemConfig.setScheduleAlarmWay(alarmWay);
					}
				});

		scheduelAlarmVibrateSwitch.setOnSlideSwitchListener(
				SystemConfigActivity.this, new SlideSwitchListener() {

					@Override
					public void onClick(boolean isOn) {
						int alarmWay;
						if (isOn) {
							scheduelAlarmVibrateSwitch.setOn();

							if (scheduelAlarmVoiceSwitch.isOn()) {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE_BELL;
							} else {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE;
							}
						} else {
							scheduelAlarmVibrateSwitch.setOff();

							if (scheduelAlarmVoiceSwitch.isOn()) {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_BELL;
							} else {
								alarmWay = SystemConfig.SCHEDULE_ALARM_WAY_OFF;
							}
						}

						easyDoDB.updateDB(SystemConfig.TABLE_NAME,
								"schedule_alarm_way", alarmWay + "", "id", "1");
						systemConfig.setScheduleAlarmWay(alarmWay);

					}
				});

		/**
		 * �ճ�ʱ������ʾ�Ƿ���ʾ����ʱ��
		 */
		showScheduleDetailTimeSwitch = (SlideSwitch) findViewById(R.id.show_schedule_detail_time_switch);
		if (systemConfig.getTimeLineShowDetailTime() == SystemConfig.TIME_LINE_NOT_SHOW_DETAIL_TIME) {
			showScheduleDetailTimeSwitch.setOff();
		} else {
			showScheduleDetailTimeSwitch.setOn();
		}
		showScheduleDetailTimeSwitch.setOnSlideSwitchListener(
				SystemConfigActivity.this, new SlideSwitchListener() {

					@Override
					public void onClick(boolean isOn) {
						if (isOn) {
							showScheduleDetailTimeSwitch.setOn();
							easyDoDB.updateDB(SystemConfig.TABLE_NAME,
									"time_line_show_detail_time",
									SystemConfig.TIME_LINE_SHOW_DETAIL_TIME
											+ "", "id", "1");
							systemConfig
									.setTimeLineShowDetailTime(SystemConfig.TIME_LINE_SHOW_DETAIL_TIME);
						} else {
							showScheduleDetailTimeSwitch.setOff();
							easyDoDB.updateDB(SystemConfig.TABLE_NAME,
									"time_line_show_detail_time",
									SystemConfig.TIME_LINE_NOT_SHOW_DETAIL_TIME
											+ "", "id", "1");
							systemConfig
									.setTimeLineShowDetailTime(SystemConfig.TIME_LINE_NOT_SHOW_DETAIL_TIME);
						}
					}
				});

		/**
		 * �ճ��Ƿ���ʾ��ǩ��ɫ����
		 */
		showScheduleColorfulSwitch = (SlideSwitch) findViewById(R.id.show_schedule_colorful_switch);
		if (systemConfig.getShowScheduleColorful() == SystemConfig.SHOW_SCHEDULE_NOT_COLORFUL) {
			showScheduleColorfulSwitch.setOff();
		} else {
			showScheduleColorfulSwitch.setOn();
		}
		showScheduleColorfulSwitch.setOnSlideSwitchListener(
				SystemConfigActivity.this, new SlideSwitchListener() {

					@Override
					public void onClick(boolean isOn) {
						if (isOn) {
							showScheduleColorfulSwitch.setOn();
							easyDoDB.updateDB(SystemConfig.TABLE_NAME,
									"show_schedule_colorful",
									SystemConfig.SHOW_SCHEDULE_COLORFUL + "",
									"id", "1");
							systemConfig
									.setShowScheduleColorful(SystemConfig.SHOW_SCHEDULE_COLORFUL);
						} else {
							showScheduleColorfulSwitch.setOff();
							easyDoDB.updateDB(SystemConfig.TABLE_NAME,
									"show_schedule_colorful",
									SystemConfig.SHOW_SCHEDULE_NOT_COLORFUL
											+ "", "id", "1");
							systemConfig
									.setShowScheduleColorful(SystemConfig.SHOW_SCHEDULE_NOT_COLORFUL);
						}
					}
				});

		/**
		 * �ճ���������
		 */
		showScheduleTypefaceTr = (TableRow) findViewById(R.id.show_schedule_typeface_tr);
		selectedScheduleTypefaceTv = (TextView) findViewById(R.id.selected_schedule_typeface_tv);
		TypefaceUtil typeface = new TypefaceUtil(SystemConfigActivity.this,
				systemConfig.getScheduleContentTypeface());
		typeface.setTypeface(selectedScheduleTypefaceTv, false);
		selectedScheduleTypefaceTv.setText(GlobalConfig
				.getTypefaceName(systemConfig.getScheduleContentTypeface()));
		showScheduleTypefaceTr.setOnClickListener(this);

		/**
		 * ��־��������
		 */
		showJournalTypefaceTr = (TableRow) findViewById(R.id.show_journal_typeface_tr);
		selectedJournalTypefaceTv = (TextView) findViewById(R.id.selected_journal_typeface_tv);
		typeface.setmTypeface(systemConfig.getJournalContentTypeface());
		typeface.setTypeface(selectedJournalTypefaceTv, false);
		selectedJournalTypefaceTv.setText(GlobalConfig
				.getTypefaceName(systemConfig.getJournalContentTypeface()));
		showJournalTypefaceTr.setOnClickListener(this);

		/**
		 * ���ݱ���
		 */
		localBackupTr = (TableRow) findViewById(R.id.local_backup_tr);
		localBackupTr.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_base_left_ib:
			setResult(ScheduleActivity.SYSTEM_CONFIG_REQUEST_CODE);
			finish();
			break;
		case R.id.show_schedule_typeface_tr:
			newChooseTypefaceDialog(ChooseTypefaceLayout.CONTENT_TYPE_SCHEDULE);
			break;
		case R.id.show_journal_typeface_tr:
			newChooseTypefaceDialog(ChooseTypefaceLayout.CONTENT_TYPE_JOURNAL);
			break;
		case R.id.local_backup_tr:
			Intent intent1 = new Intent(SystemConfigActivity.this,
					LocalBackupActivity.class);
			startActivity(intent1);
			break;
		default:
			break;
		}
	}

	private void newChooseTypefaceDialog(final int type) {
		AlertDialog dialog = new AlertDialog.Builder(SystemConfigActivity.this)
				.create();
		ChooseTypefaceLayout view = new ChooseTypefaceLayout(
				SystemConfigActivity.this, null, type);

		final AlertDialog finalDialog = dialog;
		view.setOnChooseListenter(SystemConfigActivity.this,
				new onTypefaceChooseListener() {

					@Override
					public void onChoosed(String typefaceName, String ttfPath) {
						if (type == ChooseTypefaceLayout.CONTENT_TYPE_SCHEDULE) {
							selectedScheduleTypefaceTv.setText(typefaceName);
							new TypefaceUtil(SystemConfigActivity.this, ttfPath)
									.setTypeface(selectedScheduleTypefaceTv,
											false);
						} else if (type == ChooseTypefaceLayout.CONTENT_TYPE_JOURNAL) {
							selectedJournalTypefaceTv.setText(typefaceName);
							new TypefaceUtil(SystemConfigActivity.this, ttfPath)
									.setTypeface(selectedJournalTypefaceTv,
											false);
						}

						finalDialog.dismiss();
					}
				});
		dialog.setView(view);
		dialog.show();
	}

	@Override
	public void onBackPressed() {
		setResult(ScheduleActivity.SYSTEM_CONFIG_REQUEST_CODE);
		finish();
	}
}
