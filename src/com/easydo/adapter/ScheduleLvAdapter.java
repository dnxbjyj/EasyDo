package com.easydo.adapter;

import java.util.List;

import com.easydo.activity.BaseActivity;
import com.easydo.activity.JournalActivity;
import com.easydo.activity.ScheduleActivity;
import com.easydo.constant.GlobalConfig;
import com.easydo.db.EasyDoDB;
import com.easydo.model.Schedule;
import com.easydo.model.SystemConfig;
import com.easydo.util.SbcDbcUtil;
import com.easydo.util.ToastUtil;
import com.easydo.util.TypefaceUtil;
import com.jiayongji.easydo.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleLvAdapter extends ArrayAdapter<ScheduleLvItem> {

	private int mResourceId;

	private SystemConfig systemConfig;
	// �������ù���
	private TypefaceUtil mTypefaceUtil;
	// ���õ�ttf�ļ���·��
	private String mTTFPath;

	public ScheduleLvAdapter(Context context, int textViewResourceId,
			List<ScheduleLvItem> objects) {
		super(context, textViewResourceId, objects);
		mResourceId = textViewResourceId;

		systemConfig = ((ScheduleActivity) getContext()).getSystemConfig();
		mTTFPath = systemConfig.getScheduleContentTypeface();
		if (mTTFPath == null) {
			mTTFPath = "default";
		}
		mTypefaceUtil = new TypefaceUtil(context, mTTFPath);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ScheduleLvItem item = getItem(position);

		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(mResourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.timeTv = (TextView) view.findViewById(R.id.time_tv);
			viewHolder.nodeImageTv = (TextView) view
					.findViewById(R.id.node_image_tv);
			viewHolder.contentTv = (TextView) view
					.findViewById(R.id.content_tv);
			viewHolder.todayJournalLl = (LinearLayout) view
					.findViewById(R.id.today_journal_ll);

			view.setTag(viewHolder);
			viewHolder.nodeImageTv.setTag(item);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
			viewHolder.nodeImageTv.setTag(item);
		}

		if (item.getType() == ScheduleLvItem.TYPE_DATE_NODE) {
			view.setClickable(true);

			viewHolder.timeTv.setVisibility(View.INVISIBLE);
			viewHolder.nodeImageTv
					.setBackgroundResource(R.drawable.node_image_date);
			viewHolder.nodeImageTv.setText(item.getDayOfMonth() + "");

			mTypefaceUtil.setDefaultTypeFace(viewHolder.contentTv, false);

			viewHolder.contentTv.setTextColor(getContext().getResources()
					.getColor(R.drawable.TextDefault));
			viewHolder.contentTv.setTextSize(14);
			viewHolder.contentTv.setText(item.getDateString());

			viewHolder.todayJournalLl.setVisibility(View.VISIBLE);
			final String date = item.getDate();
			viewHolder.todayJournalLl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getContext(),
							JournalActivity.class);
					intent.putExtra("date", date);
					getContext().startActivity(intent);
				}
			});
		} else {
			view.setClickable(false);
			// �жϵ�ǰϵͳ�����Ƿ�����ʾ�ճ̵ľ���ʱ�䣨ʱ:�֣�
			EasyDoDB db = EasyDoDB.getInstance(getContext());
			SystemConfig config = db.loadSystemConfig(null, null);
			int timeLineShowDetailTime = config.getTimeLineShowDetailTime();

			if (timeLineShowDetailTime == SystemConfig.TIME_LINE_NOT_SHOW_DETAIL_TIME) {
				viewHolder.timeTv.setVisibility(View.INVISIBLE);
			} else {
				viewHolder.timeTv.setVisibility(View.VISIBLE);
				viewHolder.timeTv.setText(item.getTime());
			}

			viewHolder.nodeImageTv.setText("");
			if (item.isDone()) {
				viewHolder.nodeImageTv
						.setBackgroundResource(Schedule.TAG_NODE_IMAGE_DONE_ID[item
								.getTag()]);
			} else {
				viewHolder.nodeImageTv
						.setBackgroundResource(Schedule.TAG_NODE_IMAGE_NOT_DONE_ID[item
								.getTag()]);
			}

			final ViewHolder finalViewHolder = viewHolder;
			final ScheduleLvItem finalItem = item;

			// �Ǹ�ԲȦ�ĵ���¼��������е㸴��,��Ϊ�ڵ��ʱ��Ҫ�Ķ�����ֶ�ֵ��iv��ͼƬ����Ҫ�����ݿ���ֶ�ֵ
			viewHolder.nodeImageTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ScheduleLvItem scheduleItem = (ScheduleLvItem) finalViewHolder.nodeImageTv
							.getTag();
					EasyDoDB easyDoDB = EasyDoDB.getInstance(getContext());
					if (scheduleItem.isDone()) {
						scheduleItem.setDone(false);

						// �޸����ݿ�
						easyDoDB.updateDB("Schedule", "status",
								Schedule.STATUS_UNFINISHED + "", "id",
								scheduleItem.getId() + "");
						finalViewHolder.nodeImageTv
								.setBackgroundResource(Schedule.TAG_NODE_IMAGE_NOT_DONE_ID[finalItem
										.getTag()]);
						if (((ScheduleActivity) getContext()).getSearchBar()
								.isActive()) {
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									((ScheduleActivity) getContext())
											.freshSchedulesLv();
								}
							}, 500);

						}

						ToastUtil.showShort(getContext(), "������黹��Ҫ����Ŭ��Ŷ");
					} else {
						scheduleItem.setDone(true);

						// �޸����ݿ�
						easyDoDB.updateDB("Schedule", "status",
								Schedule.STATUS_FINISHED + "", "id",
								scheduleItem.getId() + "");

						finalViewHolder.nodeImageTv
								.setBackgroundResource(Schedule.TAG_NODE_IMAGE_DONE_ID[finalItem
										.getTag()]);

						if (((ScheduleActivity) getContext()).getSearchBar()
								.isActive()) {
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									((ScheduleActivity) getContext())
											.freshSchedulesLv();
								}
							}, 500);
						}

						ToastUtil.showShort(getContext(), "�����һ�����飬�������");
					}
				}
			});

			viewHolder.todayJournalLl.setVisibility(View.GONE);

			// ��������
			if (mTTFPath.equals("default")) {
				mTypefaceUtil.setDefaultTypeFace(viewHolder.contentTv, false);
			} else {
				mTypefaceUtil.setTypeface(viewHolder.contentTv, true);
			}

			// ������ɫ
			if (systemConfig.getShowScheduleColorful() == SystemConfig.SHOW_SCHEDULE_COLORFUL) {
				viewHolder.contentTv.setTextColor(Schedule.TAG_COLOR_ARR[item
						.getTag()]);
			} else {
				viewHolder.contentTv.setTextColor(getContext().getResources()
						.getColor(R.drawable.TextBlack));
			}

			viewHolder.contentTv.setTextSize(16);
			viewHolder.contentTv.setText(item.getContent());

		}

		return view;

	}

	class ViewHolder {
		TextView timeTv;
		TextView nodeImageTv;
		TextView contentTv;
		LinearLayout todayJournalLl;
	}

}
