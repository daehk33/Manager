package com.enicom.nims.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.manager.service.ManagerService;

@RestController
public class ManagerController {
	private ManagerService managerService;

	@Autowired
	public ManagerController(ManagerService managerService) {
		this.managerService = managerService;
	}

	/**
	 * [조회] 도서관 아이디 중복여부 확인
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/checkManagerDuplicated", method = { RequestMethod.POST })
	public Map<String, Object> checkManagerDuplicated(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return managerService.checkManagerDuplicated(request, paramMap);
	}

	/**
	 * [조회] 시스템 사용자 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/getManagerInfo", method = { RequestMethod.POST })
	public Map<String, Object> getManagerInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return managerService.getManagerInfo(request, paramMap);
	}

	/**
	 * [조회] 시스템 사용자 정보 수량 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/getManagerCount", method = { RequestMethod.POST })
	public Map<String, Object> getManagerCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return managerService.getManagerCount(request, paramMap);
	}

	/**
	 * [조회] 시스템 사용자 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/getManagerList", method = { RequestMethod.POST })
	public Map<String, Object> getManagerList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return managerService.getManagerList(request, paramMap);
	}

	/**
	 * [등록] 시스템 사용자 정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @사용자 ID 중복여부 확인
	 */
	@RequestMapping(value = "/manager/insertManagerInfo", method = RequestMethod.POST)
	public Map<String, Object> insertManagerInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return managerService.insertManagerInfo(request, paramMap);
	}

	/**
	 * [삭제] 사용자 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/deleteManagerInfo", method = { RequestMethod.POST })
	public Map<String, Object> deleteManagerInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return managerService.deleteManagerInfo(request, paramMap);
	}

	/**
	 * [수정] 사용자 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @사용자 ID 중복여부 확인
	 */
	@RequestMapping(value = "/manager/updateManagerInfo", method = { RequestMethod.POST })
	public Map<String, Object> updateManagerInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return managerService.updateManagerInfo(request, paramMap);
	}

	/**
	 * [수정] 사용자 비밀번호 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/updateManagerPassword", method = { RequestMethod.POST })
	public Map<String, Object> updateManagerPassword(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return managerService.updateManagerPassword(request, paramMap);
	}

	/**
	 * [수정] 사용자 비밀번호 초기화
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/resetManagerPassword", method = { RequestMethod.POST })
	public Map<String, Object> resetManagerPassword(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return managerService.resetManagerPassword(request, paramMap);
	}

	/**
	 * [수정] 사용자 테마 사용 기록 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/manager/updateManagerTheme", method = { RequestMethod.POST })
	public Map<String, Object> updateManagerTheme(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return managerService.updateManagerTheme(request, paramMap);
	}
}
