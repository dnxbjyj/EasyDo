package com.easydo.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.easydo.layout.FileDetailLayout;
import com.easydo.util.BackupUtil;
import com.easydo.util.DateTimeUtil;
import com.jiayongji.easydo.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class LocalBackupActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	// 标题栏的控件
	private ImageButton titleLeftIb;
	private TextView titleMiddleTv;
	private ImageButton titleRightIb;

	// 空白提示Tv
	private TextView blankTv;

	// 本地当前备份目录下备份文件的'文件名：路径'键值对列表
	private File[] localBackupFiles;
	private List<Map<String, String>> fileNameTimeList;

	private BackupUtil mBackupUtil;

	// 本地当前的备份数据文件
	private ListView localBackupFilesLv;
	private ListAdapter mAdapter;

	// 备份按钮
	private TextView doLocalBackupTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}

	@Override
	public void initUI() {
		setContentView(R.layout.activity_local_backup);
		/**
		 * 标题栏的初始化
		 */
		titleLeftIb = (ImageButton) findViewById(R.id.title_base_left_ib);
		titleLeftIb.setImageResource(R.drawable.back1_64);
		titleLeftIb.setOnClickListener(this);

		titleMiddleTv = (TextView) findViewById(R.id.title_base_middle_tv);
		titleMiddleTv.setText("本地数据备份管理");
		titleMiddleTv.setTextSize(18);

		titleRightIb = (ImageButton) findViewById(R.id.title_base_right_ib);
		titleRightIb.setImageResource(R.drawable.write2_64);
		titleRightIb.setVisibility(View.INVISIBLE);

		blankTv = (TextView) findViewById(R.id.blank_tv);
		localBackupFilesLv = (ListView) findViewById(R.id.local_backup_files_lv);

		mBackupUtil = new BackupUtil(LocalBackupActivity.this);

		initBackupFiles();

		// 备份按钮
		doLocalBackupTv = (TextView) findViewById(R.id.do_local_backup_tv);
		doLocalBackupTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_base_left_ib:
			finish();
			break;
		case R.id.do_local_backup_tv:
			mBackupUtil.backupDBtoLocal();

			initBackupFiles();
			break;
		default:
			break;
		}
	}

	/**
	 * 从File数组获取文件名字符串数组
	 * 
	 */
	private String[] getFilesNameList(File[] files) {
		if (files == null || files.length == 0) {
			return null;
		}

		String[] names = new String[files.length];

		for (int i = 0; i < files.length; i++) {
			names[i] = files[i].getName();
		}

		return names;
	}

	/**
	 * 更新备份文件列表和备份文件名列表
	 * 
	 * @return 如果备份文件列表为空则返回false，否则返回true
	 */
	private boolean initBackupFiles() {
		localBackupFiles = mBackupUtil.getLocalBackupFiles();

		if (localBackupFiles == null || localBackupFiles.length == 0) {
			blankTv.setVisibility(View.VISIBLE);
			localBackupFilesLv.setVisibility(View.GONE);
			return false;
		} else {
			// localBackupFilesName = getFilesNameList(localBackupFiles);

			blankTv.setVisibility(View.GONE);
			localBackupFilesLv.setVisibility(View.VISIBLE);

			fileNameTimeList = getFileNameTimeKeyValues(localBackupFiles);

			mAdapter = new SimpleAdapter(LocalBackupActivity.this,
					fileNameTimeList, android.R.layout.simple_list_item_2,
					new String[] { "File", "Time" }, new int[] {
							android.R.id.text1, android.R.id.text2 });

			localBackupFilesLv.setAdapter(mAdapter);
			localBackupFilesLv.setOnItemClickListener(this);
			return true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 当前点击的file
		final File curFile = localBackupFiles[position];

		// 点击item弹出的dialog的选项列表
		final String[] operation = new String[] { "恢复", "删除", "文件详情" };

		AlertDialog.Builder dialog = new AlertDialog.Builder(this);

		// 设置dialog中选项的点击事件
		dialog.setItems(operation, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					mBackupUtil.restoreDB();
					break;
				case 1:
					curFile.delete();
					initBackupFiles();
					break;
				case 2:
					AlertDialog.Builder fileDetailDialog = new AlertDialog.Builder(
							LocalBackupActivity.this);
					FileDetailLayout dialogView = new FileDetailLayout(
							LocalBackupActivity.this, null);
					dialogView.setFileNameText(curFile.getName());
					dialogView.setfilePathText(curFile.getAbsolutePath());
					dialogView.setfileSizeText(FileUtils.sizeOf(curFile) / 1024
							+ "KB");
					dialogView.setfileLastModifiedText(DateTimeUtil
							.getDateTimeStringFromMillis(curFile.lastModified()));
					fileDetailDialog.setView(dialogView);
					fileDetailDialog.setNegativeButton("取消", null);

					final AlertDialog alertDialog = fileDetailDialog.create();
					alertDialog.show();

					alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									alertDialog.dismiss();
								}
							});

					break;
				default:
					break;
				}
			}

		});
		dialog.setCancelable(true);
		dialog.show();
	}

	// 获取文件名-修改时间键值对列表
	private List<Map<String, String>> getFileNameTimeKeyValues(File[] files) {

		if (files == null || files.length == 0) {
			return null;
		}

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < files.length; i++) {
			Map<String, String> fileNameTimeKeyValues = new HashMap<String, String>();
			String name = files[i].getName();
			String time = DateTimeUtil.getDateTimeStringFromMillis(files[i]
					.lastModified());
			fileNameTimeKeyValues.put("File", name);
			fileNameTimeKeyValues.put("Time", time);
			list.add(fileNameTimeKeyValues);
		}

		return list;
	}
}
