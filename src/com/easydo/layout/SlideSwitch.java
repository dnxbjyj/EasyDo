package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlideSwitch extends BaseLinearLayout {

	// 放置switch图片的Tv
	private TextView switchTv;

	// 开关是否是开启状态
	private boolean isOn;

	public SlideSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.slide_switch, this);

		initUI();
	}

	// 初始化布局
	private void initUI() {
		switchTv = (TextView) findViewById(R.id.switch_tv);
		// 默认是不开启
		setOff();
	}

	// 事件监听接口
	public interface SlideSwitchListener {
		public void onClick(boolean isOn);
	}

	// 监听点击事件
	public void setOnSlideSwitchListener(final Context context,
			final SlideSwitchListener listener) {
		switchTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOn) {
					setOff();
				} else {
					setOn();
				}

				listener.onClick(isOn);
			}
		});
	}

	// 设置开关为开启
	public void setOn() {
		isOn = true;
		switchTv.setBackgroundResource(R.drawable.slide_switch_on);
	}

	// 设置开关为关闭
	public void setOff() {
		isOn = false;
		switchTv.setBackgroundResource(R.drawable.slide_switch_off);
	}

	public boolean isOn() {
		return isOn;
	}

}
