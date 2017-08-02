package com.easydo.adapter;

/**
 * �ճ���ʾҳ����ճ�itemʵ����
 *
 */

public class ScheduleLvItem {

	// item�����
	public final static int TYPE_DATE_NODE = 0; // ���ڽ��
	public final static int TYPE_SCHEDULE_NODE = 1; // �ճ̽��

	// ������ճ̽�㣬�����ݿ���е�id
	private int id;

	// item������
	private int type;

	// item����ߵ��ճ�ʱ���ַ���
	private String time;

	// �����ŵ���ʾһ������������ԲȦ��TextView�ı���ͼƬ��id,�ñ���ͼƬ����type�Ĳ�ͬҲ��ͬ
	// type = TYPE_DATE_NODE��һ��������ͼƬ
	// type = TYPE_SCHEDULE_NODE:һ��ԲȦ��ͼƬ��������������
	private int nodeImageId;

	// �ճ̵�������
	private boolean isDone;

	// �ճ̵����ڣ����ţ�ȡֵ��1~31��,���������ڽ���С����������ʾ
	private int dayOfMonth;

	// �ճ̵ľ������ڣ���ʽ��5��1�� �����գ������������ڽ������ʾ
	private String dateString;

	// �ճ̵������ַ�������ʽ��"2016-05-12"
	private String date;

	// �ճ̵�����
	private String content;

	// �ճ̱�ǩ
	private int tag;

	// ���ڽ�㹹�캯������
	public ScheduleLvItem(int type, int nodeImageId, int dayOfMonth,
			String dateString, String date) {
		this.type = type;
		this.nodeImageId = nodeImageId;
		this.dayOfMonth = dayOfMonth;
		this.dateString = dateString;
		this.date = date;
	}

	// schedule��㹹�췽��
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
