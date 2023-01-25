package com.enicom.nims.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.enicom.nims.utils.Utils;

public class SessionManager {
	private static HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

	/**
	 * 세션 설정
	 *
	 * @param request
	 * @param paramMap
	 * @param page_hash
	 * @throws Exception
	 * @description 로그인 성공 시 사용자 정보를 등록한다.
	 */
	public static void setSession(HttpServletRequest request, Map<String, Object> paramMap) {
		HttpSession session = getSession(request);

		try {
			// Test Logging
			System.out.println("session ? paramMap : " + paramMap);

			session.setAttribute("MANAGER", paramMap);
			session.setAttribute("IP", Utils.getClientIP(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 세션 메뉴 리스트 저장
	 * 
	 * @param request
	 * @param obj
	 */
	public static void setMenuList(HttpServletRequest request, Object obj) {
		HttpSession session = getSession(request);

		try {
			session.setAttribute("MENU_LIST", obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setTheme(HttpServletRequest request, String theme) {
		HttpSession session = getSession(request);

		if (theme.equalsIgnoreCase("white"))
			session.setAttribute("THEME", theme);
		else
			session.setAttribute("THEME", "dark");
	}

	/**
	 * 세션 확인
	 *
	 * @param session
	 */
	public static boolean hasSession(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Map<String, Object> person = (Map<String, Object>) getSession(request).getAttribute("MANAGER");
		
		return person != null;
	}

	/**
	 * 세션 삭제
	 *
	 * @param session
	 */
	public static void destroy(HttpSession session) {
		session.removeAttribute("MANAGER");
		session.removeAttribute("IP");
		session.removeAttribute("THEME");
		session.removeAttribute("MENU_LIST");
		
		session.invalidate();
	}

}
