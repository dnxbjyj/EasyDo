package com.easydo.layout;

import com.easydo.model.Schedule;
import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class AdvancedSearchScheduleLayout extends BaseAdvancedSearchLayout {

	public AdvancedSearchScheduleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(
				R.layout.dialog_advanced_search_schedule, this);

		initUI();
	}

	// 初始化界面
	private void initUI() {
		findViewById(R.id.status_divide_v1).setVisibility(View.VISIBLE);
		findViewById(R.id.status_divide_v2).setVisibility(View.VISIBLE);
		statusTr.setVisibility(View.VISIBLE);
		tagTr.setVisibility(View.VISIBLE);
	}

	// 根据当前选择的各项条件，生成数据库查询语句的条件语句，调用此方法时已经保证了输入数据的合法性
	public AdvancedQueryCondition getQueryCondition() {
		String keywords;
		StringBuilder condition = new StringBuilder();
		String[] values;

		if (keywodsStatus == KEYWORDS_STATUS_INPUT_KEYWORDS) {
			keywords = inputKeywordsEt.getText().toString();
		} else {
			keywords = null;
		}

		if (dateStatus == DATE_STATUS_CHOOSE) {
			values = new String[4];
			String startDateTime = getStartDate() + " 00:00:00";
			String endDateTime = getEndDate() + " 23:59:59";
			condition.append(" start_time >= ?  and start_time <= ? ");
			values[0] = startDateTime;
			values[1] = endDateTime;

			if (scheduleStatus == SCHEDULE_STATUS_ANY) {
				condition.append(" and status >= ? ");
				values[2] = Schedule.STATUS_UNFINISHED + "";
			} else if (scheduleStatus == SCHEDULE_STATUS_NOT_DONE) {
				condition.append(" and status = ? ");
				values[2] = Schedule.STATUS_UNFINISHED + "";
			} else {
				condition.append(" and status = ? ");
				values[2] = Schedule.STATUS_FINISHED + "";
			}

			if (scheduleTag != SCHEDULE_TAG_ANY) {
				condition.append(" and tag = ? ");
				values[3] = scheduleTag + "";
			} else {
				condition.append(" and tag > ? ");
				values[3] = scheduleTag + "";
			}
		} else {
			values = new String[2];
			if (scheduleStatus == SCHEDULE_STATUS_ANY) {
				condition.append(" status >= ? ");
				values[0] = Schedule.STATUS_UNFINISHED + "";
			} else if (scheduleStatus == SCHEDULE_STATUS_NOT_DONE) {
				condition.append(" status = ? ");
				values[0] = Schedule.STATUS_UNFINISHED + "";
			} else {
				condition.append(" status = ? ");
				values[0] = Schedule.STATUS_FINISHED + "";
			}

			if (scheduleTag != SCHEDULE_TAG_ANY) {
				condition.append(" and tag = ? ");
				values[1] = scheduleTag + "";
			} else {
				condition.append(" and tag > ? ");
				values[1] = scheduleTag + "";
			}
		}
		return new AdvancedQueryCondition(keywords, condition.toString(),
				values);
	}
}
