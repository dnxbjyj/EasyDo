package com.easydo.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.easydo.adapter.JournalLvAdapter;
import com.easydo.adapter.JournalLvItem;
import com.easydo.adapter.SpecialScheduleLvAdapter;
import com.easydo.adapter.SpecialScheduleLvItem;
import com.easydo.layout.BaseTitleLayout;
import com.easydo.layout.SearchBarLayout;
import com.easydo.layout.WeekChooseBarLayout;
import com.easydo.layout.SearchBarLayout.SearchBarListener;
import com.easydo.layout.WeekChooseBarLayout.WeekChooseBarListener;
import com.easydo.model.Journal;
import com.easydo.model.SpecialSchedule;
import com.easydo.model.SystemConfig;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.InputMethodUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

public class SpecialScheduleActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener, OnItemLongClickListener {
	// 活动请求码
	public static final int CREATE_SPECIAL_SCHEDULE_REQUEST_CODE = 0;
	public static final int DETAIL_SPECIAL_SCHEDULE_REQUEST_CODE = 1;

	// 当前选择的特殊日程种类,默认为-1（全部）
	private int selectedType = -1;

	// 标题栏的控件
	private BaseTitleLayout titleLayout;
	private ImageButton titleLeftIb;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// 搜索栏（一开始是隐藏的）
	private SearchBarLayout searchBar;
	// 显示搜索结果数量的Tv
	private TextView searchResultNumTv;

	// 特殊日程数据列表
	List<SpecialSchedule> specialSchedules;
	// 特殊日程数据item适配器实体类对象列表
	List<SpecialScheduleLvItem> specialScheduleLvItems;
	// 当前数据库中处于正常状态的特殊日程数
	private int specialScheduleNum;

	// 特殊日程列表适配器
	private SpecialScheduleLvAdapter specialScheduleLvAdapter;

	// 特殊日程Lv
	private ListView specialScheduleLv;
	// 滚动位置信息
	private int specialScheduleLvPosition;

	// 空白Tv
	private TextView blankTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();

