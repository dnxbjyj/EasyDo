package com.easydo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.easydo.adapter.ScheduleLvAdapter;
import com.easydo.adapter.ScheduleLvItem;
import com.easydo.db.EasyDoDB;
import com.easydo.layout.AdvancedSearchJournalLayout;
import com.easydo.layout.AdvancedSearchScheduleLayout;
import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.layout.SearchBarLayout;
import com.easydo.layout.SimplyCreateScheduleLayout;
import com.easydo.layout.WeekChooseBarLayout;
import com.easydo.layout.SearchBarLayout.SearchBarListener;
import com.easydo.layout.WeekChooseBarLayout.WeekChooseBarListener;
import com.easydo.model.Journal;
import com.easydo.model.Schedule;
import com.easydo.model.SystemConfig;
import com.easydo.receiver.ScheduleAlarmReceiver;
import com.easydo.util.AlarmManagerUtil;
import com.easydo.util.BackupUtil;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.InputMethodUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.easydo.util.TypefaceUtil;
import com.jiayongji.easydo.R;

import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.TextUtils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

public class ScheduleActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnItemLongClickListener {
	// ����ճ�Intent��������
	public final static int CREATE_SCHEDULE_REQUEST_CODE = 0;
	// �鿴�ճ���ϸ��ϢIntent��������
	public final static int SCHEDULE_DETAIL_REQUEST_CODE = 1;
	// ϵͳ����ҳ�淵�ص�������
	public final static int SYSTEM_CONFIG_REQUEST_CODE = 2;

	// �ճ������б�
	List<Schedule> schedules;
	// �ճ�����item������ʵ��������б�
	List<ScheduleLvItem> scheduleLvItems;
	// ��ǰ���ݿ���δ��ɺ�����ɵ��ճ���
	private int scheduleNum;

	// �ճ��б�������
	private ScheduleLvAdapter scheduleLvAdapter;

	// û�п���ʾ���ճ�ʱ����ʾ��Ϣ
	private TextView blankTv;

	// ���������岼��
	private LinearLayout baseTitleLl;
	// ���������ĵ����������˵��İ�ť
	private ImageButton leftDrawerIb;
	// �������м���Զ���Tv
	private TextView titleMiddleCustomTv;
	// �������м����ֱ�ָ���
	private View titleDividerV;
	// �������м��Tv
	private TextView titleMiddleTv;
	// ��ѡ����
	private WeekChooseBarLayout weekChooseBar;
	// �������Ҳ�ӺŰ�ť
	private ImageButton titleRightIb;

	// ����˵����常����
	private LinearLayout scheduleDisplayDrawerLl;
	// ����˵�
	private DrawerLayout drawerLayout;

	TextView drawerTopTv;
	TypefaceUtil mTypeface;

	// ����˵��е��ճ���ʾ��ͼѡ��ť��������ͼ
	private TextView setScheduleShowWayMonthlyTv;
	// ����˵��е��ճ���ʾ��ͼѡ��ť��������ͼ
	private TextView setScheduleShowWayWeeklyTv;
	// ����˵��е��ճ���ʾ��ͼѡ��ť��������ͼ
	private TextView setScheduleShowWayDailyTv;

	// ����˵�����ͳ����
	private TableRow dataStatisticsTr;
	// ����˵�ϵͳ������
	private TableRow systemConfigTr;

	// ������
	private SearchBarLayout searchBar;
	// ��ʾ�������������Tv
	private TextView searchResultNumTv;
	// �߼�������������
	AdvancedSearchJournalLayout.AdvancedQueryCondition mAdvancedQueryCondition;

	// �ճ�������ʾ��ListView
	private ListView schedulesLv;
	// ����λ����Ϣ
	private int schedulesLvPosition;

	// �ճ���ʾ��ʽ
	private int scheduleShowWay;
	// ����ͼ��ѡ������£���ʽ��"2016-04"
	private String selectedYearMonth;
	// ����ͼ��ѡ��������գ���ʽ��"2016-04-01"
	private String selectedWeekDate;
	String firstDayOfChooseWeek;
	String lastDayOfChooseWeek;
	// ����ͼ��ѡ��������գ���ʽ��"2016-04-01"
	private String selectedDate;

	// ��ǰ�ճ�ListView��Ҫ��λ��λ�ã�Ĭ��Ϊ0
	private int curLvIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʼ���ճ���ʾ��ʽ��һЩ����
		initScheduleConfig();

		// ��ʼ������
		initUI();

