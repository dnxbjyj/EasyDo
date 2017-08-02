package com.easydo.layout;

import java.util.Calendar;

import com.easydo.model.Schedule;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class BaseAdvancedSearchLayout extends BaseLinearLayout {

	protected int mLayoutId;

	// 当前时间的Calendar
	protected Calendar curCalendar;

	// 关键词的选择状态常量值
	public static final int KEYWORDS_STATUS_NO_KEYWORDS = 0;
	public static final int KEYWORDS_STATUS_INPUT_KEYWORDS = 1;

	// 日期的选择状态常量值
	public static final int DATE_STATUS_ANY = 0;
	public static final int DATE_STATUS_CHOOSE = 1;

	// 关键词的选择状态
	protected int keywodsStatus;
	// 日期的选择状态
	protected int dateStatus;

	// 关键词Tr
	protected TableRow keywordsTr;
	// 关键词-无 Rb
	protected RadioButton noKeywordsRb;
	// 关键词-输入关键词 Rb
	protected RadioButton inputKeywordsRb;
	// 输入关键词的Et
	protected EditText inputKeywordsEt;

	// 日期Tr
	protected TableRow dateTr;
	// 日期-不限 Rb
	protected RadioButton noDateRb;
	// 日期-选择日期 Rb
	protected RadioButton chooseDateRb;
	// 开始日期Tv
	protected TextView startDateTv;
	// 结束日期Tv
	protected TextView endDateTv;

	// 选择的日程状态常量
	public static final int SCHEDULE_STATUS_ANY = -1;
	public static final int SCHEDULE_STATUS_NOT_DONE = 0;
	public static final int SCHEDULE_STATUS_DONE = 1;
	// 当前选择的日程状态
	protected int scheduleStatus;

	// 选择日程状态的栏
	protected TableRow statusTr;
	// 状态不限的Rb
	protected RadioButton statusAnyRb;
	// 未完成状态的Rb
	protected RadioButton statusNotDoneRb;
	// 已完成状态的Rb
	protected RadioButton statusDoneRb;

	// 选择日程的标签栏
	protected TableRow tagTr;
	// 标签不限的Rb
	protected RadioButton tagAnyRb;
	// Matters标签的Rb
	protected RadioButton tagMattersRb;
	// Interested标签的Rb
	protected RadioButton tagInterestedRb;
	// Work标签的Rb
	protected RadioButton tagWorkRb;
	// Important标签的Rb
	protected RadioButton tagImportantRb;

	// 选择的日程状态常量
	public static final int SCHEDULE_TAG_ANY = -1;
	public static final int SCHEDULE_TAG_MATTERS = Schedule.TAG_MATTERS;
	public static final int SCHEDULE_TAG_INTERESTED = Schedule.TAG_INTERESTED;
	public static final int SCHEDULE_TAG_WORK = Schedule.TAG_WORK;
	public static final int SCHEDULE_TAG_IMPORTANT = Schedule.TAG_IMPORTANT;
	// 当前选择的标签
	protected int scheduleTag;

	public BaseAdvancedSearchLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(
				R.layout.dialog_base_advanced_search, this);
		initLayout();
	}

	public int getKeywodsStatus() {
		return keywodsStatus;
	}

	public int getDateStatus() {
		return dateStatus;
	}

	// 初始化界面
	protected void initLayout() {
		curCalendar = DateTimeUtil.getCurrentCalendar();

		keywodsStatus = KEYWORDS_STATUS_NO_KEYWORDS;
		keywordsTr = (TableRow) findViewById(R.id.keywords_tr);
		noKeywordsRb = (RadioButton) findViewById(R.id.no_keywords_rb);
		inputKeywordsRb = (RadioButton) findViewById(R.id.input_keywords_rb);
		inputKeywordsEt = (EditText) findViewById(R.id.input_keywords_et);
		inputKeywordsEt.setEnabled(false);
		// 初始化keywords栏
		initKeywords();

		dateStatus = DATE_STATUS_ANY;
		dateTr = (TableRow) findViewById(R.id.date_tr);
		noDateRb = (RadioButton) findViewById(R.id.no_date_rb);
		chooseDateRb = (RadioButton) findViewById(R.id.choose_date_rb);
		startDateTv = (TextView) findViewById(R.id.start_date_tv);
		startDateTv.setText(DateTimeUtil.getCurrentDateString() + "    >");
		startDateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePickerFrg = new DatePickerFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						final Calendar c = curCalendar;
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH);
						int day = c.get(Calendar.DAY_OF_MONTH);
						return new EasyDoDatePickerDialog(getActivity(), this,
								year, month, day, "选择开始的日期",
								EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
					}

					@Override
					public void onDateSet(DatePicker view, int year, int month,
							int day) {
						String dateStr = DateTimeUtil.getDateString(year,
								month, day);
						startDateTv.setText(dateStr + "    >");
					}
				};
				datePickerFrg.show(((Activity) mContext).getFragmentManager(),
						"datePickerFrg");
			}
		});
		startDateTv.setEnabled(false);

		endDateTv = (TextView) findViewById(R.id.end_date_tv);
		endDateTv.setText(DateTimeUtil.getCurrentDateString() + "    >");
		endDateTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatePickerFragment datePickerFrg = new DatePickerFragment() {
					@Override
					public Dialog onCreateDialog(Bundle savedInstanceState) {
						final Calendar c = curCalendar;
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH);
						int day = c.get(Calendar.DAY_OF_MONTH);
						return new EasyDoDatePickerDialog(getActivity(), this,
								year, month, day, "选择结束的日期",
								EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
					}

					@Override
					public void onDateSet(DatePicker view, int year, int month,
							int day) {
						String dateStr = DateTimeUtil.getDateString(year,
								month, day);
						endDateTv.setText(dateStr + "    >");
					}
				};
				datePickerFrg.show(((Activity) mContext).getFragmentManager(),
						"datePickerFrg");
			}
		});
		endDateTv.setEnabled(false);
		// 初始化日期选择条件栏
		initDate();

		scheduleStatus = SCHEDULE_STATUS_ANY;
		statusTr = (TableRow) findViewById(R.id.status_tr);
		statusAnyRb = (RadioButton) findViewById(R.id.status_any_rb);
		statusNotDoneRb = (RadioButton) findViewById(R.id.status_not_done_rb);
		statusDoneRb = (RadioButton) findViewById(R.id.status_done_rb);
		initStatus();

		scheduleTag = SCHEDULE_TAG_ANY;
		tagTr = (TableRow) findViewById(R.id.tag_tr);
		tagAnyRb = (RadioButton) findViewById(R.id.tag_any_rb);
		tagMattersRb = (RadioButton) findViewById(R.id.tag_matters_rb);
		tagInterestedRb = (RadioButton) findViewById(R.id.tag_interested_rb);
		tagWorkRb = (RadioButton) findViewById(R.id.tag_work_rb);
		tagImportantRb = (RadioButton) findViewById(R.id.tag_important_rb);
		initTag();

	}

	// 初始化keywords栏
	protected void initKeywords() {

		noKeywordsRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {// 注：这里一定是判断为true而非false
					noKeywordsRb.setChecked(true);
					inputKeywordsEt.setText("");
					inputKeywordsEt.setEnabled(false);
					keywodsStatus = KEYWORDS_STATUS_NO_KEYWORDS;
					setOtherRbUnchecked(keywordsTr, noKeywordsRb);
				}
			}
		});

		inputKeywordsRb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) { // 注：这里一定是判断为true而非false
							inputKeywordsRb.setChecked(true);
							inputKeywordsEt.setEnabled(true);
							keywodsStatus = KEYWORDS_STATUS_INPUT_KEYWORDS;
							setOtherRbUnchecked(keywordsTr, inputKeywordsRb);
						}
					}
				});

	}

	// 初始化日期选择条件栏
	protected void initDate() {
		noDateRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					noDateRb.setChecked(true);
					startDateTv.setEnabled(false);
					startDateTv.setTextColor(getResources().getColor(
							R.drawable.TextDefault));
					endDateTv.setEnabled(false);
					endDateTv.setTextColor(getResources().getColor(
							R.drawable.TextDefault));
					dateStatus = DATE_STATUS_ANY;
					setOtherRbUnchecked(dateTr, noDateRb);
				}
			}
		});

		chooseDateRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					chooseDateRb.setChecked(true);
					startDateTv.setEnabled(true);
					endDateTv.setEnabled(true);
					startDateTv.setTextColor(getResources().getColor(
							R.drawable.TextBlack));
					endDateTv.setTextColor(getResources().getColor(
							R.drawable.TextBlack));
					dateStatus = DATE_STATUS_CHOOSE;
					setOtherRbUnchecked(dateTr, chooseDateRb);
				}
			}
		});
	}

	// 初始化日程状态选择栏
	private void initStatus() {
		statusAnyRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					statusAnyRb.setChecked(true);
					scheduleStatus = SCHEDULE_STATUS_ANY;
					setOtherRbUnchecked(statusTr, statusAnyRb);
				}
			}
		});

		statusNotDoneRb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							statusNotDoneRb.setChecked(true);
							scheduleStatus = SCHEDULE_STATUS_NOT_DONE;
							setOtherRbUnchecked(statusTr, statusNotDoneRb);
						}
					}
				});

		statusDoneRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					statusDoneRb.setChecked(true);
					scheduleStatus = SCHEDULE_STATUS_DONE;
					setOtherRbUnchecked(statusTr, statusDoneRb);
				}
			}
		});
	}

	// 初始化标签数据
	protected void initTag() {
		tagAnyRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tagAnyRb.setChecked(true);
					scheduleTag = SCHEDULE_TAG_ANY;
					setOtherRbUnchecked(tagTr, tagAnyRb);
				}
			}
		});

		tagMattersRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tagMattersRb.setChecked(true);
					scheduleTag = SCHEDULE_TAG_MATTERS;
					setOtherRbUnchecked(tagTr, tagMattersRb);
				}
			}
		});

		tagInterestedRb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							tagInterestedRb.setChecked(true);
							scheduleTag = SCHEDULE_TAG_INTERESTED;
							setOtherRbUnchecked(tagTr, tagInterestedRb);
						}
					}
				});

		tagWorkRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tagWorkRb.setChecked(true);
					scheduleTag = SCHEDULE_TAG_WORK;
					setOtherRbUnchecked(tagTr, tagWorkRb);
				}
			}
		});

		tagImportantRb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							tagImportantRb.setChecked(true);
							scheduleTag = SCHEDULE_TAG_IMPORTANT;
							setOtherRbUnchecked(tagTr, tagImportantRb);
						}
					}
				});
	}

	// 把当前group中除了curRb以外的其他Rb都设置成未选中的状态
	protected void setOtherRbUnchecked(ViewGroup group, RadioButton curRb) {
		RadioButton rb;
		rb = findNextUncheckedRb(group, curRb);
		if (rb != null) {
			rb.setChecked(false);
		}
	}

	// 递归寻找group中curRb以外的一个被选中的的Rb
	// protected RadioButton findNextUncheckedRb(ViewGroup group, RadioButton
	// curRb) {
	// RadioButton rb = null;
	//
	// for (int i = 0; i < group.getChildCount(); i++) {
	// View child = group.getChildAt(i);
	// // 如果child是RadioButton并且被选中并且不是curRb，则返回这个child
	// if (child instanceof RadioButton
	// && ((RadioButton) child).isChecked()
	// && child.getId() != curRb.getId()) {
	// rb = (RadioButton) child;
	// return rb;
	//
	// }
	// // 如果child也是ViewGroup，那么递归操作
	// else if (child instanceof ViewGroup) {
	// return findNextUncheckedRb((ViewGroup) child, curRb);
	// }
	// }
	//
	// return rb;
	// }

	// 递归寻找group中curRb以外的一个被选中的的Rb
	protected RadioButton findNextUncheckedRb(ViewGroup group, RadioButton curRb) {
		RadioButton rb = null;
		LogUtil.d("[curGroup]", group.getClass().getSimpleName());
		for (int i = 0; i < group.getChildCount(); i++) {
			View child = group.getChildAt(i);

			// 如果child是RadioButton并且被选中并且不是curRb，则返回这个child
			if (child instanceof RadioButton
					&& ((RadioButton) child).isChecked()
					&& child.getId() != curRb.getId()) {

				rb = (RadioButton) child;
				break;
			}
			// 如果child也是ViewGroup，那么递归操作
			else if (child instanceof ViewGroup) {
				LogUtil.d("[curGroup]", group.getClass().getSimpleName());
				rb = findNextUncheckedRb((ViewGroup) child, curRb);
				if (rb != null && rb.isChecked() && rb.getId() != curRb.getId()) {
					break;
				}
			}
		}

		return rb;
	}

	// 查询结果内部类，包含关键词和查询语句
	public class AdvancedQueryCondition {
		protected String keywords;
		protected String condition;
		protected String[] values;

		public AdvancedQueryCondition(String keywords, String condition,
				String[] values) {
			this.keywords = keywords;
			this.condition = condition;
			this.values = values;
		}

		public String getKeywords() {
			return keywords;
		}

		public String getCondition() {
			return condition;
		}

		public String[] getValues() {
			return values;
		}

	}

	// 获取输入的关键词
	protected String getInputKeywords() {
		return inputKeywordsEt.getText().toString();
	}

	// 获取选择的开始日期
	protected String getStartDate() {
		return startDateTv.getText().toString().substring(0, 10);
	}

	// 获取选择的结束日期
	protected String getEndDate() {
		return endDateTv.getText().toString().substring(0, 10);
	}

	// 检查输入和选择的数据的合法性,合法返回true,不合法返回false
	public boolean checkInput() {
		if (getKeywodsStatus() == KEYWORDS_STATUS_INPUT_KEYWORDS) {
			if (TextUtils.isEmpty(getInputKeywords())) {
				ToastUtil.showShort(mContext, "请输入日志的关键词！");
				return false;
			}
		}

		if (getDateStatus() == DATE_STATUS_CHOOSE) {
			if (DateTimeUtil.compareDateString(getStartDate(), getEndDate()) == 1) {
				ToastUtil.showShort(mContext, "开始日期晚于结束日期，请重新选择！");
				return false;
			}
		}

		return true;
	}
}
