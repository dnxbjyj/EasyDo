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
	// 添加日程Intent的请求码
	public final static int CREATE_SCHEDULE_REQUEST_CODE = 0;
	// 查看日程详细信息Intent的请求码
	public final static int SCHEDULE_DETAIL_REQUEST_CODE = 1;
	// 系统设置页面返回的请求码
	public final static int SYSTEM_CONFIG_REQUEST_CODE = 2;

	// 日程数据列表
	List<Schedule> schedules;
	// 日程数据item适配器实体类对象列表
	List<ScheduleLvItem> scheduleLvItems;
	// 当前数据库中未完成和已完成的日程数
	private int scheduleNum;

	// 日程列表适配器
	private ScheduleLvAdapter scheduleLvAdapter;

	// 没有可显示的日程时的提示信息
	private TextView blankTv;

	// 标题栏整体布局
	private LinearLayout baseTitleLl;
	// 标题栏左侧的点击弹出抽屉菜单的按钮
	private ImageButton leftDrawerIb;
	// 标题栏中间的自定义Tv
	private TextView titleMiddleCustomTv;
	// 标题栏中间的竖直分割线
	private View titleDividerV;
	// 标题栏中间的Tv
	private TextView titleMiddleTv;
	// 周选择栏
	private WeekChooseBarLayout weekChooseBar;
	// 标题栏右侧加号按钮
	private ImageButton titleRightIb;

	// 抽屉菜单整体父布局
	private LinearLayout scheduleDisplayDrawerLl;
	// 抽屉菜单
	private DrawerLayout drawerLayout;

	TextView drawerTopTv;
	TypefaceUtil mTypeface;

	// 抽屉菜单中的日程显示视图选择按钮——月视图
	private TextView setScheduleShowWayMonthlyTv;
	// 抽屉菜单中的日程显示视图选择按钮——周视图
	private TextView setScheduleShowWayWeeklyTv;
	// 抽屉菜单中的日程显示视图选择按钮——日视图
	private TextView setScheduleShowWayDailyTv;

	// 抽屉菜单数据统计栏
	private TableRow dataStatisticsTr;
	// 抽屉菜单系统设置栏
	private TableRow systemConfigTr;

	// 搜索栏
	private SearchBarLayout searchBar;
	// 显示搜索结果数量的Tv
	private TextView searchResultNumTv;
	// 高级搜素条件对象
	AdvancedSearchJournalLayout.AdvancedQueryCondition mAdvancedQueryCondition;

	// 日程数据显示的ListView
	private ListView schedulesLv;
	// 滚动位置信息
	private int schedulesLvPosition;

	// 日程显示方式
	private int scheduleShowWay;
	// 月视图中选择的年月，格式："2016-04"
	private String selectedYearMonth;
	// 周视图中选择的年月日，格式："2016-04-01"
	private String selectedWeekDate;
	String firstDayOfChooseWeek;
	String lastDayOfChooseWeek;
	// 日视图中选择的年月日，格式："2016-04-01"
	private String selectedDate;

	// 当前日程ListView需要定位的位置，默认为0
	private int curLvIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 初始化日程显示方式等一些设置
		initScheduleConfig();

		// 初始化界面
		initUI();

		// 初始化日程数据
		initScheduleData();
		showScheduleLv();

	}

	// 初始化日程显示方式等一些设置
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
		 * 标题栏
		 */
		baseTitleLl = (LinearLayout) findViewById(R.id.base_title_ll);
		leftDrawerIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleCustomTv = (TextView) findViewById(R.id.title_base_middle_custom_tv);
		titleDividerV = findViewById(R.id.title_base_divider_v);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		/**
		 * 标题栏-周选择栏
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
		 * 抽屉菜单
		 */
		scheduleDisplayDrawerLl = (LinearLayout) findViewById(R.id.schedule_display_drawer_ll);
		drawerLayout = (DrawerLayout) findViewById(R.id.schedule_display_dl);
		// 当抽屉菜单弹出时屏蔽下方的布局的点击等事件
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

		// 系统设置栏
		systemConfigTr = (TableRow) findViewById(R.id.system_config_tr);
		systemConfigTr.setOnClickListener(this);

		// 数据统计兰
		dataStatisticsTr = (TableRow) findViewById(R.id.data_statistics_tr);
		dataStatisticsTr.setOnClickListener(this);

		drawerTopTv = (TextView) findViewById(R.id.schedule_display_drawer_top_tv);
		mTypeface = new TypefaceUtil(ScheduleActivity.this, "fonts/Jinglei.ttf");
		mTypeface.setTypeface(drawerTopTv, true);

		/**
		 * 搜索栏的初始化
		 * 
		 */
		searchBar = (SearchBarLayout) findViewById(R.id.search_bar);
		// 设置提示文字
		searchBar.setHint("搜索日程");
		searchBar.setTag("");
		searchBar.setSearchBarListener(ScheduleActivity.this,
				new SearchBarListener() {

					@Override
					public void onInputChange(String newInput) {
						if (TextUtils.isEmpty(newInput)) {
							searchResultNumTv.setVisibility(View.GONE);
							blankTv.setText("可以输入日程包含的任意关键词进行搜索，比如日程内容、备注等的关键词");
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
		 * 空白Tv和显示日程的Lv
		 */
		blankTv = (TextView) findViewById(R.id.blank_tv);
		schedulesLv = (ListView) findViewById(R.id.schedule_display_schedules_lv);

		// schedulesLv的滚动事件，用于记录和恢复滚动位置
		schedulesLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 如果不滚动时保存当前滚动到的位置
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					schedulesLvPosition = schedulesLv.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		// 左上角抽屉按钮的点击事件，弹出抽屉菜单
		leftDrawerIb.setOnClickListener(this);

		// 初始化标题栏中间的自定义Tv
		titleMiddleCustomTv.setVisibility(View.VISIBLE);
		titleDividerV.setVisibility(View.VISIBLE);

		// 初始化标题栏中间的Tv、自定义Tv;初始化drawer中日程显示方式按钮
		switch (scheduleShowWay) {
		case SystemConfig.SCHEDULE_SHOW_WAY_MONTH_VIEW:
			titleMiddleTv.setText(selectedYearMonth + "  " + ">");
			titleMiddleCustomTv.setText("本月");

			setShowWayButtonActive(R.id.drawer_set_schedule_show_way_monthly_tv);
			break;
		case SystemConfig.SCHEDULE_SHOW_WAY_WEEK_VIEW:
			titleMiddleCustomTv.setText("本周");

			titleMiddleTv.setVisibility(View.GONE);
			findViewById(R.id.title_base_expand_ll).setVisibility(View.VISIBLE);

			setShowWayButtonActive(R.id.drawer_set_schedule_show_way_weekly_tv);
			break;
		case SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW:
			titleMiddleTv.setText(selectedDate + "  " + ">");
			titleMiddleCustomTv.setText("今日");

			setShowWayButtonActive(R.id.drawer_set_schedule_show_way_daily_tv);
			break;
		default:
			break;
		}

		// 日程显示视图选择按钮的点击事件
		setScheduleShowWayMonthlyTv.setOnClickListener(this);
		setScheduleShowWayDailyTv.setOnClickListener(this);
		setScheduleShowWayWeeklyTv.setOnClickListener(this);

		titleMiddleCustomTv.setOnClickListener(this);
		titleMiddleTv.setOnClickListener(this);

		// 右上方加号按钮点击事件
		titleRightIb.setOnClickListener(this);

	}

	// 设置Drawer中日程显示方式选择按钮的激活状态
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

	// 日程搜索函数
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
				// 根据当前的日程显示的方式不同，读取不同的数据
				switch (scheduleShowWay) {
				// 月视图
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
				// 日视图
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
		// 全文(+条件)搜索
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
		// 对schedules排序
		Collections.sort(schedules);
	}

	// 初始化和查询日程数据
	// query,condition,values都为null就表示是初始化数据；否则表示是查询日程数据,query为全文查询关键词，
	// condition为查询的where语句，values表示where语句中占位符的值
	public void initScheduleData() {
		scheduleLvItems = new ArrayList<ScheduleLvItem>();
		searchSchedule(null, null, null);
		addNode();
	}

	// 加入结点
	private void addNode() {
		if (scheduleNum == 0) {
			return;
		}

		// 缓存前一个结点的日期，如果下一个结点的日期值与之不同，则先向scheduleLvItems列表中添加日期结点，再添加日程结点数据；
		// 否则直接添加日程结点数据。用于按天给日程分组（用在月视图中）
		String formerDateString = schedules.get(0).getStartTime()
				.substring(0, 10);

		// 先往scheduleLvItems添加新的日期结点，再添加日程结点
		addDateNode(schedules.get(0));
		addScheduleNode(schedules.get(0));

		for (int i = 1; i < scheduleNum; i++) {
			Schedule schedule = schedules.get(i);

			if (schedule.getStartTime().substring(0, 10)
					.equals(formerDateString)) {
				// 直接往scheduleLvItems添加日程结点
				addScheduleNode(schedule);
			} else {
				// 先往scheduleLvItems添加新的日期结点，再添加日程结点
				addDateNode(schedule);
				addScheduleNode(schedule);
				formerDateString = schedule.getStartTime().substring(0, 10);
			}
		}
	}

	// 往scheduleLvItems添加日程结点
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

	// 往scheduleLvItems添加日期结点
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

		// 如果该日期结点和现在的日期相同，那么就要将ListView定位到这里
		if (date.equals(DateTimeUtil.getCurrentDateString())) {
			if (!searchBar.isActive()) {
				curLvIndex = scheduleLvItems.size() - 1;
			}
		}

	}

	// 显示日程Lv
	public void showScheduleLv() {
		// 如果数据库中当前没有可显示的日程，则显示提示信息
		if (scheduleNum == 0) {
			searchResultNumTv.setVisibility(View.GONE);
			if (searchBar.isActive()) {
				blankTv.setText("抱歉，没有找到满足条件的日程数据");
			} else {
				blankTv.setText("当前时间没有日程可显示，快点右上角新建一个日程吧~");
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
				searchResultNumTv.setText("一共找到" + schedules.size() + "条日程");
			}

			// item的点击事件
			schedulesLv.setOnItemClickListener(this);
			// item的长按事件
			schedulesLv.setOnItemLongClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.title_base_left_ib:
			// 打开抽屉菜单
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
								year, month, day, "选择月份",
								EasyDoDatePickerDialog.DIALOG_TYPE_YEAR_MONTH);
					}

					public void onDateSet(android.widget.DatePicker view,
							int year, int month, int day) {
						selectedYearMonth = DateTimeUtil.getDateString(year,
								month);
						titleMiddleTv.setText(selectedYearMonth + "  >");

						String nowMonth = DateTimeUtil.getCurrentMonthString();
						// 如果选择的月份不是本月，就把“本月”两个字变灰
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
								year, month, day, "选择日期",
								EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
					}

					public void onDateSet(android.widget.DatePicker view,
							int year, int month, int day) {
						selectedDate = DateTimeUtil.getDateString(year, month,
								day);
						titleMiddleTv.setText(selectedDate + "  >");

						String nowDate = DateTimeUtil.getCurrentDateString();
						// 如果选择的日期不是今天，就把“今天”两个字变灰
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
			// 显示创建简单日程dialog（只需输入日程内容即可创建）
			showSimplyCreateScheduleDialog();
			break;
		// drawer中月视图日程显示方式按钮的点击事件
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
				titleMiddleCustomTv.setText("本月");
				titleMiddleCustomTv.setTextColor(getResources().getColor(
						R.drawable.White));

				initScheduleConfig();
				initScheduleData();

				titleMiddleTv.setText(selectedYearMonth + "  " + ">");
				titleMiddleCustomTv.setText("本月");
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

				titleMiddleCustomTv.setText("本周");
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
				titleMiddleCustomTv.setText("今日");
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

	// 初始化搜索界面
	private void initSearchBar() {
		// 屏蔽抽屉菜单的手势侧滑
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

		baseTitleLl.setVisibility(View.GONE);

		blankTv.setText("可以输入日程包含的任意关键词进行搜索，比如日程内容、备注等的关键词");
		blankTv.setVisibility(View.VISIBLE);
		schedulesLv.setVisibility(View.GONE);

	}

	// 退出搜索界面
	private void cancelSearchBar() {
		searchResultNumTv.setVisibility(View.GONE);
		// 恢复抽屉菜单的手势侧滑
		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

		drawerLayout.setEnabled(true);
		blankTv.setText("当前时间没有日程可显示，快点右上角新建一个日程吧~");
		baseTitleLl.setVisibility(View.VISIBLE);
		searchBar.setActive(false);

		// 关闭输入法弹窗
		InputMethodUtil.closeInputMethod(ScheduleActivity.this);

		initScheduleData();
		showScheduleLv();
	}

	// 显示创建简单日程dialog（只需输入日程内容即可创建）
	private void showSimplyCreateScheduleDialog() {
		// 当前选择的日期时间
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
		dialog.setTitle("新建日程");

		final SimplyCreateScheduleLayout dialogView = new SimplyCreateScheduleLayout(
				ScheduleActivity.this, null);

		dialog.setView(dialogView);
		dialog.setPositiveButton("确定", null);
		dialog.setNeutralButton("详细设置", null);
		dialog.setNegativeButton("取消", null);

		// 这里这样处理是为了：如果内容为空则不关闭对话框
		final AlertDialog alertDialog = dialog.create();
		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (dialogView.isContentEmpty()) {
							ToastUtil.showShort(ScheduleActivity.this,
									"请输入日程内容！");
							return;
						}

						// 创建Schedule对象
						Schedule schedule = new Schedule();
						// 获取当前正在使用的scheduleBookId
						int scheduleBookId = systemConfig
								.getActivatedScheduleBookId();
						// 当前时间的日期-时间字符串作为日程的创建时间
						String createTime = dateTimeData;
						// 日程的内容(content)
						String content = dialogView.getContent();
						// 日程开始的时间
						String startTime = dateTimeData;
						// 提醒方式，按照设计的规则，alarmMode刚好是selectedAlarmModeIndex-1
						int alarmMode = Schedule.ALARM_MODE_OFF;

						// 重复方式，与提醒方式同理
						int repeatMode = Schedule.REPEAT_MODE_OFF;

						// 根据alarmMode和startTime计算alarmTime
						String alarmTime = Schedule.calAlarmTime(alarmMode,
								startTime);
						// 日程状态
						int status = Schedule.STATUS_UNFINISHED;
						// 日程备注
						String remark = "";
						// 日程标签,默认为日常杂事
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

	// 日程的高级搜索
	private void advancedSearchSchedule() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("日程高级搜索");

		final AdvancedSearchScheduleLayout dialogView = new AdvancedSearchScheduleLayout(
				ScheduleActivity.this, null);

		dialog.setView(dialogView);

		dialog.setPositiveButton("搜索", null);
		dialog.setNegativeButton("取消", null);

		// 这里这样处理是为了：如果内容为空则不关闭对话框
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
		// 当前长按的item的日程id
		final int itemScheduleId = scheduleLvItems.get(position).getId();
		final ScheduleLvItem lvItem = scheduleLvItems.get(position);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		// 长按item弹出的dialog的选项列表
		final String[] operation;

		final Schedule curSchedule = easyDoDB.loadScheduleById(itemScheduleId);
		if (curSchedule.getRepeatMode() == Schedule.REPEAT_MODE_OFF) {
			operation = new String[] { "删除", "复制日程内容" };
		} else {
			operation = new String[] { "删除", "复制日程内容", "关闭重复模式",
					"删除一起重复创建的所有日程" };
		}

		// 设置dialog中选项的点击事件
		dialog.setItems(operation, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					// 关闭该日程的定时提醒（如果提醒模式不为关闭的话）
					if (curSchedule.getAlarmMode() != Schedule.ALARM_MODE_OFF) {
						AlarmManagerUtil.cancelAlarmBroadcast(
								ScheduleActivity.this, itemScheduleId,
								ScheduleAlarmReceiver.class);
					}

					easyDoDB.deleteRecord("Schedule", itemScheduleId + "");
					ToastUtil.showShort(ScheduleActivity.this, "删除日程成功！");

					// 如果当前是高级搜索
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
					// 把日程内容复制到系统剪贴板
					ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cbm.setText(content);
					ToastUtil.showShort(ScheduleActivity.this, "日程内容已经复制到剪贴板");
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
					// 如果当前日程的重复模式不为不重复，那么删除同一批重复创建的所有日程
					List<Integer> toDeleteSchedulesIdList = easyDoDB
							.getSameCreateTimeScheduleId(curSchedule
									.getCreateTime());
					for (int id : toDeleteSchedulesIdList) {
						easyDoDB.deleteRecord(Schedule.TABLE_NAME, id + "");

						// 关闭该日程的定时提醒（如果提醒模式不为关闭的话）
						Schedule toDeleteSchedule = easyDoDB
								.loadScheduleById(id);
						if (toDeleteSchedule.getAlarmMode() != Schedule.ALARM_MODE_OFF) {
							AlarmManagerUtil.cancelAlarmBroadcast(
									ScheduleActivity.this, id,
									ScheduleAlarmReceiver.class);
						}
					}
					ToastUtil.showShort(ScheduleActivity.this,
							"当前日程及一起重复创建的所有日程都已删除");

					// 如果当前是高级搜索
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
				ToastUtil.showShort(this, "再按一次退出程序");
				mExitTime = System.currentTimeMillis();
			} else {
				ActivityCollector.finishAll();
			}
			return true;
		}
		return true;
	}

	// “关闭重复”按钮点击事件，处理所有的有相同创建日期的日程的关闭重复的操作
	// 删除这些日程当中开始时间在当前日程开始时间之后的所有日程，并把这之前的那些日程的重复截止日期设定为当前日程的开始日期
	private void dealWithFinishRepeat(Schedule schedule) {
		// 同一批重复创建的日程的id，它们有相同的创建时间
		List<Integer> sameBatchSchedulesIdList = easyDoDB
				.getSameCreateTimeScheduleId(schedule.getCreateTime());

		// 把sameBatchSchedulesIdList中当前schedule之后的所有日程都删掉
		int lastIndex = sameBatchSchedulesIdList.indexOf(schedule.getId());
		for (int i = lastIndex + 1; i < sameBatchSchedulesIdList.size(); i++) {
			int id = sameBatchSchedulesIdList.get(i);
			easyDoDB.deleteRecord(Schedule.TABLE_NAME, id + "");
		}
		// 把sameBatchSchedulesIdList中当前schedule之前的所有日程的重复截止日期都设置为当前schedule的开始日期
		for (int i = 0; i <= lastIndex; i++) {
			int id = sameBatchSchedulesIdList.get(i);
			easyDoDB.updateDB(Schedule.TABLE_NAME, "repeat_cut_off_date",
					schedule.getStartTime().substring(0, 10), "id", id + "");
		}

		ToastUtil
				.showLong(ScheduleActivity.this, "关闭重复成功，当前日程之后的一起重复创建的日程已删除！");

	}

	// 刷新日程列表
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
		// 先判断当前日程显示方式是不是日视图，如果不是则改成日视图方式
		SystemConfig config = easyDoDB.loadSystemConfig(null, null);
		if (config.getScheduleShowWay() != SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW) {
			// 打开应用日程默认是日视图显示
			easyDoDB.updateDB(SystemConfig.TABLE_NAME, "schedule_show_way",
					SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW + "", "id", "1");
		}

		super.onDestroy();
	}

	public SearchBarLayout getSearchBar() {
		return searchBar;
	}

}
