package com.easydo.adapter;

/**
 * 日程显示页面的日程item实体类
 *
 */

public class ScheduleLvItem {

	// item的类别
	public final static int TYPE_DATE_NODE = 0; // 日期结点
	public final static int TYPE_SCHEDULE_NODE = 1; // 日程结点

	// 如果是日程结点，在数据库表中的id
	private int id;

	// item的类型
	private int type;

	// item最左边的日程时分字符串
	private String time;

	// 紧接着的显示一个日历或者是圆圈的TextView的背景图片的id,该背景图片随着type的不同也不同
	// type = TYPE_DATE_NODE：一个日历的图片
	// type = TYPE_SCHEDULE_NODE:一个圆圈的图片，用于完成事项打勾
	private int nodeImageId;

	// 日程的完成情况
	private boolean isDone;

	// 日程的日期（几号，取值：1~31）,用于在日期结点的小日历中中显示
	private int dayOfMonth;

	// 日程的具体日期（格式：5月1日 星期日），用于在日期结点中显示
	private String dateString;

	// 日程的日期字符串，格式："2016-05-12"
	private String date;

	// 日程的内容
	private String content;

	// 日程标签
	private int tag;

	// 日期结点构造函数方法
	public ScheduleLvItem(int type, int nodeImageId, int dayOfMonth,
			String dateString, String date) {
		this.type = type;
		this.nodeImageId = nodeImageId;
		this.dayOfMonth = dayOfMonth;
		this.dateString = dateString;
		this.date = date;
	}

	// schedule结点构造方法
	public ScheduleLvItem(int id, int type, String time, int nodeImageId,
			boolean isDone, String content, int tag) {
		this.id = id;
		this.type = type;
		this.time = time;
		this.nodeImageId = nodeImageId;
		this.isDone = isDone;
		this.content = content;
		this.tag = tag;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public String getTime() {
		return time;
	}

	public int getNodeImageId() {
		return nodeImageId;
	}

	public boolean isDone() {
		return isDone;
	}

	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public String getDateString() {
		return dateString;
	}

	public String getContent() {
		return content;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public int getTag() {
		return tag;
	}

	public String getDate() {
		return date;
	}

}
