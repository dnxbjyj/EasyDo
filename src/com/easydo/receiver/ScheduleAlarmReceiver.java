package com.easydo.receiver;

import java.util.Calendar;

import com.easydo.db.EasyDoDB;
import com.easydo.model.Schedule;
import com.easydo.service.ScheduleAlarmService;
import com.easydo.util.AlarmManagerUtil;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.easydo.activity.AlarmActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScheduleAlarmReceiver extends BroadcastReceiver {

	private Context mContext;

	// 从ScheduleAlarmService服务传过来的Schedule对象
	private Schedule schedule;

	private EasyDoDB easyDoDB;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		schedule = intent.getParcelableExtra("schedule");

		// 日程提醒
		AlarmManagerUtil.cancelAlarmBroadcast(context, schedule.getId(),
				ScheduleAlarmReceiver.class);

		if (schedule.getStatus() != Schedule.STATUS_FINISHED
				&& schedule.getStatus() != Schedule.STATUS_DELETED) {
			showAlarm();
		}

	}

	// 显示日程通知dialog
	private void showAlarm() {
		// 弹出提醒窗口
		Intent i = new Intent(mContext, AlarmActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("schedule", schedule);
		mContext.startActivity(i);

	}

}
