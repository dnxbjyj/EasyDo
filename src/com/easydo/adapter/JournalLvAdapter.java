package com.easydo.adapter;

import java.util.List;

import com.easydo.activity.JournalActivity;
import com.easydo.activity.ScheduleActivity;
import com.easydo.model.SystemConfig;
import com.easydo.util.TypefaceUtil;
import com.jiayongji.easydo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class JournalLvAdapter extends ArrayAdapter<JournalLvItem> {
	private int mResourceId;

	private SystemConfig systemConfig;
	// 字体设置工具
	private TypefaceUtil mTypefaceUtil;
	// 设置的ttf文件的路径
	private String mTTFPath;

	public JournalLvAdapter(Context context, int textViewResourceId,
			List<JournalLvItem> objects) {
		super(context, textViewResourceId, objects);
		mResourceId = textViewResourceId;

		systemConfig = ((JournalActivity) getContext()).getSystemConfig();
		mTTFPath = systemConfig.getJournalContentTypeface();
		if (mTTFPath == null) {
			mTTFPath = "default";
		}
		mTypefaceUtil = new TypefaceUtil(context, mTTFPath);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		JournalLvItem item = getItem(position);

		View view;
		ViewHolder viewHolder;
		if (null == convertView) {
			view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.journalDateTimeTv = (TextView) view
					.findViewById(R.id.journal_date_time_tv);
			viewHolder.journalContentTv = (TextView) view
					.findViewById(R.id.journal_content_tv);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.journalDateTimeTv.setText(item.getCreateTime());

		viewHolder.journalContentTv.setTextColor(getContext().getResources()
				.getColor(R.drawable.LightBlack));
		if (mTTFPath.equals("default")) {
			mTypefaceUtil.setTypeface(viewHolder.journalContentTv, false);
		} else {
			mTypefaceUtil.setTypeface(viewHolder.journalContentTv, true);
		}

		viewHolder.journalContentTv.setText(item.getContent());

		return view;

	}

	class ViewHolder {
		TextView journalDateTimeTv;
		TextView journalContentTv;
	}

}
