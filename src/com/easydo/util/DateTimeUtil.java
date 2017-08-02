package com.easydo.util;

/**
 * ����ʱ����ز�����װ
 */

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtil {
	/**
	 * ��ȡ��һ���յ�Calendar
	 * ���磺������2016-5-24��birthdayDateΪ2010-06-24����ô���ص�Calendar������Ϊ��2016-06-24
	 */
	public static String getNextBirthdayDate(String birthdayDate) {
		Calendar c = StringToGregorianCalendar(birthdayDate + " 00:00:00");
		Calendar curCalendar = getCurrentCalendar();

		while (curCalendar.compareTo(c) >= 0) {
			c.add(Calendar.YEAR, 1);
		}

		return CalendarToString(c).substring(0, 10);
	}

	/**
	 * ��ȡ��������֮���������ֵ����Ϊ����
	 * 
	 * @return date1 - date2 ������
	 */
	public static long getDaysNumDiff(String date1, String date2) {
		String t1 = date1 + " 00:00:00";
		String t2 = date2 + " 00:00:00";

		Calendar c1 = StringToGregorianCalendar(t1);
		Calendar c2 = StringToGregorianCalendar(t2);

		long diff = c1.getTimeInMillis() - c2.getTimeInMillis();
		long aDayMillis = 24 * 60 * 60 * 1000;

		return (long) (diff / aDayMillis);
	}

	/**
	 * �Ӻ���ֵ��ȡCalendar����
	 */
	public static Calendar getCalendarFromMillis(long millis) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(millis);
		return c;
	}

	/**
	 * ������ʱ���ַ�����ȡ��ʱ:�֡�ʱ���ַ���
	 * 
	 * @param dateTime����ʱ���ַ���
	 * @return "2016-05-14 19:00:00" ����:"19:00"
	 */
	public static String getTimeString(String dateTime) {
		return dateTime.substring(11, 16);
	}

	// ��ȡ19λ����ʱ���ַ�������ʽ��"2016-05-12 23_10_59"������2016-05-12 23:10:59
	public static String get19BitDateTimeString(String dateTimeString) {
		String date = dateTimeString.split(" ")[0];
		String time = dateTimeString.split(" ")[1];
		String[] dateArr = date.split("-");
		String[] timeArr = time.split(":");

		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < dateArr.length - 1; i++) {
			result.append(dateArr[i] + "-");
		}
		result.append(dateArr[2] + "-");

		for (int i = 0; i < timeArr.length - 1; i++) {
			result.append(timeArr[i] + "_");
		}
		result.append(timeArr[2]);

		return result.toString();

	}

	// ��ȡ14λ����ʱ���ַ�������ʽ��"20160512231059"������2016-05-12 23:10:59
	public static String get14BitDateTimeString(String dateTimeString) {
		String date = dateTimeString.split(" ")[0];
		String time = dateTimeString.split(" ")[1];
		String[] dateArr = date.split("-");
		String[] timeArr = time.split(":");

		StringBuilder result = new StringBuilder("");
		for (int i = 0; i < dateArr.length; i++) {
			result.append(dateArr[i]);
		}

		for (int i = 0; i < timeArr.length; i++) {
			result.append(timeArr[i]);
		}

		return result.toString();

	}

	// �Ƚ�����Calendar�Ĵ�С
	public static int compareCalendar(Calendar c1, Calendar c2) {
		return compareDateTimeString(CalendarToString(c1), CalendarToString(c2));
	}

	// ��ȡ��ǰʱ��ĺ���ֵ
	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	// �ж�����Calendar�Ƿ���ͬһ��
	public static boolean isAtSameWeek(Calendar c1, Calendar c2,
			int firstDayOfWeek) {
		Calendar firstDayOfWeek1 = getDateBeginTimeCalendar(getFirstDayCalendarOfWeek(
				c1, firstDayOfWeek));
		Calendar firstDayOfWeek2 = getDateBeginTimeCalendar(getFirstDayCalendarOfWeek(
				c2, firstDayOfWeek));

		return (firstDayOfWeek1.equals(firstDayOfWeek2));
	}

	// ��ȡһ��Calendar����23:59:59ʱ���Calendar
	public static Calendar getDateEndTimeCalendar(Calendar c) {
		String dateString = CalendarToString(c).substring(0, 10);
		Calendar newCalendar = StringToGregorianCalendar(dateString + " "
				+ "23:59:59");
		return newCalendar;
	}

	// ��ȡһ��Calendar����00:00:00ʱ���Calendar
	public static Calendar getDateBeginTimeCalendar(Calendar c) {
		String dateString = CalendarToString(c).substring(0, 10);
		Calendar newCalendar = StringToGregorianCalendar(dateString + " "
				+ "00:00:00");
		return newCalendar;
	}

	// ��ȡcount�ܺ�������������ڵ�����ʱ����ַ���(count��Ϊ������������֮ǰ������)
	public static String getWeekStringAway(Calendar curCalendar, int count,
			int firstDayOfWeek) {
		Calendar tmpCalendar = getCopyCalendar(curCalendar);
		int dayDiffNum = count * 7;
		tmpCalendar.add(Calendar.DAY_OF_MONTH, dayDiffNum);
		return getWeekString(tmpCalendar, firstDayOfWeek);
	}

	// ��ȡ��ǰ�����������ڵ�����ʱ����ַ�����firstDayOfWeekΪһ�ܵĵ�һ�죬��ȡֵ��ΪCalendar.SUNDAY��Calendar.MONDAY�ȣ�����ʽ��"2016-05-02 ~ 08"
	// ע��Calendar��DAY_OF_WEEK�ֶε�ȡֵΪ1~7������1���������գ�2��������һ���Դ����ơ�
	public static String getWeekString(Calendar curCalendar, int firstDayOfWeek) {
		Calendar firstDayCalendar = getFirstDayCalendarOfWeek(curCalendar,
				firstDayOfWeek);
		Calendar lastDayCalendar = getLastDayCalendarOfWeek(curCalendar,
				firstDayOfWeek);

		StringBuilder result = new StringBuilder("");

		result.append(CalendarToString(firstDayCalendar).substring(0, 10))
				.append(" ~ ")
				.append(getDoubleNumString(lastDayCalendar
						.get(Calendar.DAY_OF_MONTH)));

		return result.toString();
	}

	// ������һ����һ�ܵĵ�һ�죬��ȡ��ǰ�ܵĵ�һ���Calendar����
	public static Calendar getFirstDayCalendarOfWeek(Calendar curCalendar,
			int firstDayOfWeek) {
		Calendar tmpCalendar = getCopyCalendar(curCalendar);
		Calendar c;

		int minus;

		// �����ǰʱ�������ֵ�ȸ����ĵ�һ�������ֵС����Ҫ�ȼ�7�ټ�ȥfirstDayOfWeek�������㷨ԭ����ͼ�Ϳ��Կ�������
		if (tmpCalendar.get(Calendar.DAY_OF_WEEK) < firstDayOfWeek) {
			minus = tmpCalendar.get(Calendar.DAY_OF_WEEK) + 7 - firstDayOfWeek;
		}
		// ����ֱ�Ӽ�ȥfirstDayOfWeek����
		else {
			minus = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
		}

		tmpCalendar.add(Calendar.DAY_OF_MONTH, -minus);
		c = tmpCalendar;

		return c;
	}

	// ������һ����һ�ܵĵ�һ�죬��ȡ��ǰ�ܵ����һ���Calendar����
	public static Calendar getLastDayCalendarOfWeek(Calendar curCalendar,
			int firstDayOfWeek) {
		Calendar tmpCalendar = getCopyCalendar(curCalendar);
		Calendar c;

		c = getFirstDayCalendarOfWeek(tmpCalendar, firstDayOfWeek);
		c.add(Calendar.DAY_OF_MONTH, 6);

		return c;
	}

	// ��ȡһ��Calendar�Ŀ���
	public static Calendar getCopyCalendar(Calendar c) {
		return StringToGregorianCalendar(CalendarToString(c));
	}

	// ��ȡ��ǰʱ���Calendar
	public static Calendar getCurrentCalendar() {
		Date d = new Date();
		return StringToGregorianCalendar(getCurrentDateTimeString());
	}

	// ��ȡ��ǰ����ֵ�ַ�������ʽ��"01"
	public static String getCurrentSecondString() {
		return getCurrentTimeLongString().substring(6, 8);
	}

	// ��ȡ����dateTime n��������֮��������ַ���
	public static String getNDayLaterWorkDayDateString(String dateTime, int n) {
		Calendar c = StringToGregorianCalendar(dateTime);
		for (int i = 0; i < n; i++) {
			c = getNextWorkDay(c);
		}
		return CalendarToString(c).substring(0, 10);
	}

	// ��ȡ��һ�������գ���һ~����Ϊ�����գ���Calendar����
	public static Calendar getNextWorkDay(Calendar c) {
		Calendar nextWorkDay = c;
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		case Calendar.SUNDAY:
		case Calendar.MONDAY:
		case Calendar.TUESDAY:
		case Calendar.WEDNESDAY:
		case Calendar.THURSDAY:
			nextWorkDay.add(Calendar.DATE, 1);
			break;
		case Calendar.FRIDAY:
			nextWorkDay.add(Calendar.DATE, 3);
			break;
		case Calendar.SATURDAY:
			nextWorkDay.add(Calendar.DATE, 2);
			break;
		default:
			break;
		}
		return nextWorkDay;
	}

	// ��ȡ���������ַ���n[]֮���ʱ��������ַ�����n�ĵ�λΪCalendar����Щ��ʾʱ��ĳ�����
	public static String getNLaterDateString(String date, int nType, int n) {
		Calendar c = StringToGregorianCalendar(date + " 23:59:59");
		c.add(nType, n);
		return CalendarToString(c).substring(0, 10);
	}

	// ��ȡ��ǰʱ��n[]֮���ʱ��������ַ�����n�ĵ�λΪCalendar����Щ��ʾʱ��ĳ�����
	public static String getNLaterDateString(int nType, int n) {
		Date date = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(nType, n);

		return CalendarToString(c).substring(0, 10);
	}

	// ��ȡ��ǰʱ��n[]֮���ʱ�������ʱ���ַ�����N�ĵ�λΪCalendar����Щ��ʾʱ��ĳ�����
	public static String getNLaterDateTimeString(int nType, int n) {
		Date date = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(nType, n);

		return CalendarToString(c);
	}

	// ʱ�����ֵת��������ʱ���ַ���
	public static String getDateTimeStringFromMillis(long millis) {
		Date date = new Date(millis);
		return (DateToString(date));
	}

	// ������ʱ���ַ�����ʱ��ת���ɺ���ֵ��RTC��
	public static long stringToMillis(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);

		return c.getTimeInMillis();
	}

	// ��ȡ��������ʱ���ַ�����ʾ��ʱ��֮��ĺ����ֵ
	public static long getDifMillis(String dateTime1, String dateTime2) {
		return (stringToMillis(dateTime1) - stringToMillis(dateTime2));
	}

	// ����һ����ʾ���ڻ�ʱ��������������"01"��"23"������ʽ���ַ���
	public static String getDoubleNumString(int n) {
		int num = n % 60;

		if (num < 10) {
			return "0" + num;
		} else {
			return num + "";
		}
	}

	// ��ȡ��׼����ʱ���ַ��������͵�����ֵ���磺"2015-01-21 00:00:00"�����أ�21
	public static int getDayOfMonth(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return day;
	}

	// ��ȡ��ǰʱ�������ʱ���ַ�������ʽ��"yyyy-MM-dd HH:mm:ss"
	public static String getCurrentDateTimeString() {
		Date date = new Date();
		return DateToString(date);
	}

	// ��ȡ��ǰ��"yyyy-MM-dd"���ڸ�ʽ�ַ���
	public static String getCurrentDateString() {
		Date date = new Date();
		return DateToString(date).substring(0, 10);
	}

	// ��ȡ��ǰ��"yyyy-MM"���ڸ�ʽ�ַ���
	public static String getCurrentMonthString() {
		Date date = new Date();
		return DateToString(date).substring(0, 7);
	}

	// ��ȡ��ǰ��"HH:mm"ʱ���ʽ�ַ���
	public static String getCurrentTimeString() {
		Date date = new Date();
		return DateToString(date).substring(11, 16);
	}

	// ��ȡ��ǰ��"HH:mm:ss"ʱ���ʽ�ַ���
	public static String getCurrentTimeLongString() {
		Date date = new Date();
		return DateToString(date).substring(11, 19);
	}

	// ��Calendar�������ɡ�11��1�� ����һ��������ʽ���ַ���
	public static String getShortDateTimeOfWeek(Calendar calendar) {
		return getShortDateTimeOfWeek(CalendarToString(calendar));
	}

	// ������ʱ���ַ������ɡ�11��1�� ����һ��������ʽ���ַ���
	public static String getShortDateTimeOfWeek(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);

		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		String[] weekStr = new String[] { "������", "����һ", "���ڶ�", "������", "������",
				"������", "������" };
		String week = weekStr[c.get(Calendar.DAY_OF_WEEK) - 1];

		String result = month + "��" + day + "��" + "  " + week;

		return result;
	}

	// ������ʱ���ַ������ɡ�2015��11��1�� ����һ��������ʽ���ַ���
	public static String getDateTimeOfWeek(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		String[] weekStr = new String[] { "������", "����һ", "���ڶ�", "������", "������",
				"������", "������" };
		String week = weekStr[c.get(Calendar.DAY_OF_WEEK) - 1];

		String result = year + "��" + month + "��" + day + "��" + "  " + week;

		return result;
	}

	// �������ַ������ɡ�2015��11��1�ա�������ʽ���ַ���
	public static String getChineseDateString(String date) {
		String result = "";

		String[] dateArr = date.split("-");
		int year = Integer.parseInt(dateArr[0]);
		int month = Integer.parseInt(dateArr[1]);
		int day = Integer.parseInt(dateArr[2]);

		result = year + "��" + month + "��" + day + "��";

		return result;
	}

	// ������ʱ���ַ������ɡ�2015��11��1�� 05:43��������ʽ���ַ���
	public static String getDateTimeOfHourMinute(String dateTime) {
		String result = "";
		String date = dateTime.split(" ")[0];
		String time = dateTime.split(" ")[1];
		String[] dateArr = date.split("-");
		String[] timeArr = time.split(":");

		int year = Integer.parseInt(dateArr[0]);
		int month = Integer.parseInt(dateArr[1]);
		int day = Integer.parseInt(dateArr[2]);

		result = year + "��" + month + "��" + day + "��" + "  " + timeArr[0] + ":"
				+ timeArr[1];

		return result;
	}

	// ������������Calendar��monthȡֵ��Χ��[0,11]
	public static Calendar getCalendar(int year, int month, int day) {
		return StringToGregorianCalendar(getDateTimeString(year, month, day));
	}

	// ����������������ʱ���ַ�����monthȡֵ��Χ��[0,11]
	public static String getDateTimeString(int year, int month, int day) {
		return getDateString(year, month, day) + " " + "00:00:00";
	}

	// �����������������ַ�����monthȡֵ��Χ��[0,11]
	public static String getDateString(int year, int month, int day) {
		String m;
		String d;

		if (month >= 9) {
			m = (month + 1) + "";
		} else {
			m = "0" + (month + 1);
		}

		if (day >= 10) {
			d = day + "";
		} else {
			d = "0" + day;
		}

		String dateString = year + "-" + m + "-" + d;
		return dateString;
	}

	// �������������������ַ�����monthȡֵ��Χ��[0,11]
	public static String getDateString(int year, int month) {
		String m;
		String d;

		if (month >= 9) {
			m = (month + 1) + "";
		} else {
			m = "0" + (month + 1);
		}

		String dateString = year + "-" + m;
		return dateString;
	}

	// ��ʱ��������ʱ���ַ���
	public static String getTimeString(int hour, int minute) {
		String h;
		String m;

		if (hour >= 10) {
			h = hour + "";
		} else {
			h = "0" + hour;
		}

		if (minute >= 10) {
			m = minute + "";
		} else {
			m = "0" + minute;
		}

		return h + ":" + m;
	}

	// ��ʱ���֡�������ʱ���ַ���
	public static String getTimeString(int hour, int minute, int second) {
		String h;
		String m;
		String c;

		if (hour >= 10) {
			h = hour + "";
		} else {
			h = "0" + hour;
		}

		if (minute >= 10) {
			m = minute + "";
		} else {
			m = "0" + minute;
		}

		if (second >= 10) {
			c = second + "";
		} else {
			c = "0" + second;
		}

		return h + ":" + m + ":" + c;
	}

	// ���ڲ��������ڶ�����ʱ���ַ���������������
	public class DateTimeString implements Comparable<DateTimeString> {
		private String mDateTimeStr;

		public DateTimeString(String dateTimeStr) {
			mDateTimeStr = dateTimeStr;
		}

		@Override
		public int compareTo(DateTimeString another) {
			return compareDateTimeString(mDateTimeStr.toString(),
					another.toString());
		}

		@Override
		public String toString() {
			return mDateTimeStr;
		}

	}

	// ������ʱ���ַ��������������,�������������飨������˳���Ǵ�С����
	public static String[] sortDateTimeStringArray(String[] dateTimeStringArray) {
		// ������ʱ���ַ�������ת����DateTimeString�������飬DateTimeStringʵ����Comparable�ӿڣ����Խ�������
		DateTimeString[] tmpArray = new DateTimeString[dateTimeStringArray.length];

		// ����tmpArray����
		int i = 0;
		DateTimeUtil tmpUtil = new DateTimeUtil();
		for (String str : dateTimeStringArray) {
			tmpArray[i++] = tmpUtil.new DateTimeString(str);
		}

		// ��tmpArray��������
		Arrays.sort(tmpArray);

		String[] result = new String[tmpArray.length];
		i = 0;
		for (DateTimeString str : tmpArray) {
			result[i++] = str.toString();
		}
		return result;
	}

	// �Ƚ����������ַ����Ĵ�С
	public static int compareDateString(String str1, String str2) {
		Date d1 = StringToDate(str1 + " 00:00:00");
		Date d2 = StringToDate(str2 + " 00:00:00");
		if (d1.getTime() - d2.getTime() < 0) {
			return -1;
		} else if (d1.getTime() - d2.getTime() > 0) {
			return 1;
		} else {
			return 0;
		}

	}

	// �Ƚ���������ʱ���ַ����Ĵ�С�����str1��str2�磬�򷵻�-1�������ȷ���0�����򷵻�1
	public static int compareDateTimeString(String str1, String str2) {
		Date d1 = StringToDate(str1);
		Date d2 = StringToDate(str2);
		if (d1.getTime() - d2.getTime() < 0) {
			return -1;
		} else if (d1.getTime() - d2.getTime() > 0) {
			return 1;
		} else {
			return 0;
		}

	}

	// ʱ�������ַ���ת����Date����
	// ע��dateTimeStr������ǰ��0���ǿ��Եģ�����"2011-01-01 01:02:03"��"2011-1-1 1:2:3"���ǺϷ���
	public static Date StringToDate(String dateTimeStr) {
		Date date = new Date();
		// DateFormat fmt = DateFormat.getDateTimeInstance();
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = fmt.parse(dateTimeStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// Date����ת��������ʱ���ַ���
	public static String DateToString(Date date) {
		String dateTimeStr = null;
		// DateFormat fmt = DateFormat.getDateTimeInstance();
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTimeStr = fmt.format(date);
		return dateTimeStr;
	}

	// �ַ���ת����Calendar
	public static Calendar StringToGregorianCalendar(String dateTimeStr) {
		Date date = StringToDate(dateTimeStr);
		Calendar calendar = new GregorianCalendar();

		calendar.setTime(date);
		return calendar;
	}

	// Calendarת����String
	public static String CalendarToString(Calendar calendar) {
		Date date = ((GregorianCalendar) calendar).getTime();
		return DateToString(date);
	}

}
