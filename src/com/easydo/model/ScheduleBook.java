package com.easydo.model;

import com.easydo.util.DateTimeUtil;

/**
 * 类名：ScheduleBook
 * 
 * 功能：数据库ScheduleBook表的实体类
 */

public class ScheduleBook implements Comparable<ScheduleBook> {
	// 日程本状态值常量
	public static final int STATUS_DELETED = -1; // 已删除
	public static final int STATUS_INACTIVED = 0; // 未激活
	public static final int STATUS_ACTIVED = 1; // 已激活（正在使用的）

	private int id;
	private String name;
	private String createTime;
	private int status;
	private int scheduleNum;
	private int typeId;

	public ScheduleBook() {
		status = STATUS_INACTIVED;
		scheduleNum = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getScheduleNum() {
		return scheduleNum;
	}

	public void setScheduleNum(int scheduleNum) {
		this.scheduleNum = scheduleNum;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	@Override
	public int compareTo(ScheduleBook another) {
		return DateTimeUtil.compareDateTimeString(createTime,
				another.getCreateTime());
	}

}
