package com.easydo.layout;

import com.easydo.util.DictationListener;
import com.easydo.util.DictationUtil;
import com.easydo.util.EditTextUtil;
import com.jiayongji.easydo.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class SimplyCreateScheduleLayout extends BaseLinearLayout {

	// 日程内容输入框
	private EditText contentEt;
	// 日程内容语音听写按钮
	private ImageButton contentDictationIb;

	public SimplyCreateScheduleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(
				R.layout.dialog_simply_create_schedule, this);

		contentEt = (EditText) findViewById(R.id.content_et);
		contentDictationIb = (ImageButton) findViewById(R.id.content_dictation_ib);

		contentDictationIb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DictationUtil.showDictationDialog(mContext,
						new DictationListener() {

							@Override
							public void onDictationListener(
									String dictationResultStr) {
								EditTextUtil.insertText(contentEt,
										dictationResultStr);
								contentEt.requestFocus();
							}
						});
			}
		});
	}

	// 获取输入的日程内容
	public String getContent() {
		String content = contentEt.getText().toString();
		return content;
	}

	// 判断输入的日程内容是否为空，如果为空返回true否则返回false
	public boolean isContentEmpty() {
		String content = getContent();
		return TextUtils.isEmpty(content);
	}

}
