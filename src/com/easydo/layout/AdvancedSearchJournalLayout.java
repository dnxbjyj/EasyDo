package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

public class AdvancedSearchJournalLayout extends BaseAdvancedSearchLayout {

	public AdvancedSearchJournalLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(
				R.layout.dialog_advanced_search_journal, this);
	}

	// ���ݵ�ǰѡ��ĸ����������������ݿ��ѯ����������䣬���ô˷���ʱ�Ѿ���֤���������ݵĺϷ���
	public AdvancedQueryCondition getQueryCondition() {
		String keywords;
		String condition;
		String[] values = new String[2];

		if (keywodsStatus == KEYWORDS_STATUS_INPUT_KEYWORDS) {
			keywords = inputKeywordsEt.getText().toString();
		} else {
			keywords = null;
		}

		if (dateStatus == DATE_STATUS_CHOOSE) {
			String startDateTime = getStartDate() + " 00:00:00";
			String endDateTime = getEndDate() + " 23:59:59";
			condition = " create_time >= ?  and create_time <= ? ";
			values[0] = startDateTime;
			values[1] = endDateTime;
		} else {
			condition = null;
			values = null;
		}
		return new AdvancedQueryCondition(keywords, condition, values);
	}

}
