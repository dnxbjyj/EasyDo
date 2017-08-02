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

	// ��ScheduleAlarmService���񴫹�����Schedule����
	private Schedule schedule;

	private EasyDoDB easyDoDB;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		schedule = intent.getParcelableExtra("schedule");

		// �ճ�����
		AlarmManagerUtil.cancelAlarmBroadcast(context, schedule.getId(),
				ScheduleAlarmReceiver.class);

		if (schedule.getStatus() != Schedule.STATUS_FINISHED
				&& schedule.getStatus() != Schedule.STATUS_DELETED) {
			showAlarm();
		}

	}

	// ��ʾ�ճ�֪ͨdialog
	private void showAlarm() {
		// �������Ѵ���
		Intent i = new Intent(mContext, AlarmActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("schedule", schedule);
		mContext.startActivity(i);

	}

}
