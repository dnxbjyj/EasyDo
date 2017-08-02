package com.easydo.adapter;

/**
 * SpecialSchedule列表项实体类
 *
 */

public class SpecialScheduleLvItem {
	// 在数据库表中的id
	private int id;
	// 标题
	private String title;
	// 分类
	private int type;
	// 日期，格式："2016-04-22"
	private String date;
	// 备注
	private String remark;
	// 状态
	private int status;

	public SpecialScheduleLvItem(int id, String title, int type, String date,
			String remark) {
		super();
		this.id = id;
		this.title = title;
		this.type = type;
		this.date = date;
		this.remark = remark;
	}

	public SpecialScheduleLvItem(int id, String title, int type, String date,
			String remark, int status) {
		super();
		this.id = id;
		this.title = title;
		this.type = type;
		this.date = date;
		this.remark = remark;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
