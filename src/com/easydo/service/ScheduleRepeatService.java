package com.easydo.service;

/**
 * �����ճ��ظ��ķ���
 */

import com.easydo.model.Schedule;

import android.app.IntentService;
import android.content.Intent;

public class ScheduleRepeatService extends IntentService {
	// �ӻ��������Schedule����
	private Schedule schedule;

	// �ճ̵�id
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
