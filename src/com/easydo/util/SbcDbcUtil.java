package com.easydo.util;

/**
 * 全角-半角字符处理类
 *
 */

public class SbcDbcUtil {
	// 判断是否为半角字符
	public static boolean isDbcCase(char c) {
		if (c >= 32 && c <= 127) {
			return true;
		}
		// 日文半角片假名和符号
		else if (c >= 65377 && c <= 65439) {
			return true;
		}

		return false;
	}

	// 返回字符串长度
	// 计算方法：长度 = 半角字符个数 + 全角字符个数 * 2
	public static int getLength(String str) {
		int len = 0;

		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (isDbcCase(c)) { // 半角
				++len;
			} else { // 全角
				len += 2;
			}
		}

		return len;
	}

	// 字符串截取（区分全/半角）
	// 截取str中下标为[0,index)范围内的子字符串(加上"...")
	// 如果index大于等于str的长度（半角长度），则直接返回str
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
