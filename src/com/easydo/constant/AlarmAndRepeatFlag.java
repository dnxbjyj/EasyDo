package com.easydo.constant;

/**
 * 日程的提醒和重复的FLAG
 *
 */

public class AlarmAndRepeatFlag {
	public static final int NO_ALARM_AND_REPEAT = -1; // 既不提醒又不重复
	public static final int ALARM_AND_REPEAT = 0; // 既提醒又重复
	public static final int ALARM_ONLY = 1; // 只提醒，不重复
	public static final int REPEAT_ONLY = 2; // 只重复，不提醒
}
