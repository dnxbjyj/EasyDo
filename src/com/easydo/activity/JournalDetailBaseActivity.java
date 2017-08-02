package com.easydo.activity;

import com.easydo.constant.GlobalConfig;
import com.easydo.layout.PunctuationBarLayout;
import com.easydo.layout.PunctuationBarLayout.PunctuationBarListener;
import com.easydo.model.Schedule;
import com.easydo.model.SystemConfig;
import com.easydo.service.ScheduleAlarmService;
import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.EditTextUtil;
import com.easydo.util.LogUtil;
import com.easydo.util.TypefaceUtil;
import com.jiayongji.easydo.R;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class JournalDetailBaseActivity extends BaseActivity implements
		OnClickListener {
	// �ճ������Ƿ��޸�
	boolean isChanged = false;
	// �������ؼ�
	protected ImageButton titleLeftIb;
	protected TextView titleMiddleTv;
	protected ImageButton titleRightIb;

	// �����־���ݵİ�ťIb
	protected ImageButton clearContentIb;

	// ��־����Et
	protected EditText journalContentEt;

	// ���ñ�������
	protected PunctuationBarLayout punctuationBar;

	// ѡ����־���ڵ�tr
	protected TableRow journalSetDateTr;
	// ��ʾѡ������ڵ�tv
	protected TextView journalDateTv;

	// ѡ����־ʱ���tr
	protected TableRow journalSetTimeTr;
	// ��ʾѡ���ʱ���tv
	protected TextView journalTimeTv;

	// ����ʶ��������־������д��ť
	protected ImageButton dictationIb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_create_detail_journal);

		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);
		// ��ʼ�������ؼ�
		titleLeftIb.setImageResource(R.drawable.back1_64);

		clearContentIb = (ImageButton) findViewById(R.id.clear_content_ib);
		clearContentIb.setOnClickListener(this);

		journalContentEt = (EditText) findViewById(R.id.journal_content_et);
		TypefaceUtil typeface = new TypefaceUtil(
				JournalDetailBaseActivity.this,
				systemConfig.getJournalContentTypeface());
		if (systemConfig.getJournalContentTypeface() == null
				|| systemConfig.getJournalContentTypeface().equals("default")) {
			typeface.setTypeface(journalContentEt, false);
		} else {
			typeface.setTypeface(journalContentEt, true);
		}

		punctuationBar = (PunctuationBarLayout) findViewById(R.id.punctuation_bar);
		punctuationBar.setOnPunctuationBarListener(
				JournalDetailBaseActivity.this, new PunctuationBarListener() {

					@Override
					public void onPunctuationClick(String punc) {
						if (journalContentEt.isEnabled()) {
							EditTextUtil.insertText(journalContentEt, punc);
						}
					}

					@Override
					public void onDeleteClick() {
						if (!TextUtils.isEmpty(journalContentEt.getText()
								.toString()) && journalContentEt.isEnabled()) {
							EditTextUtil.deleteText(journalContentEt);
						}
					}
				});

		journalSetDateTr = (TableRow) findViewById(R.id.journal_set_date_tr);
		journalDateTv = (TextView) findViewById(R.id.journal_date_tv);

		journalSetTimeTr = (TableRow) findViewById(R.id.journal_set_time_tr);
		journalTimeTv = (TextView) findViewById(R.id.journal_time_tv);

		dictationIb = (ImageButton) findViewById(R.id.dictation_ib);
		dictationIb.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear_content_ib:
			journalContentEt.setText("");
			break;
		case R.id.dictation_ib:
			DictationUtil.showDictationDialog(this, new DictationListener() {

				@Override
				public void onDictationListener(String dictationResultStr) {
					EditTextUtil.insertText(journalContentEt,
							dictationResultStr);
					journalContentEt.requestFocus();
					isChanged = true;
				}
			});
			break;
		default:
			break;
		}
	}

}
