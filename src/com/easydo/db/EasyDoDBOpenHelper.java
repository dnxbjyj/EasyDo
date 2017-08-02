package com.easydo.db;

/**
 * 类名：EasyDoDBOpenHelper
 * 功能：创建和升级数据库
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class EasyDoDBOpenHelper extends SQLiteOpenHelper {

	// 日程本表（ScheduleBook）建表语句
	public static final String CREATE_SCHEDULE_BOOK = "create table ScheduleBook ("
			+ "id integer primary key autoincrement," + "name text,"
			// name：日程本的名称

			+ "create_time text,"
			// create_time：日程本创建时间，日期-时间字符串格式，例如：“2016-03-20 00:12:34”

			+ "status integer not null default 0,"
			// status：日程本的状态，-1：已删除;0：未激活;1：已激活（正在使用的日程本）

			+ "schedule_num integer default 0," + "type_id integer not null )";
	// schedule_num:包含的待完成和已完成的日程的数目（不包含已删除的日程）
	// type_id:为ScheduleBookType的id，表示日程本的类别

	// 日程本类别表（ScheduleBookType）建表语句
	public static final String CREATE_SCHEDULE_BOOK_TYPE = "create table ScheduleBookType ("
			+ "id integer primary key autoincrement," + "name text )";
	// name:日程本类别的名称

	// 日程表（Schedule）建表语句
	public static final String CREATE_SCHEDULE = "create table Schedule ("
			+ "id integer primary key autoincrement,"
			+ "schedule_book_id integer not null," + "create_time text,"
			// schedule_book_id:所在的日程本的id
			// create_time:日程创建时间，日期-时间字符串格式

			+ "content text not null," + "start_time text,"
			// content:日程内容
			// start_time:日程开始的时间，默认为日程创建的时间的下一个半整数小时

			+ "finish_time text," + "alarm_mode integer default -1,"
			// finish_time:完成日程的时间
			// alarm_mode:提醒方式（只针对状态为未完成的日程进行提醒）-1：不提醒;0：准时提醒（按日程开始时间提醒）;1：自定义提醒时间（alarm_time字段）

			+ "alarm_time text," + "location text,"
			// alarm_time:自定义提醒时间
			// location:日程地点

			+ "repeat_mode integer default -1," + "remark text,"
			// repeat_mode:日程重复模式
			// -1：不重复;0：每天重复一次;1：每周重复一次（同一天，如每周星期一）;2:每周工作日重复（周一至周五）;3：每月重复一次;4：每年重复一次
			// remark:日程备注

			+ "status integer default 0,"
			// status:日程状态 -1：已删除;0：未完成;1：已完成
			+ "tag integer default 0,"
			// tag:日程标签，分为四种. 0:默认值，表示日常杂事; 1:表示娱乐计划和自己比较期待的日程； 2:表示健康等重要事项；
			// 3:表示工作事项
			+ "repeat_cut_off_date text)";
	// 日程重复的截止日期

	// 日志表（Journal）表建表语句
	public static final String CREATE_JOURNAL = "create table Journal ("
			+ "id integer primary key autoincrement," + "title text,"
			// title:日志的标题

			+ "create_time text," + "status integer default 0,"
			// create_time:日志创建时间，日期-时间字符串格式
			// status:日志状态 -1：已删除;0：正常

			+ "content text not null )";
	// content：日志内容

	// 系统设置表（SystemConfig）建表语句
	public static final String CREATE_SYSTEM_CONFIG = "create table SystemConfig ("
			+ "id integer primary key autoincrement," + "version text,"
			// 当前程序版本号（格式："1.5.3"）

			+ "schedule_show_way integer default 0,"
			// 日程显示方式 0：月视图显示方式;1：日视图方式;2:周视图方式

			+ "privacy_way integer default 0,"
			// 隐私保护方式 0：无隐私保护;1：一般密码保护;2：手势密码保护

			+ "password text,"
			// 系统密码，格式为至少4位数字字符串

			+ "schedule_alarm_way integer default 0,"
			// 日程提醒方式 -1：静音不提醒;0：振动+响铃方式;1：只振动提醒;2：只响铃提醒

			+ "activated_schedule_book_id integer not null,"
			// 当前激活的日程本的id
			+ "time_line_show_detail_time integer default 1,"
			// 时间轴显示日程时是否显示详细的小时和分钟，0:不显示；1:显示. 默认为1，显示
			+ "schedule_content_typeface text,"
			// 日程内容显示字体
			+ "journal_content_typeface text,"
			// 日志内容显示字体
			+ "show_schedule_colorful integer default 0)";
	// 日志内容是否显示标签颜色, 默认0:不显示; 1:显示

	// InitOriginalDataDone表建表语句，该表只有一个id字段和一个名为is_done的整型字段，值为0表示原始数据未初始化，值为1表示已经初始化
	public static final String CREATE_INIT_ORIGINAL_DATA_DONE = "create table InitOriginalDataDone("
			+ "id integer primary key autoincrement," + "is_done integer)";

	// added by jyj @2016-5-23
	// SpecialSchedule表建表语句
	public static final String CREATE_SPECIAL_SCHEDULE = "create table SpecialSchedule("
			+ "id integer primary key autoincrement," + "title text," // 标题
			+ "date text," // 日期，标准日期时间字符串格式
			+ "remark text," // 备注
			+ "type int," // 类型，0:纪念日;1：生日;2:倒数日
			+ "status integer)"; // 状态，-1：已删除;0:正常

	/**
	 * 
	 * 数据库更新日志
	 */
	// 2016-5-10 am,oldVersion = 1,newVersion = 2
	// SystemConfig表新增字段：time_line_show_detail_time
	private static final String ALTER_SYSTEM_CONFIG_ADD_TIME_LINE_SHOW_DETAIL_TIME = "alter table SystemConfig "
			+ "add column time_line_show_detail_time integer default 1";

	// 2016-5-10 am,oldVersion = 2,newVersion = 3
	// Schedule表新增字段：tag
	private static final String ALTER_SCHEDULE_ADD_TAG = "alter table Schedule "
			+ "add column tag integer default 0";

	// 2016-5-14 pm,oldVersion = 3; newVersion = 4
	// Schedule表新增字段：repeat_cut_off_date
	private static final String ALTER_SCHEDULE_ADD_REPEAT_CUT_OFF_DATE = "alter table Schedule "
			+ "add column repeat_cut_off_date text";

	// 2016-5-21 am,oldVersion = 4; newVersion = 5
	// SystemConfig表新增字段： schedule_content_typeface
	private static final String ALTER_SYSTEM_CONFIG_ADD_SCHEDULE_CONTENT_TYPEFACE = "alter table SystemConfig "
			+ "add column schedule_content_typeface text";

	// 2016-5-21 am,oldVersion = 4; newVersion = 5
	// SystemConfig表新增字段： journal_content_typeface
	private static final String ALTER_SYSTEM_CONFIG_ADD_JOURNAL_CONTENT_TYPEFACE = "alter table SystemConfig "
			+ "add column journal_content_typeface text";

	// 2016-5-21 pm,oldVersion = 5; newVersion = 6
	// SystemConfig表新增字段： show_schedule_colorful
	private static final String ALTER_SYSTEM_CONFIG_ADD_SHOW_SCHEDULE_COLORFUL = "alter table SystemConfig "
			+ "add column show_schedule_colorful integer default 0";

	public EasyDoDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建各个表
		db.execSQL(CREATE_SCHEDULE_BOOK_TYPE);
		db.execSQL(CREATE_SCHEDULE_BOOK);
		db.execSQL(CREATE_SCHEDULE);
		db.execSQL(CREATE_JOURNAL);
		db.execSQL(CREATE_SYSTEM_CONFIG);
		db.execSQL(CREATE_INIT_ORIGINAL_DATA_DONE);
		db.execSQL(CREATE_SPECIAL_SCHEDULE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch (oldVersion) {
		case 1:
			db.execSQL(ALTER_SYSTEM_CONFIG_ADD_TIME_LINE_SHOW_DETAIL_TIME);
		case 2:
			db.execSQL(ALTER_SCHEDULE_ADD_TAG);
		case 3:
			db.execSQL(ALTER_SCHEDULE_ADD_REPEAT_CUT_OFF_DATE);
		case 4:
			db.execSQL(ALTER_SYSTEM_CONFIG_ADD_SCHEDULE_CONTENT_TYPEFACE);
			db.execSQL(ALTER_SYSTEM_CONFIG_ADD_JOURNAL_CONTENT_TYPEFACE);
		case 5:
			db.execSQL(ALTER_SYSTEM_CONFIG_ADD_SHOW_SCHEDULE_COLORFUL);
		case 6:
			db.execSQL(CREATE_SPECIAL_SCHEDULE);
		default:
			break;
		}
	}

}