		initLvData(-1);
		showLv();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_special_schedule);

		/**
		 * 标题栏
		 */
		titleLayout = (BaseTitleLayout) findViewById(R.id.title_layout);
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		titleLeftIb.setImageResource(R.drawable.search2_64);
		titleMiddleTv.setText("全部" + "  >");
		titleMiddleTv.setTextSize(18);

		titleRightIb.setImageResource(R.drawable.add3_64);
		titleLeftIb.setOnClickListener(this);
		titleMiddleTv.setOnClickListener(this);
		titleRightIb.setOnClickListener(this);

		/**
		 * ListView
		 */
		specialScheduleLv = (ListView) findViewById(R.id.special_schedule_lv);
		blankTv = (TextView) findViewById(R.id.blank_tv);

		specialScheduleLv.setOnItemClickListener(this);
		specialScheduleLv.setOnItemLongClickListener(this);
		specialScheduleLv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					specialScheduleLvPosition = specialScheduleLv
							.getFirstVisiblePosition();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

		searchBar = (SearchBarLayout) findViewById(R.id.search_bar);
		searchBar.setHint("搜索特殊日程");
		searchBar.setTag("");
		searchBar.hideAdvanced();
		searchBar.setSearchBarListener(SpecialScheduleActivity.this,
				new SearchBarListener() {

					@Override
					public void onInputChange(String newInput) {
						if (TextUtils.isEmpty(newInput)) {
							searchResultNumTv.setVisibility(View.GONE);
							blankTv.setText("可以输入特殊日程的标题、备注等关键词进行搜索");
							blankTv.setVisibility(View.VISIBLE);
							specialScheduleLv.setVisibility(View.GONE);
						} else {
							searchSpecialSchedule(newInput, null, null);
							addLvNode();
							showLv();
						}
						searchBar.setTag("");
					}

					@Override
					public void onFocusChange(boolean hasFocus) {

					}

					@Override
					public void onDictationClick() {
						DictationUtil.showDictationDialog(
								SpecialScheduleActivity.this,
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

					}
				});
		searchResultNumTv = (TextView) findViewById(R.id.search_result_num_tv);
	}

	/**
	 * 初始化特殊日程Lv数据
	 * 
	 * @param type
	 *            要初始化的特殊日程类型，默认为-1，代表全部
	 */
	private void initLvData(int type) {
		specialSchedules = new ArrayList<SpecialSchedule>();
		specialScheduleLvItems = new ArrayList<SpecialScheduleLvItem>();

		if (type != -1) {
			searchSpecialSchedule(null, " status != ? and type = ? ",
					new String[] { SpecialSchedule.STATUS_DELETED + "",
							type + "" });
		} else {
			searchSpecialSchedule(null, " status != ? ",
					new String[] { SpecialSchedule.STATUS_DELETED + "" });
		}

		if (specialScheduleNum == 0) {
			return;
		}

		addLvNode();

	}

	// 根据条件搜索，参数说明：
	// query,condition,values都为null就表示是查找所有的状态为正常的特殊日程；否则表示是查询特殊日程,query为全文查询关键词，
	// condition为查询的where语句，values表示where语句中占位符的值
	private void searchSpecialSchedule(String query, String condition,
			String[] values) {
		// 先清空journalLvItems
		if (specialScheduleLvItems != null && !specialScheduleLvItems.isEmpty()) {
			specialScheduleLvItems.clear();
		}
		if (specialSchedules != null && !specialSchedules.isEmpty()) {
			specialSchedules.clear();
		}

		if (specialScheduleLvAdapter != null) {
			specialScheduleLvAdapter.notifyDataSetChanged();
		}

		// 查询所有日志（未删除的）
		if (query == null) {
			specialSchedules = easyDoDB.loadSpecialSchedules(false, null,
					condition, values);
		}
		// 全文(+条件)搜索
		else {
			specialSchedules = easyDoDB.loadSpecialSchedules(true, query,
					condition, values);
		}

		final int size = specialSchedules.size();
		specialScheduleNum = size;
		if (size == 0) {
			return;
		}

		Collections.sort(specialSchedules);
	}

	// 往列表里添加数据（调用时已保证数量大于0）
	private void addLvNode() {
		for (int i = 0; i < specialScheduleNum; i++) {
			SpecialSchedule specialSchedule = specialSchedules.get(i);
			int id = specialSchedule.getId();
			String title = specialSchedule.getTitle();
			int type = specialSchedule.getType();
			String date = specialSchedule.getDate();
			String remark = specialSchedule.getRemark();
			int status = specialSchedule.getStatus();

			SpecialScheduleLvItem item = new SpecialScheduleLvItem(id, title,
					type, date, remark, status);

			specialScheduleLvItems.add(item);
		}

	}

	// 显示列表
	private void showLv() {
		// 如果数据库中没有日志可显示，则显示提示信息
		if (specialScheduleNum == 0) {
			searchResultNumTv.setVisibility(View.GONE);
			// 搜索栏正在激活状态
			if (searchBar.isActive()) {
				blankTv.setText("抱歉，没有找到满足条件的特殊日程");
			} else {
				if (selectedType == -1) {
					blankTv.setText("没有特殊日程可显示，点右上角新建一个吧~");
				} else {
					blankTv.setText("没有"
							+ SpecialSchedule.TYPE_TEXT_ARRAY[selectedType]
							+ "可显示，点右上角新建一个吧~");
				}
			}

			blankTv.setVisibility(View.VISIBLE);
			specialScheduleLv.setVisibility(View.GONE);
		} else {
			blankTv.setVisibility(View.GONE);
			specialScheduleLv.setVisibility(View.VISIBLE);

			specialScheduleLvAdapter = new SpecialScheduleLvAdapter(this,
					R.layout.special_schedule_lv_item, specialScheduleLvItems);
			specialScheduleLv.setAdapter(specialScheduleLvAdapter);

			if (searchBar.isActive()) {
				searchResultNumTv.setVisibility(View.VISIBLE);
				searchResultNumTv.setText("一共找到" + specialSchedules.size()
						+ "条结果");
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_base_left_ib:
			openSearchBar();
			break;
		case R.id.title_base_right_ib:
			Intent intent = new Intent(SpecialScheduleActivity.this,
					CreateSpecialScheduleActivity.class);
			if (selectedType == -1) {
				intent.putExtra("selected_type", 0);
			} else {
				intent.putExtra("selected_type", selectedType);
			}
			startActivityForResult(intent, CREATE_SPECIAL_SCHEDULE_REQUEST_CODE);
			break;
		case R.id.title_base_middle_tv:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					SpecialScheduleActivity.this);
			dialog.setTitle("选择分类");

			final int size = SpecialSchedule.TYPE_TEXT_ARRAY.length + 1;
			String[] typeArray = new String[size];

			typeArray[0] = "全部";
			for (int i = 1; i < size; i++) {
				typeArray[i] = SpecialSchedule.TYPE_TEXT_ARRAY[i - 1];
			}

			dialog.setItems(typeArray, new AlertDialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (which > 0) {
						titleMiddleTv
								.setText(SpecialSchedule.TYPE_TEXT_ARRAY[which - 1]
										+ "  >");
					} else {
						titleMiddleTv.setText("全部" + "  >");
					}

					selectedType = which - 1;

					initLvData(selectedType);
					showLv();

				}
			});
			dialog.show();
			break;
		default:
			break;
		}
	}

	// 打开SearchBar
	private void openSearchBar() {
		searchBar.setActive(true);
		titleLayout.setVisibility(View.GONE);

		blankTv.setText("可以输入特殊日程的标题、备注等关键词进行搜索");
		blankTv.setVisibility(View.VISIBLE);
		specialScheduleLv.setVisibility(View.GONE);

		searchBar.setVisibility(View.VISIBLE);
	}

	// 关闭SearchBar
	private void cancelSearchBar() {
		searchResultNumTv.setVisibility(View.GONE);
		InputMethodUtil.closeInputMethod(SpecialScheduleActivity.this);
		searchBar.setActive(false);
		searchBar.setVisibility(View.GONE);

		blankTv.setText("没有特殊日程可显示，点右上角新建一个吧~");
		titleLayout.setVisibility(View.VISIBLE);

		InputMethodUtil.closeInputMethod(SpecialScheduleActivity.this);

		initLvData(selectedType);
		showLv();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - mExitTime > 2000) {
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int clickedId = specialScheduleLvItems.get(position).getId();
		Intent intent = new Intent(SpecialScheduleActivity.this,
				SpecialScheduleDetailActivity.class);
		intent.putExtra("clicked_id", clickedId);
		startActivityForResult(intent, DETAIL_SPECIAL_SCHEDULE_REQUEST_CODE);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		// 当前长按的item的数据库id
		final int itemId = specialScheduleLvItems.get(position).getId();
		final SpecialScheduleLvItem lvItem = specialScheduleLvItems
				.get(position);

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		// 长按item弹出的dialog的选项列表
		final String[] operation = new String[] { "删除", "复制标题" };
		dialog.setCancelable(true);
		// 设置dialog中选项的点击事件
		dialog.setItems(operation, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					easyDoDB.deleteRecord(SpecialSchedule.TABLE_NAME, itemId
							+ "");

					initLvData(selectedType);
					showLv();
					specialScheduleLv.setSelection(specialScheduleLvPosition);
					break;
				case 1:
					String title = lvItem.getTitle();
					// 把日志内容复制到系统剪贴板
					ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cbm.setText(title);
					ToastUtil.showShort(SpecialScheduleActivity.this,
							SpecialSchedule.TYPE_TEXT_ARRAY[lvItem.getType()]
									+ "标题已经复制到剪贴板");
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
		case CREATE_SPECIAL_SCHEDULE_REQUEST_CODE:
		case DETAIL_SPECIAL_SCHEDULE_REQUEST_CODE:
			if (searchBar.isActive()) {
				if (TextUtils.isEmpty(searchBar.getQuery())) {
					blankTv.setText("可以输入特殊日程的标题、备注等关键词进行搜索");
					blankTv.setVisibility(View.VISIBLE);
					specialScheduleLv.setVisibility(View.GONE);
				} else {
					searchSpecialSchedule(searchBar.getQuery(), null, null);
					addLvNode();
					showLv();
				}
			} else {
				initLvData(selectedType);
				showLv();
			}
			specialScheduleLv.setSelection(specialScheduleLvPosition);
			break;
		default:
			break;
		}
	}
}
