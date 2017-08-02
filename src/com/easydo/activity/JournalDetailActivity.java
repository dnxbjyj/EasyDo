package com.easydo.activity;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;

import com.easydo.adapter.JournalLvItem;
import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.layout.EasyDoTimePickerDialog;
import com.easydo.layout.TimePickerFragment;
import com.easydo.model.Journal;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

public class JournalDetailActivity extends JournalDetailBaseActivity {
	// ǰһ������ݹ�������־id��Ӧ����־����
	private Journal formerJournal;

	// �µ�Journal����
	private Journal newJournal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ʼ������
		initData();

		// ��ʼ������
		titleLeftIb.setOnClickListener(this);

		titleMiddleTv.setText("��־��ϸ����");
		titleMiddleTv.setTextSize(20);

		titleRightIb.setImageResource(R.drawable.edit1_64);
		titleRightIb.setOnClickListener(this);

		clearContentIb.setImageResource(R.drawable.clear1_50);
		journalContentEt.setText(formerJournal.getContent());

		journalDateTv.setText(formerJournal.getCreateTime().substring(0, 10));
		journalTimeTv.setText(formerJournal.getCreateTime().substring(11, 16));

		journalSetDateTr.setOnClickListener(this);
		journalSetTimeTr.setOnClickListener(this);

		setEnabled(false);
	}

	// ��ʼ������
	private void initData() {
		Intent intent = getIntent();
		int journalId = intent.getIntExtra("journal_id", 0);
		formerJournal = easyDoDB.loadJournalById(journalId);
	}

	private void setEnabled(boolean isEnabled) {
		if (isEnabled) {
			titleRightIb.setTag("enabled");
			titleRightIb.setImageResource(R.drawable.done1_64);
			clearContentIb.setEnabled(true);
			clearContentIb.setImageResource(R.drawable.clear1_50);
			journalContentEt.setEnabled(true);
			journalContentEt.setTextColor(this.getResources().getColor(
					R.drawable.LightBlack));
			punctuationBar.setEnabled(true);
			journalSetDateTr.setEnabled(true);
			journalSetTimeTr.setEnabled(true);
			dictationIb.setEnabled(true);
			dictationIb.setImageResource(R.drawable.dictation_big_normal);
		} else {
			titleRightIb.setTag("not_enabled");
			titleRightIb.setImageResource(R.drawable.edit1_64);
			clearContentIb.setEnabled(false);
			clearContentIb.setImageResource(R.drawable.clear2_50);
			journalContentEt.setEnabled(false);
			journalContentEt.setTextColor(this.getResources().getColor(
					R.drawable.TextDefault));
			punctuationBar.setEnabled(false);
			journalSetDateTr.setEnabled(false);
			journalSetTimeTr.setEnabled(false);
			dictationIb.setEnabled(false);
			dictationIb.setImageResource(R.drawable.dictation_big_normal2);
		}
	}

	// �����־�������Ƿ��б仯
	private void checkChanged() {
		if (TextUtils.isEmpty(journalContentEt.getText().toString())) {
			isChanged = true;
			return;
		}

		if (!journalContentEt.getText().toString()
				.equals(formerJournal.getContent())) {
			isChanged = true;
			return;
		}

		if (!journalDateTv.getText().toString()
				.equals(formerJournal.getCreateTime().substring(0, 10))) {
			isChanged = true;
			return;
		}

		if (!journalTimeTv.getText().toString()
				.equals(formerJournal.getCreateTime().substring(11, 16))) {
			isChanged = true;
			return;
		}
	}

	// �����޸ĺ����־,����ʱ�Ѿ���֤��־���ݲ�Ϊ��
	private void saveEdit() {
		String createTime = journalDateTv.getText().toString() + " "
				+ journalTimeTv.getText().toString() + ":"
				+ formerJournal.getCreateTime().substring(14, 16);
		newJournal = new Journal(formerJournal.getId(), createTime,
				Journal.STATUS_NORMAL, journalContentEt.getText().toString());

		easyDoDB.updateJournalById(newJournal.getId(), newJournal);
		ToastUtil.showShort(JournalDetailActivity.this, "�޸���־�ɹ���");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.title_base_left_ib:
			if (TextUtils.isEmpty(journalContentEt.getText().toString())) {
				ToastUtil.showShort(this, "��־�����ݲ���Ϊ��Ŷ~");
				return;
			}
			checkChanged();
			if (titleRightIb.getTag().equals("enabled") && isChanged) {
				ToastUtil.showShort(JournalDetailActivity.this, "�뱣���޸��ٷ��أ�");
				return;
			}

			setResult(JournalActivity.JOURNAL_DETAIL_REQUEST_CODE);
			// ������ǰ�
			finish();

			break;
		case R.id.title_base_right_ib:
			if (titleRightIb.getTag().equals("not_enabled")) {
				setEnabled(true);
			} else if (titleRightIb.getTag().equals("enabled")) {
				if (TextUtils.isEmpty(journalContentEt.getText().toString())) {
					ToastUtil.showShort(this, "��־�����ݲ���Ϊ��Ŷ~");
					return;
				}

				checkChanged();

				if (isChanged) {
					saveEdit();
				}
				setEnabled(false);

			}
			break;
		case R.id.journal_set_date_tr:
			DatePickerFragment datePickerFrg = new DatePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil
							.StringToGregorianCalendar(formerJournal
									.getCreateTime());
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					return new EasyDoDatePickerDialog(getActivity(), this,
							year, month, day, "ѡ����־������",
							EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
				}

				@Override
				public void onDateSet(DatePicker view, int year, int month,
						int day) {
					String dateStr = DateTimeUtil.getDateString(year, month,
							day);
					journalDateTv.setText(dateStr);
				}
			};
			datePickerFrg.show(getFragmentManager(), "datePickerFrg");
			break;
		case R.id.journal_set_time_tr:
			TimePickerFragment timePickerFrg = new TimePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil
							.StringToGregorianCalendar(formerJournal
									.getCreateTime());
					int hour = c.get(Calendar.HOUR_OF_DAY);
					int minute = c.get(Calendar.MINUTE);

					return new EasyDoTimePickerDialog(getActivity(), this,
							hour, minute,
							DateFormat.is24HourFormat(getActivity()), "ѡ����־��ʱ��");
				}

				@Override
				public void onTimeSet(android.widget.TimePicker view,
						int hourOfDay, int minute) {
					String timeStr = DateTimeUtil.getTimeString(hourOfDay,
							minute);
					journalTimeTv.setText(timeStr);
				};
			};
			timePickerFrg.show(getFragmentManager(), "timePickerFrg");
			break;
		default:
			break;
		}
	}
}
