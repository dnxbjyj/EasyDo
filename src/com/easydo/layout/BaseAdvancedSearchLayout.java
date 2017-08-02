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

	// ��ǰʱ���Calendar
	protected Calendar curCalendar;

	// �ؼ��ʵ�ѡ��״̬����ֵ
	public static final int KEYWORDS_STATUS_NO_KEYWORDS = 0;
	public static final int KEYWORDS_STATUS_INPUT_KEYWORDS = 1;

	// ���ڵ�ѡ��״̬����ֵ
	public static final int DATE_STATUS_ANY = 0;
	public static final int DATE_STATUS_CHOOSE = 1;

	// �ؼ��ʵ�ѡ��״̬
	protected int keywodsStatus;
	// ���ڵ�ѡ��״̬
	protected int dateStatus;

	// �ؼ���Tr
	protected TableRow keywordsTr;
	// �ؼ���-�� Rb
	protected RadioButton noKeywordsRb;
	// �ؼ���-����ؼ��� Rb
	protected RadioButton inputKeywordsRb;
	// ����ؼ��ʵ�Et
	protected EditText inputKeywordsEt;

	// ����Tr
	protected TableRow dateTr;
	// ����-���� Rb
	protected RadioButton noDateRb;
	// ����-ѡ������ Rb
	protected RadioButton chooseDateRb;
	// ��ʼ����Tv
	protected TextView startDateTv;
	// ��������Tv
	protected TextView endDateTv;

	// ѡ����ճ�״̬����
	public static final int SCHEDULE_STATUS_ANY = -1;
	public static final int SCHEDULE_STATUS_NOT_DONE = 0;
	public static final int SCHEDULE_STATUS_DONE = 1;
	// ��ǰѡ����ճ�״̬
	protected int scheduleStatus;

	// ѡ���ճ�״̬����
	protected TableRow statusTr;
	// ״̬���޵�Rb
	protected RadioButton statusAnyRb;
	// δ���״̬��Rb
	protected RadioButton statusNotDoneRb;
	// �����״̬��Rb
	protected RadioButton statusDoneRb;

	// ѡ���ճ̵ı�ǩ��
	protected TableRow tagTr;
	// ��ǩ���޵�Rb
	protected RadioButton tagAnyRb;
	// Matters��ǩ��Rb
	protected RadioButton tagMattersRb;
	// Interested��ǩ��Rb
	protected RadioButton tagInterestedRb;
	// Work��ǩ��Rb
	protected RadioButton tagWorkRb;
	// Important��ǩ��Rb
	protected RadioButton tagImportantRb;

	// ѡ����ճ�״̬����
	public static final int SCHEDULE_TAG_ANY = -1;
	public static final int SCHEDULE_TAG_MATTERS = Schedule.TAG_MATTERS;
	public static final int SCHEDULE_TAG_INTERESTED = Schedule.TAG_INTERESTED;
	public static final int SCHEDULE_TAG_WORK = Schedule.TAG_WORK;
	public static final int SCHEDULE_TAG_IMPORTANT = Schedule.TAG_IMPORTANT;
	// ��ǰѡ��ı�ǩ
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

	// ��ʼ������
	protected void initLayout() {
		curCalendar = DateTimeUtil.getCurrentCalendar();

		keywodsStatus = KEYWORDS_STATUS_NO_KEYWORDS;
		keywordsTr = (TableRow) findViewById(R.id.keywords_tr);
		noKeywordsRb = (RadioButton) findViewById(R.id.no_keywords_rb);
		inputKeywordsRb = (RadioButton) findViewById(R.id.input_keywords_rb);
		inputKeywordsEt = (EditText) findViewById(R.id.input_keywords_et);
		inputKeywordsEt.setEnabled(false);
		// ��ʼ��keywords��
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
								year, month, day, "ѡ��ʼ������",
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
								year, month, day, "ѡ�����������",
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
		// ��ʼ������ѡ��������
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

	// ��ʼ��keywords��
	protected void initKeywords() {

		noKeywordsRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {// ע������һ�����ж�Ϊtrue����false
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
						if (isChecked) { // ע������һ�����ж�Ϊtrue����false
							inputKeywordsRb.setChecked(true);
							inputKeywordsEt.setEnabled(true);
							keywodsStatus = KEYWORDS_STATUS_INPUT_KEYWORDS;
							setOtherRbUnchecked(keywordsTr, inputKeywordsRb);
						}
					}
				});

	}

	// ��ʼ������ѡ��������
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

	// ��ʼ���ճ�״̬ѡ����
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

	// ��ʼ����ǩ����
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

	// �ѵ�ǰgroup�г���curRb���������Rb�����ó�δѡ�е�״̬
	protected void setOtherRbUnchecked(ViewGroup group, RadioButton curRb) {
		RadioButton rb;
		rb = findNextUncheckedRb(group, curRb);
		if (rb != null) {
			rb.setChecked(false);
		}
	}

	// �ݹ�Ѱ��group��curRb�����һ����ѡ�еĵ�Rb
	// protected RadioButton findNextUncheckedRb(ViewGroup group, RadioButton
	// curRb) {
	// RadioButton rb = null;
	//
	// for (int i = 0; i < group.getChildCount(); i++) {
	// View child = group.getChildAt(i);
	// // ���child��RadioButton���ұ�ѡ�в��Ҳ���curRb���򷵻����child
	// if (child instanceof RadioButton
	// && ((RadioButton) child).isChecked()
	// && child.getId() != curRb.getId()) {
	// rb = (RadioButton) child;
	// return rb;
	//
	// }
	// // ���childҲ��ViewGroup����ô�ݹ����
	// else if (child instanceof ViewGroup) {
	// return findNextUncheckedRb((ViewGroup) child, curRb);
	// }
	// }
	//
	// return rb;
	// }

	// �ݹ�Ѱ��group��curRb�����һ����ѡ�еĵ�Rb
	protected RadioButton findNextUncheckedRb(ViewGroup group, RadioButton curRb) {
		RadioButton rb = null;
		LogUtil.d("[curGroup]", group.getClass().getSimpleName());
		for (int i = 0; i < group.getChildCount(); i++) {
			View child = group.getChildAt(i);

			// ���child��RadioButton���ұ�ѡ�в��Ҳ���curRb���򷵻����child
			if (child instanceof RadioButton
					&& ((RadioButton) child).isChecked()
					&& child.getId() != curRb.getId()) {

				rb = (RadioButton) child;
				break;
			}
			// ���childҲ��ViewGroup����ô�ݹ����
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

	// ��ѯ����ڲ��࣬�����ؼ��ʺͲ�ѯ���
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

	// ��ȡ����Ĺؼ���
	protected String getInputKeywords() {
		return inputKeywordsEt.getText().toString();
	}

	// ��ȡѡ��Ŀ�ʼ����
	protected String getStartDate() {
		return startDateTv.getText().toString().substring(0, 10);
	}

	// ��ȡѡ��Ľ�������
	protected String getEndDate() {
		return endDateTv.getText().toString().substring(0, 10);
	}

	// ��������ѡ������ݵĺϷ���,�Ϸ�����true,���Ϸ�����false
	public boolean checkInput() {
		if (getKeywodsStatus() == KEYWORDS_STATUS_INPUT_KEYWORDS) {
			if (TextUtils.isEmpty(getInputKeywords())) {
				ToastUtil.showShort(mContext, "��������־�Ĺؼ��ʣ�");
				return false;
			}
		}

		if (getDateStatus() == DATE_STATUS_CHOOSE) {
			if (DateTimeUtil.compareDateString(getStartDate(), getEndDate()) == 1) {
				ToastUtil.showShort(mContext, "��ʼ�������ڽ������ڣ�������ѡ��");
				return false;
			}
		}

		return true;
	}
}
