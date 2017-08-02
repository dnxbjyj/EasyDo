package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlideSwitch extends BaseLinearLayout {

	// ����switchͼƬ��Tv
	private TextView switchTv;

	// �����Ƿ��ǿ���״̬
	private boolean isOn;

	public SlideSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.slide_switch, this);

		initUI();
	}

	// ��ʼ������
	private void initUI() {
		switchTv = (TextView) findViewById(R.id.switch_tv);
		// Ĭ���ǲ�����
		setOff();
	}

	// �¼������ӿ�
	public interface SlideSwitchListener {
		public void onClick(boolean isOn);
	}

	// ��������¼�
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

	// ���ÿ���Ϊ����
	public void setOn() {
		isOn = true;
		switchTv.setBackgroundResource(R.drawable.slide_switch_on);
	}

	// ���ÿ���Ϊ�ر�
	public void setOff() {
		isOn = false;
		switchTv.setBackgroundResource(R.drawable.slide_switch_off);
	}

	public boolean isOn() {
		return isOn;
	}

}
