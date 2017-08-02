package com.easydo.db;

import java.util.ArrayList;
import java.util.List;

import com.easydo.constant.GlobalConfig;
import com.easydo.model.Journal;
import com.easydo.model.Schedule;
import com.easydo.model.ScheduleBook;
import com.easydo.model.ScheduleBookType;
import com.easydo.model.SpecialSchedule;
import com.easydo.model.SystemConfig;
import com.easydo.util.LogUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ������EasyDoDB
 * 
 * ���ܣ���װ���õ����ݿ����
 * 
 *
 */

public class EasyDoDB {
	// ���ݿ���
	public static final String DB_NAME = GlobalConfig.DB_NAME;

	// ���ݿ�汾
	public static final int VERSION = GlobalConfig.CUR_DB_VERSION;

	// ����ģʽ����֤ȫ��ֻ��һ��EasyDoDB�����
	private static EasyDoDB easyDoDB;

	// ���ݿ����
	private static SQLiteDatabase db;

	// �����췽��˽�л�ʵ�ֵ���ģʽ
	private EasyDoDB(Context context) {
		EasyDoDBOpenHelper dbHelper = new EasyDoDBOpenHelper(context, DB_NAME,
				null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	// ��ȡEasyDoDBʵ����Ҫ��ͬ���������Σ���Ӧ�Զ��̻߳���
	public synchronized static EasyDoDB getInstance(Context context) {
		if (easyDoDB == null) {
			easyDoDB = new EasyDoDB(context);
		}
		return easyDoDB;
	}

	/**
	 * ��һ�����г������ݳ�ʼ����־��
	 */

	// �洢���ݵ�InitOriginalDataDone����
	public void saveInitOriginalDataDone(int isDone) {
		ContentValues values = new ContentValues();
		values.put("is_done", isDone);
		db.insert("InitOriginalDataDone", null, values);
	}

	// ��InitOriginalDataDone���ж�ȡһ����������
	public int loadInitOriginalDataDone() {
		Cursor cursor = db.rawQuery("select * from InitOriginalDataDone", null);
		if (cursor.moveToFirst()) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * ����Ϊ���ݿ��д��������
	 * 
	 */

	/****************************************************************************************/
	// ��Journalʵ���洢�����ݿ�
	public void saveJournal(Journal journal) {
		if (journal != null) {
			ContentValues values = new ContentValues();
			values.put("title", journal.getTitle());
			values.put("create_time", journal.getCreateTime());
			values.put("status", journal.getStatus());
			values.put("content", journal.getContent());
			db.insert("Journal", null, values);
		}
	}

	// �����ݿ��л�ȡ�����ѯ������Journal��¼
	// isFTS��ʾ�Ƿ���ȫ������(Full Text Search)
	// conditionΪ��ѯ�����ַ�����Ϊ����ռλ�����ַ���;valuesΪ�ַ������飬��ʾ����condition��ռλ���Ķ�Ӧֵ
	// ���conditionΪnull�������ѯ���ݱ��е����м�¼;�����Ϊnull��������ʾ�����в�ѯ��
	// ʾ����List<Journal> result =
	// loadJournals("where id < ? and status = ?",["10","1"]);
	// �������д���Ľ���൱�������ѯ����ѯ�Ľ���б�select * from Journal where id < 10 and status
	// = 1;
	// ����������Ĳ�ѯ�����������������ͬ
	public List<Journal> loadJournals(boolean isFTS, String FTSQuery,
			String condition, String[] values) {
		List<Journal> list = new ArrayList<Journal>();
		Cursor cursor;

		if (!isFTS) {
			if (condition != null) {
				cursor = db.rawQuery(
						"select * from Journal where " + condition, values);
			} else {
				cursor = db.rawQuery("select * from Journal where status != ?",
						new String[] { Journal.STATUS_DELETED + "" });
			}
		} else {
			FTSQuery = "%" + FTSQuery + "%";

			if (condition == null) {
				cursor = db.rawQuery(
						"select * from Journal where status != ? and (create_time like ? "
								+ "or content like ?)",
						new String[] { Journal.STATUS_DELETED + "", FTSQuery,
								FTSQuery });
			} else {
				String[] queryValues = new String[values.length + 2];
				queryValues[0] = FTSQuery;
				queryValues[1] = FTSQuery;
				for (int i = 2; i < values.length + 2; i++) {
					queryValues[i] = values[i - 2];
				}

				cursor = db.rawQuery("select * from Journal where status != "
						+ Journal.STATUS_DELETED + " and (create_time like ? "
						+ "or content like ?) and " + condition, queryValues);
			}
		}

		if (cursor.moveToFirst()) {
			do {
				Journal journal = new Journal();
				journal.setId(cursor.getInt(cursor.getColumnIndex("id")));
				journal.setTitle(cursor.getString(cursor
						.getColumnIndex("title")));
				journal.setCreateTime(cursor.getString(cursor
						.getColumnIndex("create_time")));
				journal.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				journal.setContent(cursor.getString(cursor
						.getColumnIndex("content")));

				list.add(journal);
			} while (cursor.moveToNext());
		}

		return list;
	}

	// ��Journal���л�ȡָ��id�ļ�¼
	public Journal loadJournalById(int id) {
		Journal journal;

		List<Journal> queryList = loadJournals(false, null, " id = ?",
				new String[] { id + "" });

		if (queryList.size() > 0) {
			journal = queryList.get(0);
		} else {
			journal = null;
		}

		return journal;
	}

	// ��һ��Journalʵ�������������Journal����ָ��id������
	public boolean updateJournalById(int id, Journal journal) {
		String tableName = "Journal";
		String queryColumn = "id";
		String queryValue = id + "";

		updateDB(tableName, "title", journal.getTitle(), queryColumn,
				queryValue);
		updateDB(tableName, "create_time", journal.getCreateTime(),
				queryColumn, queryValue);
		updateDB(tableName, "status", journal.getStatus() + "", queryColumn,
				queryValue);
		updateDB(tableName, "content", journal.getContent(), queryColumn,
				queryValue);

		return true;

	}

	/****************************************************************************************/

	/****************************************************************************************/
	// ��Scheduleʵ���洢�����ݿ�
	public int saveSchedule(Schedule schedule) {
		if (schedule != null) {
			ContentValues values = new ContentValues();
			values.put("schedule_book_id", schedule.getScheduleBookId());
			values.put("create_time", schedule.getCreateTime());
			values.put("content", schedule.getContent());
			values.put("start_time", schedule.getStartTime());
			values.put("finish_time", schedule.getFinishTime());
			values.put("alarm_mode", schedule.getAlarmMode());
			values.put("alarm_time", schedule.getAlarmTime());
			values.put("location", schedule.getLocation());
			values.put("repeat_mode", schedule.getRepeatMode());
			values.put("remark", schedule.getRemark());
			values.put("status", schedule.getStatus());
			values.put("tag", schedule.getTag());
			values.put("repeat_cut_off_date", schedule.getRepeatCutOffDate());
			db.insert("Schedule", null, values);

			Cursor cursor = db.rawQuery(
					"select last_insert_rowid() from Schedule", null);
			cursor.moveToFirst();
			int id = cursor.getInt(0);
			return id;
		}
		return 0;
	}

	// �����ݿ��л�ȡSchedule��¼
	public List<Schedule> loadSchedules(boolean isFTS, String FTSQuery,
			String condition, String[] values) {
		List<Schedule> list = new ArrayList<Schedule>();
		Cursor cursor;

		if (!isFTS) {
			if (condition != null) {
				cursor = db.rawQuery("select * from Schedule where "
						+ condition, values);
			} else {
				cursor = db.rawQuery(
						"select * from Schedule where status != ?",
						new String[] { Schedule.STATUS_DELETED + "" });
			}
		} else {
			FTSQuery = "%" + FTSQuery + "%";

			if (condition == null) {
				cursor = db.rawQuery(
						"select * from Schedule where status != -1 and (content like ? or "
								+ "location like ? or remark like ?)",
						new String[] { FTSQuery, FTSQuery, FTSQuery });
			} else {
				String[] queryValues = new String[values.length + 3];
				queryValues[0] = FTSQuery;
				queryValues[1] = FTSQuery;
				queryValues[2] = FTSQuery;
				for (int i = 3; i < values.length + 3; i++) {
					queryValues[i] = values[i - 3];
				}

				cursor = db.rawQuery(
						"select * from Schedule where status != -1 and (content like ? or "
								+ "location like ? or remark like ?) and "
								+ condition, queryValues);
			}
		}

		if (cursor.moveToFirst()) {
			do {
				Schedule schedule = new Schedule();
				schedule.setId(cursor.getInt(cursor.getColumnIndex("id")));
				schedule.setScheduleBookId(cursor.getInt(cursor
						.getColumnIndex("schedule_book_id")));
				schedule.setCreateTime(cursor.getString(cursor
						.getColumnIndex("create_time")));
				schedule.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				schedule.setStartTime(cursor.getString(cursor
						.getColumnIndex("start_time")));
				schedule.setFinishTime(cursor.getString(cursor
						.getColumnIndex("finish_time")));
				schedule.setAlarmMode(cursor.getInt(cursor
						.getColumnIndex("alarm_mode")));
				schedule.setAlarmTime(cursor.getString(cursor
						.getColumnIndex("alarm_time")));
				schedule.setLocation(cursor.getString(cursor
						.getColumnIndex("location")));
				schedule.setRepeatMode(cursor.getInt(cursor
						.getColumnIndex("repeat_mode")));
				schedule.setRemark(cursor.getString(cursor
						.getColumnIndex("remark")));
				schedule.setStatus(cursor.getInt(cursor
						.getColumnIndex("status")));
				schedule.setTag(cursor.getInt(cursor.getColumnIndex("tag")));
				schedule.setRepeatCutOffDate(cursor.getString(cursor
						.getColumnIndex("repeat_cut_off_date")));
				list.add(schedule);
			} while (cursor.moveToNext());
		}

		return list;
	}

	// ��Schedule���л�ȡ�������ʱ�������ͬ����ʱ���ҽ��������ѻ��ظ���ʱ����������ճ̵�id
	public List<Integer> getSameCreateTimeScheduleId(String sameCreateDateTime) {
		List<Integer> list = new ArrayList<Integer>();
		Cursor cursor;
		cursor = db.rawQuery("select * from Schedule", null);
		if (cursor.moveToFirst()) {
			do {
				String createTime = cursor.getString(cursor
						.getColumnIndex("create_time"));
				int alarmMode = cursor.getInt(cursor
						.getColumnIndex("alarm_mode"));
				int repeatMode = cursor.getInt(cursor
						.getColumnIndex("repeat_mode"));

				if (createTime.equals(sameCreateDateTime)
						&& repeatMode != Schedule.REPEAT_MODE_OFF) {
					list.add(cursor.getInt(cursor.getColumnIndex("id")));
				}
			} while (cursor.moveToNext());
		}

		return list;
	}

	// ��Schedule���л�ȡָ��id�ļ�¼
	public Schedule loadScheduleById(int id) {
		Schedule schedule;

		List<Schedule> queryList = loadSchedules(false, null, " id = ?",
				new String[] { id + "" });

		if (queryList.size() > 0) {
			schedule = queryList.get(0);
		} else {
			schedule = null;
		}

		return schedule;
	}

	// ��һ��Scheduleʵ�������������Schedule����ָ��id������
	public boolean updateScheduleById(int id, Schedule schedule) {
		String tableName = "Schedule";
		String queryColumn = "id";
		String queryValue = id + "";

		updateDB(tableName, "schedule_book_id", schedule.getScheduleBookId()
				+ "", queryColumn, queryValue);
		updateDB(tableName, "create_time", schedule.getCreateTime(),
				queryColumn, queryValue);
		updateDB(tableName, "content", schedule.getContent(), queryColumn,
				queryValue);
		updateDB(tableName, "start_time", schedule.getStartTime(), queryColumn,
				queryValue);
		updateDB(tableName, "finish_time", schedule.getFinishTime(),
				queryColumn, queryValue);
		updateDB(tableName, "alarm_mode", schedule.getAlarmMode() + "",
				queryColumn, queryValue);
		updateDB(tableName, "alarm_time", schedule.getAlarmTime(), queryColumn,
				queryValue);
		updateDB(tableName, "location", schedule.getLocation(), queryColumn,
				queryValue);
		updateDB(tableName, "repeat_mode", schedule.getRepeatMode() + "",
				queryColumn, queryValue);
		updateDB(tableName, "remark", schedule.getRemark(), queryColumn,
				queryValue);
		updateDB(tableName, "status", schedule.getStatus() + "", queryColumn,
				queryValue);

		updateDB(tableName, "tag", schedule.getTag() + "", queryColumn,
				queryValue);

		updateDB(tableName, "repeat_cut_off_date",
				schedule.getRepeatCutOffDate(), queryColumn, queryValue);

		return true;

	}

	// ��ȡ�����������ճ����ݵ�����
	public int getScheduleNum(String condition, String[] values) {
		Cursor cursor;
		if (condition != null) {
			cursor = db.rawQuery("select * from Schedule where " + condition,
					values);
		} else {
			cursor = db.rawQuery("select * from Schedule", null);
		}

		return cursor.getCount();
	}

	/****************************************************************************************/

	/****************************************************************************************/
	// ��SpecialScheduleʵ���洢�����ݿ�
	public int saveSpecialSchedule(SpecialSchedule specialSchedule) {
		if (specialSchedule != null) {
			ContentValues values = new ContentValues();
			values.put("title", specialSchedule.getTitle());
			values.put("date", specialSchedule.getDate());
			values.put("remark", specialSchedule.getRemark());
			values.put("type", specialSchedule.getType());
			values.put("status", specialSchedule.getStatus());

			db.insert(SpecialSchedule.TABLE_NAME, null, values);

			Cursor cursor = db.rawQuery("select last_insert_rowid() from "
					+ SpecialSchedule.TABLE_NAME, null);
			cursor.moveToFirst();
			int id = cursor.getInt(0);
			return id;
		}
		return 0;
	}

	// �����ݿ��л�ȡSpecialSchedule��¼
	public List<SpecialSchedule> loadSpecialSchedules(boolean isFTS,
			String FTSQuery, String condition, String[] values) {
		List<SpecialSchedule> list = new ArrayList<SpecialSchedule>();
		Cursor cursor;

		if (!isFTS) {
			if (condition != null) {
				cursor = db.rawQuery("select * from SpecialSchedule where "
						+ condition, values);
			} else {
				cursor = db.rawQuery(
						"select * from SpecialSchedule where status != ?",
						new String[] { SpecialSchedule.STATUS_DELETED + "" });
			}
		} else {
			// ��ʱcondition��Ϊnull
			FTSQuery = "%" + FTSQuery + "%";

			cursor = db.rawQuery(
					"select * from SpecialSchedule where status != -1 and (title like ? or "
							+ "remark like ?)", new String[] { FTSQuery,
							FTSQuery });
		}

		if (cursor.moveToFirst()) {
			do {
				SpecialSchedule specialSchedule = new SpecialSchedule();
				specialSchedule
						.setId(cursor.getInt(cursor.getColumnIndex("id")));
				specialSchedule.setTitle(cursor.getString(cursor
						.getColumnIndex("title")));
				specialSchedule.setDate(cursor.getString(cursor
						.getColumnIndex("date")));
				specialSchedule.setRemark(cursor.getString(cursor
						.getColumnIndex("remark")));
				specialSchedule.setType(cursor.getInt(cursor
						.getColumnIndex("type")));
				specialSchedule.setStatus(cursor.getInt(cursor
						.getColumnIndex("status")));

				list.add(specialSchedule);
			} while (cursor.moveToNext());
		}

		return list;
	}

	// ��SpecialSchedule���л�ȡָ��id�ļ�¼
	public SpecialSchedule loadSpecialScheduleById(int id) {
		SpecialSchedule specialSchedule;

		List<SpecialSchedule> queryList = loadSpecialSchedules(false, null,
				" id = ?", new String[] { id + "" });

		if (queryList.size() > 0) {
			specialSchedule = queryList.get(0);
		} else {
			specialSchedule = null;
		}

		return specialSchedule;
	}

	// ��һ��SpecialScheduleʵ�������������SpecialSchedule����ָ��id������
	public boolean updateSpecialScheduleById(int id,
			SpecialSchedule specialSchedule) {
		String tableName = SpecialSchedule.TABLE_NAME;
		String queryColumn = "id";
		String queryValue = id + "";

		updateDB(tableName, "title", specialSchedule.getTitle(), queryColumn,
				queryValue);
		updateDB(tableName, "date", specialSchedule.getDate(), queryColumn,
				queryValue);
		updateDB(tableName, "remark", specialSchedule.getRemark(), queryColumn,
				queryValue);
		updateDB(tableName, "type", specialSchedule.getType() + "",
				queryColumn, queryValue);
		updateDB(tableName, "status", specialSchedule.getStatus() + "",
				queryColumn, queryValue);

		return true;

	}

	/****************************************************************************************/

	/****************************************************************************************/
	// ��ScheduleBookʵ���洢�����ݿ�
	public void saveScheduleBook(ScheduleBook scheduleBook) {
		if (scheduleBook != null) {
			ContentValues values = new ContentValues();
			values.put("name", scheduleBook.getName());
			values.put("create_time", scheduleBook.getCreateTime());
			values.put("status", scheduleBook.getStatus());
			values.put("schedule_num", scheduleBook.getScheduleNum());
			values.put("type_id", scheduleBook.getTypeId());

			db.insert("ScheduleBook", null, values);
		}
	}

	// �����ݿ��л�ȡ����ScheduleBook��¼
	public List<ScheduleBook> loadScheduleBooks(String condition,
			String[] values) {
		List<ScheduleBook> list = new ArrayList<ScheduleBook>();
		Cursor cursor;
		if (condition != null) {
			cursor = db.rawQuery("select * from ScheduleBook " + condition,
					values);
		} else {
			cursor = db.rawQuery("select * from ScheduleBook", null);
		}

		if (cursor.moveToFirst()) {
			do {
				ScheduleBook scheduleBook = new ScheduleBook();
				scheduleBook.setId(cursor.getInt(cursor.getColumnIndex("id")));
				scheduleBook.setName(cursor.getString(cursor
						.getColumnIndex("name")));
				scheduleBook.setCreateTime(cursor.getString(cursor
						.getColumnIndex("create_time")));
				scheduleBook.setStatus(cursor.getInt(cursor
						.getColumnIndex("status")));
				scheduleBook.setScheduleNum(cursor.getInt(cursor
						.getColumnIndex("schedule_num")));
				scheduleBook.setTypeId(cursor.getInt(cursor
						.getColumnIndex("type_id")));

				list.add(scheduleBook);
			} while (cursor.moveToNext());
		}

		return list;
	}

	// ��ScheduleBook���л�ȡָ��id�ļ�¼
	public ScheduleBook loadScheduleBookById(int id) {
		ScheduleBook scheduleBook;

		List<ScheduleBook> queryList = loadScheduleBooks("where id = ?",
				new String[] { id + "" });

		if (queryList.size() > 0) {
			scheduleBook = queryList.get(0);
		} else {
			scheduleBook = null;
		}

		return scheduleBook;
	}

	// ��һ��ScheduleBookʵ�������������ScheduleBook����ָ��id������
	public boolean updateScheduleBookById(int id, ScheduleBook scheduleBook) {
		String tableName = "ScheduleBook";
		String queryColumn = "id";
		String queryValue = id + "";

		updateDB(tableName, "name", scheduleBook.getName(), queryColumn,
				queryValue);
		updateDB(tableName, "create_time", scheduleBook.getCreateTime(),
				queryColumn, queryValue);
		updateDB(tableName, "status", scheduleBook.getStatus() + "",
				queryColumn, queryValue);
		updateDB(tableName, "schedule_num", scheduleBook.getScheduleNum() + "",
				queryColumn, queryValue);
		updateDB(tableName, "type_id", scheduleBook.getTypeId() + "",
				queryColumn, queryValue);

		return true;

	}

	/****************************************************************************************/

	/****************************************************************************************/
	// ��ScheduleBookTypeʵ���洢�����ݿ�
	public void saveScheduleBookType(ScheduleBookType scheduleBookType) {
		if (scheduleBookType != null) {
			ContentValues values = new ContentValues();
			values.put("name", scheduleBookType.getName());

			db.insert("ScheduleBookType", null, values);
		}
	}

	// �����ݿ��л�ȡ����ScheduleBookType��¼
	public List<ScheduleBookType> loadScheduleBookTypes(String condition,
			String[] values) {
		List<ScheduleBookType> list = new ArrayList<ScheduleBookType>();
		Cursor cursor;
		if (condition != null) {
			cursor = db.rawQuery("select * from ScheduleBookType " + condition,
					values);
		} else {
			cursor = db.rawQuery("select * from ScheduleBookType", null);
		}

		if (cursor.moveToFirst()) {
			do {
				ScheduleBookType scheduleBookType = new ScheduleBookType();
				scheduleBookType.setId(cursor.getInt(cursor
						.getColumnIndex("id")));
				scheduleBookType.setName(cursor.getString(cursor
						.getColumnIndex("name")));

				list.add(scheduleBookType);
			} while (cursor.moveToNext());
		}

		return list;
	}

	// ��ScheduleBookType���л�ȡָ��id�ļ�¼
	public ScheduleBookType loadScheduleBookTypeById(int id) {
		ScheduleBookType scheduleBookType;

		List<ScheduleBookType> queryList = loadScheduleBookTypes(
				"where id = ?", new String[] { id + "" });

		if (queryList.size() > 0) {
			scheduleBookType = queryList.get(0);
		} else {
			scheduleBookType = null;
		}

		return scheduleBookType;
	}

	// ��һ��ScheduleBookʵ�������������ScheduleBook����ָ��id������
	public boolean updateScheduleBookTypeById(int id,
			ScheduleBookType scheduleBookType) {
		String tableName = "ScheduleBookType";
		String queryColumn = "id";
		String queryValue = id + "";

		updateDB(tableName, "name", scheduleBookType.getName(), queryColumn,
				queryValue);

		return true;

	}

	/****************************************************************************************/

	/****************************************************************************************/
	// ��SystemConfigʵ���洢�����ݿ�
	public void saveSystemConfig(SystemConfig systemConfig) {
		if (systemConfig != null) {
			ContentValues values = new ContentValues();
			values.put("version", systemConfig.getVersion());
			values.put("schedule_show_way", systemConfig.getScheduleShowWay());
			values.put("privacy_way", systemConfig.getPrivacyWay());
			values.put("password", systemConfig.getPassword());
			values.put("schedule_alarm_way", systemConfig.getScheduleAlarmWay());
			values.put("activated_schedule_book_id",
					systemConfig.getActivatedScheduleBookId());
			values.put("time_line_show_detail_time",
					systemConfig.getTimeLineShowDetailTime());
			values.put("schedule_content_typeface",
					systemConfig.getScheduleContentTypeface());
			values.put("journal_content_typeface",
					systemConfig.getJournalContentTypeface());
			values.put("show_schedule_colorful",
					systemConfig.getShowScheduleColorful());
			db.insert("SystemConfig", null, values);
		}
	}

	// �����ݿ��л�ȡSystemConfig��¼
	public SystemConfig loadSystemConfig(String condition, String[] values) {
		SystemConfig systemConfig = new SystemConfig();

		Cursor cursor;
		if (condition != null) {
			cursor = db.rawQuery("select * from SystemConfig " + condition,
					values);
		} else {
			cursor = db.rawQuery("select * from SystemConfig", null);
		}

		if (cursor.moveToFirst()) {
			do {
				systemConfig.setId(cursor.getInt(cursor.getColumnIndex("id")));
				systemConfig.setVersion(cursor.getString(cursor
						.getColumnIndex("version")));
				systemConfig.setScheduleShowWay(cursor.getInt(cursor
						.getColumnIndex("schedule_show_way")));
				systemConfig.setPrivacyWay(cursor.getInt(cursor
						.getColumnIndex("privacy_way")));
				systemConfig.setPassword(cursor.getString(cursor
						.getColumnIndex("password")));
				systemConfig.setScheduleAlarmWay(cursor.getInt(cursor
						.getColumnIndex("schedule_alarm_way")));
				systemConfig.setActivatedScheduleBookId(cursor.getInt(cursor
						.getColumnIndex("activated_schedule_book_id")));
				systemConfig.setTimeLineShowDetailTime(cursor.getInt(cursor
						.getColumnIndex("time_line_show_detail_time")));
				systemConfig.setScheduleContentTypeface(cursor.getString(cursor
						.getColumnIndex("schedule_content_typeface")));
				systemConfig.setJournalContentTypeface(cursor.getString(cursor
						.getColumnIndex("journal_content_typeface")));
				systemConfig.setShowScheduleColorful(cursor.getInt(cursor
						.getColumnIndex("show_schedule_colorful")));
			} while (cursor.moveToNext());
		}

		return systemConfig;
	}

	// ��һ��SystemConfigʵ�������������SystemConfig����ָ��id������
	public boolean updateSystemConfigById(int id, SystemConfig systemConfig) {
		String tableName = "SystemConfig";
		String queryColumn = "id";
		String queryValue = id + "";

		updateDB(tableName, "version", systemConfig.getVersion(), queryColumn,
				queryValue);
		updateDB(tableName, "schedule_show_way",
				systemConfig.getScheduleShowWay() + "", queryColumn, queryValue);
		updateDB(tableName, "privacy_way", systemConfig.getPrivacyWay() + "",
				queryColumn, queryValue);
		updateDB(tableName, "password", systemConfig.getPassword(),
				queryColumn, queryValue);
		updateDB(tableName, "schedule_alarm_way",
				systemConfig.getScheduleAlarmWay() + "", queryColumn,
				queryValue);
		updateDB(tableName, "activated_schedule_book_id",
				systemConfig.getActivatedScheduleBookId() + "", queryColumn,
				queryValue);
		updateDB(tableName, "time_line_show_detail_time",
				systemConfig.getTimeLineShowDetailTime() + "", queryColumn,
				queryValue);

		updateDB(tableName, "schedule_content_typeface",
				systemConfig.getScheduleContentTypeface() + "", queryColumn,
				queryValue);

		updateDB(tableName, "journal_content_typeface",
				systemConfig.getJournalContentTypeface() + "", queryColumn,
				queryValue);
		updateDB(tableName, "show_schedule_colorful",
				systemConfig.getShowScheduleColorful() + "", queryColumn,
				queryValue);
		return true;

	}

	/****************************************************************************************/

	/****************************************************************************************/
	/**
	 * ���ݿ��޸ĵķ���
	 */
	// �������ݿ����ݵ�ͨ�÷���������ʾ����update [tableName] set [toSetColumn] = [newValue] where
	// [queryColumn] = [queryValue];
	public boolean updateDB(String tableName, String toSetColumn,
			String newValue, String queryColumn, String queryValue) {
		db.execSQL("update " + tableName + " set " + toSetColumn
				+ " = ? where " + queryColumn + " = ?", new String[] {
				newValue, queryValue });
		return true;
	}

	// ����idList�б��������ճ̵�����
	public boolean updateDB(String tableName, String toSetColumn,
			String newValue, List<Integer> idList) {
		for (int id : idList) {
			updateDB(tableName, toSetColumn, newValue, "id", id + "");
		}
		return true;
	}

	// ɾ��[tableName]���idΪ[id]�ļ�¼
	public boolean deleteRecord(String tableName, String id) {
		// ������ճ̱���ռǱ�ִ��αɾ������status�ֶ���Ϊ-1�����Ա�֮���ܹ��ָ�
		if (tableName.equalsIgnoreCase(Schedule.TABLE_NAME)) {
			updateDB(Schedule.TABLE_NAME, "status", Schedule.STATUS_DELETED
					+ "", "id", id + "");
		} else if (tableName.equalsIgnoreCase(Journal.TABLE_NAME)) {
			updateDB(Journal.TABLE_NAME, "status", Journal.STATUS_DELETED + "",
					"id", id + "");
		} else if (tableName.equalsIgnoreCase(SpecialSchedule.TABLE_NAME)) {
			updateDB(SpecialSchedule.TABLE_NAME, "status",
					SpecialSchedule.STATUS_DELETED + "", "id", id + "");
		} else {
			db.execSQL("delete from " + tableName + " where id = ?",
					new String[] { id });
		}

		return true;
	}

	// ���ݻָ����ȴ�����ʹ�ã�
	public void recover(String sql, String[] values) {
		db.execSQL(sql, values);
	}

	/****************************************************************************************/

}
