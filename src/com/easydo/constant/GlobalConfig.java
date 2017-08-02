package com.easydo.constant;

import java.util.LinkedHashMap;

public class GlobalConfig {
	// �ƴ�Ѷ������SDK AppID
	public static final String IFLY_VOICE_SDK_APP_ID = "570657ad";

	// ʱ�����ճ���ʾ��ʽ����ʾ���ճ����ݵ��ַ�����󳤶ȣ���ǳ��ȣ�
	public static final int TIME_LINE_SCHEDULE_CONTENT_MAX_LEN = 25;

	// ��ǰ���ݿ�İ汾
	public static final int CUR_DB_VERSION = 7;

	// ���ݿ���
	public static final String DB_NAME = "easy_do";

	// Ĭ���ظ���ֹ���ڵ��ظ�������
	public static final int DEFAULT_REPEAT_CYCLE_NUM = 10;

	// key��ttf�����ļ�����; value:��Ŀ/assets/fonts/Ŀ¼�µ�ttf�ļ������·���ַ�������
	public static final LinkedHashMap<String, String> TYPE_FACES = new LinkedHashMap<String, String>();
	static {
		TYPE_FACES.put("ϵͳĬ������", "default");
		TYPE_FACES.put("�������ټ���", "fonts/Jinglei.ttf");
		TYPE_FACES.put("�ż���д��", "fonts/LetterHandWritten.ttf");
	}

	// ����ttf�ļ�����ȡ��������
	public static String getTypefaceName(String ttfPath) {
		if (ttfPath == null || ttfPath.equals("default")) {
			return "ϵͳĬ������";
		} else if (ttfPath.equals("fonts/Jinglei.ttf")) {
			return "�������ټ���";
		} else {
			return "�ż���д��";
		}
	}
}
