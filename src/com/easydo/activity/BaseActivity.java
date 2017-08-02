package com.easydo.activity;

import com.easydo.db.EasyDoDB;
import com.easydo.model.ScheduleBook;
import com.easydo.model.ScheduleBookType;
import com.easydo.model.SystemConfig;
import com.easydo.util.DateTimeUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public abstract class BaseActivity extends Activity {
	// 活动存在的时间，在活动类中重写了onKeyDown方法，用于在四个基本活动中实现连续按两次返回键退出程序
	protected long mExitTime;

	protected static EasyDoDB easyDoDB;
	protected static SystemConfig systemConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);

		initSystemConfig();
	}

	// 初始化系统配置
	protected void initSystemConfig() {
		easyDoDB = EasyDoDB.getInstance(this);

		// 判断数据库是否已经初始化，如果已经初始化了就读取系统配置表
		if (easyDoDB.loadInitOriginalDataDone() == 1) {
			// 获取系统设置
			systemConfig = new SystemConfig();
			systemConfig = easyDoDB.loadSystemConfig(null, null);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	// 初始化界面布局及各个控件的抽象方法
	public abstract void initUI();

	public EasyDoDB getEasyDoDB() {
		return easyDoDB;
	}

	public SystemConfig getSystemConfig() {
		return systemConfig;
	}

}
