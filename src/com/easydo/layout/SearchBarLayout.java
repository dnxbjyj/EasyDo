package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class SearchBarLayout extends BaseLinearLayout {
	// ״̬
	private boolean isActive;
	// ����Ĳ�ѯ�ַ���
	private String query;

	// SearchBar���岼��
	private LinearLayout searchBar;

	// SearchBarδ����ʱ��Ĭ����ʾ�Ĳ���
	private LinearLayout blankLl;
	// δ������е�Tv
	private TextView blankTv;

	// SearchBar������������
	private LinearLayout searchLl;
	// ��������Et
	private EditText inputEt;
	// ����������ݰ�ť
	private ImageButton clearInputIb;
	// ����ʶ�����밴ť
	private ImageButton dictationIb;
	// �߼�������ť
	private TextView advancedTv;
	// ȡ����ť
	private TextView cancelTv;

	public SearchBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.search_bar, this);
		isActive = false;
		// ��ʼ������
		initUI();
	}

	// ��ʼ������
	private void initUI() {
		searchBar = (LinearLayout) findViewById(R.id.search_bar);

		blankLl = (LinearLayout) findViewById(R.id.search_bar_blank_ll);
		blankTv = (TextView) findViewById(R.id.search_bar_blank_tv);

		searchLl = (LinearLayout) findViewById(R.id.search_bar_search_ll);
		inputEt = (EditText) findViewById(R.id.search_bar_input_et);
		clearInputIb = (ImageButton) findViewById(R.id.search_bar_clear_input_ib);
		clearInputIb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				inputEt.setText("");
			}
		});
		dictationIb = (ImageButton) findViewById(R.id.search_bar_dictation_ib);
		advancedTv = (TextView) findViewById(R.id.search_bar_advanced_tv);
		cancelTv = (TextView) findViewById(R.id.search_bar_cancel_tv);
	}

	// ����SearchBar��ʾ��hint����
	public void setHint(String hint) {
		blankTv.setText(hint);
		inputEt.setHint(hint);
	}

	// SearchBar��һϵ���¼�����
	public interface SearchBarListener {
		// ���δ����״̬��SearchBar�ĵ���¼�����
		void onBlankClick();

		// inputEt����ı��¼��ļ���
		void onFocusChange(boolean hasFocus);

		// inputEt�������ݱ仯ʱ����¼�����
		void onInputChange(String newInput);

		// SearchBar������д��ť�ĵ�������¼�
		void onDictationClick();

		// "�߼�"��ť����¼�����
		void onAdvancedClick();

		// "ȡ��"��ť����¼�����
		void onCancelClick();
	}

	// SearchBarһϵ���¼�����
	public void setSearchBarListener(final Context context,
			final SearchBarListener listener) {
		// ���δ����״̬��SearchBar�ĵ���¼�����
		blankLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setActive(true);
				listener.onBlankClick();
			}
		});

		// inputEt����ı��¼��ļ���
		inputEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listener.onFocusChange(hasFocus);
			}
		});

		// inputEt�������ݱ仯ʱ����¼�����
		inputEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				listener.onInputChange(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		// SearchBar������д��ť�ĵ�������¼�
		dictationIb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onDictationClick();
			}
		});

		// "�߼�"��ť����¼�����
		advancedTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onAdvancedClick();
			}
		});

		// "ȡ��"��ť����¼�����
		cancelTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setActive(false);
				listener.onCancelClick();
			}
		});
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
		if (isActive) {
			inputEt.setText("");
			blankLl.setVisibility(View.GONE);
			searchLl.setVisibility(View.VISIBLE);
		} else {
			inputEt.setText("");
			inputEt.clearFocus();
			blankLl.setVisibility(View.VISIBLE);
			searchLl.setVisibility(View.GONE);
		}
	}

	public String getQuery() {
		return inputEt.getText().toString();
	}

	public void setQuery(String query) {
		this.query = query;
		inputEt.setText(query);
	}

	// ���ظ߼�������ť
	public void hideAdvanced() {
		advancedTv.setVisibility(View.GONE);
	}

}
