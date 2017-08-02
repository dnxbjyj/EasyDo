package com.easydo.adapter;

import java.util.List;

import com.easydo.model.SpecialSchedule;
import com.easydo.util.DateTimeUtil;
import com.jiayongji.easydo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpecialScheduleLvAdapter extends
		ArrayAdapter<SpecialScheduleLvItem> {

	private int mResourceId;

	public SpecialScheduleLvAdapter(Context context, int textViewResourceId,
			List<SpecialScheduleLvItem> objects) {
		super(context, textViewResourceId, objects);
		mResourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SpecialScheduleLvItem item = getItem(position);

		View view;
		ViewHolder viewHolder;

		if (null == convertView) {
			view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.titleTv = (TextView) view.findViewById(R.id.title_tv);
			viewHolder.daysNumTv = (TextView) view.findViewById(R.id.day_num);
			viewHolder.dayTextTv = (TextView) view
					.findViewById(R.id.day_text_tv);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}

		int type = item.getType();
		String date = item.getDate();
		long daysNum = 0;

		// �ж������ճ�����
		if (type == SpecialSchedule.TYPE_ANNIVERSARY) {
			viewHolder.titleTv.setText(item.getTitle() + "�Ѿ�");
			daysNum = DateTimeUtil.getDaysNumDiff(
					DateTimeUtil.getCurrentDateString(), date);
			viewHolder.daysNumTv.setBackgroundResource(R.drawable.textbar_mid);
			viewHolder.dayTextTv
					.setBackgroundResource(R.drawable.textbar_right);
		} else if (type == SpecialSchedule.TYPE_BIRTHDAY) {
			viewHolder.titleTv.setText("�������գ�" + item.getTitle() + "����");
			// ��������յĻ�����ȡ��һ�����յ�date
			date = DateTimeUtil.getNextBirthdayDate(date);
			daysNum = DateTimeUtil.getDaysNumDiff(date,
					DateTimeUtil.getCurrentDateString());
			viewHolder.daysNumTv.setBackgroundResource(R.drawable.textbar_mid2);
			viewHolder.dayTextTv
					.setBackgroundResource(R.drawable.textbar_right2);
		} else if (type == SpecialSchedule.TYPE_COUNTDOWN) {
			daysNum = DateTimeUtil.getDaysNumDiff(date,
					DateTimeUtil.getCurrentDateString());

			if (daysNum >= 0) {
				viewHolder.titleTv.setText("����" + item.getTitle() + "����");
			} else {
				viewHolder.titleTv.setText(item.getTitle() + "�Ѿ���ȥ��");
				daysNum = 0 - daysNum;
			}

			viewHolder.daysNumTv.setBackgroundResource(R.drawable.textbar_mid2);
			viewHolder.dayTextTv
					.setBackgroundResource(R.drawable.textbar_right2);
		}

		viewHolder.daysNumTv.setText(daysNum + "");

		return view;

	}

	class ViewHolder {
		TextView titleTv;
		TextView daysNumTv;
		TextView dayTextTv;
	}

}
