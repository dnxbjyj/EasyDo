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
 * 数据备份与恢复工具类
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

		// 判断是否有SD卡
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			BACKUP_PATH = "/sdcard/com.easydo/backup/";
		} else {
			BACKUP_PATH = "/com.easydo/backup/";
			ToastUtil.showShort(context, "未检测到sd卡，数据可能无法备份成功！");
		}
		BACKUP_DATABASES = BACKUP_PATH + "/databases";
		BACKUP_DB_FILE = BACKUP_DATABASES + "/" + GlobalConfig.DB_NAME;
	}

	/**
	 * 备份数据库
	 * 
	 * @return 成功时返回true，否则返回false
	 */
	public boolean backupDBtoLocal() {
		boolean result = copyFileOrDir(DB_FILE, BACKUP_DATABASES,
				"成功将最新数据文件备份到：" + BACKUP_DATABASES, "备份数据文件失败！");

		return result;
	}

	/**
	 * 从备份目录恢复数据库
	 */
	public boolean restoreDB() {
		boolean result = copyFileOrDir(BACKUP_DB_FILE, DATABASES, "恢复数据文件成功！",
				"恢复数据文件失败！");

		return result;
	}

	/**
	 * 获取当前备份目录的所有files
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
	 * 获取当前系统数据库目录的数据库文件
	 * 
	 */
	public File getCurDatabaseFile() {
		return new File(DATABASES + "/" + GlobalConfig.DB_NAME);
	}

	/**
	 * 复制目录
	 * 
	 * @param src
	 *            源目录（或文件）
	 * @param dest
	 *            目标目录
	 * @param successMsg
	 *            成功时候的提示信息
	 * @param failedMsg
	 *            失败时候的提示信息
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
