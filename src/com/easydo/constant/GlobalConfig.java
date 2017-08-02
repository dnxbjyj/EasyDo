package com.easydo.constant;

import java.util.LinkedHashMap;

public class GlobalConfig {
	// 科大讯飞语音SDK AppID
	public static final String IFLY_VOICE_SDK_APP_ID = "570657ad";

	// 时间轴日程显示方式，显示的日程内容的字符串最大长度（半角长度）
	public static final int TIME_LINE_SCHEDULE_CONTENT_MAX_LEN = 25;

	// 当前数据库的版本
	public static final int CUR_DB_VERSION = 7;

	// 数据库名
	public static final String DB_NAME = "easy_do";

	// 默认重复截止日期的重复周期数
	public static final int DEFAULT_REPEAT_CYCLE_NUM = 10;

	// key：ttf字体文件名称; value:项目/assets/fonts/目录下的ttf文件的相对路径字符串常量
	public static final LinkedHashMap<String, String> TYPE_FACES = new LinkedHashMap<String, String>();
	static {
		TYPE_FACES.put("系统默认字体", "default");
		TYPE_FACES.put("方正静蕾简体", "fonts/Jinglei.ttf");
		TYPE_FACES.put("信笺手写体", "fonts/LetterHandWritten.ttf");
	}

	// 根据ttf文件名获取字体名称
	public static String getTypefaceName(String ttfPath) {
		if (ttfPath == null || ttfPath.equals("default")) {
			return "系统默认字体";
		} else if (ttfPath.equals("fonts/Jinglei.ttf")) {
			return "方正静蕾简体";
		} else {
			return "信笺手写体";
		}
	}
}
