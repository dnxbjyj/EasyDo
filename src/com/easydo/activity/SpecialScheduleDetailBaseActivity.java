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
	// 检测内容是否发生变化（用在详情中）
	protected boolean isChanged = false;

	// 标题栏的控件
	protected ImageButton titleLeftIb;
	protected TextView titleMiddleTv;
	protected ImageButton titleRightIb;

	// 特殊日程标题Et
	protected EditText titleEt;
	// 标题栏语音听写Ib
	protected ImageButton dictationIb;
	// 分类Tr
	protected TableRow typeTr;
	// 分类Tv
	protected TextView typeTv;
	// 日期Tr
	protected TableRow dateTr;
	// 日期Tv
	protected TextView dateTv;
	// 备注Et
	protected EditText remarkEt;

	// 当前选择的分类
	protected int selectedType;
	// 当前日期
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
		// 让点击别处时备注Et失去焦点
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
		 * 标题栏
		 */
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);

		titleLeftIb.setImageResource(R.drawable.back1_64);
		titleMiddleTv.setTextSize(18);

		/**
		 * 输入特殊日程标题
		 */
		titleEt = (EditText) findViewById(R.id.title_et);

		/**
		 * 语音听写按钮及监听事件
		 */
		dictationIb = (ImageButton) findViewById(R.id.dictation_ib);
		dictationIb.setOnClickListener(this);

		/**
		 * 选择分类
		 */
		typeTr = (TableRow) findViewById(R.id.type_tr);
		typeTv = (TextView) findViewById(R.id.type_tv);
		typeTr.setOnClickListener(this);

		/**
		 * 选择日期
		 */
		dateTr = (TableRow) findViewById(R.id.date_tr);
		dateTv = (TextView) findViewById(R.id.date_tv);

		/**
		 * 填写备注
		 */
		remarkEt = (EditText) findViewById(R.id.remark_et);
	}

	// 根据type判断日期是否合法
	protected boolean checkTypeAndDate() {
		// 纪念日、生日的日期要小于等于现在日期；倒数日日期要大于等于当前日期
		switch (selectedType) {
		case SpecialSchedule.TYPE_ANNIVERSARY:
		case SpecialSchedule.TYPE_BIRTHDAY:
			if (DateTimeUtil.compareDateString(dateTv.getText().toString(),
					DateTimeUtil.getCurrentDateString()) > 0) {
				ToastUtil.showShort(SpecialScheduleDetailBaseActivity.this,
						SpecialSchedule.TYPE_TEXT_ARRAY[selectedType]
								+ "的日期不能晚于今天的日期！");
				return false;
			}
			break;
		case SpecialSchedule.TYPE_COUNTDOWN:
			if (DateTimeUtil.compareDateString(dateTv.getText().toString(),
					DateTimeUtil.getCurrentDateString()) < 0) {
				ToastUtil.showShort(SpecialScheduleDetailBaseActivity.this,
						SpecialSchedule.TYPE_TEXT_ARRAY[selectedType]
								+ "的日期不能早于今天的日期！");
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
			dialog.setTitle("选择分类");
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
