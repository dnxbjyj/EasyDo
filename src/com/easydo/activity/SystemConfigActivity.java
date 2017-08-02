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
	// 标题栏的控件
	private ImageButton titleLeftIb;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// 日程时间轴是否显示具体时间的开关按钮
	private SlideSwitch showScheduleDetailTimeSwitch;

	// 日程提醒时是否响铃
	private SlideSwitch scheduelAlarmVoiceSwitch;
	// 日程提醒时是否振动
	private SlideSwitch scheduelAlarmVibrateSwitch;

	// 日程是否显示标签颜色设置
	private SlideSwitch showScheduleColorfulSwitch;

	// 日程字体设置Tr
	private TableRow showScheduleTypefaceTr;
	// 日志字体设置Tr
	private TableRow showJournalTypefaceTr;
	// 选择的日程字体
	private TextView selectedScheduleTypefaceTv;
	// 选择的日志字体
	private TextView selectedJournalTypefaceTv;

	// 本地数据备份管理
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
		 * 标题栏的初始化
		 */
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleLeftIb.setImageResource(R.drawable.back1_64);
		titleLeftIb.setOnClickListener(this);

		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleMiddleTv.setText("设置");
		titleMiddleTv.setTextSize(20);

		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);
		titleRightIb.setImageResource(R.drawable.write2_64);
		titleRightIb.setVisibility(View.INVISIBLE);

		/**
		 * 各选项栏的依次初始化
		 */
		/**
		 * 日程提醒是否响铃和振动
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
		 * 日程时间轴显示是否显示具体时间
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
		 * 日程是否显示标签颜色设置
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
		 * 日程字体设置
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
		 * 日志字体设置
		 */
		showJournalTypefaceTr = (TableRow) findViewById(R.id.show_journal_typeface_tr);
		selectedJournalTypefaceTv = (TextView) findViewById(R.id.selected_journal_typeface_tv);
		typeface.setmTypeface(systemConfig.getJournalContentTypeface());
		typeface.setTypeface(selectedJournalTypefaceTv, false);
		selectedJournalTypefaceTv.setText(GlobalConfig
				.getTypefaceName(systemConfig.getJournalContentTypeface()));
		showJournalTypefaceTr.setOnClickListener(this);

		/**
		 * 数据备份
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
