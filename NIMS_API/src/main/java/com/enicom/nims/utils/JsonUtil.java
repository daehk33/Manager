package com.enicom.nims.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.enicom.nims.vo.PageVO;
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
	public static LinkedHashMap<String, Object> makeJSON(String groupName, Map<String, ?> pMap) {

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
	public static LinkedHashMap<String, Object> makeListJSON(List<?> pList) {

		LinkedHashMap<String, Object> jsonSub = new LinkedHashMap<String, Object>();
		List<LinkedHashMap<String, Object>> jsonArr = new ArrayList<LinkedHashMap<String, Object>>();

		int total = pList.size();

		for (int i = 0; i < pList.size(); i++) {

			LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();

			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
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
	 * 리스트형[페이징]
	 * 
	 * @param var0
	 * @param var1
	 * @return
	 */
	public static LinkedHashMap<String, Serializable> makePagingListJSON(List<?> var0, PageVO var1) {
		LinkedHashMap<String, Integer> var2 = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Serializable> var3 = new LinkedHashMap<String, Serializable>();
		new LinkedHashMap<Object, Object>();
		ArrayList<LinkedHashMap<String, Object>> var4 = new ArrayList<LinkedHashMap<String, Object>>();
		int var5 = var0.size();

		for (int var6 = 0; var6 < var0.size(); ++var6) {
			LinkedHashMap<String, Object> var7 = new LinkedHashMap<String, Object>();

			LinkedHashMap<?, ?> var8;
			String var10;
			Object var11;
			for (Iterator<?> var9 = (var8 = (new ObjectMapper()).convertValue(var0.get(var6), LinkedHashMap.class))
					.keySet().iterator(); var9.hasNext(); var7.put(var10, var11)) {
				var10 = (String) var9.next();
				if ((var11 = var8.get(var10)) instanceof Double) {
					var11 = (int) Double.parseDouble(String.valueOf(var11));
				}

				if (var11 instanceof String) {
					var11 = StringUtil.convertHtmlBr((String) var11);
				}
			}

			var4.add(var7);
		}

		var2.put("pageNo", 		var1.getPageNo());
		var2.put("pageBlock", 	var1.getPageBlock());
		var2.put("firstPageNo", var1.getFirstPageNo());
		var2.put("endPageNo", 	var1.getEndPageNo());
		var2.put("finalPageNo", var1.getFinalPageNo());
		var2.put("prevPageNo", 	var1.getPrevPageNo());
		var2.put("nextPageNo", 	var1.getNextPageNo());
		var2.put("startPageNo", var1.getStartPageNo());
		var2.put("totalCount", 	var1.getTotalCount());
		var2.put("pageSize", 	var1.getPageSize());
		var3.put("itemsCount", 	var5);
		var3.put("items", 		var4);
		var3.put("paging", 		var2);
		return var3;
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
	 * 
	 * @param code
	 * @return
	 */
	public static LinkedHashMap<String, Object> makeResultJSON(String code) {
		LinkedHashMap<String, Object> jsonObject = new LinkedHashMap<String, Object>();
		String msg = code;
		if (code.equals("0000")) { msg = "시스템에 문제가 발생하였습니다.\r\n관리자에게 문의해 주시기 바랍니다.";
		} else if (code.equals("0001")) { msg = "접속이 종료되었습니다.\r\n다시 로그인 해주시 바랍니다.";
		} else if (code.equals("0002")) { msg = "접속 오류가 발생하였습니다. \r\n네트워크 연결 상태를 확인해주시기 바랍니다.";
		
		} else if (code.equals("200")) { msg = "성공적으로 처리되었습니다.";
		} else if (code.equals("210")) { msg = "조회 결과가 없습니다.";
		} else if (code.equals("220")) { msg = "로그인에 성공하였습니다.";
		} else if (code.equals("221")) { msg = "로그아웃에 성공하였습니다.";
		
		} else if (code.equals("400")) { msg = "처리 중 오류가 발생하였습니다.";
		} else if (code.equals("410")) { msg = "필수 파라미터 값이 누락되었습니다.";
		} else if (code.equals("420")) { msg = "잘못된 아이디 또는 패스워드입니다.";
		} else if (code.equals("421")) { msg = "로그인 세션 정보가 없습니다.";
		} else if (code.equals("440")) { msg = "사용자조회 결과가 없습니다.";
		} else if (code.equals("450")) { msg = "모듈 삭제에 실패하였습니다.\r\n모듈 삭제는 마지막에 등록한 모듈부터 삭제가 가능합니다.";
		} else if (code.equals("451")) { msg = "모듈 삭제에 실패하였습니다.\r\n기본 모듈 1~3은 삭제할 수 없습니다.";
		} else if (code.equals("480")) { msg = "로그인에 실패하였습니다.\r\n아이디 또는 비밀번호를 확인해주세요.";
		}
		jsonObject = JsonUtil.makeResultJSON(code, msg);
		return jsonObject;
	}
}
