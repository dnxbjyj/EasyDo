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
	// Popup���ڵ��ճ�����
	private TextView contentTv;
	// �ճ̾���ʱ��
	private TextView timeTv;
	// �ճ̱�ǩ
	private TextView tagTv;
	// �鿴�ճ����鰴ť
	private LinearLayout forDetailLl;
	// �رմ��ڰ�ť
	private LinearLayout closeLl;
	// ����ճ̰�ť
	private LinearLayout doneLl;

	private Vibrator vibrator;
	private MediaPlayer mMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ����״̬�»�����Ļ
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
				// ������ʾ����
				showPopupWindow();

				switch (systemConfig.getScheduleAlarmWay()) {
				case SystemConfig.SCHEDULE_ALARM_WAY_BELL:
					// ��������
					startVoiceAlarm();
					break;
				case SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE:
					// ����ʾ
					startVibrator();
					break;
				case SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE_BELL:
					// ��������
					startVoiceAlarm();
					// ����ʾ
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

	// ����Popup����
	private void showPopupWindow() {
		// ����LayoutInflater���View
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

		// ����PopupWindow
		window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT);

		// ����window�ɵ������仰����Ҫ�У�������true
		window.setFocusable(true);

		// ����window����ʾ����ʧ����
		window.setAnimationStyle(R.style.MyPopupWindowAnimStyle);

		// �ڵײ���ʾ
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
			ToastUtil.showShort(AlarmActivity.this, "�����һ�����飬�������");
			onDestroy();
			break;
		default:
			break;
		}
	}

	// ����ʾ
	private void startVibrator() {
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 100, 800, 500, 800 }; // ֹͣ ���� ֹͣ ����
		vibrator.vibrate(pattern, -1); // �ظ����������pattern ���ֻ����һ�Σ�index��Ϊ-1
	}

	// ����ϵͳĬ����ʾ��������
	private void startVoiceAlarm() {
		// ��ȡalarm uri
		Uri alert = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		// ����media player
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
