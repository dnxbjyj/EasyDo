package com.easydo.util;

/**
 * 日期时间相关操作封装
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
	 * 获取下一生日的Calendar
	 * 比如：现在是2016-5-24，birthdayDate为2010-06-24，那么返回的Calendar的日期为：2016-06-24
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
	 * 获取两个日期之间的天数差值，可为负数
	 * 
	 * @return date1 - date2 的天数
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
	 * 从毫秒值获取Calendar对象
	 */
	public static Calendar getCalendarFromMillis(long millis) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(millis);
		return c;
	}

	/**
	 * 从日期时间字符串获取“时:分”时间字符串
	 * 
	 * @param dateTime日期时间字符串
	 * @return "2016-05-14 19:00:00" 返回:"19:00"
	 */
	public static String getTimeString(String dateTime) {
		return dateTime.substring(11, 16);
	}

	// 获取19位日期时间字符串，格式："2016-05-12 23_10_59"，代表：2016-05-12 23:10:59
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

	// 获取14位日期时间字符串，格式："20160512231059"，代表：2016-05-12 23:10:59
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

	// 比较两个Calendar的大小
	public static int compareCalendar(Calendar c1, Calendar c2) {
		return compareDateTimeString(CalendarToString(c1), CalendarToString(c2));
	}

	// 获取当前时间的毫秒值
	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	// 判断两个Calendar是否在同一周
	public static boolean isAtSameWeek(Calendar c1, Calendar c2,
			int firstDayOfWeek) {
		Calendar firstDayOfWeek1 = getDateBeginTimeCalendar(getFirstDayCalendarOfWeek(
				c1, firstDayOfWeek));
		Calendar firstDayOfWeek2 = getDateBeginTimeCalendar(getFirstDayCalendarOfWeek(
				c2, firstDayOfWeek));

		return (firstDayOfWeek1.equals(firstDayOfWeek2));
	}

	// 获取一个Calendar日期23:59:59时间的Calendar
	public static Calendar getDateEndTimeCalendar(Calendar c) {
		String dateString = CalendarToString(c).substring(0, 10);
		Calendar newCalendar = StringToGregorianCalendar(dateString + " "
				+ "23:59:59");
		return newCalendar;
	}

	// 获取一个Calendar日期00:00:00时间的Calendar
	public static Calendar getDateBeginTimeCalendar(Calendar c) {
		String dateString = CalendarToString(c).substring(0, 10);
		Calendar newCalendar = StringToGregorianCalendar(dateString + " "
				+ "00:00:00");
		return newCalendar;
	}

	// 获取count周后的日期所在星期的日期时间段字符串(count可为负数，代表是之前的日期)
	public static String getWeekStringAway(Calendar curCalendar, int count,
			int firstDayOfWeek) {
		Calendar tmpCalendar = getCopyCalendar(curCalendar);
		int dayDiffNum = count * 7;
		tmpCalendar.add(Calendar.DAY_OF_MONTH, dayDiffNum);
		return getWeekString(tmpCalendar, firstDayOfWeek);
	}

	// 获取当前日期所在星期的日期时间段字符串（firstDayOfWeek为一周的第一天，其取值可为Calendar.SUNDAY、Calendar.MONDAY等），格式："2016-05-02 ~ 08"
	// 注：Calendar的DAY_OF_WEEK字段的取值为1~7，其中1代表星期日，2代表星期一，以此类推。
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

	// 根据哪一天是一周的第一天，获取当前周的第一天的Calendar对象
	public static Calendar getFirstDayCalendarOfWeek(Calendar curCalendar,
			int firstDayOfWeek) {
		Calendar tmpCalendar = getCopyCalendar(curCalendar);
		Calendar c;

		int minus;

		// 如果当前时间的星期值比给定的第一天的星期值小，就要先加7再减去firstDayOfWeek（具体算法原理画个图就可以看出来）
		if (tmpCalendar.get(Calendar.DAY_OF_WEEK) < firstDayOfWeek) {
			minus = tmpCalendar.get(Calendar.DAY_OF_WEEK) + 7 - firstDayOfWeek;
		}
		// 否则直接减去firstDayOfWeek即可
		else {
			minus = tmpCalendar.get(Calendar.DAY_OF_WEEK) - firstDayOfWeek;
		}

		tmpCalendar.add(Calendar.DAY_OF_MONTH, -minus);
		c = tmpCalendar;

		return c;
	}

	// 根据哪一天是一周的第一天，获取当前周的最后一天的Calendar对象
	public static Calendar getLastDayCalendarOfWeek(Calendar curCalendar,
			int firstDayOfWeek) {
		Calendar tmpCalendar = getCopyCalendar(curCalendar);
		Calendar c;

		c = getFirstDayCalendarOfWeek(tmpCalendar, firstDayOfWeek);
		c.add(Calendar.DAY_OF_MONTH, 6);

		return c;
	}

	// 获取一个Calendar的拷贝
	public static Calendar getCopyCalendar(Calendar c) {
		return StringToGregorianCalendar(CalendarToString(c));
	}

	// 获取当前时间的Calendar
	public static Calendar getCurrentCalendar() {
		Date d = new Date();
		return StringToGregorianCalendar(getCurrentDateTimeString());
	}

	// 获取当前的秒值字符串，格式："01"
	public static String getCurrentSecondString() {
		return getCurrentTimeLongString().substring(6, 8);
	}

	// 获取给定dateTime n个工作日之后的日期字符串
	public static String getNDayLaterWorkDayDateString(String dateTime, int n) {
		Calendar c = StringToGregorianCalendar(dateTime);
		for (int i = 0; i < n; i++) {
			c = getNextWorkDay(c);
		}
		return CalendarToString(c).substring(0, 10);
	}

	// 获取下一个工作日（周一~周五为工作日）的Calendar对象
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

	// 获取给定日期字符串n[]之后的时间的日期字符串（n的单位为Calendar的那些表示时间的常量）
	public static String getNLaterDateString(String date, int nType, int n) {
		Calendar c = StringToGregorianCalendar(date + " 23:59:59");
		c.add(nType, n);
		return CalendarToString(c).substring(0, 10);
	}

	// 获取当前时间n[]之后的时间的日期字符串（n的单位为Calendar的那些表示时间的常量）
	public static String getNLaterDateString(int nType, int n) {
		Date date = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(nType, n);

		return CalendarToString(c).substring(0, 10);
	}

	// 获取当前时间n[]之后的时间的日期时间字符串（N的单位为Calendar的那些表示时间的常量）
	public static String getNLaterDateTimeString(int nType, int n) {
		Date date = new Date();
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		c.add(nType, n);

		return CalendarToString(c);
	}

	// 时间毫秒值转换成日期时间字符串
	public static String getDateTimeStringFromMillis(long millis) {
		Date date = new Date(millis);
		return (DateToString(date));
	}

	// 把日期时间字符串的时间转换成毫秒值（RTC）
	public static long stringToMillis(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);

		return c.getTimeInMillis();
	}

	// 获取两个日期时间字符串表示的时间之间的毫秒差值
	public static long getDifMillis(String dateTime1, String dateTime2) {
		return (stringToMillis(dateTime1) - stringToMillis(dateTime2));
	}

	// 输入一个表示日期或时间的整型数，输出"01"或"23"这样格式的字符串
	public static String getDoubleNumString(int n) {
		int num = n % 60;

		if (num < 10) {
			return "0" + num;
		} else {
			return num + "";
		}
	}

	// 获取标准日期时间字符串的整型的日期值，如："2015-01-21 00:00:00"，返回：21
	public static int getDayOfMonth(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return day;
	}

	// 获取当前时间的日期时间字符串，格式："yyyy-MM-dd HH:mm:ss"
	public static String getCurrentDateTimeString() {
		Date date = new Date();
		return DateToString(date);
	}

	// 获取当前的"yyyy-MM-dd"日期格式字符串
	public static String getCurrentDateString() {
		Date date = new Date();
		return DateToString(date).substring(0, 10);
	}

	// 获取当前的"yyyy-MM"日期格式字符串
	public static String getCurrentMonthString() {
		Date date = new Date();
		return DateToString(date).substring(0, 7);
	}

	// 获取当前的"HH:mm"时间格式字符串
	public static String getCurrentTimeString() {
		Date date = new Date();
		return DateToString(date).substring(11, 16);
	}

	// 获取当前的"HH:mm:ss"时间格式字符串
	public static String getCurrentTimeLongString() {
		Date date = new Date();
		return DateToString(date).substring(11, 19);
	}

	// 由Calendar对象生成“11月1日 星期一”这样格式的字符串
	public static String getShortDateTimeOfWeek(Calendar calendar) {
		return getShortDateTimeOfWeek(CalendarToString(calendar));
	}

	// 由日期时间字符串生成“11月1日 星期一”这样格式的字符串
	public static String getShortDateTimeOfWeek(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);

		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		String[] weekStr = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四",
				"星期五", "星期六" };
		String week = weekStr[c.get(Calendar.DAY_OF_WEEK) - 1];

		String result = month + "月" + day + "日" + "  " + week;

		return result;
	}

	// 由日期时间字符串生成“2015年11月1日 星期一”这样格式的字符串
	public static String getDateTimeOfWeek(String dateTime) {
		Calendar c = StringToGregorianCalendar(dateTime);

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		String[] weekStr = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四",
				"星期五", "星期六" };
		String week = weekStr[c.get(Calendar.DAY_OF_WEEK) - 1];

		String result = year + "年" + month + "月" + day + "日" + "  " + week;

		return result;
	}

	// 由日期字符串生成“2015年11月1日”这样格式的字符串
	public static String getChineseDateString(String date) {
		String result = "";

		String[] dateArr = date.split("-");
		int year = Integer.parseInt(dateArr[0]);
		int month = Integer.parseInt(dateArr[1]);
		int day = Integer.parseInt(dateArr[2]);

		result = year + "年" + month + "月" + day + "日";

		return result;
	}

	// 由日期时间字符串生成“2015年11月1日 05:43”这样格式的字符串
	public static String getDateTimeOfHourMinute(String dateTime) {
		String result = "";
		String date = dateTime.split(" ")[0];
		String time = dateTime.split(" ")[1];
		String[] dateArr = date.split("-");
		String[] timeArr = time.split(":");

		int year = Integer.parseInt(dateArr[0]);
		int month = Integer.parseInt(dateArr[1]);
		int day = Integer.parseInt(dateArr[2]);

		result = year + "年" + month + "月" + day + "日" + "  " + timeArr[0] + ":"
				+ timeArr[1];

		return result;
	}

	// 用年月日生成Calendar，month取值范围：[0,11]
	public static Calendar getCalendar(int year, int month, int day) {
		return StringToGregorianCalendar(getDateTimeString(year, month, day));
	}

	// 用年月日生成日期时间字符串，month取值范围：[0,11]
	public static String getDateTimeString(int year, int month, int day) {
		return getDateString(year, month, day) + " " + "00:00:00";
	}

	// 用年月日生成日期字符串，month取值范围：[0,11]
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

	// 用年月生成年月日期字符串，month取值范围：[0,11]
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

	// 用时、分生成时间字符串
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

	// 用时、分、秒生成时间字符串
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

	// 该内部类是用于对日期时间字符串数组进行排序的
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

	// 对日期时间字符串数组进行排序,返回排序后的数组（排序后的顺序是从小到大）
	public static String[] sortDateTimeStringArray(String[] dateTimeStringArray) {
		// 将日期时间字符串数组转换成DateTimeString类型数组，DateTimeString实现了Comparable接口，可以进行排序
		DateTimeString[] tmpArray = new DateTimeString[dateTimeStringArray.length];

		// 生成tmpArray数组
		int i = 0;
		DateTimeUtil tmpUtil = new DateTimeUtil();
		for (String str : dateTimeStringArray) {
			tmpArray[i++] = tmpUtil.new DateTimeString(str);
		}

		// 对tmpArray进行排序
		Arrays.sort(tmpArray);

		String[] result = new String[tmpArray.length];
		i = 0;
		for (DateTimeString str : tmpArray) {
			result[i++] = str.toString();
		}
		return result;
	}

	// 比较两个日期字符串的大小
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

	// 比较两个日期时间字符串的大小，如果str1比str2早，则返回-1，如果相等返回0，否则返回1
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

	// 时间日期字符串转换成Date对象
	// 注：dateTimeStr带不带前导0都是可以的，比如"2011-01-01 01:02:03"和"2011-1-1 1:2:3"都是合法的
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

	// Date对象转换成日期时间字符串
	public static String DateToString(Date date) {
		String dateTimeStr = null;
		// DateFormat fmt = DateFormat.getDateTimeInstance();
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateTimeStr = fmt.format(date);
		return dateTimeStr;
	}

	// 字符串转换成Calendar
	public static Calendar StringToGregorianCalendar(String dateTimeStr) {
		Date date = StringToDate(dateTimeStr);
		Calendar calendar = new GregorianCalendar();

		calendar.setTime(date);
		return calendar;
	}

	// Calendar转换成String
	public static String CalendarToString(Calendar calendar) {
		Date date = ((GregorianCalendar) calendar).getTime();
		return DateToString(date);
	}

}