		// ��ʼ���ճ�����
		initScheduleData();
		showScheduleLv();

	}

	// ��ʼ���ճ���ʾ��ʽ��һЩ����
	public void initScheduleConfig() {
		systemConfig = easyDoDB.loadSystemConfig(null, null);

		scheduleShowWay = systemConfig.getScheduleShowWay();
		selectedYearMonth = DateTimeUtil.getCurrentMonthString();
		selectedWeekDate = DateTimeUtil.getCurrentDateString();
		selectedDate = DateTimeUtil.getCurrentDateString();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_schedule);

		/**
		 * ������
		 */
		baseTitleLl = (LinearLayout) findViewById(R.id.base_title_ll);
		leftDrawerIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleCustomTv = (TextView) findViewById(R.id.title_base_middle_custom_tv);
		titleDividerV = findViewById(R.id.title_base_divider_v);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		/**
		 * ������-��ѡ����
		 */
		weekChooseBar = (WeekChooseBarLayout) findViewById(R.id.week_choose_bar);
		weekChooseBar.setOnChooseBarListener(ScheduleActivity.this,
				new WeekChooseBarListener() {

					@Override
					public void onPlusClick() {
						Calendar curCalendar = DateTimeUtil
								.getCurrentCalendar();
						if (!DateTimeUtil.isAtSameWeek(curCalendar,
								weekChooseBar.getmCurCalendar(),
								Calendar.MONDAY)) {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.TitleGray));
						} else {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.White));
						}
						initScheduleData();
						showScheduleLv();
					}

					@Override
					public void onMinusClick() {
						Calendar curCalendar = DateTimeUtil
								.getCurrentCalendar();
						if (!DateTimeUtil.isAtSameWeek(curCalendar,
								weekChooseBar.getmCurCalendar(),
								Calendar.MONDAY)) {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.TitleGray));
						} else {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.White));
						}
						initScheduleData();
						showScheduleLv();
					}

					@Override
					public void onDisplaySetDateClick() {
						Calendar curCalendar = DateTimeUtil
								.getCurrentCalendar();
						if (!DateTimeUtil.isAtSameWeek(curCalendar,
								weekChooseBar.getmCurCalendar(),
								Calendar.MONDAY)) {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.TitleGray));
						} else {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.White));
						}
						initScheduleData();
						showScheduleLv();
					}
				});

		/**
		 * ����˵�
		 */
		scheduleDisplayDrawerLl = (LinearLayout) findViewById(R.id.schedule_display_drawer_ll);
		drawerLayout = (DrawerLayout) findViewById(R.id.schedule_display_dl);
		// ������˵�����ʱ�����·��Ĳ��ֵĵ�����¼�
		drawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int arg0) {

			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {

			}

			@Override
			public void onDrawerOpened(View arg0) {
				arg0.setClickable(true);
			}

			@Override
			public void onDrawerClosed(View arg0) {

			}
		});

		setScheduleShowWayMonthlyTv = (TextView) findViewById(R.id.drawer_set_schedule_show_way_monthly_tv);
		setScheduleShowWayWeeklyTv = (TextView) findViewById(R.id.drawer_set_schedule_show_way_weekly_tv);
		setScheduleShowWayDailyTv = (TextView) findViewById(R.id.drawer_set_schedule_show_way_daily_tv);

		// ϵͳ������
		systemConfigTr = (TableRow) findViewById(R.id.system_config_tr);
		systemConfigTr.setOnClickListener(this);

		// ����ͳ����
		dataStatisticsTr = (TableRow) findViewById(R.id.data_statistics_tr);
		dataStatisticsTr.setOnClickListener(this);

		drawerTopTv = (TextView) findViewById(R.id.schedule_display_drawer_top_tv);
		mTypeface = new TypefaceUtil(ScheduleActivity.this, "fonts/Jinglei.ttf");
		mTypeface.setTypeface(drawerTopTv, true);

		/**
		 * �������ĳ�ʼ��
		 * 
		 */
		searchBar = (SearchBarLayout) findViewById(R.id.search_bar);
		// ������ʾ����
		searchBar.setHint("�����ճ�");
		searchBar.setTag("");
		searchBar.setSearchBarListener(ScheduleActivity.this,
				new SearchBarListener() {

					@Override
					public void onInputChange(String newInput) {
						if (TextUtils.isEmpty(newInput)) {
							searchResultNumTv.setVisibility(View.GONE);
							blankTv.setText("���������ճ̰���������ؼ��ʽ��������������ճ����ݡ���ע�ȵĹؼ���");
							blankTv.setVisibility(View.VISIBLE);
							schedulesLv.setVisibility(View.GONE);
						} else {
							searchSchedule(newInput, null, null);
							addNode();
							showScheduleLv();
						}
						searchBar.setTag("");
					}

					@Override
					public void onFocusChange(boolean hasFocus) {

					}

					@Override
					public void onDictationClick() {
						DictationUtil.showDictationDialog(
								ScheduleActivity.this, new DictationListener() {

									@Override
									public void onDictationListener(
											String dictationResultStr) {
										searchBar.setQuery(dictationResultStr);
									}
								});
						searchBar.setTag("");
					}

					@Override
					public void onCancelClick() {
						cancelSearchBar();
						searchBar.setTag("");
					}

					@Override
					public void onBlankClick() {
						initSearchBar();
					}

					@Override
					public void onAdvancedClick() {
						searchBar.setQuery("");
						advancedSearchSchedule();
					}
				});
		searchResultNumTv = (TextView) findViewById(R.id.search_result_num_tv);

		/**
		 * �հ�Tv����ʾ�ճ̵�Lv
		 */
		blankTv = (TextView) findViewById(R.id.blank_tv);
		schedulesLv = (ListView) findViewById(R.id.schedule_display_schedules_lv);

		// schedulesLv�Ĺ����¼������ڼ�¼�ͻָ�����λ��
		schedulesLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// ���������ʱ���浱ǰ��������λ��
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					schedulesLvPosition = schedulesLv.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		// ���Ͻǳ��밴ť�ĵ���¼�����������˵�
		leftDrawerIb.setOnClickListener(this);

		// ��ʼ���������м���Զ���Tv
		titleMiddleCustomTv.setVisibility(View.VISIBLE);
		titleDividerV.setVisibility(View.VISIBLE);

		// ��ʼ���������м��Tv���Զ���Tv;��ʼ��drawer���ճ���ʾ��ʽ��ť
		switch (scheduleShowWay) {
		case SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW:
			titleMiddleTv.setText(selectedYearMonth + "  " + ">");
			titleMiddleCustomTv.setText("����");

			setShowWayButtonActive(R.id.drawer_set_schedule_show_way_monthly_tv);
			break;
		case SystemConfig.SCHEDULE_SHOW_WAY_WEEK_VIEW:
			titleMiddleCustomTv.setText("����");

			titleMiddleTv.setVisibility(View.GONE);
			findViewById(R.id.title_base_expand_ll).setVisibility(View.VISIBLE);

			setShowWayButtonActive(R.id.drawer_set_schedule_show_way_weekly_tv);
			break;
		case SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW:
			titleMiddleTv.setText(selectedDate + "  " + ">");
			titleMiddleCustomTv.setText("����");

			setShowWayButtonActive(R.id.drawer_set_schedule_show_way_daily_tv);
			break;
		default:
			break;
		}

		// �ճ���ʾ��ͼѡ��ť�ĵ���¼�
		setScheduleShowWayMonthlyTv.setOnClickListener(this);
		setScheduleShowWayDailyTv.setOnClickListener(this);
		setScheduleShowWayWeeklyTv.setOnClickListener(this);

		titleMiddleCustomTv.setOnClickListener(this);
		titleMiddleTv.setOnClickListener(this);

		// ���Ϸ��ӺŰ�ť����¼�
		titleRightIb.setOnClickListener(this);

	}

	// ����Drawer���ճ���ʾ��ʽѡ��ť�ļ���״̬
	private void setShowWayButtonActive(int activieButtonId) {
		switch (activieButtonId) {
		case R.id.drawer_set_schedule_show_way_monthly_tv:
			setScheduleShowWayMonthlyTv
					.setBackgroundResource(R.drawable.schedule_show_way2);
			setScheduleShowWayMonthlyTv.setTextColor(getResources().getColor(
					R.drawable.White));

			setScheduleShowWayWeeklyTv
					.setBackgroundResource(R.drawable.schedule_show_way1);
			setScheduleShowWayWeeklyTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));

			setScheduleShowWayDailyTv
					.setBackgroundResource(R.drawable.schedule_show_way1);
			setScheduleShowWayDailyTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));
			break;
		case R.id.drawer_set_schedule_show_way_weekly_tv:
			setScheduleShowWayWeeklyTv
					.setBackgroundResource(R.drawable.schedule_show_way2);
			setScheduleShowWayWeeklyTv.setTextColor(getResources().getColor(
					R.drawable.White));

			setScheduleShowWayMonthlyTv
					.setBackgroundResource(R.drawable.schedule_show_way1);
			setScheduleShowWayMonthlyTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));

			setScheduleShowWayDailyTv
					.setBackgroundResource(R.drawable.schedule_show_way1);
			setScheduleShowWayDailyTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));
			break;
		case R.id.drawer_set_schedule_show_way_daily_tv:
			setScheduleShowWayDailyTv
					.setBackgroundResource(R.drawable.schedule_show_way2);
			setScheduleShowWayDailyTv.setTextColor(getResources().getColor(
					R.drawable.White));

			setScheduleShowWayWeeklyTv
					.setBackgroundResource(R.drawable.schedule_show_way1);
			setScheduleShowWayWeeklyTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));

			setScheduleShowWayMonthlyTv
					.setBackgroundResource(R.drawable.schedule_show_way1);
			setScheduleShowWayMonthlyTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));
			break;
		default:
			break;
		}
	}

	// �ճ���������
	private void searchSchedule(String query, String condition, String[] values) {
		if (schedules != null && !schedules.isEmpty()) {
			schedules.clear();
		}

		if (scheduleLvItems != null && !scheduleLvItems.isEmpty()) {
			scheduleLvItems.clear();
		}

		if (scheduleLvAdapter != null) {
			scheduleLvAdapter.notifyDataSetChanged();
		}

		curLvIndex = 0;

		easyDoDB = EasyDoDB.getInstance(ScheduleActivity.this);

		if (query == null) {
			if (searchBar.isActive()) {
				schedules = easyDoDB.loadSchedules(false, null, condition,
						values);
			} else {
				// ���ݵ�ǰ���ճ���ʾ�ķ�ʽ��ͬ����ȡ��ͬ������
				switch (scheduleShowWay) {
				// ����ͼ
				case SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW:
					schedules = easyDoDB.loadSchedules(false, null,
							" status != ? and start_time like ?", new String[] {
									Schedule.STATUS_DELETED + "",
									selectedYearMonth + "%" });
					break;
				case SystemConfig.SCHEDULE_SHOW_WAY_WEEK_VIEW:
					firstDayOfChooseWeek = DateTimeUtil
							.CalendarToString(DateTimeUtil
									.getDateBeginTimeCalendar(weekChooseBar
											.getFirstDayCalendarOfWeek()));
					lastDayOfChooseWeek = DateTimeUtil
							.CalendarToString(DateTimeUtil
									.getDateEndTimeCalendar(weekChooseBar
											.getLastDayCalendarOfWeek()));
					schedules = easyDoDB
							.loadSchedules(
									false,
									null,
									" status != ? and start_time >= ? and start_time <= ?",
									new String[] {
											Schedule.STATUS_DELETED + "",
											firstDayOfChooseWeek,
											lastDayOfChooseWeek });
					break;
				// ����ͼ
				case SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW:
					schedules = easyDoDB.loadSchedules(false, null,
							" status != ? and start_time like ?", new String[] {
									Schedule.STATUS_DELETED + "",
									selectedDate + "%" });
					break;
				default:
					break;
				}
			}
		}
		// ȫ��(+����)����
		else {
			if (TextUtils.isEmpty(query)) {
				return;
			}
			schedules = easyDoDB.loadSchedules(true, query, condition, values);
		}

		final int size = schedules.size();
		scheduleNum = size;

		if (size == 0) {
			return;
		}
		// ��schedules����
		Collections.sort(schedules);
	}

	// ��ʼ���Ͳ�ѯ�ճ�����
	// query,condition,values��Ϊnull�ͱ�ʾ�ǳ�ʼ�����ݣ������ʾ�ǲ�ѯ�ճ�����,queryΪȫ�Ĳ�ѯ�ؼ��ʣ�
	// conditionΪ��ѯ��where��䣬values��ʾwhere�����ռλ����ֵ
	public void initScheduleData() {
		scheduleLvItems = new ArrayList<ScheduleLvItem>();
		searchSchedule(null, null, null);
		addNode();
	}

	// ������
	private void addNode() {
		if (scheduleNum == 0) {
			return;
		}

		// ����ǰһ���������ڣ������һ����������ֵ��֮��ͬ��������scheduleLvItems�б���������ڽ�㣬������ճ̽�����ݣ�
		// ����ֱ������ճ̽�����ݡ����ڰ�����ճ̷��飨��������ͼ�У�
		String formerDateString = schedules.get(0).getStartTime()
				.substring(0, 10);

		// ����scheduleLvItems����µ����ڽ�㣬������ճ̽��
		addDateNode(schedules.get(0));
		addScheduleNode(schedules.get(0));

		for (int i = 1; i < scheduleNum; i++) {
			Schedule schedule = schedules.get(i);

			if (schedule.getStartTime().substring(0, 10)
					.equals(formerDateString)) {
				// ֱ����scheduleLvItems����ճ̽��
				addScheduleNode(schedule);
			} else {
				// ����scheduleLvItems����µ����ڽ�㣬������ճ̽��
				addDateNode(schedule);
				addScheduleNode(schedule);
				formerDateString = schedule.getStartTime().substring(0, 10);
			}
		}
	}

	// ��scheduleLvItems����ճ̽��
	private void addScheduleNode(Schedule schedule) {
		int id = schedule.getId();
		int type = ScheduleLvItem.TYPE_SCHEDULE_NODE;
		String time = schedule.getStartTime().substring(11, 16);
		int status = schedule.getStatus();
		boolean isDone;
		int nodeImageId;
		if (status == Schedule.STATUS_FINISHED) {
			isDone = true;
			nodeImageId = R.drawable.node_image_schedule2;
		} else {
			isDone = false;
			nodeImageId = R.drawable.node_image_schedule1;
		}
		String content = schedule.getContent();
		int tag = schedule.getTag();

		ScheduleLvItem item = new ScheduleLvItem(id, type, time, nodeImageId,
				isDone, content, tag);

		scheduleLvItems.add(item);
	}

	// ��scheduleLvItems������ڽ��
	private void addDateNode(Schedule schedule) {
		int type = ScheduleLvItem.TYPE_DATE_NODE;
		int nodeImageId = R.drawable.node_image_date;
		int dayOfMonth = DateTimeUtil.getDayOfMonth(schedule.getStartTime());
		String dateString = DateTimeUtil.getShortDateTimeOfWeek(schedule
				.getStartTime());
		String date = schedule.getStartTime().substring(0, 10);

		ScheduleLvItem item = new ScheduleLvItem(type, nodeImageId, dayOfMonth,
				dateString, date);

		scheduleLvItems.add(item);

		// ��������ڽ������ڵ�������ͬ����ô��Ҫ��ListView��λ������
		if (date.equals(DateTimeUtil.getCurrentDateString())) {
			if (!searchBar.isActive()) {
				curLvIndex = scheduleLvItems.size() - 1;
			}
		}

	}

	// ��ʾ�ճ�Lv
	public void showScheduleLv() {
		// ������ݿ��е�ǰû�п���ʾ���ճ̣�����ʾ��ʾ��Ϣ
		if (scheduleNum == 0) {
			searchResultNumTv.setVisibility(View.GONE);
			if (searchBar.isActive()) {
				blankTv.setText("��Ǹ��û���ҵ������������ճ�����");
			} else {
				blankTv.setText("��ǰʱ��û���ճ̿���ʾ��������Ͻ��½�һ���ճ̰�~");
			}
			blankTv.setVisibility(View.VISIBLE);
			schedulesLv.setVisibility(View.GONE);
		} else {
			blankTv.setVisibility(View.GONE);
			schedulesLv.setVisibility(View.VISIBLE);

			scheduleLvAdapter = new ScheduleLvAdapter(ScheduleActivity.this,
					R.layout.schedule_lv_item, scheduleLvItems);
			schedulesLv.setAdapter(scheduleLvAdapter);

			schedulesLv.setSelection(curLvIndex);

			if (searchBar.isActive()) {
				searchResultNumTv.setVisibility(View.VISIBLE);
				searchResultNumTv.setText("һ���ҵ�" + schedules.size() + "���ճ�");
			}

			// item�ĵ���¼�
			schedulesLv.setOnItemClickListener(this);
			// item�ĳ����¼�
			schedulesLv.setOnItemLongClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.title_base_left_ib:
			// �򿪳���˵�
			drawerLayout.openDrawer(Gravity.START);
			break;
		case R.id.title_base_middle_custom_tv:
			titleMiddleCustomTv.setTextColor(getResources().getColor(
					R.drawable.White));
			switch (scheduleShowWay) {
			case SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW:
				selectedYearMonth = DateTimeUtil.getCurrentMonthString();
				titleMiddleTv.setText(selectedYearMonth + "  " + ">");
				break;
			case SystemConfig.SCHEDULE_SHOW_WAY_WEEK_VIEW:
				selectedWeekDate = DateTimeUtil.getCurrentDateString();
				weekChooseBar
						.setmCurCalendar(DateTimeUtil.getCurrentCalendar());
				weekChooseBar.setDisplayText(DateTimeUtil.getCurrentCalendar());
				break;
			case SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW:
				selectedDate = DateTimeUtil.getCurrentDateString();
				titleMiddleTv.setText(selectedDate + "  " + ">");
				break;
			default:
				break;
			}

			initScheduleData();
			showScheduleLv();
			break;
		case R.id.title_base_middle_tv:
			DatePickerFragment datePickerFrg;
			switch (scheduleShowWay) {
			case SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW:
				datePickerFrg = new DatePickerFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						final Calendar c = DateTimeUtil
								.StringToGregorianCalendar(selectedYearMonth
										+ "-01 00:00:00");
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH);
						int day = c.get(Calendar.DAY_OF_MONTH);
						return new EasyDoDatePickerDialog(getActivity(), this,
								year, month, day, "ѡ���·�",
								EasyDoDatePickerDialog.DIALOG_TYPE_YEAR_MONTH);
					}

					public void onDateSet(android.widget.DatePicker view,
							int year, int month, int day) {
						selectedYearMonth = DateTimeUtil.getDateString(year,
								month);
						titleMiddleTv.setText(selectedYearMonth + "  >");

						String nowMonth = DateTimeUtil.getCurrentMonthString();
						// ���ѡ����·ݲ��Ǳ��£��Ͱѡ����¡������ֱ��
						if (!nowMonth.equals(selectedYearMonth)) {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.TitleGray));
						} else {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.White));
						}

						initScheduleData();
						showScheduleLv();
					};
				};
				datePickerFrg.show(getFragmentManager(), "datePickerFrg");

				break;
			case SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW:
				datePickerFrg = new DatePickerFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						final Calendar c = DateTimeUtil
								.StringToGregorianCalendar(selectedDate
										+ " 00:00:00");
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH);
						int day = c.get(Calendar.DAY_OF_MONTH);
						return new EasyDoDatePickerDialog(getActivity(), this,
								year, month, day, "ѡ������",
								EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
					}

					public void onDateSet(android.widget.DatePicker view,
							int year, int month, int day) {
						selectedDate = DateTimeUtil.getDateString(year, month,
								day);
						titleMiddleTv.setText(selectedDate + "  >");

						String nowDate = DateTimeUtil.getCurrentDateString();
						// ���ѡ������ڲ��ǽ��죬�Ͱѡ����족�����ֱ��
						if (!nowDate.equals(selectedDate)) {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.TitleGray));
						} else {
							titleMiddleCustomTv.setTextColor(getResources()
									.getColor(R.drawable.White));
						}

						initScheduleData();
						showScheduleLv();
					};
				};
				datePickerFrg.show(getFragmentManager(), "datePickerFrg");
				break;
			default:
				break;
			}
			break;
		case R.id.title_base_right_ib:
			// ��ʾ�������ճ�dialog��ֻ�������ճ����ݼ��ɴ�����
			showSimplyCreateScheduleDialog();
			break;
		// drawer������ͼ�ճ���ʾ��ʽ��ť�ĵ���¼�
		case R.id.drawer_set_schedule_show_way_monthly_tv:
			if (scheduleShowWay != SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW) {
				setShowWayButtonActive(R.id.drawer_set_schedule_show_way_monthly_tv);

				easyDoDB.updateDB("SystemConfig", "schedule_show_way",
						SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW + "", "id",
						1 + "");

				titleMiddleTv.setVisibility(View.VISIBLE);
				findViewById(R.id.title_base_expand_ll)
						.setVisibility(View.GONE);

				titleMiddleTv.setText(selectedYearMonth + "  " + ">");
				titleMiddleCustomTv.setText("����");
				titleMiddleCustomTv.setTextColor(getResources().getColor(
						R.drawable.White));

				initScheduleConfig();
				initScheduleData();

				titleMiddleTv.setText(selectedYearMonth + "  " + ">");
				titleMiddleCustomTv.setText("����");
				titleMiddleCustomTv.setTextColor(getResources().getColor(
						R.drawable.White));

				showScheduleLv();

			}
			drawerLayout.closeDrawer(Gravity.START);
			break;
		case R.id.drawer_set_schedule_show_way_weekly_tv:
			if (scheduleShowWay != SystemConfig.SCHEDULE_SHOW_WAY_WEEK_VIEW) {

				setShowWayButtonActive(R.id.drawer_set_schedule_show_way_weekly_tv);

				easyDoDB.updateDB("SystemConfig", "schedule_show_way",
						SystemConfig.SCHEDULE_SHOW_WAY_WEEK_VIEW + "", "id",
						1 + "");

				titleMiddleTv.setVisibility(View.GONE);
				findViewById(R.id.title_base_expand_ll).setVisibility(
						View.VISIBLE);

				selectedWeekDate = DateTimeUtil.getCurrentDateString();
				weekChooseBar
						.setmCurCalendar(DateTimeUtil.getCurrentCalendar());
				weekChooseBar.setDisplayText(DateTimeUtil.getCurrentCalendar());

				initScheduleConfig();
				initScheduleData();

				titleMiddleCustomTv.setText("����");
				titleMiddleCustomTv.setTextColor(getResources().getColor(
						R.drawable.White));

				showScheduleLv();
			}
			drawerLayout.closeDrawer(Gravity.START);
			break;
		case R.id.drawer_set_schedule_show_way_daily_tv:
			if (scheduleShowWay != SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW) {
				setShowWayButtonActive(R.id.drawer_set_schedule_show_way_daily_tv);

				easyDoDB.updateDB("SystemConfig", "schedule_show_way",
						SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW + "", "id",
						1 + "");
				titleMiddleTv.setVisibility(View.VISIBLE);
				findViewById(R.id.title_base_expand_ll)
						.setVisibility(View.GONE);

				initScheduleConfig();
				initScheduleData();

				titleMiddleTv.setText(selectedDate + "  " + ">");
				titleMiddleCustomTv.setText("����");
				titleMiddleCustomTv.setTextColor(getResources().getColor(
						R.drawable.White));

				showScheduleLv();
			}

			drawerLayout.closeDrawer(Gravity.START);
			break;
		case R.id.data_statistics_tr:
			intent = new Intent(ScheduleActivity.this, StatisticsActivity.class);
			startActivity(intent);
			break;
		case R.id.system_config_tr:
			intent = new Intent(ScheduleActivity.this,
					SystemConfigActivity.class);
			startActivityForResult(intent, SYSTEM_CONFIG_REQUEST_CODE);
			break;
		default:
			break;
		}
	}

	// ��ʼ����������
	private void initSearchBar() {
		// ���γ���˵������Ʋ໬
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

		baseTitleLl.setVisibility(View.GONE);

		blankTv.setText("���������ճ̰���������ؼ��ʽ��������������ճ����ݡ���ע�ȵĹؼ���");
		blankTv.setVisibility(View.VISIBLE);
		schedulesLv.setVisibility(View.GONE);

	}

	// �˳���������
	private void cancelSearchBar() {
		searchResultNumTv.setVisibility(View.GONE);
		// �ָ�����˵������Ʋ໬
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

		drawerLayout.setEnabled(true);
		blankTv.setText("��ǰʱ��û���ճ̿���ʾ��������Ͻ��½�һ���ճ̰�~");
		baseTitleLl.setVisibility(View.VISIBLE);
		searchBar.setActive(false);

		// �ر����뷨����
		InputMethodUtil.closeInputMethod(ScheduleActivity.this);

		initScheduleData();
		showScheduleLv();
	}

	// ��ʾ�������ճ�dialog��ֻ�������ճ����ݼ��ɴ�����
	private void showSimplyCreateScheduleDialog() {
		// ��ǰѡ�������ʱ��
		String dateTime = "";
		Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (scheduleShowWay == SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW) {
			dateTime = selectedYearMonth + "-"
					+ DateTimeUtil.getDoubleNumString(day) + " "
					+ DateTimeUtil.getCurrentTimeString() + ":00";
		} else if (scheduleShowWay == SystemConfig.SCHEDULE_SHOW_WAY_WEEK_VIEW) {
			dateTime = DateTimeUtil.CalendarToString(weekChooseBar
					.getmCurCalendar());
		} else if (scheduleShowWay == SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW) {
			dateTime = selectedDate + " " + DateTimeUtil.getCurrentTimeString()
					+ ":00";
		}
		final String dateTimeData = dateTime;

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("�½��ճ�");

		final SimplyCreateScheduleLayout dialogView = new SimplyCreateScheduleLayout(
				ScheduleActivity.this, null);

		dialog.setView(dialogView);
		dialog.setPositiveButton("ȷ��", null);
		dialog.setNeutralButton("��ϸ����", null);
		dialog.setNegativeButton("ȡ��", null);

		// ��������������Ϊ�ˣ��������Ϊ���򲻹رնԻ���
		final AlertDialog alertDialog = dialog.create();
		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (dialogView.isContentEmpty()) {
							ToastUtil.showShort(ScheduleActivity.this,
									"�������ճ����ݣ�");
							return;
						}

						// ����Schedule����
						Schedule schedule = new Schedule();
						// ��ȡ��ǰ����ʹ�õ�scheduleBookId
						int scheduleBookId = systemConfig
								.getActivatedScheduleBookId();
						// ��ǰʱ�������-ʱ���ַ�����Ϊ�ճ̵Ĵ���ʱ��
						String createTime = dateTimeData;
						// �ճ̵�����(content)
						String content = dialogView.getContent();
						// �ճ̿�ʼ��ʱ��
						String startTime = dateTimeData;
						// ���ѷ�ʽ��������ƵĹ���alarmMode�պ���selectedAlarmModeIndex-1
						int alarmMode = Schedule.ALARM_MODE_OFF;

						// �ظ���ʽ�������ѷ�ʽͬ��
						int repeatMode = Schedule.REPEAT_MODE_OFF;

						// ����alarmMode��startTime����alarmTime
						String alarmTime = Schedule.calAlarmTime(alarmMode,
								startTime);
						// �ճ�״̬
						int status = Schedule.STATUS_UNFINISHED;
						// �ճ̱�ע
						String remark = "";
						// �ճ̱�ǩ,Ĭ��Ϊ�ճ�����
						int tag = Schedule.TAG_MATTERS;

						schedule.setScheduleBookId(scheduleBookId);
						schedule.setCreateTime(createTime);
						schedule.setContent(content);
						schedule.setStartTime(startTime);
						schedule.setAlarmMode(alarmMode);
						schedule.setRepeatMode(repeatMode);
						schedule.setAlarmTime(alarmTime);
						schedule.setStatus(status);
						schedule.setRemark(remark);
						schedule.setTag(tag);

						easyDoDB.saveSchedule(schedule);

						initScheduleData();
						showScheduleLv();
						alertDialog.dismiss();
					}
				});

		alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ScheduleActivity.this,
								CreateScheduleActivity.class);
						intent.putExtra("datetime_data", dateTimeData);
						intent.putExtra("is_content_empty",
								dialogView.isContentEmpty());
						intent.putExtra("content", dialogView.getContent());
						alertDialog.dismiss();
						startActivityForResult(intent,
								CREATE_SCHEDULE_REQUEST_CODE);
					}
				});

		alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
	}

	// �ճ̵ĸ߼�����
	private void advancedSearchSchedule() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("�ճ̸߼�����");

		final AdvancedSearchScheduleLayout dialogView = new AdvancedSearchScheduleLayout(
				ScheduleActivity.this, null);

		dialog.setView(dialogView);

		dialog.setPositiveButton("����", null);
		dialog.setNegativeButton("ȡ��", null);

		// ��������������Ϊ�ˣ��������Ϊ���򲻹رնԻ���
		final AlertDialog alertDialog = dialog.create();
		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!dialogView.checkInput()) {
							return;
						}

						mAdvancedQueryCondition = dialogView
								.getQueryCondition();

						searchSchedule(mAdvancedQueryCondition.getKeywords(),
								mAdvancedQueryCondition.getCondition(),
								mAdvancedQueryCondition.getValues());
						addNode();
						showScheduleLv();

						searchBar.setTag("advanced_search");

						alertDialog.dismiss();

					}
				});

		alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int clickedScheduleId = scheduleLvItems.get(position).getId();
		Schedule clickedSchedule = easyDoDB.loadScheduleById(clickedScheduleId);
		Intent intent = new Intent(ScheduleActivity.this,
				ScheduleDetailActivity.class);
		intent.putExtra("schedule_data", clickedSchedule);
		startActivityForResult(intent, SCHEDULE_DETAIL_REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CREATE_SCHEDULE_REQUEST_CODE:
			initScheduleData();
			showScheduleLv();
			break;
		case SCHEDULE_DETAIL_REQUEST_CODE:
			if (searchBar.isActive()) {
				if (searchBar.getTag().equals("advanced_search")) {
					searchSchedule(mAdvancedQueryCondition.getKeywords(),
							mAdvancedQueryCondition.getCondition(),
							mAdvancedQueryCondition.getValues());
					addNode();
					showScheduleLv();
				} else {
					searchSchedule(searchBar.getQuery(), null, null);
					addNode();
				}
			} else {
				initScheduleData();
			}

			showScheduleLv();
			schedulesLv.setSelection(schedulesLvPosition);
			break;
		case SYSTEM_CONFIG_REQUEST_CODE:
			freshSchedulesLv();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// ��ǰ������item���ճ�id
		final int itemScheduleId = scheduleLvItems.get(position).getId();
		final ScheduleLvItem lvItem = scheduleLvItems.get(position);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		// ����item������dialog��ѡ���б�
		final String[] operation;

		final Schedule curSchedule = easyDoDB.loadScheduleById(itemScheduleId);
		if (curSchedule.getRepeatMode() == Schedule.REPEAT_MODE_OFF) {
			operation = new String[] { "ɾ��", "�����ճ�����" };
		} else {
			operation = new String[] { "ɾ��", "�����ճ�����", "�ر��ظ�ģʽ",
					"ɾ��һ���ظ������������ճ�" };
		}

		// ����dialog��ѡ��ĵ���¼�
		dialog.setItems(operation, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					// �رո��ճ̵Ķ�ʱ���ѣ��������ģʽ��Ϊ�رյĻ���
					if (curSchedule.getAlarmMode() != Schedule.ALARM_MODE_OFF) {
						AlarmManagerUtil.cancelAlarmBroadcast(
								ScheduleActivity.this, itemScheduleId,
								ScheduleAlarmReceiver.class);
					}

					easyDoDB.deleteRecord("Schedule", itemScheduleId + "");
					ToastUtil.showShort(ScheduleActivity.this, "ɾ���ճ̳ɹ���");

					// �����ǰ�Ǹ߼�����
					if (searchBar.isActive()
							&& searchBar.getTag().equals("advanced_search")) {
						searchSchedule(mAdvancedQueryCondition.getKeywords(),
								mAdvancedQueryCondition.getCondition(),
								mAdvancedQueryCondition.getValues());
						addNode();
						showScheduleLv();
					} else if (searchBar.isActive()) {
						searchSchedule(searchBar.getQuery(), null, null);
						addNode();
					} else {
						initScheduleData();
					}

					showScheduleLv();

					break;
				case 1:
					String content = lvItem.getContent();
					// ���ճ����ݸ��Ƶ�ϵͳ������
					ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cbm.setText(content);
					ToastUtil.showShort(ScheduleActivity.this, "�ճ������Ѿ����Ƶ�������");
					break;
				case 2:
					dealWithFinishRepeat(curSchedule);
					if (searchBar.isActive()) {
						searchSchedule(searchBar.getQuery(), null, null);
						addNode();
					} else {
						initScheduleData();
					}

					showScheduleLv();
					break;
				case 3:
					// �����ǰ�ճ̵��ظ�ģʽ��Ϊ���ظ�����ôɾ��ͬһ���ظ������������ճ�
					List<Integer> toDeleteSchedulesIdList = easyDoDB
							.getSameCreateTimeScheduleId(curSchedule
									.getCreateTime());
					for (int id : toDeleteSchedulesIdList) {
						easyDoDB.deleteRecord(Schedule.TABLE_NAME, id + "");

						// �رո��ճ̵Ķ�ʱ���ѣ��������ģʽ��Ϊ�رյĻ���
						Schedule toDeleteSchedule = easyDoDB
								.loadScheduleById(id);
						if (toDeleteSchedule.getAlarmMode() != Schedule.ALARM_MODE_OFF) {
							AlarmManagerUtil.cancelAlarmBroadcast(
									ScheduleActivity.this, id,
									ScheduleAlarmReceiver.class);
						}
					}
					ToastUtil.showShort(ScheduleActivity.this,
							"��ǰ�ճ̼�һ���ظ������������ճ̶���ɾ��");

					// �����ǰ�Ǹ߼�����
					if (searchBar.isActive()
							&& searchBar.getTag().equals("advanced_search")) {
						searchSchedule(mAdvancedQueryCondition.getKeywords(),
								mAdvancedQueryCondition.getCondition(),
								mAdvancedQueryCondition.getValues());
						addNode();
						showScheduleLv();
					} else if (searchBar.isActive()) {
						searchSchedule(searchBar.getQuery(), null, null);
						addNode();
					} else {
						initScheduleData();
					}

					showScheduleLv();
					break;
				default:
					break;
				}
			}
		});

		dialog.setCancelable(true);
		dialog.show();

		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (drawerLayout.isDrawerOpen(Gravity.START)) {
				drawerLayout.closeDrawer(Gravity.START);
			} else if (searchBar.isActive()) {
				cancelSearchBar();
			} else if (System.currentTimeMillis() - mExitTime > 2000) {
				ToastUtil.showShort(this, "�ٰ�һ���˳�����");
				mExitTime = System.currentTimeMillis();
			} else {
				ActivityCollector.finishAll();
			}
			return true;
		}
		return true;
	}

	// ���ر��ظ�����ť����¼����������е�����ͬ�������ڵ��ճ̵Ĺر��ظ��Ĳ���
	// ɾ����Щ�ճ̵��п�ʼʱ���ڵ�ǰ�ճ̿�ʼʱ��֮��������ճ̣�������֮ǰ����Щ�ճ̵��ظ���ֹ�����趨Ϊ��ǰ�ճ̵Ŀ�ʼ����
	private void dealWithFinishRepeat(Schedule schedule) {
		// ͬһ���ظ��������ճ̵�id����������ͬ�Ĵ���ʱ��
		List<Integer> sameBatchSchedulesIdList = easyDoDB
				.getSameCreateTimeScheduleId(schedule.getCreateTime());

		// ��sameBatchSchedulesIdList�е�ǰschedule֮��������ճ̶�ɾ��
		int lastIndex = sameBatchSchedulesIdList.indexOf(schedule.getId());
		for (int i = lastIndex + 1; i < sameBatchSchedulesIdList.size(); i++) {
			int id = sameBatchSchedulesIdList.get(i);
			easyDoDB.deleteRecord(Schedule.TABLE_NAME, id + "");
		}
		// ��sameBatchSchedulesIdList�е�ǰschedule֮ǰ�������ճ̵��ظ���ֹ���ڶ�����Ϊ��ǰschedule�Ŀ�ʼ����
		for (int i = 0; i <= lastIndex; i++) {
			int id = sameBatchSchedulesIdList.get(i);
			easyDoDB.updateDB(Schedule.TABLE_NAME, "repeat_cut_off_date",
					schedule.getStartTime().substring(0, 10), "id", id + "");
		}

		ToastUtil
				.showLong(ScheduleActivity.this, "�ر��ظ��ɹ�����ǰ�ճ�֮���һ���ظ��������ճ���ɾ����");

	}

	// ˢ���ճ��б�
	public void freshSchedulesLv() {
		systemConfig = easyDoDB.loadSystemConfig(null, null);
		if (searchBar.isActive()) {
			if (searchBar.getTag().equals("advanced_search")) {
				searchSchedule(mAdvancedQueryCondition.getKeywords(),
						mAdvancedQueryCondition.getCondition(),
						mAdvancedQueryCondition.getValues());
				addNode();
				showScheduleLv();
			} else {
				searchSchedule(searchBar.getQuery(), null, null);
				addNode();
			}
		} else {
			initScheduleData();
		}

		showScheduleLv();
		schedulesLv.setSelection(schedulesLvPosition);
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	protected void onDestroy() {
		// ���жϵ�ǰ�ճ���ʾ��ʽ�ǲ�������ͼ�����������ĳ�����ͼ��ʽ
		SystemConfig config = easyDoDB.loadSystemConfig(null, null);
		if (config.getScheduleShowWay() != SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW) {
			// ��Ӧ���ճ�Ĭ��������ͼ��ʾ
			easyDoDB.updateDB(SystemConfig.TABLE_NAME, "schedule_show_way",
					SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW + "", "id", "1");
		}

		super.onDestroy();
	}

	public SearchBarLayout getSearchBar() {
		return searchBar;
	}

}
