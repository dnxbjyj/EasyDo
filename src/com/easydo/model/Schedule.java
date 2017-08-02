package com.easydo.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;

import com.easydo.constant.AlarmAndRepeatFlag;
import com.easydo.constant.GlobalConfig;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.SbcDbcUtil;
import com.jiayongji.easydo.R;

/**
 * ������Schedule
 * 
 * ���ܣ����ݿ�Schedule���ʵ����
 */

public class Schedule implements Comparable<Schedule>, Parcelable {
	// ʵ��Comparable�ӿ���Ϊ���ܹ�����
	// ʵ��Parcelable�ӿ���Ϊ���ܹ��ڻ֮����д���

	// ��Ӧ�����ݿ�����ַ���
	public static final String TABLE_NAME = "Schedule";

	// �ճ����ѷ�ʽֵ��̬����
	public static final int ALARM_MODE_OFF = -1; // ������
	public static final int ALARM_MODE_ON_TIME = 0; // ׼ʱ����
	public static final int ALARM_MODE_PRE_5_MIN = 1; // ��ǰ5��������
	public static final int ALARM_MODE_PRE_10_MIN = 2; // ��ǰ10��������
	public static final int ALARM_MODE_PRE_30_MIN = 3; // ��ǰ30��������
	public static final int ALARM_MODE_PRE_1_HOUR = 4; // ��ǰ1Сʱ����
	public static final String[] ALARM_MODE_STRING_ARRAY = new String[] {
			"������", "׼ʱ����", "��ǰ5��������", "��ǰ10��������", "��ǰ30��������", "��ǰ1Сʱ����" };

	// �ճ��ظ�ģʽ��̬����
	public static final int REPEAT_MODE_OFF = -1; // ���ظ�
	public static final int REPEAT_MODE_PER_DAY = 0; // ÿ���ظ�һ��
	public static final int REPEAT_MODE_PER_WEEK = 1; // ÿ���ظ�һ��
	public static final int REPEAT_MODE_PER_WORKDAY = 2; // ������(��һ������)
	public static final int REPEAT_MODE_PER_MONTH = 3; // ÿ���ظ�һ��
	public static final int REPEAT_MODE_PER_YEAR = 4; // ÿ���ظ�һ��
	public static final String[] REPEAT_MODE_STRING_ARRAY = new String[] {
			"���ظ�", "ÿ���ظ�һ��", "ÿ���ظ�һ��", "������(��һ������)", "ÿ���ظ�һ��", "ÿ���ظ�һ��" };

	/**
	 * ���ݿ�ʼ����startDate���ظ�ģʽrepeatMode�Լ�������ظ�������cycleNum��
	 * �����startDate����cycleNum�����ں������
	 */
	public static String getNCycleNumLaterDateString(String startDate,
			int repeatMode, int cycleNum) {
		String result = null;

		if (repeatMode == REPEAT_MODE_OFF) {
			return null;
		}

		switch (repeatMode) {
		case REPEAT_MODE_PER_DAY:
			result = DateTimeUtil.getNLaterDateString(startDate,
					Calendar.DAY_OF_MONTH, cycleNum);
			break;
		case REPEAT_MODE_PER_WEEK:
			result = DateTimeUtil.getNLaterDateString(startDate,
					Calendar.WEEK_OF_MONTH, cycleNum);
			break;
		case REPEAT_MODE_PER_WORKDAY:
			result = DateTimeUtil.getNDayLaterWorkDayDateString(startDate
					+ " 23:59:59", cycleNum);
			break;
		case REPEAT_MODE_PER_MONTH:
			result = DateTimeUtil.getNLaterDateString(startDate,
					Calendar.MONTH, cycleNum);
			break;
		case REPEAT_MODE_PER_YEAR:
			result = DateTimeUtil.getNLaterDateString(startDate, Calendar.YEAR,
					cycleNum);
			break;
		default:
			break;
		}

		return result;
	}

	// �ճ�״̬��̬����
	public static final int STATUS_DELETED = -1; // ��ɾ��
	public static final int STATUS_UNFINISHED = 0; // �����
	public static final int STATUS_FINISHED = 1; // �����
	public static final String[] STATUS_STRING_ARRAY = new String[] { "��ɾ��",
			"�����", "�����" };

	// �ճ̵ı�ǩ
	public static final int TAG_MATTERS = 0; // �ճ�����
	public static final int TAG_INTERESTED = 1; // ���ּƻ��������ڴ����ճ�
	public static final int TAG_WORK = 2; // ������ѧϰ����
	public static final int TAG_IMPORTANT = 3; // ��������Ҫ����

	// ��ͬ�ճ̱�ǩ������
	public static final String TAG_MATTERS_TEXT = "�ճ�����";
	public static final String TAG_INTERESTED_TEXT = "˽��";
	public static final String TAG_WORK_TEXT = "����";
	public static final String TAG_IMPORTANT_TEXT = "��������Ҫ����";

