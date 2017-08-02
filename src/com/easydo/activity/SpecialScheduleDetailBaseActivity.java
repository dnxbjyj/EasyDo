package com.easydo.activity;

import java.util.Calendar;

import com.easydo.layout.DatePickerFragment;
import com.easydo.layout.EasyDoDatePickerDialog;
import com.easydo.model.Journal;
import com.easydo.model.SpecialSchedule;
import com.easydo.util.DateTimeUtil;
import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.EditTextUtil;
import com.easydo.util.ToastUtil;
import com.jiayongji.easydo.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SpecialScheduleDetailBaseActivity extends BaseActivity implements
		OnClickListener {
	// ��������Ƿ����仯�����������У�
	protected boolean isChanged = false;

	// �������Ŀؼ�
	protected ImageButton titleLeftIb;
	protected TextView titleMiddleTv;
	protected ImageButton titleRightIb;

	// �����ճ̱���Et
	protected EditText titleEt;
	// ������������дIb
	protected ImageButton dictationIb;
	// ����Tr
	protected TableRow typeTr;
	// ����Tv
	protected TextView typeTv;
	// ����Tr
	protected TableRow dateTr;
	// ����Tv
	protected TextView dateTv;
	// ��עEt
	protected EditText remarkEt;

	// ��ǰѡ��ķ���
	protected int selectedType;
	// ��ǰ����
	protected String selectedDate;

	private TableLayout tableTl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_create_detail_special_schedule);
		// �õ����ʱ��עEtʧȥ����
		tableTl = (TableLayout) findViewById(R.id.table_tl);
		tableTl.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				tableTl.setFocusable(true);
				tableTl.setFocusableInTouchMode(true);
				tableTl.requestFocus();
				return false;
			}
		});
		/**
		 * ������
		 */
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		titleLeftIb.setImageResource(R.drawable.back1_64);
		titleMiddleTv.setTextSize(18);

		/**
		 * ���������ճ̱���
		 */
		titleEt = (EditText) findViewById(R.id.title_et);

		/**
		 * ������д��ť�������¼�
		 */
		dictationIb = (ImageButton) findViewById(R.id.dictation_ib);
		dictationIb.setOnClickListener(this);

		/**
		 * ѡ�����
		 */
		typeTr = (TableRow) findViewById(R.id.type_tr);
		typeTv = (TextView) findViewById(R.id.type_tv);
		typeTr.setOnClickListener(this);

		/**
		 * ѡ������
		 */
		dateTr = (TableRow) findViewById(R.id.date_tr);
		dateTv = (TextView) findViewById(R.id.date_tv);

		/**
		 * ��д��ע
		 */
		remarkEt = (EditText) findViewById(R.id.remark_et);
	}

	// ����type�ж������Ƿ�Ϸ�
	protected boolean checkTypeAndDate() {
		// �����ա����յ�����ҪС�ڵ����������ڣ�����������Ҫ���ڵ��ڵ�ǰ����
		switch (selectedType) {
		case SpecialSchedule.TYPE_ANNIVERSARY:
		case SpecialSchedule.TYPE_BIRTHDAY:
			if (DateTimeUtil.compareDateString(dateTv.getText().toString(),
					DateTimeUtil.getCurrentDateString()) > 0) {
				ToastUtil.showShort(SpecialScheduleDetailBaseActivity.this,
						SpecialSchedule.TYPE_TEXT_ARRAY[selectedType]
								+ "�����ڲ������ڽ�������ڣ�");
				return false;
			}
			break;
		case SpecialSchedule.TYPE_COUNTDOWN:
			if (DateTimeUtil.compareDateString(dateTv.getText().toString(),
					DateTimeUtil.getCurrentDateString()) < 0) {
				ToastUtil.showShort(SpecialScheduleDetailBaseActivity.this,
						SpecialSchedule.TYPE_TEXT_ARRAY[selectedType]
								+ "�����ڲ������ڽ�������ڣ�");
				return false;
			}
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dictation_ib:
			DictationUtil.showDictationDialog(this, new DictationListener() {

				@Override
				public void onDictationListener(String dictationResultStr) {
					EditTextUtil.insertText(titleEt, dictationResultStr);
					titleEt.requestFocus();
					isChanged = true;
				}
			});
			break;
		case R.id.type_tr:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					SpecialScheduleDetailBaseActivity.this);
			dialog.setTitle("ѡ�����");
			dialog.setItems(SpecialSchedule.TYPE_TEXT_ARRAY,
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							typeTv.setText(SpecialSchedule.TYPE_TEXT_ARRAY[which]);
							selectedType = which;
						}
					});
			dialog.show();
			break;
		default:
			break;
		}
	}
}
