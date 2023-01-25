package com.enicom.nims.utils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class ParamUtil {
	public static String parseString(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public static String parseString(Object obj, String str) {
		return obj == null ? str : obj.toString();
	}

	public static int parseInt(Object obj) {
		return obj == null ? -1 : Integer.parseInt(obj.toString());
	}

	public static int parseInt(Object obj, int value) {
		if (obj == null)
			return value;
		try {
			return Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return value;
		}
	}

	public static Map<String, Object> parseInt(Map<String, Object> paramMap, String key) {
		if (paramMap.get(key) != null) {
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
			parseInt(paramMap, key);
		}
		return paramMap;
	}

	/**
	 * [ParamUtil] String 데이터를 Timestamp 형식으로 변환 - YYYY-MM-DD HH24:MI:SS
	 * 
	 * @param paramMap
	 * @param key
	 * @return
	 * @throws ParseException
	 */
	public static String strToTimestamp(String key) {
		// Date Parsing
		StringBuffer date = new StringBuffer(key);

		date.insert(12, ':');
		date.insert(10, ':');
		date.insert(8, ' ');
		date.insert(6, '-');
		date.insert(4, '-');

		return date.toString();
	}

	/**
	 * [ParamUtil] paramMap의 date 형식의 데이터를 Timestamp 형식으로 변환
	 * 
	 * @param paramMap
	 * @param key
	 * @return
	 * @throws ParseException
	 */
	public static Map<String, Object> strToTimestamp(Map<String, Object> paramMap, String key) throws ParseException {
		if (paramMap.get(key) != null) {
			String date = paramMap.get(key).toString();
			paramMap.put(key, strToTimestamp(date));
		}
		return paramMap;
	}

	/*
	 * YYYY-MM-DDtHH:MI:SS -> YYYY-MM-DD HH:MI:SS
	 */
	public static Map<String, Object> strToTimestampWithT(Map<String, Object> paramMap, String[] keys)
			throws ParseException {
		for (String key : keys) {
			if (paramMap.get(key) != null) {
				String date = paramMap.get(key).toString();
				date.replace("t", " ");
				paramMap.put(key, date);
			}
		}

		return paramMap;
	}

	/**
	 * [ParamUtil] Request 객체의 parameter 목록을 받아 Map<key, value> 형태로 변환
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> convertRequestToMap(HttpServletRequest request) {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		Enumeration<?> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			paramMap.put(key, request.getParameter(key));
		}
		return paramMap;
	}

	/**
	 * [파일 업로드] Request 객체의 Param들과 File을 받아서 Byte로 변환한 뒤 Map 형태로 변환
	 * 
	 * @param request
	 * @param uploadFile
	 * @param fileParamName
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> convertRequestToMapWithFile(MultipartHttpServletRequest request,
			MultipartFile uploadFile, String fileParamName) throws IOException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Enumeration<?> keys = request.getParameterNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			paramMap.put(key, request.getParameter(key));
		}
		paramMap.put(fileParamName, uploadFile.getBytes());
		return paramMap;
	}
}
