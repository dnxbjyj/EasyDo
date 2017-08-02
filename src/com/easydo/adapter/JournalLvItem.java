package com.easydo.adapter;

/**
 * 日志显示页面的日志item实体类
 * 
 * @author Administrator
 *
 */

public class JournalLvItem {
	// 在数据库表中的id
	private int id;
	// 日志创建的时间，格式："2016-04-22 19:08"
	private String createTime;
	// 日志的内容
	private String content;

	// 日志的状态，默认都为正常状态，暂且不用
	private int status;
	// title字段目前暂时不用
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