	public static final int TAG_MATTERS_COLOR = 0xFF555555;
	public static final int TAG_INTERESTED_COLOR = 0xFF32CD32;
	public static final int TAG_WORK_COLOR = 0xFF33CCFF;
	public static final int TAG_IMPORTANT_COLOR = 0xFFFF8888;
	// ��ͬ��ǩ��Ӧ����ɫֵ
	public static final int[] TAG_COLOR_ARR = { TAG_MATTERS_COLOR,
			TAG_INTERESTED_COLOR, TAG_WORK_COLOR, TAG_IMPORTANT_COLOR };

	// ��ͬ��ǩ��Ӧ���ճ�ʱ������ʾ��ʽ��δ���״̬�ճ̵Ľ��ԲȦͼƬ����Դid
	public static final int[] TAG_NODE_IMAGE_NOT_DONE_ID = {
			R.drawable.node_image_schedule_tag_matters_not_done,
			R.drawable.node_image_schedule_tag_interested_not_done,
			R.drawable.node_image_schedule_tag_work_not_done,
			R.drawable.node_image_schedule_tag_important_not_done };

	// ��ͬ��ǩ��Ӧ���ճ�ʱ������ʾ��ʽ�������״̬�ճ̵Ľ��ԲȦͼƬ����Դid
	public static final int[] TAG_NODE_IMAGE_DONE_ID = {
			R.drawable.node_image_schedule_tag_matters_done,
			R.drawable.node_image_schedule_tag_interested_done,
			R.drawable.node_image_schedule_tag_work_done,
			R.drawable.node_image_schedule_tag_important_done };

	public static String[] getTagTextArray() {
		String[] arr = new String[4];
		arr[0] = TAG_MATTERS_TEXT;
		arr[1] = TAG_INTERESTED_TEXT;
		arr[2] = TAG_WORK_TEXT;
		arr[3] = TAG_IMPORTANT_TEXT;
		return arr;
	}

	private int id;
	private int scheduleBookId;
	private String createTime;
	private String content;
	private String startTime;
	private String finishTime;
	private int alarmMode;
	private String alarmTime;
	private String location;
	private int repeatMode;
	private String remark;
	private int status;

	// added by jyj @2016-5-10pm
	private int tag;

	// added by jyj @2016-5-14pm
	private String repeatCutOffDate;

	public Schedule() {
		alarmMode = ALARM_MODE_OFF;
		repeatMode = REPEAT_MODE_OFF;
		status = STATUS_UNFINISHED;
		tag = TAG_MATTERS;
	}

