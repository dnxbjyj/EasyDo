package com.easydo.layout;

import com.jiayongji.easydo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

public class FileDetailLayout extends BaseLinearLayout {

	private TextView fileNameTv;
	private TextView filePathTv;
	private TextView fileSizeTv;
	private TextView fileLastModifiedTv;

	public FileDetailLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.dialog_file_detail, this);

		fileNameTv = (TextView) findViewById(R.id.file_name_tv);
		filePathTv = (TextView) findViewById(R.id.file_path_tv);
		fileSizeTv = (TextView) findViewById(R.id.file_size_tv);
		fileLastModifiedTv = (TextView) findViewById(R.id.file_last_modified_tv);
	}

	public void setFileNameText(String fileName) {
		fileNameTv.setText(fileName);
	}

	public void setfilePathText(String filePath) {
		filePathTv.setText(filePath);
	}

	public void setfileSizeText(String fileSize) {
		fileSizeTv.setText(fileSize);
	}

	public void setfileLastModifiedText(String fileLastModified) {
		fileLastModifiedTv.setText(fileLastModified);
	}

}
