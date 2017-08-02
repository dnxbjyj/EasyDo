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

	// 日程Ll
	private LinearLayout scheduleLl;
	// 日程Iv
	private ImageView scheduleIv;
	// 日程Tv
	private TextView scheduleTv;

	// 日志本Ll
	private LinearLayout JournalLl;
	// 日志本Iv
	private ImageView JournalIv;
	// 日志本Tv
	private TextView JournalTv;

	// 特殊日程Ll
	private LinearLayout specialScheduleLl;
	// 特殊日程Iv
	private ImageView specialScheduleleIv;
	// 特殊日程Tv
	private TextView specialScheduleTv;

	public HomePageBottomLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.home_page_bottom, this);

		// 初始化UI，根据Context的class来判断哪个按钮是激活状态
		initUI(context);
	}

	// 初始化UI
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

	// 根据context的class来设置按钮的激活状态
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

	// 设置日程按钮的激活状态
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

	// 设置日志本按钮的激活状态
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

	// 设置特殊日程按钮的激活状态
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
