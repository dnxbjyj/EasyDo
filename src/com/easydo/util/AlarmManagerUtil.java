package com.easydo.util;

import java.util.List;

import com.easydo.activity.BaseActivity;
import com.easydo.db.EasyDoDB;
import com.easydo.model.Schedule;
import com.easydo.receiver.ScheduleAlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * AlarmManager������
 *
 */

public class AlarmManagerUtil {
	// ��ȡAlarmManagerʵ��
	public static AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	// ���Ͷ�ʱ�㲥��ִ�й㲥�еĶ�ʱ����
	// ������
	// context:������
	// requestCode:�����룬�������ֲ�ͬ������
	// type:alarm��������
	// triggerAtTime:��ʱ��������ʱ�䣬����Ϊ��λ
	// cls:�㲥��������class
	public static void sendAlarmBroadcast(Context context, int requestCode,
			int type, long triggerAtTime, Class cls) {
		AlarmManager mgr = getAlarmManager(context);

		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent, 0);

		mgr.set(type, triggerAtTime, pi);
	}

	// �����ճ����Ѷ�ʱ�㲥�������أ���schedule�����ģ����ڴ����ճ�����
	// ������
	// context:������
	// requestCode:�����룬�������ֲ�ͬ������
	// type:alarm��������
	// triggerAtTime:��ʱ��������ʱ�䣬����Ϊ��λ
	// data:��Ҫ���ݸ��㲥������������
	// cls:�㲥��������class
	public static void sendScheduleAlarmBroadcast(Context context,
			int requestCode, int type, long triggerAtTime, Schedule schedule,
			Class cls) {
		AlarmManager mgr = getAlarmManager(context);

		Intent intent = new Intent(context, cls);
		intent.putExtra("schedule", schedule);

		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);

		mgr.set(type, triggerAtTime, pi);
	}

	// ȡ��ָ��requestCode�Ķ�ʱ����
	// ������
	// context:������
	// requestCode:�����룬�������ֲ�ͬ������
	// cls:�㲥��������class
	public static void cancelAlarmBroadcast(Context context, int requestCode,
			Class cls) {
		AlarmManager mgr = getAlarmManager(context);

		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent, 0);

		mgr.cancel(pi);
	}

	// �ر����о�����ͬ����ʱ���Ҵ����˶�ʱ������ճ�
	public static void cancelSameAlarmBroadcast(Context context,
			String sameDateTime, Class cls) {
		EasyDoDB db = EasyDoDB.getInstance(context);
		List<Integer> idList = db.getSameCreateTimeScheduleId(sameDateTime);
		for (int i = 0; i < idList.size(); i++) {
			int id = idList.get(i);
			cancelAlarmBroadcast(context, id, ScheduleAlarmReceiver.class);

			db.updateDB(Schedule.TABLE_NAME, "repeat_mode",
					Schedule.REPEAT_MODE_OFF + "", "id", id + "");
		}

	}

	// ������ִ�ж�ʱ����
	// ������
	// context:������
	// requestCode:�����룬�������ֲ�ͬ������
	// type:alarm��������
	// startTime:��ʼ��ʱ�䣬����Ϊ��λ
	// cycleTime:��ʱ������ظ����ڣ�����Ϊ��λ
	// cls:�㲥��������class
	public static void sendRepeatAlarmBroadcast(Context context,
			int requestCode, int type, long startTime, long cycleTime, Class cls) {
		AlarmManager mgr = getAlarmManager(context);

		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent, 0);

		mgr.setRepeating(type, startTime, cycleTime, pi);
	}
}
