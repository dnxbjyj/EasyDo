package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PunctuationBarLayout extends BaseLinearLayout {
	// 中英文状态常量
	public static final int STATUS_ZH = 0;
	public static final int STATUS_EN = 1;

	// 当前中英文状态
	private int status;

	// 中午标点Ll
	private LinearLayout zhLl;
	// 英文标点Ll
	private LinearLayout enLl;

	// 中英文标点切换按钮
	private TextView switchZhEn;

	// 删除按钮
	private TextView deleteTv;

	public PunctuationBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.punctuation_bar, this);

		status = STATUS_ZH;

		initUI();
	}

	// 初始化布局
	private void initUI() {
		zhLl = (LinearLayout) findViewById(R.id.zh_ll);
		enLl = (LinearLayout) findViewById(R.id.en_ll);

		switchZhEn = (TextView) findViewById(R.id.switch_zh_en_tv);
		switchZhEn.setText(Html.fromHtml("<font color=#000000>中</font>/英"));
		switchZhEn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (status == STATUS_ZH) {
					status = STATUS_EN;
					zhLl.setVisibility(View.GONE);
					enLl.setVisibility(View.VISIBLE);
					switchZhEn.setText(Html
							.fromHtml("中/<font color=#000000>英</font>"));
				} else {
					status = STATUS_ZH;
					enLl.setVisibility(View.GONE);
					zhLl.setVisibility(View.VISIBLE);
					switchZhEn.setText(Html
							.fromHtml("<font color=#000000>中</font>/英"));
				}
			}
		});

		deleteTv = (TextView) findViewById(R.id.delete_tv);
	}

	public interface PunctuationBarListener {
		// 在每个标点按钮上点击的事件
		void onPunctuationClick(String punc);

		// 点击删除按钮的点击事件
		void onDeleteClick();
	}

	// 监听事件
	public void setOnPunctuationBarListener(final Context context,
			final PunctuationBarListener listener) {
		for (int i = 0; i < zhLl.getChildCount(); i++) {
			final TextView child = (TextView) zhLl.getChildAt(i);
			child.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onPunctuationClick(child.getText().toString());
				}
			});
		}

		for (int i = 0; i < enLl.getChildCount(); i++) {
			final TextView child = (TextView) enLl.getChildAt(i);
			child.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onPunctuationClick(child.getText().toString());
				}
			});
		}

		deleteTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onDeleteClick();
			}
		});
	}

	// 设置是否enabled
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (int i = 0; i < zhLl.getChildCount(); i++) {
			zhLl.getChildAt(i).setEnabled(enabled);
		}

		for (int i = 0; i < enLl.getChildCount(); i++) {
			enLl.getChildAt(i).setEnabled(enabled);
		}
		switchZhEn.setEnabled(enabled);
		deleteTv.setEnabled(enabled);
	}

}
