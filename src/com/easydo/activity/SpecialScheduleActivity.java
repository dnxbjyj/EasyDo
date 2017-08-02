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
	// �������
	public static final int CREATE_SPECIAL_SCHEDULE_REQUEST_CODE = 0;
	public static final int DETAIL_SPECIAL_SCHEDULE_REQUEST_CODE = 1;

	// ��ǰѡ��������ճ�����,Ĭ��Ϊ-1��ȫ����
	private int selectedType = -1;

	// �������Ŀؼ�
	private BaseTitleLayout titleLayout;
	private ImageButton titleLeftIb;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// ��������һ��ʼ�����صģ�
	private SearchBarLayout searchBar;
	// ��ʾ�������������Tv
	private TextView searchResultNumTv;

	// �����ճ������б�
	List<SpecialSchedule> specialSchedules;
	// �����ճ�����item������ʵ��������б�
	List<SpecialScheduleLvItem> specialScheduleLvItems;
	// ��ǰ���ݿ��д�������״̬�������ճ���
	private int specialScheduleNum;

	// �����ճ��б�������
	private SpecialScheduleLvAdapter specialScheduleLvAdapter;

	// �����ճ�Lv
	private ListView specialScheduleLv;
	// ����λ����Ϣ
	private int specialScheduleLvPosition;

	// �հ�Tv
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
		 * ������
		 */
		titleLayout = (BaseTitleLayout) findViewById(R.id.title_layout);
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		titleLeftIb.setImageResource(R.drawable.search2_64);
		titleMiddleTv.setText("ȫ��" + "  >");
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
		searchBar.setHint("���������ճ�");
		searchBar.setTag("");
		searchBar.hideAdvanced();
		searchBar.setSearchBarListener(SpecialScheduleActivity.this,
				new SearchBarListener() {

					@Override
					public void onInputChange(String newInput) {
						if (TextUtils.isEmpty(newInput)) {
							searchResultNumTv.setVisibility(View.GONE);
							blankTv.setText("�������������ճ̵ı��⡢��ע�ȹؼ��ʽ�������");
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
	 * ��ʼ�������ճ�Lv����
	 * 
	 * @param type
	 *            Ҫ��ʼ���������ճ����ͣ�Ĭ��Ϊ-1������ȫ��
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

	// ������������������˵����
	// query,condition,values��Ϊnull�ͱ�ʾ�ǲ������е�״̬Ϊ�����������ճ̣������ʾ�ǲ�ѯ�����ճ�,queryΪȫ�Ĳ�ѯ�ؼ��ʣ�
	// conditionΪ��ѯ��where��䣬values��ʾwhere�����ռλ����ֵ
	private void searchSpecialSchedule(String query, String condition,
			String[] values) {
		// �����journalLvItems
		if (specialScheduleLvItems != null && !specialScheduleLvItems.isEmpty()) {
			specialScheduleLvItems.clear();
		}
		if (specialSchedules != null && !specialSchedules.isEmpty()) {
			specialSchedules.clear();
		}

		if (specialScheduleLvAdapter != null) {
			specialScheduleLvAdapter.notifyDataSetChanged();
		}

		// ��ѯ������־��δɾ���ģ�
		if (query == null) {
			specialSchedules = easyDoDB.loadSpecialSchedules(false, null,
					condition, values);
		}
		// ȫ��(+����)����
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

	// ���б���������ݣ�����ʱ�ѱ�֤��������0��
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

	// ��ʾ�б�
	private void showLv() {
		// ������ݿ���û����־����ʾ������ʾ��ʾ��Ϣ
		if (specialScheduleNum == 0) {
			searchResultNumTv.setVisibility(View.GONE);
			// ���������ڼ���״̬
			if (searchBar.isActive()) {
				blankTv.setText("��Ǹ��û���ҵ����������������ճ�");
			} else {
				if (selectedType == -1) {
					blankTv.setText("û�������ճ̿���ʾ�������Ͻ��½�һ����~");
				} else {
					blankTv.setText("û��"
							+ SpecialSchedule.TYPE_TEXT_ARRAY[selectedType]
							+ "����ʾ�������Ͻ��½�һ����~");
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
				searchResultNumTv.setText("һ���ҵ�" + specialSchedules.size()
						+ "�����");
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
			dialog.setTitle("ѡ�����");

			final int size = SpecialSchedule.TYPE_TEXT_ARRAY.length + 1;
			String[] typeArray = new String[size];

			typeArray[0] = "ȫ��";
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
						titleMiddleTv.setText("ȫ��" + "  >");
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

	// ��SearchBar
	private void openSearchBar() {
		searchBar.setActive(true);
		titleLayout.setVisibility(View.GONE);

		blankTv.setText("�������������ճ̵ı��⡢��ע�ȹؼ��ʽ�������");
		blankTv.setVisibility(View.VISIBLE);
		specialScheduleLv.setVisibility(View.GONE);

		searchBar.setVisibility(View.VISIBLE);
	}

	// �ر�SearchBar
	private void cancelSearchBar() {
		searchResultNumTv.setVisibility(View.GONE);
		InputMethodUtil.closeInputMethod(SpecialScheduleActivity.this);
		searchBar.setActive(false);
		searchBar.setVisibility(View.GONE);

		blankTv.setText("û�������ճ̿���ʾ�������Ͻ��½�һ����~");
		titleLayout.setVisibility(View.VISIBLE);

		InputMethodUtil.closeInputMethod(SpecialScheduleActivity.this);

		initLvData(selectedType);
		showLv();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - mExitTime > 2000) {
				ToastUtil.showShort(this, "�ٰ�һ���˳�����");
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
		// ���жϵ�ǰ�ճ���ʾ��ʽ�ǲ�������ͼ�����������ĳ�����ͼ��ʽ
		SystemConfig config = easyDoDB.loadSystemConfig(null, null);
		if (config.getScheduleShowWay() != SystemConfig.SCHEDULE_SHOW_WAY_DAY_VIEW) {
			// ��Ӧ���ճ�Ĭ��������ͼ��ʾ
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
		// ��ǰ������item�����ݿ�id
		final int itemId = specialScheduleLvItems.get(position).getId();
		final SpecialScheduleLvItem lvItem = specialScheduleLvItems
				.get(position);

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		// ����item������dialog��ѡ���б�
		final String[] operation = new String[] { "ɾ��", "���Ʊ���" };
		dialog.setCancelable(true);
		// ����dialog��ѡ��ĵ���¼�
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
					// ����־���ݸ��Ƶ�ϵͳ������
					ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cbm.setText(title);
					ToastUtil.showShort(SpecialScheduleActivity.this,
							SpecialSchedule.TYPE_TEXT_ARRAY[lvItem.getType()]
									+ "�����Ѿ����Ƶ�������");
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
					blankTv.setText("�������������ճ̵ı��⡢��ע�ȹؼ��ʽ�������");
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
