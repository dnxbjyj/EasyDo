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
	// ����ڵ�ʱ�䣬�ڻ������д��onKeyDown�������������ĸ��������ʵ�����������η��ؼ��˳�����
	protected long mExitTime;

	protected static EasyDoDB easyDoDB;
	protected static SystemConfig systemConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);

		initSystemConfig();
	}

	// ��ʼ��ϵͳ����
	protected void initSystemConfig() {
		easyDoDB = EasyDoDB.getInstance(this);

		// �ж����ݿ��Ƿ��Ѿ���ʼ��������Ѿ���ʼ���˾Ͷ�ȡϵͳ���ñ�
		if (easyDoDB.loadInitOriginalDataDone() == 1) {
			// ��ȡϵͳ����
			systemConfig = new SystemConfig();
			systemConfig = easyDoDB.loadSystemConfig(null, null);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	// ��ʼ�����沼�ּ������ؼ��ĳ��󷽷�
	public abstract void initUI();

	public EasyDoDB getEasyDoDB() {
		return easyDoDB;
	}

	public SystemConfig getSystemConfig() {
		return systemConfig;
	}

}
