package com.easydo.model;

import com.easydo.util.DateTimeUtil;

/**
 * ������Journal
 * 
 * ���ܣ����ݿ�Journal���ʵ����
 */

public class Journal implements Comparable<Journal> {
	// ��Ӧ�����ݿ�����ַ���
	public static final String TABLE_NAME = "Journal";

	// ��־״̬��̬����
	public static final int STATUS_DELETED = -1; // ��ɾ��
	public static final int STATUS_NORMAL = 0; // ����

	private int id;
	private String title;
	private String createTime;
	private int status;
	private String content;

	public Journal() {
		status = STATUS_NORMAL;
	}
	

	public Journal(int id, String createTime, int status, String content) {
		super();
		this.id = id;
		this.createTime = createTime;
		this.status = status;
		this.content = content;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// �Դ���ʱ��Ϊ���ݱȽϴ�С
	@Override
	public int compareTo(Journal another) {
		return 0 - DateTimeUtil.compareDateTimeString(createTime,
				another.getCreateTime());
	}

}
