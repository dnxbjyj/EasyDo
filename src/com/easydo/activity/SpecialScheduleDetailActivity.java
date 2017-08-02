package com.easydo.activity;

import java.util.Calendar;

import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.model.Journal;
import com.easydo.model.SpecialSchedule;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;

public class SpecialScheduleDetailActivity extends
		SpecialScheduleDetailBaseActivity implements OnClickListener {

	// 前一个活动传递过来的日志id对应的日志对象
	private SpecialSchedule formerSpecialSchedule;

	// 新的Journal对象
	private SpecialSchedule newSpecialSchedule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 初始化数据
		initData();

		// 初始化界面
		titleLeftIb.setOnClickListener(this);

		titleMiddleTv.setText("详细内容");
		titleMiddleTv.setTextSize(20);

		titleRightIb.setImageResource(R.drawable.edit1_64);
		titleRightIb.setOnClickListener(this);

		titleEt.setText(formerSpecialSchedule.getTitle());

		typeTv.setText(SpecialSchedule.TYPE_TEXT_ARRAY[formerSpecialSchedule
				.getType()]);

		dateTr.setOnClickListener(this);
		dateTv.setText(formerSpecialSchedule.getDate());

		remarkEt.setText(formerSpecialSchedule.getRemark());

		setEnabled(false);
	}

	// 初始化数据
	private void initData() {
		Intent intent = getIntent();
		int clickedId = intent.getIntExtra("clicked_id", 0);
		formerSpecialSchedule = easyDoDB.loadSpecialScheduleById(clickedId);

		selectedType = formerSpecialSchedule.getType();
		selectedDate = formerSpecialSchedule.getDate();
	}

	private void setEnabled(boolean isEnabled) {
		if (isEnabled) {
			titleRightIb.setTag("enabled");
			titleRightIb.setImageResource(R.drawable.done1_64);
			titleEt.setEnabled(true);
			dictationIb.setEnabled(true);
			typeTr.setEnabled(true);
			dateTr.setEnabled(true);
			typeTv.setTextColor(getResources().getColor(R.drawable.TextBlack));
			dateTv.setTextColor(getResources().getColor(R.drawable.TextBlack));
			remarkEt.setEnabled(true);
		} else {
			titleRightIb.setTag("not_enabled");
			titleRightIb.setImageResource(R.drawable.edit1_64);
			titleEt.setEnabled(false);
			dictationIb.setEnabled(false);
			typeTr.setEnabled(false);
			dateTr.setEnabled(false);
			typeTv.setTextColor(getResources().getColor(R.drawable.TextDefault));
			dateTv.setTextColor(getResources().getColor(R.drawable.TextDefault));
			remarkEt.setEnabled(false);
		}
	}

	// 检测内容是否有变化
	private void checkChanged() {
		if (!titleEt.getText().toString()
				.equals(formerSpecialSchedule.getTitle())) {
			isChanged = true;
			return;
		}

		if (selectedType != formerSpecialSchedule.getType()) {
			isChanged = true;
			return;
		}

		if (!dateTv.getText().toString()
				.equals(formerSpecialSchedule.getDate())) {
			isChanged = true;
			return;
		}

		if (!remarkEt.getText().toString()
				.equals(formerSpecialSchedule.getRemark())) {
			isChanged = true;
			return;
		}
	}

	// 保存修改后的特殊日程,调用时已经保证日志内容不为空
	private void saveEdit() {
		int id = formerSpecialSchedule.getId();
		String title = titleEt.getText().toString();
		int type = selectedType;
		String date = dateTv.getText().toString();
		String remark = remarkEt.getText().toString();
		int status = SpecialSchedule.STATUS_NORMAL;

		newSpecialSchedule = new SpecialSchedule(id, title, date, remark, type,
				status);

		easyDoDB.updateSpecialScheduleById(id, newSpecialSchedule);
		ToastUtil.showShort(SpecialScheduleDetailActivity.this, "修改特殊日程成功！");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.title_base_left_ib:
			if (TextUtils.isEmpty(titleEt.getText().toString())) {
				ToastUtil.showShort(this, "标题不能为空哦~");
				return;
			}
			checkChanged();

			if (titleRightIb.getTag().equals("enabled") && isChanged) {
				ToastUtil.showShort(SpecialScheduleDetailActivity.this,
						"请保存修改再返回！");
				return;
			}

			setResult(JournalActivity.JOURNAL_DETAIL_REQUEST_CODE);
			// 结束当前活动
			finish();

			break;
		case R.id.title_base_right_ib:
			if (titleRightIb.getTag().equals("not_enabled")) {
				setEnabled(true);
			} else if (titleRightIb.getTag().equals("enabled")) {
				if (TextUtils.isEmpty(titleEt.getText().toString())) {
					ToastUtil.showShort(this, "标题不能为空哦~");
					return;
				}

				checkChanged();

				if (isChanged) {
					// 根据type判断日期是否合法
					if (!checkTypeAndDate()) {
						return;
					}

					saveEdit();
				}
				setEnabled(false);

			}
			break;
		case R.id.date_tr:
			DatePickerFragment datePickerFrg = new DatePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil
							.StringToGregorianCalendar(formerSpecialSchedule
									.getDate() + " 00:00:00");
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					return new EasyDoDatePickerDialog(getActivity(), this,
							year, month, day, "选择日期",
							EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
				}

				@Override
				public void onDateSet(DatePicker view, int year, int month,
						int day) {
					String dateStr = DateTimeUtil.getDateString(year, month,
							day);
					dateTv.setText(dateStr);
					selectedDate = dateStr;
				}
			};
			datePickerFrg.show(getFragmentManager(), "datePickerFrg");
			break;
		default:
			break;
		}
	}

}
