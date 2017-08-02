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

		// 初始化数据库
		initDB();

		handler = new Handler();
		// 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到ScheduleDisplayActivity
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

	// 初始化数据库
	private void initDB() {
		easyDoDB = EasyDoDB.getInstance(this);

		// 判断数据库是否已经初始化，如果没有初始化则开始初始化一些数据
		if (easyDoDB.loadInitOriginalDataDone() == 0) {
			// 初始化系统设置数据
			initSystemData();

			// 写入初始化完成标志数据
			easyDoDB.saveInitOriginalDataDone(1);
		}
	}

	// 初始化系统设置数据
	protected void initSystemData() {
		// 创建默认日程本分类
		ScheduleBookType scheduleBookType = new ScheduleBookType();
		scheduleBookType.setName("默认分类");
		easyDoDB.saveScheduleBookType(scheduleBookType);

		// 创建默认笔记本
		ScheduleBook scheduleBook = new ScheduleBook();
		scheduleBook.setName("默认日程本");
		scheduleBook.setCreateTime(DateTimeUtil.getCurrentDateTimeString());
		scheduleBook.setStatus(ScheduleBook.STATUS_ACTIVED);
		scheduleBook.setScheduleNum(0);
		scheduleBook.setTypeId(1);
		easyDoDB.saveScheduleBook(scheduleBook);

		// 默认系统设置
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

	// 禁用返回按钮
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
