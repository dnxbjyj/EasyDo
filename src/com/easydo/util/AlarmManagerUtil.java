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
 * AlarmManager工具类
 *
 */

public class AlarmManagerUtil {
	// 获取AlarmManager实例
	public static AlarmManager getAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

	// 发送定时广播（执行广播中的定时任务）
	// 参数：
	// context:上下文
	// requestCode:请求码，用于区分不同的任务
	// type:alarm启动类型
	// triggerAtTime:定时任务开启的时间，毫秒为单位
	// cls:广播接收器的class
	public static void sendAlarmBroadcast(Context context, int requestCode,
			int type, long triggerAtTime, Class cls) {
		AlarmManager mgr = getAlarmManager(context);

		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent, 0);

		mgr.set(type, triggerAtTime, pi);
	}

	// 发送日程提醒定时广播方法重载：带schedule参数的，用于传递日程数据
	// 参数：
	// context:上下文
	// requestCode:请求码，用于区分不同的任务
	// type:alarm启动类型
	// triggerAtTime:定时任务开启的时间，毫秒为单位
	// data:需要传递给广播接收器的数据
	// cls:广播接收器的class
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

	// 取消指定requestCode的定时任务
	// 参数：
	// context:上下文
	// requestCode:请求码，用于区分不同的任务
	// cls:广播接收器的class
	public static void cancelAlarmBroadcast(Context context, int requestCode,
			Class cls) {
		AlarmManager mgr = getAlarmManager(context);

		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent, 0);

		mgr.cancel(pi);
	}

	// 关闭所有具有相同创建时间且创建了定时任务的日程
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

	// 周期性执行定时任务
	// 参数：
	// context:上下文
	// requestCode:请求码，用于区分不同的任务
	// type:alarm启动类型
	// startTime:开始的时间，毫秒为单位
	// cycleTime:定时任务的重复周期，毫秒为单位
	// cls:广播接收器的class
	public static void sendRepeatAlarmBroadcast(Context context,
			int requestCode, int type, long startTime, long cycleTime, Class cls) {
		AlarmManager mgr = getAlarmManager(context);

		Intent intent = new Intent(context, cls);
		PendingIntent pi = PendingIntent.getBroadcast(context, requestCode,
				intent, 0);

		mgr.setRepeating(type, startTime, cycleTime, pi);
	}
}
