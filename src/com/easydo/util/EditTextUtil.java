package com.easydo.util;

import android.text.Selection;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * EditText常用的一些方法的封装
 *
 */

public class EditTextUtil {
	// 获取当前光标所在位置
	public static int getCursorIndex(EditText mEt) {
		return mEt.getSelectionStart();
	}

	// 在当前光标所在位置插入字符串
	public static void insertText(EditText mEt, String text) {
		int newIndex = getCursorIndex(mEt) + text.length();
		mEt.getText().insert(getCursorIndex(mEt), text);
		// 将光标位置移动到新插入内容的后面
		mEt.setSelection(newIndex);
	}

	// 在EditText当前内容的尾部追加字符串
	public static void appendText(EditText mEt, String text) {
		mEt.append(text);
	}

	// 如果内容非空，删除当前光标所在位置之前的一个字符
	public static void deleteText(EditText mEt) {
		if (!TextUtils.isEmpty(mEt.getText().toString())
				&& getCursorIndex(mEt) != 0) {
			mEt.getText().delete(getCursorIndex(mEt) - 1, getCursorIndex(mEt));
		}
	}
}
