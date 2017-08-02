package com.easydo.activity;

import java.util.Calendar;

import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
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

public class CreateSpecialScheduleActivity extends
		SpecialScheduleDetailBaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();

		titleMiddleTv.setText("�½������ճ�");
		titleRightIb.setImageResource(R.drawable.done1_64);
		titleRightIb.setOnClickListener(this);

		titleLeftIb.setOnClickListener(this);

		if (selectedType == -1) {
			typeTv.setText(SpecialSchedule.TYPE_TEXT_ARRAY[SpecialSchedule.TYPE_ANNIVERSARY]);
		} else {
			typeTv.setText(SpecialSchedule.TYPE_TEXT_ARRAY[selectedType]);
		}

		dateTv.setText(DateTimeUtil.getCurrentDateString());
		dateTr.setOnClickListener(this);
	}

	private void initData() {
		Intent intent = getIntent();
		selectedType = intent.getIntExtra("selected_type", -1);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.title_base_left_ib:
			finish();
			break;
		case R.id.title_base_right_ib:
			saveScheduleData();
			break;
		case R.id.date_tr:
			DatePickerFragment datePickerFrg = new DatePickerFragment() {
				@Override
				public Dialog onCreateDialog(Bundle savedInstanceState) {
					final Calendar c = DateTimeUtil.getCurrentCalendar();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					return new EasyDoDatePickerDialog(getActivity(), this,
							year, month, day, "ѡ������",
							EasyDoDatePickerDialog.DIALOG_TYPE_WHOLE);
				}

				@Override
				public void onDateSet(DatePicker view, int year, int month,
						int day) {
					String dateStr = DateTimeUtil.getDateString(year, month,
							day);
					dateTv.setText(dateStr);
				}
			};
			datePickerFrg.show(getFragmentManager(), "datePickerFrg");
			break;
		default:
			break;
		}
	}

	// �洢�����ճ�����
	private void saveScheduleData() {
		// �����������
		if (TextUtils.isEmpty(titleEt.getText().toString())) {
			ToastUtil.showShort(CreateSpecialScheduleActivity.this,
					"���ⲻ��Ϊ�գ���������⣡");
			return;
		}

		// ����type�ж������Ƿ�Ϸ�
		if (!checkTypeAndDate()) {
			return;
		}

		SpecialSchedule specialSchedule = new SpecialSchedule();
		specialSchedule.setTitle(titleEt.getText().toString());
		specialSchedule.setDate(dateTv.getText().toString());
		specialSchedule.setRemark(remarkEt.getText().toString());
		specialSchedule.setType(selectedType);
		specialSchedule.setStatus(SpecialSchedule.STATUS_NORMAL);

		easyDoDB.saveSpecialSchedule(specialSchedule);

		// �����½��ճ̳ɹ���ʾ��
		ToastUtil.showShort(CreateSpecialScheduleActivity.this, "�½�"
				+ SpecialSchedule.TYPE_TEXT_ARRAY[selectedType] + "�ɹ���");
		setResult(SpecialScheduleActivity.CREATE_SPECIAL_SCHEDULE_REQUEST_CODE);
		// ������ǰ�
		finish();
	}
}
