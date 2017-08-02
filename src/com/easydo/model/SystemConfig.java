package com.easydo.model;

import android.content.Context;

import com.easydo.db.EasyDoDB;
import com.easydo.util.ApplicationUtil;

/**
 * ������SystemConfig
 * 
 * ���ܣ����ݿ�SystemConfig���ʵ����
 */

public class SystemConfig {
	// ��Ӧ�����ݿ�����ַ���
	public static final String TABLE_NAME = "SystemConfig";

	// �ճ���ʾ��ʽ����
	public static final int SCHEDULE_SHOW_WAY_MONTH_VIEW = 0; // ����ͼ��ʾ��ʽ
	public static final int SCHEDULE_SHOW_WAY_DAY_VIEW = 1; // ����ͼ��ʽ
	public static final int SCHEDULE_SHOW_WAY_WEEK_VIEW = 2; // ����ͼ��ʽ

	// ��˽������ʽ����
	public static final int PRIVACY_WAY_OFF = 0; // ����˽����
	public static final int PRIVACY_WAY_PASSWORD = 1; // ���뱣����ʽ
	public static final int PRIVACY_WAY_GESTURE = 2; // �������뱣��

	// �ճ����ѷ�ʽ����
	public static final int SCHEDULE_ALARM_WAY_OFF = -1; // ���������𶯡�������
	public static final int SCHEDULE_ALARM_WAY_VIBRATE_BELL = 0; // ����������
	public static final int SCHEDULE_ALARM_WAY_VIBRATE = 1; // ֻ������
	public static final int SCHEDULE_ALARM_WAY_BELL = 2; // ֻ��������

	// ʱ������ʾģʽ���Ƿ���ʾ�ճ̵�Сʱ�ͷ��ӵĳ���
	public static final int TIME_LINE_SHOW_DETAIL_TIME = 1; // ��ʾ
	public static final int TIME_LINE_NOT_SHOW_DETAIL_TIME = 0; // ����ʾ

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
	// 0������ʾ��ɫ; 1:��ʾ��ɫ
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
