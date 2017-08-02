package com.easydo.util;

import android.content.Context;

import com.easydo.constant.GlobalConfig;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * ������д�����࣬���ڵ���������дDialog������д
 *
 */

public class DictationUtil {
	private static final String DICTATION_APPID = GlobalConfig.IFLY_VOICE_SDK_APP_ID;

	private static SpeechRecognizer mIat;
	private static RecognizerDialog iatDialog;
	private static String dictationResultStr;
	private static String finalResult;

	public static void showDictationDialog(final Context context,
			final DictationListener listener) {
		// ��ʼ����������
		initConfig(context);

		// ��ʼ��д
		iatDialog.setListener(new RecognizerDialogListener() {

			@Override
			public void onResult(RecognizerResult results, boolean isLast) {
				if (!isLast) {
					dictationResultStr += results.getResultString() + ",";
				} else {
					dictationResultStr += results.getResultString() + "]";

					finalResult = DictationJsonParseUtil
							.parseJsonData(dictationResultStr);

					listener.onDictationListener(finalResult);
				}

			}

			@Override
			public void onError(SpeechError error) {
				error.getPlainDescription(true);
			}
		});

		// ��ʼ��д
		iatDialog.show();
	}

	private static void initConfig(Context context) {
		dictationResultStr = "[";
		finalResult = "";

		// �������ö����ʼ��
		SpeechUtility.createUtility(context, SpeechConstant.APPID + "="
				+ DICTATION_APPID);

		// 1.����SpeechRecognizer���󣬵�2��������������дʱ��InitListener
		mIat = SpeechRecognizer.createRecognizer(context, null);
		// ��������
		iatDialog = new RecognizerDialog(context, null);

		// 2.������д������������ƴ�Ѷ��MSC API�ֲ�(Android)��SpeechConstant��
		mIat.setParameter(SpeechConstant.DOMAIN, "iat"); // domain:����
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin"); // mandarin:��ͨ��
	}
}
