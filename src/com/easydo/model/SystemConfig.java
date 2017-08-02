package com.easydo.model;

import android.content.Context;

import com.easydo.db.EasyDoDB;
import com.easydo.util.ApplicationUtil;

/**
 * 类名：SystemConfig
 * 
 * 功能：数据库SystemConfig表的实体类
 */

public class SystemConfig {
	// 对应的数据库表名字符串
	public static final String TABLE_NAME = "SystemConfig";

	// 日程显示方式常量
	public static final int SCHEDULE_SHOW_WAY_MONTH_VIEW = 0; // 月视图显示方式
	public static final int SCHEDULE_SHOW_WAY_DAY_VIEW = 1; // 日视图方式
	public static final int SCHEDULE_SHOW_WAY_WEEK_VIEW = 2; // 周视图方式

	// 隐私保护方式常量
	public static final int PRIVACY_WAY_OFF = 0; // 无隐私保护
	public static final int PRIVACY_WAY_PASSWORD = 1; // 密码保护方式
	public static final int PRIVACY_WAY_GESTURE = 2; // 手势密码保护

	// 日程提醒方式常量
	public static final int SCHEDULE_ALARM_WAY_OFF = -1; // 静音、不震动、不提醒
	public static final int SCHEDULE_ALARM_WAY_VIBRATE_BELL = 0; // 振动响铃提醒
	public static final int SCHEDULE_ALARM_WAY_VIBRATE = 1; // 只振动提醒
	public static final int SCHEDULE_ALARM_WAY_BELL = 2; // 只响铃提醒

	// 时间轴显示模式下是否显示日程的小时和分钟的常量
	public static final int TIME_LINE_SHOW_DETAIL_TIME = 1; // 显示
	public static final int TIME_LINE_NOT_SHOW_DETAIL_TIME = 0; // 不显示

	private int id;
	private String version;
	private int scheduleShowWay;
	private int privacyWay;
	private String password;
	private int scheduleAlarmWay;
	private int activatedScheduleBookId;

	// added by jyj @2016-5-9 pm
	private int timeLineShowDetailTime;

	// added by jyj @2016-5-21 am
	private String scheduleContentTypeface;
	private String JournalContentTypeface;

	// added by jyj @2016-5-21 pm
	// 0：不显示颜色; 1:显示颜色
	private int showScheduleColorful;
	public static final int SHOW_SCHEDULE_NOT_COLORFUL = 0;
	public static final int SHOW_SCHEDULE_COLORFUL = 1;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getScheduleShowWay() {
		return scheduleShowWay;
	}

	public void setScheduleShowWay(int scheduleShowWay) {
		this.scheduleShowWay = scheduleShowWay;
	}

	public int getPrivacyWay() {
		return privacyWay;
	}

	public void setPrivacyWay(int privacyWay) {
		this.privacyWay = privacyWay;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getScheduleAlarmWay() {
		return scheduleAlarmWay;
	}

	public void setScheduleAlarmWay(int scheduleAlarmWay) {
		this.scheduleAlarmWay = scheduleAlarmWay;
	}

	public int getActivatedScheduleBookId() {
		return activatedScheduleBookId;
	}

	public void setActivatedScheduleBookId(int activatedScheduleBookId) {
		this.activatedScheduleBookId = activatedScheduleBookId;
	}

	public int getTimeLineShowDetailTime() {
		return timeLineShowDetailTime;
	}

	public void setTimeLineShowDetailTime(int timeLineShowDetailTime) {
		this.timeLineShowDetailTime = timeLineShowDetailTime;
	}

	public String getScheduleContentTypeface() {
		return scheduleContentTypeface;
	}

	public void setScheduleContentTypeface(String scheduleContentTypeface) {
		this.scheduleContentTypeface = scheduleContentTypeface;
	}

	public String getJournalContentTypeface() {
		return JournalContentTypeface;
	}

	public void setJournalContentTypeface(String journalContentTypeface) {
		JournalContentTypeface = journalContentTypeface;
	}

	public int getShowScheduleColorful() {
		return showScheduleColorful;
	}

	public void setShowScheduleColorful(int showScheduleColorful) {
		this.showScheduleColorful = showScheduleColorful;
	}

}
