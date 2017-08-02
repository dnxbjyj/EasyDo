package com.easydo.activity;

import java.io.IOException;

import com.easydo.model.Schedule;
import com.easydo.model.SystemConfig;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AlarmActivity extends BaseActivity implements OnClickListener {
	private Window mWin;
	private PopupWindow window;

	private Schedule schedule;
	// Popup窗口的日程内容
	private TextView contentTv;
	// 日程具体时间
	private TextView timeTv;
	// 日程标签
	private TextView tagTv;
	// 查看日程详情按钮
	private LinearLayout forDetailLl;
	// 关闭窗口按钮
	private LinearLayout closeLl;
	// 完成日程按钮
	private LinearLayout doneLl;

	private Vibrator vibrator;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 锁屏状态下唤醒屏幕
		mWin = getWindow();
		mWin.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		mWin.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		initUI();

	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_alarm);

		systemConfig = easyDoDB.loadSystemConfig(null, null);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// 弹出提示窗口
				showPopupWindow();

				switch (systemConfig.getScheduleAlarmWay()) {
				case SystemConfig.SCHEDULE_ALARM_WAY_BELL:
					// 声音提醒
					startVoiceAlarm();
					break;
				case SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE:
					// 振动提示
					startVibrator();
					break;
				case SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE_BELL:
					// 声音提醒
					startVoiceAlarm();
					// 振动提示
					startVibrator();
					break;
				case SystemConfig.SCHEDULE_ALARM_WAY_OFF:
					break;
				default:
					break;
				}

			}
		}, 1000);
	}

	// 弹出Popup窗口
	private void showPopupWindow() {
		// 利用LayoutInflater获得View
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.popup_window_layout, null);

		schedule = getIntent().getParcelableExtra("schedule");

		contentTv = (TextView) view.findViewById(R.id.content_tv);
		contentTv.setText(schedule.getContent());
		timeTv = (TextView) view.findViewById(R.id.time_tv);
		timeTv.setText(DateTimeUtil.getTimeString(schedule.getStartTime()));
		tagTv = (TextView) view.findViewById(R.id.tag_tv);
		tagTv.setText(Schedule.getTagTextArray()[schedule.getTag()]);
		forDetailLl = (LinearLayout) view.findViewById(R.id.for_detail_ll);
		forDetailLl.setOnClickListener(this);
		closeLl = (LinearLayout) view.findViewById(R.id.close_ll);
		closeLl.setOnClickListener(this);
		doneLl = (LinearLayout) view.findViewById(R.id.done_ll);
		doneLl.setOnClickListener(this);

		// 创建PopupWindow
		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// 设置window可点击，这句话必须要有，并且是true
		window.setFocusable(true);

		// 设置window的显示和消失动画
		window.setAnimationStyle(R.style.MyPopupWindowAnimStyle);

		// 在底部显示
		window.showAtLocation(
				AlarmActivity.this.findViewById(R.id.main_layout),
				Gravity.BOTTOM, 0, 0);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.for_detail_ll:
			Intent intent = new Intent(AlarmActivity.this,
					ScheduleDetailActivity.class);
			intent.putExtra("schedule_data", schedule);
			startActivity(intent);
			onDestroy();
			break;
		case R.id.close_ll:
			onDestroy();
			break;
		case R.id.done_ll:
			easyDoDB.updateDB(Schedule.TABLE_NAME, "status",
					Schedule.STATUS_FINISHED + "", "id", schedule.getId() + "");
			ToastUtil.showShort(AlarmActivity.this, "完成了一件事情，你真棒！");
			onDestroy();
			break;
		default:
			break;
		}
	}

	// 振动提示
	private void startVibrator() {
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 100, 800, 500, 800 }; // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1); // 重复两次上面的pattern 如果只想震动一次，index设为-1
	}

	// 播放系统默认提示声音提醒
	private void startVoiceAlarm() {
		// 获取alarm uri
		Uri alert = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		// 创建media player
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(this, alert);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mMediaPlayer.setLooping(false);
			try {
				mMediaPlayer.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mMediaPlayer.start();
		}
	}

	@Override
	protected void onDestroy() {
		if (window != null) {
			window.dismiss();
		}

		if (vibrator != null) {
			vibrator.cancel();
		}

		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
		}

		finish();
		super.onDestroy();
	}

}
