package com.enicom.nims.utils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParamUtil {
	public static String parseString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String parseString(Object obj, String str) {
		return obj == null ? str : obj.toString();
	}

	public static String parseString(Map<String, Object> paramMap, String key) {
		if (paramMap == null || paramMap.get(key) == null)
			return "";
		return paramMap.get(key).toString();
	}

	public static int parseInt(Object obj) {
		return obj == null ? -1 : Integer.parseInt(obj.toString());
	}

	public static double parseDouble(Object obj) {
		return obj == null ? -1 : Double.parseDouble(obj.toString());
	}

	public static int parseIntOne(Map<String, Object> paramMap, String key) {
		if (paramMap == null || paramMap.get(key) == null)
			return -1;
		return Integer.parseInt(paramMap.get(key).toString());
	}

	public static Map<String, Object> parseInt(Map<String, Object> paramMap, String key) {
		if (paramMap.get(key) != null && paramMap.get(key) != "") {
			paramMap.put(key, Integer.parseInt(paramMap.get(key).toString()));
		}
		return paramMap;
	}

	/**
	 * [ParamUtil] paramMap의 여러 키 값들을 int로 변환
	 * 
	 * @param paramMap
	 * @param keys
	 * @return
	 */
	public static Map<String, Object> parseInt(Map<String, Object> paramMap, String[] keys) {
		for (String key : keys) {
			if (paramMap.get(key) != null && paramMap.get(key) != "") {
				paramMap.put(key, Integer.parseInt(paramMap.get(key).toString()));
			}
		}
		return paramMap;
	}

	/**
	 * [ParamUtil] SHA-256 방식으로 암호화
	 * 
	 * @param paramMap
	 * @param key
	 * @return
	 */
	public static Map<String, Object> encrypt(Map<String, Object> paramMap, String key) {
		if (paramMap.get(key) != null && paramMap.get(key) != "") {
			paramMap.put(key, CryptoUtil.encrypt(paramMap.get(key).toString()));
		}
		return paramMap;
	}

	public static Map<String, Object> encrypt(Map<String, Object> paramMap, String[] keys) {
		for (String key : keys) {
			if (paramMap.get(key) != null && paramMap.get(key) != "") {
				paramMap.put(key, CryptoUtil.encrypt(paramMap.get(key).toString()));
			}
		}
		return paramMap;
	}

	/**
	 * [ParamUtil] String 데이터를 Timestamp 형식으로 변환
	 * 
	 * @param paramMap
	 * @param key
	 * @return
	 * @throws ParseException
	 */
	public static Map<String, Object> strToTimestamp(Map<String, Object> paramMap, String key) throws ParseException {
		if (paramMap.get(key) != null && paramMap.get(key) != "") {
			// date parsing
			StringBuffer date = new StringBuffer(paramMap.get(key).toString());
			date.insert(12, ':');
			date.insert(10, ':');
			date.insert(8, ' ');
			date.insert(6, '-');
			date.insert(4, '-');

			paramMap.put(key, date.toString());
		}
		return paramMap;
	}

	public static boolean formatDateByType(Map<String, Object> paramMap, String type) {
		String startDate = ParamUtil.parseString(paramMap.get("startDate"));
		type = ParamUtil.parseString(paramMap.get(type)).toLowerCase();

		if ("".equals(type)) {
			return false;
		}

		if ("day".equals(type)) {
			paramMap.put("DateFormat", "YYYY-MM-DD");
			paramMap.put("SeriesFormat", "YYYY-MM");
			paramMap.put("Series", "1 day");
			paramMap.put("endDate", Utils.getNextDate(startDate, "Month"));
		} else if ("month".equals(type)) {
			paramMap.put("DateFormat", "YYYY-MM");
			paramMap.put("SeriesFormat", "YYYY");
			paramMap.put("Series", "1 month");
			paramMap.put("endDate", Utils.getNextDate(startDate, "Year"));
		} else if ("all".equals(type)) {
			paramMap.put("DateFormat", "YYYY-MM-DD");
			paramMap.put("SeriesFormat", "YYYY-MM-DD");
			paramMap.put("Series", "1 day");
			paramMap.put("endDate",
					ParamUtil.parseString(paramMap.get("endDate"), Utils.getNextDate(startDate, "Day")));
		} else
			return false;

		return true;
	}

	public static boolean formatDateByTypeHistory(Map<String, Object> paramMap, String type) {
		String startDate = ParamUtil.parseString(paramMap.get("startDate"));
		type = ParamUtil.parseString(paramMap.get(type)).toLowerCase();

		if ("".equals(type)) {
			return false;
		}

		if ("day".equals(type)) {
			paramMap.put("endDate", Utils.getEndDate(startDate, "Month"));
		} else if ("month".equals(type)) {
			paramMap.put("endDate", Utils.getEndDate(startDate, "Year"));
		} else if ("all".equals(type)) {
			paramMap.put("endDate", ParamUtil.parseString(paramMap.get("endDate"), Utils.getEndDate(startDate, "Day")));
		} else
			return false;
		
		System.err.println(paramMap);
		return true;
	}

	/**
	 * [ParamUtil] data의 키 값들을 소문자로 변경
	 * 
	 * @param paramMap
	 * @return
	 */
	public static Map<String, Object> keyChangeLowerMap(Map<String, Object> paramMap) {
		Map<String, Object> result = new HashMap<String, Object>();

		Iterator<String> keyIter = paramMap.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = keyIter.next();
			result.put(key.toLowerCase(), paramMap.get(key));
		}

		return result;
	}

	/**
	 * [ParamUtil] data의 Empty String 값들을 Null로 변경
	 * 
	 * @param paramMap
	 * @return
	 */
	public static Map<String, Object> emptyToNull(Map<String, Object> paramMap) {
		for (String strKey : paramMap.keySet()) {
			if (paramMap.get(strKey).toString().isEmpty()) {
				paramMap.put(strKey, null);
			}
		}

		return paramMap;
	}
}
