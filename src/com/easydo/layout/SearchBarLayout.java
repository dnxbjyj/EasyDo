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
	// 状态
	private boolean isActive;
	// 输入的查询字符串
	private String query;

	// SearchBar整体布局
	private LinearLayout searchBar;

	// SearchBar未激活时候默认显示的布局
	private LinearLayout blankLl;
	// 未激活布局中的Tv
	private TextView blankTv;

	// SearchBar激活后的搜索栏
	private LinearLayout searchLl;
	// 内容输入Et
	private EditText inputEt;
	// 清除输入内容按钮
	private ImageButton clearInputIb;
	// 语音识别输入按钮
	private ImageButton dictationIb;
	// 高级搜索按钮
	private TextView advancedTv;
	// 取消按钮
	private TextView cancelTv;

	public SearchBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.search_bar, this);
		isActive = false;
		// 初始化布局
		initUI();
	}

	// 初始化布局
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

	// 设置SearchBar显示的hint文字
	public void setHint(String hint) {
		blankTv.setText(hint);
		inputEt.setHint(hint);
	}

	// SearchBar的一系列事件监听
	public interface SearchBarListener {
		// 点击未激活状态的SearchBar的点击事件监听
		void onBlankClick();

		// inputEt焦点改变事件的监听
		void onFocusChange(boolean hasFocus);

		// inputEt输入内容变化时候的事件监听
		void onInputChange(String newInput);

		// SearchBar语音听写按钮的点击监听事件
		void onDictationClick();

		// "高级"按钮点击事件监听
		void onAdvancedClick();

		// "取消"按钮点击事件监听
		void onCancelClick();
	}

	// SearchBar一系列事件监听
	public void setSearchBarListener(final Context context,
			final SearchBarListener listener) {
		// 点击未激活状态的SearchBar的点击事件监听
		blankLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setActive(true);
				listener.onBlankClick();
			}
		});

		// inputEt焦点改变事件的监听
		inputEt.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				listener.onFocusChange(hasFocus);
			}
		});

		// inputEt输入内容变化时候的事件监听
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

		// SearchBar语音听写按钮的点击监听事件
		dictationIb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onDictationClick();
			}
		});

		// "高级"按钮点击事件监听
		advancedTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onAdvancedClick();
			}
		});

		// "取消"按钮点击事件监听
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

	// 隐藏高级搜索按钮
	public void hideAdvanced() {
		advancedTv.setVisibility(View.GONE);
	}

}
