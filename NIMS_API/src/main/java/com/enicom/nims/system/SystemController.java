package com.enicom.nims.system;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.system.service.SystemService;

@RestController
public class SystemController {
	private SystemService systemService;

	@Autowired
	public SystemController(SystemService systemService) {
		this.systemService = systemService;
	}

	/**
	 * [조회] 시스템 정책 아이디 중복 여부
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/checkSystemRuleDuplicated", method = { RequestMethod.POST })
	public Map<String, Object> checkSystemRuleDuplicated(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return systemService.checkSystemRuleDuplicated(request, paramMap);
	}

	/**
	 * [조회] 시스템 정책 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/getSystemRule", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getSystemRule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return systemService.getSystemRule(request, paramMap);
	}

	/**
	 * [조회] 시스템 정책 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/getSystemRuleList", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getSystemRuleList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return systemService.getSystemRuleList(request, paramMap);
	}

	/**
	 * [등록] 시스템 정책 정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/insertSystemRule", method = { RequestMethod.POST })
	public Map<String, Object> insertSystemRule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return systemService.insertSystemRule(request, paramMap);
	}

	/**
	 * [삭제] 시스템 정책 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/deleteSystemRule", method = { RequestMethod.POST })
	public Map<String, Object> deleteSystemRule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return systemService.deleteSystemRule(request, paramMap);
	}

	/**
	 * [수정] 시스템 정책 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/system/updateSystemRule", method = { RequestMethod.POST })
	public Map<String, Object> updateSystemRule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return systemService.updateSystemRule(request, paramMap);
	}
}
