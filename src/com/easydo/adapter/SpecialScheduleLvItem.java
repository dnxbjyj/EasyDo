package com.easydo.adapter;

/**
 * SpecialSchedule�б���ʵ����
 *
 */

public class SpecialScheduleLvItem {
	// �����ݿ���е�id
	private int id;
	// ����
	private String title;
	// ����
	private int type;
	// ���ڣ���ʽ��"2016-04-22"
	private String date;
	// ��ע
	private String remark;
	// ״̬
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
