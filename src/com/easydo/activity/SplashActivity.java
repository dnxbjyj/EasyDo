package com.easydo.activity;

import com.easydo.db.EasyDoDB;
import com.easydo.model.ScheduleBook;
import com.easydo.model.ScheduleBookType;
import com.easydo.model.SystemConfig;
import com.easydo.util.DateTimeUtil;
import com.jiayongji.easydo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class SplashActivity extends BaseActivity {

	private final int SPLASH_DISPLAY_LENGHT = 1000;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();

		// ��ʼ�����ݿ�
		initDB();

		handler = new Handler();
		// �ӳ�SPLASH_DISPLAY_LENGHTʱ��Ȼ����ת��ScheduleDisplayActivity
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						ScheduleActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);

	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_splash);
	}

	// ��ʼ�����ݿ�
	private void initDB() {
		easyDoDB = EasyDoDB.getInstance(this);

		// �ж����ݿ��Ƿ��Ѿ���ʼ�������û�г�ʼ����ʼ��ʼ��һЩ����
		if (easyDoDB.loadInitOriginalDataDone() == 0) {
			// ��ʼ��ϵͳ��������
			initSystemData();

			// д���ʼ����ɱ�־����
			easyDoDB.saveInitOriginalDataDone(1);
		}
	}

	// ��ʼ��ϵͳ��������
	protected void initSystemData() {
		// ����Ĭ���ճ̱�����
		ScheduleBookType scheduleBookType = new ScheduleBookType();
		scheduleBookType.setName("Ĭ�Ϸ���");
		easyDoDB.saveScheduleBookType(scheduleBookType);

		// ����Ĭ�ϱʼǱ�
		ScheduleBook scheduleBook = new ScheduleBook();
		scheduleBook.setName("Ĭ���ճ̱�");
		scheduleBook.setCreateTime(DateTimeUtil.getCurrentDateTimeString());
		scheduleBook.setStatus(ScheduleBook.STATUS_ACTIVED);
		scheduleBook.setScheduleNum(0);
		scheduleBook.setTypeId(1);
		easyDoDB.saveScheduleBook(scheduleBook);

		// Ĭ��ϵͳ����
		systemConfig = new SystemConfig();
		systemConfig.setVersion("1.0.0");
		systemConfig
				.setScheduleShowWay(SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW);
		systemConfig.setPrivacyWay(SystemConfig.PRIVACY_WAY_OFF);
		systemConfig.setPassword("");
		systemConfig
				.setScheduleAlarmWay(SystemConfig.SCHEDULE_ALARM_WAY_VIBRATE_BELL);
		systemConfig.setActivatedScheduleBookId(1);
		systemConfig
				.setTimeLineShowDetailTime(SystemConfig.TIME_LINE_SHOW_DETAIL_TIME);
		systemConfig.setScheduleContentTypeface("default");
		systemConfig.setJournalContentTypeface("default");
		systemConfig
				.setShowScheduleColorful(SystemConfig.SHOW_SCHEDULE_NOT_COLORFUL);
		easyDoDB.saveSystemConfig(systemConfig);
	}

	// ���÷��ذ�ť
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
