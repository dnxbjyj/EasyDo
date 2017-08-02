package com.easydo.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.easydo.adapter.JournalLvAdapter;
import com.easydo.adapter.JournalLvItem;
import com.easydo.adapter.ScheduleLvAdapter;
import com.easydo.adapter.ScheduleLvItem;
import com.easydo.layout.AdvancedSearchJournalLayout;
import com.easydo.layout.BaseTitleLayout;
import com.easydo.layout.SearchBarLayout;
import com.easydo.layout.SearchBarLayout.SearchBarListener;
import com.easydo.model.Journal;
import com.easydo.model.Schedule;
import com.easydo.model.SystemConfig;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.InputMethodUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class JournalActivity extends BaseActivity implements OnClickListener,
		OnItemLongClickListener, OnItemClickListener {
	// 添加日程Intent的请求码
	public final static int CREATE_JOURNAL_REQUEST_CODE = 0;
	// 查看日程详细信息Intent的请求码
	public final static int JOURNAL_DETAIL_REQUEST_CODE = 1;

	// 判断是不是之前活动启动的当前活动
	private boolean isStartedByFormer = false;
	// 如果是之前活动启动的当前活动，那么传递日期
	private String formerDate = null;

	// 日志数据列表
	List<Journal> journals;
	// 日志数据item适配器实体类对象列表
	List<JournalLvItem> journalLvItems;
	// 当前数据库中处于正常状态的日志数
	private int journalNum;

	// 日志列表适配器
	private JournalLvAdapter journalLvAdapter;

	// 标题栏的控件
	private BaseTitleLayout titleLayout;
	private ImageButton titleLeftIb;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// 搜索栏（一开始是隐藏的）
	private SearchBarLayout searchBar;
	// 显示搜索结果数量的Tv
	private TextView searchResultNumTv;
	// 高级搜索结果对象
	private AdvancedSearchJournalLayout.AdvancedQueryCondition mQueryCondition;

	// 日志Lv
	private ListView journalLv;
	// 滚动位置信息
	private int journalLvPosition;

	// 空白Tv
	private TextView blankTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 初始化intent数据，看是不是其他活动启动的当前日志活动
		initIntentData();

		initUI();

		// 初始化日志数据
		if (isStartedByFormer) {
			initJournalData(formerDate);
		} else {
			initJournalData(null);
		}
		// 显示日志列表
		showJournalLv();
	}

	// 初始化intent数据，看是不是其他活动启动的当前日志活动
	private void initIntentData() {
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");
		if (date != null) {
			isStartedByFormer = true;
			formerDate = date;
		} else {
			isStartedByFormer = false;
		}
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_journal);

		titleLayout = (BaseTitleLayout) findViewById(R.id.title_layout);
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		if (isStartedByFormer) {
			titleLeftIb.setImageResource(R.drawable.back1_64);
			titleMiddleTv.setText(DateTimeUtil.getChineseDateString(formerDate)
					+ " 日志列表");
			titleMiddleTv.setTextSize(16);
			findViewById(R.id.page_bottom_layout).setVisibility(View.GONE);
		} else {
			titleLeftIb.setImageResource(R.drawable.search2_64);
			titleMiddleTv.setText("日志列表");
			titleMiddleTv.setTextSize(18);
		}
		titleRightIb.setImageResource(R.drawable.write2_64);
		titleLeftIb.setOnClickListener(this);
		titleRightIb.setOnClickListener(this);

		searchBar = (SearchBarLayout) findViewById(R.id.search_bar);
		searchBar.setHint("搜索日志");
		searchBar.setTag("");
		searchBar.setSearchBarListener(JournalActivity.this,
				new SearchBarListener() {

					@Override
					public void onInputChange(String newInput) {
						if (TextUtils.isEmpty(newInput)) {
							searchResultNumTv.setVisibility(View.GONE);
							blankTv.setText("可以输入日志的创建时间、内容等关键词进行搜索");
							blankTv.setVisibility(View.VISIBLE);
							journalLv.setVisibility(View.GONE);
						} else {
							searchJournal(newInput, null, null);
							addJournalNode();
							showJournalLv();
						}
						searchBar.setTag("");
					}

					@Override
					public void onFocusChange(boolean hasFocus) {

					}

					@Override
					public void onDictationClick() {
						DictationUtil.showDictationDialog(JournalActivity.this,
								new DictationListener() {

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

					}

					@Override
					public void onAdvancedClick() {
						searchBar.setQuery("");
						advancedSearchJournal();
					}
				});
		searchResultNumTv = (TextView) findViewById(R.id.search_result_num_tv);

		journalLv = (ListView) findViewById(R.id.journal_lv);

		journalLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					journalLvPosition = journalLv.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		blankTv = (TextView) findViewById(R.id.blank_tv);

		journalLv.setOnItemClickListener(this);
		journalLv.setOnItemLongClickListener(this);
	}

	// 打开SearchBar
	private void openSearchBar() {
		searchBar.setActive(true);
		titleLayout.setVisibility(View.GONE);

		blankTv.setText("可以输入日志的创建时间、内容等关键词进行搜索");
		blankTv.setVisibility(View.VISIBLE);
		journalLv.setVisibility(View.GONE);

		searchBar.setVisibility(View.VISIBLE);
	}

	// 关闭SearchBar
	private void cancelSearchBar() {
		searchResultNumTv.setVisibility(View.GONE);
		InputMethodUtil.closeInputMethod(JournalActivity.this);
		searchBar.setActive(false);
		searchBar.setVisibility(View.GONE);

		blankTv.setText("没有日志可显示，点右上角写一篇日志吧~");
		titleLayout.setVisibility(View.VISIBLE);

		InputMethodUtil.closeInputMethod(JournalActivity.this);

		initJournalData(null);
		showJournalLv();

	}

	// 日志的高级搜索
	private void advancedSearchJournal() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("日志高级搜索");

		final AdvancedSearchJournalLayout dialogView = new AdvancedSearchJournalLayout(
				JournalActivity.this, null);

		dialog.setView(dialogView);

		dialog.setPositiveButton("搜索", null);
		dialog.setNegativeButton("取消", null);

		final AlertDialog alertDialog = dialog.create();
		alertDialog.show();

		alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!dialogView.checkInput()) {
							return;
						}

						mQueryCondition = dialogView.getQueryCondition();

						searchJournal(mQueryCondition.getKeywords(),
								mQueryCondition.getCondition(),
								mQueryCondition.getValues());
						addJournalNode();
						showJournalLv();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_base_left_ib:
			if (isStartedByFormer) {
				finish();
			} else {
				openSearchBar();
			}
			break;
		case R.id.title_base_right_ib:
			Intent intent = new Intent(this, CreateJournalActivity.class);
			if (isStartedByFormer) {
				intent.putExtra("date", formerDate);
			}
			startActivityForResult(intent, CREATE_JOURNAL_REQUEST_CODE);
			break;
		default:
			break;
		}
	}

	// 初始化日志数据
	private void initJournalData(String date) {
		journals = new ArrayList<Journal>();
		journalLvItems = new ArrayList<JournalLvItem>();

		if (date == null) {
			searchJournal(null, " status != ?",
					new String[] { Journal.STATUS_DELETED + "" });
		} else {
			searchJournal(null, " status != ? and create_time like ?",
					new String[] { Journal.STATUS_DELETED + "",
							formerDate + "%" });
		}

		if (journalNum == 0) {
			return;
		}

		addJournalNode();
	}

	// 根据条件搜索日志，参数说明：
	// query,condition,values都为null就表示是查找所有的状态为正常的日志；否则表示是查询日志数据,query为全文查询关键词，
	// condition为查询的where语句，values表示where语句中占位符的值
	private void searchJournal(String query, String condition, String[] values) {
		// 先清空journalLvItems
		if (journalLvItems != null && !journalLvItems.isEmpty()) {
			journalLvItems.clear();
		}
		if (journals != null && !journals.isEmpty()) {
			journals.clear();
		}

		if (journalLvAdapter != null) {
			journalLvAdapter.notifyDataSetChanged();
		}

		// 查询所有日志（未删除的）
		if (query == null) {
			journals = easyDoDB.loadJournals(false, null, condition, values);
		}
		// 全文(+条件)搜索
		else {
			journals = easyDoDB.loadJournals(true, query, condition, values);
		}

		final int size = journals.size();
		journalNum = size;
		if (size == 0) {
			return;
		}

		Collections.sort(journals);
	}

	// 往JournalLvItems列表里添加数据（调用时已保证日志数量大于0）
	private void addJournalNode() {
		for (int i = 0; i < journalNum; i++) {
			Journal journal = journals.get(i);
			int id = journal.getId();
			String createTime = journal.getCreateTime().substring(0, 16);
			String content = journal.getContent();
			JournalLvItem item = new JournalLvItem(id, createTime, content);

			journalLvItems.add(item);
		}

	}

	// 显示日志列表
	private void showJournalLv() {
		// 如果数据库中没有日志可显示，则显示提示信息
		if (journalNum == 0) {
			searchResultNumTv.setVisibility(View.GONE);
			// 搜索栏正在激活状态
			if (searchBar.isActive()) {
				blankTv.setText("抱歉，没有找到满足条件的日志");
			} else {
				blankTv.setText("没有日志可显示，点右上角写一篇日志吧~");
			}
			blankTv.setVisibility(View.VISIBLE);
			journalLv.setVisibility(View.GONE);
		} else {
			blankTv.setVisibility(View.GONE);
			journalLv.setVisibility(View.VISIBLE);

			journalLvAdapter = new JournalLvAdapter(this,
					R.layout.journal_lv_item, journalLvItems);
			journalLv.setAdapter(journalLvAdapter);

			if (searchBar.isActive()) {
				searchResultNumTv.setVisibility(View.VISIBLE);
				searchResultNumTv.setText("一共找到" + journals.size() + "篇日志");
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int clickedJournalId = journalLvItems.get(position).getId();
		Intent intent = new Intent(JournalActivity.this,
				JournalDetailActivity.class);
		intent.putExtra("journal_id", clickedJournalId);
		startActivityForResult(intent, JOURNAL_DETAIL_REQUEST_CODE);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// 当前长按的item的日志id
		final int itemJournalId = journalLvItems.get(position).getId();
		final JournalLvItem lvItem = journalLvItems.get(position);

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		// 长按item弹出的dialog的选项列表
		final String[] operation = new String[] { "删除", "复制日志内容" };
		dialog.setCancelable(true);
		// 设置dialog中选项的点击事件
		dialog.setItems(operation, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					easyDoDB.deleteRecord(Journal.TABLE_NAME, itemJournalId
							+ "");
					// 如果当前是高级搜索
					if (searchBar.isActive()
							&& searchBar.getTag().equals("advanced_search")) {
						searchJournal(mQueryCondition.getKeywords(),
								mQueryCondition.getCondition(),
								mQueryCondition.getValues());
						addJournalNode();
						showJournalLv();
					} else if (searchBar.isActive()) {
						if (TextUtils.isEmpty(searchBar.getQuery())) {
							blankTv.setText("可以输入日志的创建时间、内容等关键词进行搜索");
							blankTv.setVisibility(View.VISIBLE);
							journalLv.setVisibility(View.GONE);
						} else {
							searchJournal(searchBar.getQuery(), null, null);
							addJournalNode();
							showJournalLv();
						}
					} else {
						initJournalData(formerDate);
						showJournalLv();
					}
					break;
				case 1:
					String content = lvItem.getContent();
					// 把日志内容复制到系统剪贴板
					ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cbm.setText(content);
					ToastUtil.showShort(JournalActivity.this, "日志内容已经复制到剪贴板");
					break;
				default:
					break;
				}
			}
		});
		dialog.show();

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case CREATE_JOURNAL_REQUEST_CODE:
		case JOURNAL_DETAIL_REQUEST_CODE:
			if (searchBar.isActive()) {
				if (searchBar.getTag().equals("advanced_search")) {
					searchJournal(mQueryCondition.getKeywords(),
							mQueryCondition.getCondition(),
							mQueryCondition.getValues());
					addJournalNode();
					showJournalLv();
				} else if (TextUtils.isEmpty(searchBar.getQuery())) {
					blankTv.setText("可以输入日志的创建时间、内容等关键词进行搜索");
					blankTv.setVisibility(View.VISIBLE);
					journalLv.setVisibility(View.GONE);
				} else {
					searchJournal(searchBar.getQuery(), null, null);
					addJournalNode();
					showJournalLv();
				}
			} else {
				initJournalData(formerDate);
				showJournalLv();
			}
			journalLv.setSelection(journalLvPosition);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isStartedByFormer) {
				finish();
				return true;
			}

			if (searchBar.isActive()) {
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

}
