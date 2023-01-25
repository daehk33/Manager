package com.enicom.nims.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	// 네이밍 규칙
	// ABC_DEF : DB 조회한 값
	// abcDef : 가공한 값

	/**
	 * 결과 : 단일건
	 * 
	 * @param pMap
	 * @return
	 */
	public static LinkedHashMap<String, Object> makeJSON(String groupName, Map pMap) {

		LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> jsonSub = new LinkedHashMap<String, Object>();
		List<LinkedHashMap<String, Object>> jsonArr = new ArrayList<LinkedHashMap<String, Object>>();

		Iterator<String> keys = pMap.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Object value = pMap.get(key);

			json.put(key, value);

			if (value instanceof Double) {
				value = (int) Double.parseDouble((String.valueOf(value)));
			}

			if (value instanceof String) {
				value = (String) value;
			}
		}

		jsonArr.add(json);
		jsonSub.put(groupName, json);

		return jsonSub;
	}

	/**
	 * 리스트형
	 * 
	 * @param pList
	 * @return
	 */
	public static LinkedHashMap<String, Object> makeListJSON(List pList) {

		LinkedHashMap<String, Object> jsonSub = new LinkedHashMap<String, Object>();
		List<LinkedHashMap<String, Object>> jsonArr = new ArrayList<LinkedHashMap<String, Object>>();

		int total = pList.size();

		for (int i = 0; i < pList.size(); i++) {

			LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();

//			LinkedHashMap<String, Object> pMap = (LinkedHashMap)pList.get(i);

			ObjectMapper mapper = new ObjectMapper();
			LinkedHashMap<String, Object> pMap = mapper.convertValue(pList.get(i), LinkedHashMap.class);

			Iterator<String> keys = pMap.keySet().iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				Object value = pMap.get(key);

				if (value instanceof Double) {
					value = (int) Double.parseDouble((String.valueOf(value)));
				}

				if (value instanceof String) {
					value = (String) value;
				}

				json.put(key, value);

			}

			jsonArr.add(json);

		}

		jsonSub.put("itemsCount", total);
		jsonSub.put("items", jsonArr);

		return jsonSub;
	}

	/**
	 * 결과코드발생
	 * 
	 * @param code
	 * @param msg
	 * @return
	 */
	public static LinkedHashMap<String, Object> makeResultJSON(String code, String msg) {

		LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> jsonErr = new LinkedHashMap<String, Object>();

		json.put("CODE", code);
		json.put("DESC", msg);

		jsonErr.put("result", json);

		return jsonErr;
	}

	/**
	 * 결과코드
	 * @param code
	 * @return
	 */
	public static LinkedHashMap<String, Object> makeResultJSON(String code){
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		String msg = code;
		if (code.equals("0000")) { msg = "시스템에 문제가 발생하였습니다.\r\n관리자에게 문의해 주시기 바랍니다.";
		} else if (code.equals("0001")) { msg = "접속이 종료되었습니다.\r\n 다시 로그인 해주시 바랍니다.";
		} else if (code.equals("200")) { msg = "성공적으로 처리되었습니다.";
		} else if (code.equals("210")) { msg = "조회 결과가 없습니다.";
		} else if (code.equals("220")) { msg = "로그인에 성공하였습니다.";
		} else if (code.equals("221")) { msg = "로그아웃에 성공하였습니다.";
		
		} else if (code.equals("400")) { msg = "처리 중 오류가 발생하였습니다.";
		} else if (code.equals("410")) { msg = "필수 파라미터 값이 누락되었습니다.";
		} else if (code.equals("420")) { msg = "잘못된 아이디 또는 패스워드입니다.";
		} else if (code.equals("421")) { msg = "로그인 세션 정보가 없습니다.";
		} else if (code.equals("422")) { msg = "장시간 이용하지 않아 페이지가 만료되었습니다.\n페이지가 새로 로딩됩니다.";
		} else if (code.equals("440")) { msg = "사용자조회 결과가 없습니다.";
		} else if (code.equals("480")) { msg = "로그인에 실패하였습니다.\r\n아이디 또는 비밀번호를 확인해주세요.";
		}

		jsonObject = JsonUtil.makeResultJSON(code, msg);
		return jsonObject;
	}

}
