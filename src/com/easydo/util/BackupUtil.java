package com.easydo.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.easydo.constant.GlobalConfig;
import com.easydo.model.SystemConfig;
import com.sinaapp.msdxblog.androidkit.util.ResourceUtil;
import com.sinaapp.msdxblog.androidkit.util.entity.ApkInfo;

import android.content.Context;
import android.os.Environment;

/**
 * ���ݱ�����ָ�������
 *
 */

public class BackupUtil {

	private Context mContext;

	private String APP_PATH;
	private String DATABASES;
	private String DB_FILE;

	private String BACKUP_PATH;
	private String BACKUP_DATABASES;
	private String BACKUP_DB_FILE;

	public BackupUtil(Context context) {
		mContext = context;

		ApkInfo apkInfo = new ResourceUtil(context).getApkInfo();
		APP_PATH = new StringBuilder("/data/data/").append(apkInfo.packageName)
				.toString();
		DATABASES = APP_PATH + "/databases";
		DB_FILE = DATABASES + "/" + GlobalConfig.DB_NAME;

		// �ж��Ƿ���SD��
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			BACKUP_PATH = "/sdcard/com.easydo/backup/";
		} else {
			BACKUP_PATH = "/com.easydo/backup/";
			ToastUtil.showShort(context, "δ��⵽sd�������ݿ����޷����ݳɹ���");
		}
		BACKUP_DATABASES = BACKUP_PATH + "/databases";
		BACKUP_DB_FILE = BACKUP_DATABASES + "/" + GlobalConfig.DB_NAME;
	}

	/**
	 * �������ݿ�
	 * 
	 * @return �ɹ�ʱ����true�����򷵻�false
	 */
	public boolean backupDBtoLocal() {
		boolean result = copyFileOrDir(DB_FILE, BACKUP_DATABASES,
				"�ɹ������������ļ����ݵ���" + BACKUP_DATABASES, "���������ļ�ʧ�ܣ�");

		return result;
	}

	/**
	 * �ӱ���Ŀ¼�ָ����ݿ�
	 */
	public boolean restoreDB() {
		boolean result = copyFileOrDir(BACKUP_DB_FILE, DATABASES, "�ָ������ļ��ɹ���",
				"�ָ������ļ�ʧ�ܣ�");

		return result;
	}

	/**
	 * ��ȡ��ǰ����Ŀ¼������files
	 */
	public File[] getLocalBackupFiles() {
		File backupDir = new File(BACKUP_DATABASES);
		File[] files = backupDir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String filename) {
				return filename.endsWith("easy_do");
			}
		});

		if (files == null || files.length == 0) {
			return null;
		}

		List<File> filesList = Arrays.asList(files);
		Collections.sort(filesList, new Comparator<File>() {

			@Override
			public int compare(File file1, File file2) {
				long lastModified1 = file1.lastModified();
				long lastModified2 = file2.lastModified();
				if (lastModified1 == lastModified2) {
					return 0;
				} else {
					return lastModified1 < lastModified2 ? 1 : -1;
				}
			}
		});

		final int size = files.length;
		return (File[]) filesList.toArray(new File[size]);
	}

	/**
	 * ��ȡ��ǰϵͳ���ݿ�Ŀ¼�����ݿ��ļ�
	 * 
	 */
	public File getCurDatabaseFile() {
		return new File(DATABASES + "/" + GlobalConfig.DB_NAME);
	}

	/**
	 * ����Ŀ¼
	 * 
	 * @param src
	 *            ԴĿ¼�����ļ���
	 * @param dest
	 *            Ŀ��Ŀ¼
	 * @param successMsg
	 *            �ɹ�ʱ�����ʾ��Ϣ
	 * @param failedMsg
	 *            ʧ��ʱ�����ʾ��Ϣ
	 */
	private final boolean copyFileOrDir(String src, String dest,
			String successMsg, String failedMsg) {
		File srcFile = new File(src);
		return copyFileOrDir(srcFile, dest, successMsg, failedMsg);
	}

	private final boolean copyFileOrDir(File srcFile, String dest,
			String successMsg, String failedMsg) {
		try {
			if (srcFile.isDirectory()) {
				FileUtils.copyDirectory(srcFile, new File(dest));
			} else if (srcFile.isFile()) {
				FileUtils.copyFileToDirectory(srcFile, new File(dest));
			}
		} catch (IOException e) {
			e.printStackTrace();
			ToastUtil.showShort(mContext, failedMsg);
			return false;
		}

		ToastUtil.showShort(mContext, successMsg);
		return true;
	}

}