	// ����һ�����е�Schedule������µ��ճ̿�ʼʱ��newStartTime�������ճ̵Ĺ��췽��
	public Schedule(Schedule oldSchedule, String newStartTime) {
		id = oldSchedule.getId();
		scheduleBookId = oldSchedule.getScheduleBookId();
		createTime = oldSchedule.getCreateTime();
		content = oldSchedule.getContent();

		// �µĿ�ʼʱ��
		startTime = newStartTime;

		alarmMode = oldSchedule.getAlarmMode();

		// �����µĿ�ʼʱ������ѷ�ʽ��������µ�����ʱ��
		alarmTime = calAlarmTime(alarmMode, newStartTime);

		location = oldSchedule.getLocation();
		repeatMode = oldSchedule.getRepeatMode();
		remark = oldSchedule.getRemark();

		// status��ʼ��Ϊ�����
		status = STATUS_UNFINISHED;

		tag = oldSchedule.getTag();
		repeatCutOffDate = oldSchedule.getRepeatCutOffDate();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getScheduleBookId() {
		return scheduleBookId;
	}

	public void setScheduleBookId(int scheduleBookId) {
		this.scheduleBookId = scheduleBookId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public int getAlarmMode() {
		return alarmMode;
	}

	public void setAlarmMode(int alarmMode) {
		this.alarmMode = alarmMode;
	}

	public String getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(String alarmTime) {
		this.alarmTime = alarmTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getRepeatMode() {
		return repeatMode;
	}

	public void setRepeatMode(int repeatMode) {
		this.repeatMode = repeatMode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getRepeatCutOffDate() {
		return repeatCutOffDate;
	}

	public void setRepeatCutOffDate(String repeatCutOffDate) {
		this.repeatCutOffDate = repeatCutOffDate;
	}

	// ����startTime��alarmMode�����㲢����alarmTime
	public static String calAlarmTime(int alarmMode, String startTime) {
		String alarmTime = "";
		Calendar startCalendar = DateTimeUtil
				.StringToGregorianCalendar(startTime);

		switch (alarmMode) {
		case ALARM_MODE_OFF:
			alarmTime = "";
			break;
		case ALARM_MODE_ON_TIME:
			alarmTime = startTime;
			break;
		case ALARM_MODE_PRE_5_MIN:
			startCalendar.add(Calendar.MINUTE, -5);
			alarmTime = DateTimeUtil.CalendarToString(startCalendar);
			break;
		case ALARM_MODE_PRE_10_MIN:
			startCalendar.add(Calendar.MINUTE, -10);
			alarmTime = DateTimeUtil.CalendarToString(startCalendar);
			break;
		case ALARM_MODE_PRE_30_MIN:
			startCalendar.add(Calendar.MINUTE, -30);
			alarmTime = DateTimeUtil.CalendarToString(startCalendar);
			break;
		case ALARM_MODE_PRE_1_HOUR:
			startCalendar.add(Calendar.MINUTE, 60);
			alarmTime = DateTimeUtil.CalendarToString(startCalendar);
			break;
		default:
			break;
		}

		return alarmTime;
	}

	// ���ճ̿�ʼʱ��Ϊ���ݱȽϴ�С���Ӵ�С����
	@Override
	public int compareTo(Schedule another) {
		return DateTimeUtil.compareDateTimeString(startTime,
				another.getStartTime());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(scheduleBookId);
		dest.writeString(createTime);
		dest.writeString(content);
		dest.writeString(startTime);
		dest.writeString(finishTime);
		dest.writeInt(alarmMode);
		dest.writeString(alarmTime);
		dest.writeString(location);
		dest.writeInt(repeatMode);
		dest.writeString(remark);
		dest.writeInt(status);
		dest.writeInt(tag);
		dest.writeString(repeatCutOffDate);
	}

	public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
		public Schedule createFromParcel(Parcel source) {
			Schedule schedule = new Schedule();

			// ˳��һ��Ҫ��writeToParcel������д���˳����ȫ��ͬ��
			schedule.id = source.readInt();
			schedule.scheduleBookId = source.readInt();
			schedule.createTime = source.readString();
			schedule.content = source.readString();
			schedule.startTime = source.readString();
			schedule.finishTime = source.readString();
			schedule.alarmMode = source.readInt();
			schedule.alarmTime = source.readString();
			schedule.location = source.readString();
			schedule.repeatMode = source.readInt();
			schedule.remark = source.readString();
			schedule.status = source.readInt();
			schedule.tag = source.readInt();
			schedule.repeatCutOffDate = source.readString();
			return schedule;
		};

		public Schedule[] newArray(int size) {
			return new Schedule[size];
		};
	};

	// ��ȡ�ճ̵�����-�ظ�ģʽ
	public int getAlarmAndRepeatMode() {
		int flag;
		if (alarmMode == ALARM_MODE_OFF && repeatMode == REPEAT_MODE_OFF) {
			flag = AlarmAndRepeatFlag.NO_ALARM_AND_REPEAT;
		} else if (alarmMode != ALARM_MODE_OFF && repeatMode == REPEAT_MODE_OFF) {
			flag = AlarmAndRepeatFlag.ALARM_ONLY;
		} else if (alarmMode == ALARM_MODE_OFF && repeatMode != REPEAT_MODE_OFF) {
			flag = AlarmAndRepeatFlag.REPEAT_ONLY;
		} else {
			flag = AlarmAndRepeatFlag.ALARM_AND_REPEAT;
		}

		return flag;
	}

	/**
	 * ���ʱ���(startDateTime,endDateTime]�����߶�Ϊ��׼����ʱ���ַ�����
	 * ��repeatModeΪ�ظ�ģʽ������ʱ�����ڿ�ʼʱ�������ʱ���ַ����б�
	 */

	public static List<String> getAllCycleDateTime(String startDateTime,
			String endDateTime, int repeatMode) {
		List<String> result = new ArrayList<String>();

		if (repeatMode == REPEAT_MODE_OFF) {
			result.add(startDateTime);
			return result;
		}

		long startMillis = DateTimeUtil.stringToMillis(startDateTime);
		long endMillis = DateTimeUtil.stringToMillis(endDateTime);

		for (long t = getNextCycleMillis(startMillis, repeatMode); t <= endMillis; t = getNextCycleMillis(
				t, repeatMode)) {
			result.add(DateTimeUtil.getDateTimeStringFromMillis(t));
		}

		return result;
	}

	/**
	 * ���ݿ�ʼʱ����ظ�ģʽ����ȡ��һ���ظ����ڵĿ�ʼʱ��millisֵ
	 * 
	 */
	public static long getNextCycleMillis(long startTime, int repeatMode) {
		if (repeatMode == REPEAT_MODE_OFF) {
			return startTime;
		}

		Calendar c = DateTimeUtil.getCalendarFromMillis(startTime);

		switch (repeatMode) {
		case REPEAT_MODE_PER_DAY:
			c.add(Calendar.DAY_OF_MONTH, 1);
			break;
		case REPEAT_MODE_PER_WORKDAY:
			c = DateTimeUtil.getNextWorkDay(c);
			break;
		case REPEAT_MODE_PER_WEEK:
			c.add(Calendar.WEEK_OF_MONTH, 1);
			break;
		case REPEAT_MODE_PER_MONTH:
			c.add(Calendar.MONTH, 1);
			break;
		case REPEAT_MODE_PER_YEAR:
			c.add(Calendar.YEAR, 1);
			break;
		default:
			break;
		}

		return c.getTimeInMillis();
	}
}
