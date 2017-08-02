package com.easydo.model;

import com.easydo.util.DateTimeUtil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 特殊日程表实体类
 *
 */

public class SpecialSchedule implements Comparable<SpecialSchedule>, Parcelable {
	// 实现Comparable接口是为了能够排序
	// 实现Parcelable接口是为了能够在活动之间进行传递

	// 对应的数据库表名字符串
	public static final String TABLE_NAME = "SpecialSchedule";

	// 特殊日程类型常量
	public static final int TYPE_ANNIVERSARY = 0; // 纪念日
	public static final int TYPE_BIRTHDAY = 1; // 生日
	public static final int TYPE_COUNTDOWN = 2; // 倒数日
	public static final String[] TYPE_TEXT_ARRAY = new String[] { "纪念日", "生日",
			"倒数日" };

	// 根据特殊日程的类型获取其类型名称
	public String getTypeText() {
		return TYPE_TEXT_ARRAY[type];
	}

	// 特殊日程状态静态常量
	public static final int STATUS_DELETED = -1; // 已删除
	public static final int STATUS_NORMAL = 0; // 正常

	private int id;
	private String title;
	private String date;
	private String remark;
	private int type;
	private int status;

	public SpecialSchedule() {

	}

	public SpecialSchedule(int id, String title, String date, String remark,
			int type, int status) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.remark = remark;
		this.type = type;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(title);
		dest.writeString(date);
		dest.writeString(remark);
		dest.writeInt(type);
		dest.writeInt(status);
	}

	public static final Parcelable.Creator<SpecialSchedule> CREATOR = new Parcelable.Creator<SpecialSchedule>() {
		public SpecialSchedule createFromParcel(Parcel source) {
			SpecialSchedule specialSchedule = new SpecialSchedule();

			// 顺序一定要与writeToParcel方法中写入的顺序完全相同！
			specialSchedule.id = source.readInt();
			specialSchedule.title = source.readString();
			specialSchedule.date = source.readString();
			specialSchedule.remark = source.readString();
			specialSchedule.type = source.readInt();
			specialSchedule.status = source.readInt();

			return specialSchedule;
		};

		public SpecialSchedule[] newArray(int size) {
			return new SpecialSchedule[size];
		};
	};

	@Override
	public int compareTo(SpecialSchedule another) {
		return DateTimeUtil.compareDateString(date, another.getDate());
	}

}
