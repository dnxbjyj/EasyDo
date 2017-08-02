package com.easydo.layout;

import com.easydo.activity.JournalActivity;
import com.easydo.activity.ScheduleActivity;
import com.easydo.activity.SpecialScheduleActivity;
import com.easydo.model.Journal;
import com.easydo.model.Schedule;
import com.jiayongji.easydo.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class HomePageBottomLayout extends BaseLinearLayout implements
		OnClickListener {

	// �ճ�Ll
	private LinearLayout scheduleLl;
	// �ճ�Iv
	private ImageView scheduleIv;
	// �ճ�Tv
	private TextView scheduleTv;

	// ��־��Ll
	private LinearLayout JournalLl;
	// ��־��Iv
	private ImageView JournalIv;
	// ��־��Tv
	private TextView JournalTv;

	// �����ճ�Ll
	private LinearLayout specialScheduleLl;
	// �����ճ�Iv
	private ImageView specialScheduleleIv;
	// �����ճ�Tv
	private TextView specialScheduleTv;

	public HomePageBottomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.home_page_bottom, this);

		// ��ʼ��UI������Context��class���ж��ĸ���ť�Ǽ���״̬
		initUI(context);
	}

	// ��ʼ��UI
	private void initUI(Context context) {
		scheduleLl = (LinearLayout) findViewById(R.id.schedule_switch);
		scheduleIv = (ImageView) findViewById(R.id.schedule_iv);
		scheduleTv = (TextView) findViewById(R.id.schedule_tv);

		JournalLl = (LinearLayout) findViewById(R.id.journal_switch);
		JournalIv = (ImageView) findViewById(R.id.journal_iv);
		JournalTv = (TextView) findViewById(R.id.journal_tv);

		specialScheduleLl = (LinearLayout) findViewById(R.id.special_schedule_switch);
		specialScheduleleIv = (ImageView) findViewById(R.id.special_schedule_iv);
		specialScheduleTv = (TextView) findViewById(R.id.special_schedule_tv);

		setSwitchStatus(context);

		scheduleLl.setOnClickListener(this);
		JournalLl.setOnClickListener(this);
		specialScheduleLl.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.schedule_switch:
			if (scheduleLl.getTag().equals("INACTIVE")) {
				Intent intent = new Intent(mContext, ScheduleActivity.class);
				mContext.startActivity(intent);
			}
			break;
		case R.id.journal_switch:
			if (JournalLl.getTag().equals("INACTIVE")) {
				Intent intent = new Intent(mContext, JournalActivity.class);
				mContext.startActivity(intent);
			}
			break;
		case R.id.special_schedule_switch:
			if (specialScheduleLl.getTag().equals("INACTIVE")) {
				Intent intent = new Intent(mContext,
						SpecialScheduleActivity.class);
				mContext.startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	// ����context��class�����ð�ť�ļ���״̬
	private void setSwitchStatus(Context context) {
		String contextClassName = context.getClass().getName();

		if (contextClassName.equals(ScheduleActivity.class.getName())) {
			setScheduleLlStatus(true);
			setJournalLlStatus(false);
			setSpecialScheduleLlStatus(false);
		} else if (contextClassName.equals(JournalActivity.class.getName())) {
			setScheduleLlStatus(false);
			setJournalLlStatus(true);
			setSpecialScheduleLlStatus(false);
		} else if (contextClassName.equals(SpecialScheduleActivity.class
				.getName())) {
			setScheduleLlStatus(false);
			setJournalLlStatus(false);
			setSpecialScheduleLlStatus(true);
		}
	}

	// �����ճ̰�ť�ļ���״̬
	private void setScheduleLlStatus(boolean isActive) {
		if (isActive) {
			scheduleLl.setTag("ACTIVE");
			scheduleIv.setImageResource(R.drawable.schedule2_40);
			scheduleTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));
		} else {
			scheduleLl.setTag("INACTIVE");
			scheduleIv.setImageResource(R.drawable.schedule1_40);
			scheduleTv.setTextColor(getResources()
					.getColor(R.drawable.IconGray));
		}
	}

	// ������־����ť�ļ���״̬
	private void setJournalLlStatus(boolean isActive) {
		if (isActive) {
			JournalLl.setTag("ACTIVE");
			JournalIv.setImageResource(R.drawable.journal2_40);
			JournalTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));
		} else {
			JournalLl.setTag("INACTIVE");
			JournalIv.setImageResource(R.drawable.journal1_40);
			JournalTv
					.setTextColor(getResources().getColor(R.drawable.IconGray));
		}
	}

	// ���������ճ̰�ť�ļ���״̬
	private void setSpecialScheduleLlStatus(boolean isActive) {
		if (isActive) {
			specialScheduleLl.setTag("ACTIVE");
			specialScheduleleIv
					.setImageResource(R.drawable.special_schedule2_40);
			specialScheduleTv.setTextColor(getResources().getColor(
					R.drawable.ThemeDefault));
		} else {
			specialScheduleLl.setTag("INACTIVE");
			specialScheduleleIv
					.setImageResource(R.drawable.special_schedule1_40);
			specialScheduleTv.setTextColor(getResources().getColor(
					R.drawable.IconGray));
		}
	}

}
