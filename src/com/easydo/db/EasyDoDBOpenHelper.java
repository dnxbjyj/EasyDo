package com.easydo.db;

/**
 * ������EasyDoDBOpenHelper
 * ���ܣ��������������ݿ�
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class EasyDoDBOpenHelper extends SQLiteOpenHelper {

	// �ճ̱���ScheduleBook���������
	public static final String CREATE_SCHEDULE_BOOK = "create table ScheduleBook ("
			+ "id integer primary key autoincrement," + "name text,"
			// name���ճ̱�������

			+ "create_time text,"
			// create_time���ճ̱�����ʱ�䣬����-ʱ���ַ�����ʽ�����磺��2016-03-20 00:12:34��

			+ "status integer not null default 0,"
			// status���ճ̱���״̬��-1����ɾ��;0��δ����;1���Ѽ������ʹ�õ��ճ̱���

			+ "schedule_num integer default 0," + "type_id integer not null )";
	// schedule_num:�����Ĵ���ɺ�����ɵ��ճ̵���Ŀ����������ɾ�����ճ̣�
	// type_id:ΪScheduleBookType��id����ʾ�ճ̱������

	// �ճ̱�����ScheduleBookType���������
	public static final String CREATE_SCHEDULE_BOOK_TYPE = "create table ScheduleBookType ("
			+ "id integer primary key autoincrement," + "name text )";
	// name:�ճ̱���������

	// �ճ̱�Schedule���������
	public static final String CREATE_SCHEDULE = "create table Schedule ("
			+ "id integer primary key autoincrement,"
			+ "schedule_book_id integer not null," + "create_time text,"
			// schedule_book_id:���ڵ��ճ̱���id
			// create_time:�ճ̴���ʱ�䣬����-ʱ���ַ�����ʽ

			+ "content text not null," + "start_time text,"
			// content:�ճ�����
			// start_time:�ճ̿�ʼ��ʱ�䣬Ĭ��Ϊ�ճ̴�����ʱ�����һ��������Сʱ

			+ "finish_time text," + "alarm_mode integer default -1,"
			// finish_time:����ճ̵�ʱ��
			// alarm_mode:���ѷ�ʽ��ֻ���״̬Ϊδ��ɵ��ճ̽������ѣ�-1��������;0��׼ʱ���ѣ����ճ̿�ʼʱ�����ѣ�;1���Զ�������ʱ�䣨alarm_time�ֶΣ�

			+ "alarm_time text," + "location text,"
			// alarm_time:�Զ�������ʱ��
			// location:�ճ̵ص�

			+ "repeat_mode integer default -1," + "remark text,"
			// repeat_mode:�ճ��ظ�ģʽ
			// -1�����ظ�;0��ÿ���ظ�һ��;1��ÿ���ظ�һ�Σ�ͬһ�죬��ÿ������һ��;2:ÿ�ܹ������ظ�����һ�����壩;3��ÿ���ظ�һ��;4��ÿ���ظ�һ��
			// remark:�ճ̱�ע

			+ "status integer default 0,"
			// status:�ճ�״̬ -1����ɾ��;0��δ���;1�������
			+ "tag integer default 0,"
			// tag:�ճ̱�ǩ����Ϊ����. 0:Ĭ��ֵ����ʾ�ճ�����; 1:��ʾ���ּƻ����Լ��Ƚ��ڴ����ճ̣� 2:��ʾ��������Ҫ���
			// 3:��ʾ��������
			+ "repeat_cut_off_date text)";
	// �ճ��ظ��Ľ�ֹ����

	// ��־��Journal���������
	public static final String CREATE_JOURNAL = "create table Journal ("
			+ "id integer primary key autoincrement," + "title text,"
			// title:��־�ı���

			+ "create_time text," + "status integer default 0,"
			// create_time:��־����ʱ�䣬����-ʱ���ַ�����ʽ
			// status:��־״̬ -1����ɾ��;0������

			+ "content text not null )";
	// content����־����

	// ϵͳ���ñ�SystemConfig���������
	public static final String CREATE_SYSTEM_CONFIG = "create table SystemConfig ("
			+ "id integer primary key autoincrement," + "version text,"
			// ��ǰ����汾�ţ���ʽ��"1.5.3"��

			+ "schedule_show_way integer default 0,"
			// �ճ���ʾ��ʽ 0������ͼ��ʾ��ʽ;1������ͼ��ʽ;2:����ͼ��ʽ

			+ "privacy_way integer default 0,"
			// ��˽������ʽ 0������˽����;1��һ�����뱣��;2���������뱣��

			+ "password text,"
			// ϵͳ���룬��ʽΪ����4λ�����ַ���

			+ "schedule_alarm_way integer default 0,"
			// �ճ����ѷ�ʽ -1������������;0����+���巽ʽ;1��ֻ������;2��ֻ��������

			+ "activated_schedule_book_id integer not null,"
			// ��ǰ������ճ̱���id
			+ "time_line_show_detail_time integer default 1,"
			// ʱ������ʾ�ճ�ʱ�Ƿ���ʾ��ϸ��Сʱ�ͷ��ӣ�0:����ʾ��1:��ʾ. Ĭ��Ϊ1����ʾ
			+ "schedule_content_typeface text,"
			// �ճ�������ʾ����
			+ "journal_content_typeface text,"
			// ��־������ʾ����
			+ "show_schedule_colorful integer default 0)";
	// ��־�����Ƿ���ʾ��ǩ��ɫ, Ĭ��0:����ʾ; 1:��ʾ

	// InitOriginalDataDone������䣬�ñ�ֻ��һ��id�ֶκ�һ����Ϊis_done�������ֶΣ�ֵΪ0��ʾԭʼ����δ��ʼ����ֵΪ1��ʾ�Ѿ���ʼ��
	public static final String CREATE_INIT_ORIGINAL_DATA_DONE = "create table InitOriginalDataDone("
			+ "id integer primary key autoincrement," + "is_done integer)";

	// added by jyj @2016-5-23
	// SpecialSchedule�������
	public static final String CREATE_SPECIAL_SCHEDULE = "create table SpecialSchedule("
			+ "id integer primary key autoincrement," + "title text," // ����
			+ "date text," // ���ڣ���׼����ʱ���ַ�����ʽ
			+ "remark text," // ��ע
			+ "type int," // ���ͣ�0:������;1������;2:������
			+ "status integer)"; // ״̬��-1����ɾ��;0:����

	/**
	 * 
	 * ���ݿ������־
	 */
	// 2016-5-10 am,oldVersion = 1,newVersion = 2
	// SystemConfig�������ֶΣ�time_line_show_detail_time
	private static final String ALTER_SYSTEM_CONFIG_ADD_TIME_LINE_SHOW_DETAIL_TIME = "alter table SystemConfig "
			+ "add column time_line_show_detail_time integer default 1";

	// 2016-5-10 am,oldVersion = 2,newVersion = 3
	// Schedule�������ֶΣ�tag
	private static final String ALTER_SCHEDULE_ADD_TAG = "alter table Schedule "
			+ "add column tag integer default 0";

	// 2016-5-14 pm,oldVersion = 3; newVersion = 4
	// Schedule�������ֶΣ�repeat_cut_off_date
	private static final String ALTER_SCHEDULE_ADD_REPEAT_CUT_OFF_DATE = "alter table Schedule "
			+ "add column repeat_cut_off_date text";

	// 2016-5-21 am,oldVersion = 4; newVersion = 5
	// SystemConfig�������ֶΣ� schedule_content_typeface
	private static final String ALTER_SYSTEM_CONFIG_ADD_SCHEDULE_CONTENT_TYPEFACE = "alter table SystemConfig "
			+ "add column schedule_content_typeface text";

	// 2016-5-21 am,oldVersion = 4; newVersion = 5
	// SystemConfig�������ֶΣ� journal_content_typeface
	private static final String ALTER_SYSTEM_CONFIG_ADD_JOURNAL_CONTENT_TYPEFACE = "alter table SystemConfig "
			+ "add column journal_content_typeface text";

	// 2016-5-21 pm,oldVersion = 5; newVersion = 6
	// SystemConfig�������ֶΣ� show_schedule_colorful
	private static final String ALTER_SYSTEM_CONFIG_ADD_SHOW_SCHEDULE_COLORFUL = "alter table SystemConfig "
			+ "add column show_schedule_colorful integer default 0";

	public EasyDoDBOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// ����������
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
