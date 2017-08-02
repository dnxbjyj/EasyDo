package com.easydo.service;

/**
 * 处理日程重复的服务
 */

import com.easydo.model.Schedule;

import android.app.IntentService;
import android.content.Intent;

public class ScheduleRepeatService extends IntentService {
	// 从活动传过来的Schedule对象
	private Schedule schedule;

	// 日程的id
	private int id;

	public ScheduleRepeatService() {
		super("ScheduleRepeatService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		schedule = intent.getParcelableExtra("schedule");
		id = schedule.getId();
	}
}
