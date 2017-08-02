package com.easydo.model;

import com.easydo.util.DateTimeUtil;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * �����ճ̱�ʵ����
 *
 */

public class SpecialSchedule implements Comparable<SpecialSchedule>, Parcelable {
	// ʵ��Comparable�ӿ���Ϊ���ܹ�����
	// ʵ��Parcelable�ӿ���Ϊ���ܹ��ڻ֮����д���

	// ��Ӧ�����ݿ�����ַ���
	public static final String TABLE_NAME = "SpecialSchedule";

	// �����ճ����ͳ���
	public static final int TYPE_ANNIVERSARY = 0; // ������
	public static final int TYPE_BIRTHDAY = 1; // ����
	public static final int TYPE_COUNTDOWN = 2; // ������
	public static final String[] TYPE_TEXT_ARRAY = new String[] { "������", "����",
			"������" };

	// ���������ճ̵����ͻ�ȡ����������
	public String getTypeText() {
		return TYPE_TEXT_ARRAY[type];
	}

	// �����ճ�״̬��̬����
	public static final int STATUS_DELETED = -1; // ��ɾ��
	public static final int STATUS_NORMAL = 0; // ����

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

			// ˳��һ��Ҫ��writeToParcel������д���˳����ȫ��ͬ��
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
