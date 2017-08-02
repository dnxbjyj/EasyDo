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
	// ����ճ�Intent��������
	public final static int CREATE_JOURNAL_REQUEST_CODE = 0;
	// �鿴�ճ���ϸ��ϢIntent��������
	public final static int JOURNAL_DETAIL_REQUEST_CODE = 1;

	// �ж��ǲ���֮ǰ������ĵ�ǰ�
	private boolean isStartedByFormer = false;
	// �����֮ǰ������ĵ�ǰ�����ô��������
	private String formerDate = null;

	// ��־�����б�
	List<Journal> journals;
	// ��־����item������ʵ��������б�
	List<JournalLvItem> journalLvItems;
	// ��ǰ���ݿ��д�������״̬����־��
	private int journalNum;

	// ��־�б�������
	private JournalLvAdapter journalLvAdapter;

	// �������Ŀؼ�
	private BaseTitleLayout titleLayout;
	private ImageButton titleLeftIb;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// ��������һ��ʼ�����صģ�
	private SearchBarLayout searchBar;
	// ��ʾ�������������Tv
	private TextView searchResultNumTv;
	// �߼������������
	private AdvancedSearchJournalLayout.AdvancedQueryCondition mQueryCondition;

	// ��־Lv
	private ListView journalLv;
	// ����λ����Ϣ
	private int journalLvPosition;

	// �հ�Tv
	private TextView blankTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��ʼ��intent���ݣ����ǲ�������������ĵ�ǰ��־�
		initIntentData();

		initUI();

		// ��ʼ����־����
		if (isStartedByFormer) {
			initJournalData(formerDate);
		} else {
			initJournalData(null);
		}
		// ��ʾ��־�б�
		showJournalLv();
	}

	// ��ʼ��intent���ݣ����ǲ�������������ĵ�ǰ��־�
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
					+ " ��־�б�");
			titleMiddleTv.setTextSize(16);
			findViewById(R.id.page_bottom_layout).setVisibility(View.GONE);
		} else {
			titleLeftIb.setImageResource(R.drawable.search2_64);
			titleMiddleTv.setText("��־�б�");
			titleMiddleTv.setTextSize(18);
		}
		titleRightIb.setImageResource(R.drawable.write2_64);
		titleLeftIb.setOnClickListener(this);
		titleRightIb.setOnClickListener(this);

		searchBar = (SearchBarLayout) findViewById(R.id.search_bar);
		searchBar.setHint("������־");
		searchBar.setTag("");
		searchBar.setSearchBarListener(JournalActivity.this,
				new SearchBarListener() {

					@Override
					public void onInputChange(String newInput) {
						if (TextUtils.isEmpty(newInput)) {
							searchResultNumTv.setVisibility(View.GONE);
							blankTv.setText("����������־�Ĵ���ʱ�䡢���ݵȹؼ��ʽ�������");
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

	// ��SearchBar
	private void openSearchBar() {
		searchBar.setActive(true);
		titleLayout.setVisibility(View.GONE);

		blankTv.setText("����������־�Ĵ���ʱ�䡢���ݵȹؼ��ʽ�������");
		blankTv.setVisibility(View.VISIBLE);
		journalLv.setVisibility(View.GONE);

		searchBar.setVisibility(View.VISIBLE);
	}

	// �ر�SearchBar
	private void cancelSearchBar() {
		searchResultNumTv.setVisibility(View.GONE);
		InputMethodUtil.closeInputMethod(JournalActivity.this);
		searchBar.setActive(false);
		searchBar.setVisibility(View.GONE);

		blankTv.setText("û����־����ʾ�������Ͻ�дһƪ��־��~");
		titleLayout.setVisibility(View.VISIBLE);

		InputMethodUtil.closeInputMethod(JournalActivity.this);

		initJournalData(null);
		showJournalLv();

	}

	// ��־�ĸ߼�����
	private void advancedSearchJournal() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("��־�߼�����");

		final AdvancedSearchJournalLayout dialogView = new AdvancedSearchJournalLayout(
				JournalActivity.this, null);

		dialog.setView(dialogView);

		dialog.setPositiveButton("����", null);
		dialog.setNegativeButton("ȡ��", null);

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

	// ��ʼ����־����
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

	// ��������������־������˵����
	// query,condition,values��Ϊnull�ͱ�ʾ�ǲ������е�״̬Ϊ��������־�������ʾ�ǲ�ѯ��־����,queryΪȫ�Ĳ�ѯ�ؼ��ʣ�
	// conditionΪ��ѯ��where��䣬values��ʾwhere�����ռλ����ֵ
	private void searchJournal(String query, String condition, String[] values) {
		// �����journalLvItems
		if (journalLvItems != null && !journalLvItems.isEmpty()) {
			journalLvItems.clear();
		}
		if (journals != null && !journals.isEmpty()) {
			journals.clear();
		}

		if (journalLvAdapter != null) {
			journalLvAdapter.notifyDataSetChanged();
		}

		// ��ѯ������־��δɾ���ģ�
		if (query == null) {
			journals = easyDoDB.loadJournals(false, null, condition, values);
		}
		// ȫ��(+����)����
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

	// ��JournalLvItems�б���������ݣ�����ʱ�ѱ�֤��־��������0��
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

	// ��ʾ��־�б�
	private void showJournalLv() {
		// ������ݿ���û����־����ʾ������ʾ��ʾ��Ϣ
		if (journalNum == 0) {
			searchResultNumTv.setVisibility(View.GONE);
			// ���������ڼ���״̬
			if (searchBar.isActive()) {
				blankTv.setText("��Ǹ��û���ҵ�������������־");
			} else {
				blankTv.setText("û����־����ʾ�������Ͻ�дһƪ��־��~");
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
				searchResultNumTv.setText("һ���ҵ�" + journals.size() + "ƪ��־");
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
		// ��ǰ������item����־id
		final int itemJournalId = journalLvItems.get(position).getId();
		final JournalLvItem lvItem = journalLvItems.get(position);

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		// ����item������dialog��ѡ���б�
		final String[] operation = new String[] { "ɾ��", "������־����" };
		dialog.setCancelable(true);
		// ����dialog��ѡ��ĵ���¼�
		dialog.setItems(operation, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					easyDoDB.deleteRecord(Journal.TABLE_NAME, itemJournalId
							+ "");
					// �����ǰ�Ǹ߼�����
					if (searchBar.isActive()
							&& searchBar.getTag().equals("advanced_search")) {
						searchJournal(mQueryCondition.getKeywords(),
								mQueryCondition.getCondition(),
								mQueryCondition.getValues());
						addJournalNode();
						showJournalLv();
					} else if (searchBar.isActive()) {
						if (TextUtils.isEmpty(searchBar.getQuery())) {
							blankTv.setText("����������־�Ĵ���ʱ�䡢���ݵȹؼ��ʽ�������");
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
					// ����־���ݸ��Ƶ�ϵͳ������
					ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
					cbm.setText(content);
					ToastUtil.showShort(JournalActivity.this, "��־�����Ѿ����Ƶ�������");
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
					blankTv.setText("����������־�Ĵ���ʱ�䡢���ݵȹؼ��ʽ�������");
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

}
