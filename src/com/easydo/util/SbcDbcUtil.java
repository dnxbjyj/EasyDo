package com.easydo.util;

/**
 * ȫ��-����ַ�������
 *
 */

public class SbcDbcUtil {
	// �ж��Ƿ�Ϊ����ַ�
	public static boolean isDbcCase(char c) {
		if (c >= 32 && c <= 127) {
			return true;
		}
		// ���İ��Ƭ�����ͷ���
		else if (c >= 65377 && c <= 65439) {
			return true;
		}

		return false;
	}

	// �����ַ�������
	// ���㷽�������� = ����ַ����� + ȫ���ַ����� * 2
	public static int getLength(String str) {
		int len = 0;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (isDbcCase(c)) { // ���
				++len;
			} else { // ȫ��
				len += 2;
			}
		}

		return len;
	}

	// �ַ�����ȡ������ȫ/��ǣ�
	// ��ȡstr���±�Ϊ[0,index)��Χ�ڵ����ַ���(����"...")
	// ���index���ڵ���str�ĳ��ȣ���ǳ��ȣ�����ֱ�ӷ���str
	public static String subString(String str, int index) {
		if (getLength(str) <= index) {
			return str;
		}

		char[] chars = str.toCharArray();
		int charLenSum = 0;
		StringBuilder result = new StringBuilder("");

		for (int i = 0; i < chars.length; i++) {
			int charLen = isDbcCase(chars[i]) ? 1 : 2;
			if (charLenSum + charLen > index) {
				return result.toString() + "...";
			}

			charLenSum += charLen;
			result.append(chars[i]);
			if (charLenSum == index) {
				return result.toString() + "...";
			}
		}

		return "";
	}

}
