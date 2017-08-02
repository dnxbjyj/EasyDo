package com.easydo.activity;

import java.util.Calendar;

import com.easydo.layout.BaseTitleLayout;
import com.easydo.layout.CustomRatingBarLayout;
import com.easydo.model.Schedule;
import com.easydo.model.SpecialSchedule;
import com.easydo.util.DateTimeUtil;
import com.jiayongji.easydo.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class StatisticsActivity extends BaseActivity implements OnClickListener {

	// 时间段常量
	public static final int TIME_PERIOD_TODAY = 0; // 今天
	public static final int TIME_PERIOD_CUR_WEEK = 1; // 本周
	public static final int TIME_PERIOD_CUR_MONTH = 2; // 本月
	public static final int TIME_PERIOD_CUR_YEAR = 3; // 今年
	public static final int TIME_PERIOD_ALL = 4; // 所有
	// 时间段字符串数组
	public static final String[] TIME_PERIOD_TEXT_ARRAY = { "今天", "本周", "本月",
			"今年", "所有" };

	// 当前选择的时间段，默认为今天
	private int selectedTimePeriod;

	// 标题栏的控件
	private BaseTitleLayout titleLayout;
	private ImageButton titleLeftIb;
	private TextView titleBaseMiddleCustomTv;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// 数据控件
	private TextView dateTv;
	private TextView totalNumTv;
	private TextView finishedNumTv;
	private TextView unfinishedNumTv;
	private CustomRatingBarLayout efficiencyRatingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();

	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_statistics);

		selectedTimePeriod = TIME_PERIOD_TODAY;
		/**
		 * 标题栏
		 */
		titleLayout = (BaseTitleLayout) findViewById(R.id.title_layout);
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleLeftIb.setImageResource(R.drawable.back1_64);
		titleBaseMiddleCustomTv = (TextView) findViewById(R.id.title_base_middle_custom_tv);
		titleBaseMiddleCustomTv.setVisibility(View.VISIBLE);
		titleBaseMiddleCustomTv.setText("数据统计");
		titleBaseMiddleCustomTv.setTextSize(18);
		findViewById(R.id.title_base_divider_v).setVisibility(View.VISIBLE);
		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);
		// 现在先隐藏着，后期可以做成一个分享按钮
		titleRightIb.setVisibility(View.INVISIBLE);

		titleLeftIb.setOnClickListener(this);

		titleMiddleTv.setText(TIME_PERIOD_TEXT_ARRAY[selectedTimePeriod]
				+ "  >");
		titleMiddleTv.setOnClickListener(this);

		/**
		 * 数据显示控件
		 */
		dateTv = (TextView) findViewById(R.id.date_tv);
		totalNumTv = (TextView) findViewById(R.id.total_num_tv);
		finishedNumTv = (TextView) findViewById(R.id.finished_num_tv);
		unfinishedNumTv = (TextView) findViewById(R.id.unfinished_num_tv);
		efficiencyRatingBar = (CustomRatingBarLayout) findViewById(R.id.rating_bar);

		showResult(selectedTimePeriod);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_base_left_ib:
			finish();
			break;
		case R.id.title_base_middle_tv:
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					StatisticsActivity.this);
			dialog.setTitle("选择时间段");

			dialog.setItems(TIME_PERIOD_TEXT_ARRAY,
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							selectedTimePeriod = which;
							titleMiddleTv
									.setText(TIME_PERIOD_TEXT_ARRAY[selectedTimePeriod]
											+ "  >");
							showResult(selectedTimePeriod);
						}
					});
			dialog.show();
			break;
		default:
			break;
		}
	}

	// 显示数据统计结果
	private void showResult(int selectedTimePeriod) {
		String curDateTime = DateTimeUtil.getCurrentDateTimeString();
		String curDate = DateTimeUtil.getCurrentDateString();
		Calendar curCalendar = DateTimeUtil.getCurrentCalendar();

		String dateText;

		String totalCondition = "";
		String[] totalValues = null;
		String finishedCondition = "";
		String[] finishedValues = null;
		String unfinishedCondition = "";
		String[] unfinishedValues = null;

		int totalNum;
		int finishedNum;
		int unfinishedNum;
		float efficiency;

		switch (selectedTimePeriod) {
		case TIME_PERIOD_TODAY:
			dateTv.setText(DateTimeUtil.getShortDateTimeOfWeek(curDateTime));

			totalCondition = " status != ? and start_time like ? ";
			totalValues = new String[] { Schedule.STATUS_DELETED + "",
					curDate + "%" };

			finishedCondition = " status = ? and start_time like ? ";
			finishedValues = new String[] { Schedule.STATUS_FINISHED + "",
					curDate + "%" };

			unfinishedCondition = " status = ? and start_time like ? ";
			unfinishedValues = new String[] { Schedule.STATUS_UNFINISHED + "",
					curDate + "%" };
			break;
		case TIME_PERIOD_CUR_WEEK:
			Calendar firstDayOfWeek = DateTimeUtil
					.getDateBeginTimeCalendar(DateTimeUtil
							.getFirstDayCalendarOfWeek(curCalendar,
									Calendar.MONDAY));
			Calendar lastDayOfWeek = DateTimeUtil
					.getDateEndTimeCalendar(DateTimeUtil
							.getLastDayCalendarOfWeek(curCalendar,
									Calendar.MONDAY));

			dateText = (firstDayOfWeek.get(Calendar.MONTH) + 1) + "月"
					+ firstDayOfWeek.get(Calendar.DAY_OF_MONTH) + "日" + " ~ "
					+ (lastDayOfWeek.get(Calendar.MONTH) + 1) + "月"
					+ lastDayOfWeek.get(Calendar.DAY_OF_MONTH) + "日";

			dateTv.setText(dateText);

			String firstDayOfWeekStr = DateTimeUtil
					.CalendarToString(firstDayOfWeek);
			String lastDayOfWeekStr = DateTimeUtil
					.CalendarToString(lastDayOfWeek);

			totalCondition = " status != ? and start_time >= ? and start_time <= ? ";
			totalValues = new String[] { Schedule.STATUS_DELETED + "",
					firstDayOfWeekStr, lastDayOfWeekStr };

			finishedCondition = " status = ? and start_time >= ? and start_time <= ? ";
			finishedValues = new String[] { Schedule.STATUS_FINISHED + "",
					firstDayOfWeekStr, lastDayOfWeekStr };

			unfinishedCondition = " status = ? and start_time >= ? and start_time <= ? ";
			unfinishedValues = new String[] { Schedule.STATUS_UNFINISHED + "",
					firstDayOfWeekStr, lastDayOfWeekStr };
			break;
		case TIME_PERIOD_CUR_MONTH:
			dateText = (curCalendar.get(Calendar.MONTH) + 1) + "月";
			dateTv.setText(dateText);

			totalCondition = " status != ? and start_time like ? ";
			totalValues = new String[] { Schedule.STATUS_DELETED + "",
					curDate.substring(0, 7) + "%" };

			finishedCondition = " status = ? and start_time like ? ";
			finishedValues = new String[] { Schedule.STATUS_FINISHED + "",
					curDate.substring(0, 7) + "%" };

			unfinishedCondition = " status = ? and start_time like ? ";
			unfinishedValues = new String[] { Schedule.STATUS_UNFINISHED + "",
					curDate.substring(0, 7) + "%" };
			break;
		case TIME_PERIOD_CUR_YEAR:
			dateText = curCalendar.get(Calendar.YEAR) + "年";
			dateTv.setText(dateText);

			totalCondition = " status != ? and start_time like ? ";
			totalValues = new String[] { Schedule.STATUS_DELETED + "",
					curDate.substring(0, 4) + "%" };

			finishedCondition = " status = ? and start_time like ? ";
			finishedValues = new String[] { Schedule.STATUS_FINISHED + "",
					curDate.substring(0, 4) + "%" };

			unfinishedCondition = " status = ? and start_time like ? ";
			unfinishedValues = new String[] { Schedule.STATUS_UNFINISHED + "",
					curDate.substring(0, 4) + "%" };
			break;
		case TIME_PERIOD_ALL:
			dateTv.setText("全部日程");

			totalCondition = " status != ?";
			totalValues = new String[] { Schedule.STATUS_DELETED + "" };

			finishedCondition = " status = ? ";
			finishedValues = new String[] { Schedule.STATUS_FINISHED + "" };

			unfinishedCondition = " status = ? ";
			unfinishedValues = new String[] { Schedule.STATUS_UNFINISHED + "" };
			break;
		default:
			break;
		}

		totalNum = getScheduleNum(totalCondition, totalValues);
		finishedNum = getScheduleNum(finishedCondition, finishedValues);
		unfinishedNum = getScheduleNum(unfinishedCondition, unfinishedValues);
		if (totalNum > 0) {
			efficiency = ((float) finishedNum) / totalNum;
		} else {
			efficiency = 0;
		}

		totalNumTv.setText(totalNum + "");
		finishedNumTv.setText(finishedNum + "");
		unfinishedNumTv.setText(unfinishedNum + "");

		efficiencyRatingBar.setStarNum(efficiency);

		StringBuilder tipStr = new StringBuilder("");
		int starNum = efficiencyRatingBar.getStarNum();
		if (totalNum == 0) {
			tipStr.append("今天没有制定日程计划，好好放松一下吧！");
		} else if (selectedTimePeriod != TIME_PERIOD_ALL) {
			tipStr.append(TIME_PERIOD_TEXT_ARRAY[selectedTimePeriod] + "效率指数为"
					+ starNum + "颗星，");

		} else {
			tipStr.append("到目前为止，总的效率指数为" + starNum + "颗星，");
		}

		if (starNum < CustomRatingBarLayout.STAR_TOTAL_NUM / 2.0
				&& totalNum != 0) {
			tipStr.append("要加油哦！");
		} else if (starNum < CustomRatingBarLayout.STAR_TOTAL_NUM
				&& totalNum != 0) {
			tipStr.append("继续保持哦！");
		} else if (totalNum != 0) {
			tipStr.append("非常完美！");
		}

		((TextView) findViewById(R.id.tip_tv)).setText(tipStr.toString());
	}

	// 从数据库查询满足条件的日志，返回数量
	private int getScheduleNum(String condition, String[] values) {
		return easyDoDB.getScheduleNum(condition, values);
	}

}
