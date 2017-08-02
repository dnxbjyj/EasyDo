package com.easydo.service;

/**
 * 处理日程提醒的服务
 */

import java.util.Date;

import com.easydo.constant.AlarmAndRepeatFlag;
import com.easydo.model.Schedule;
import com.easydo.receiver.ScheduleAlarmReceiver;
import com.easydo.util.AlarmManagerUtil;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.LogUtil;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class ScheduleAlarmService extends IntentService {

	// 从活动传过来的Schedule对象
	private Schedule schedule;

	// 日程的id
	private int id;
	// 日程的提醒的时间毫秒值
	private long alarmTime;

	public ScheduleAlarmService() {
		super("ScheduleAlarmService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		schedule = intent.getParcelableExtra("schedule");
		id = schedule.getId();

		alarmTime = DateTimeUtil.stringToMillis(schedule.getAlarmTime());

		if (alarmTime < DateTimeUtil.getCurrentTimeMillis()) {
			onDestroy();
			return;
		}

		AlarmManagerUtil.sendScheduleAlarmBroadcast(this, id,
				AlarmManager.RTC_WAKEUP, alarmTime, schedule,
				ScheduleAlarmReceiver.class);
	}
}
