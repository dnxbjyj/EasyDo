package com.easydo.util;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/*
 * ��װ��GSON���������࣬�ṩ���Ͳ���
 * 
 */
public class GsonUtil {
	// ������Json���ݽ�������Ӧ��ӳ�����
	public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
		Gson gson = new Gson();
		T result = gson.fromJson(jsonData, type);
		return result;
	}

	// ��Json�����������Ӧ��ӳ������б�
	public static <T> List<T> parseJsonArrayWithGson(String jsonData,
			Class<T> type) {
		Gson gson = new Gson();
		List<T> result = new ArrayList<T>();

		// ���������Ϊ�����ڱ��������ͻᱻ�������Ӷ��������´���
		// java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap
		// cannot be cast to DictationResult
		// List<T> result = gson.fromJson(jsonData, new TypeToken<List<T>>() {
		// }.getType());

		// ��ȷд��
		JsonArray array = new JsonParser().parse(jsonData).getAsJsonArray();
		for (final JsonElement elem : array) {
			result.add(new Gson().fromJson(elem, type));
		}

		return result;
	}
}
