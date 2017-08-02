package com.easydo.adapter;

/**
 * ��־��ʾҳ�����־itemʵ����
 * 
 * @author Administrator
 *
 */

public class JournalLvItem {
	// �����ݿ���е�id
	private int id;
	// ��־������ʱ�䣬��ʽ��"2016-04-22 19:08"
	private String createTime;
	// ��־������
	private String content;

	// ��־��״̬��Ĭ�϶�Ϊ����״̬�����Ҳ���
	private int status;
	// title�ֶ�Ŀǰ��ʱ����
	private String title;

	public JournalLvItem(int id, String createTime, String content, int status,
			String title) {
		super();
		this.id = id;
		this.createTime = createTime;
		this.content = content;
		this.status = status;
		this.title = title;
	}

	public JournalLvItem(int id, String createTime, String content) {
		super();
		this.id = id;
		this.createTime = createTime;
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getContent() {
		return content;
	}

	public int getStatus() {
		return status;
	}

	public String getTitle() {
		return title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
