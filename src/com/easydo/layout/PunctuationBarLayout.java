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
	// ��Ӣ��״̬����
	public static final int STATUS_ZH = 0;
	public static final int STATUS_EN = 1;

	// ��ǰ��Ӣ��״̬
	private int status;

	// ������Ll
	private LinearLayout zhLl;
	// Ӣ�ı��Ll
	private LinearLayout enLl;

	// ��Ӣ�ı���л���ť
	private TextView switchZhEn;

	// ɾ����ť
	private TextView deleteTv;

	public PunctuationBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.punctuation_bar, this);

		status = STATUS_ZH;

		initUI();
	}

	// ��ʼ������
	private void initUI() {
		zhLl = (LinearLayout) findViewById(R.id.zh_ll);
		enLl = (LinearLayout) findViewById(R.id.en_ll);

		switchZhEn = (TextView) findViewById(R.id.switch_zh_en_tv);
		switchZhEn.setText(Html.fromHtml("<font color=#000000>��</font>/Ӣ"));
		switchZhEn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (status == STATUS_ZH) {
					status = STATUS_EN;
					zhLl.setVisibility(View.GONE);
					enLl.setVisibility(View.VISIBLE);
					switchZhEn.setText(Html
							.fromHtml("��/<font color=#000000>Ӣ</font>"));
				} else {
					status = STATUS_ZH;
					enLl.setVisibility(View.GONE);
					zhLl.setVisibility(View.VISIBLE);
					switchZhEn.setText(Html
							.fromHtml("<font color=#000000>��</font>/Ӣ"));
				}
			}
		});

		deleteTv = (TextView) findViewById(R.id.delete_tv);
	}

	public interface PunctuationBarListener {
		// ��ÿ����㰴ť�ϵ�����¼�
		void onPunctuationClick(String punc);

		// ���ɾ����ť�ĵ���¼�
		void onDeleteClick();
	}

	// �����¼�
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

	// �����Ƿ�enabled
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
