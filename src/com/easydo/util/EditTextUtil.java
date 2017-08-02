package com.easydo.util;

import android.text.Selection;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * EditText���õ�һЩ�����ķ�װ
 *
 */

public class EditTextUtil {
	// ��ȡ��ǰ�������λ��
	public static int getCursorIndex(EditText mEt) {
		return mEt.getSelectionStart();
	}

	// �ڵ�ǰ�������λ�ò����ַ���
	public static void insertText(EditText mEt, String text) {
		int newIndex = getCursorIndex(mEt) + text.length();
		mEt.getText().insert(getCursorIndex(mEt), text);
		// �����λ���ƶ����²������ݵĺ���
		mEt.setSelection(newIndex);
	}

	// ��EditText��ǰ���ݵ�β��׷���ַ���
	public static void appendText(EditText mEt, String text) {
		mEt.append(text);
	}

	// ������ݷǿգ�ɾ����ǰ�������λ��֮ǰ��һ���ַ�
	public static void deleteText(EditText mEt) {
		if (!TextUtils.isEmpty(mEt.getText().toString())
				&& getCursorIndex(mEt) != 0) {
			mEt.getText().delete(getCursorIndex(mEt) - 1, getCursorIndex(mEt));
		}
	}
}
