package com.enicom.nims.gate;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.api.service.ApiGateService;
import com.enicom.nims.gate.service.GateService;

@RestController
public class GateController {
	private GateService gateService;
	private ApiGateService apiGateService;

	@Autowired
	public GateController(GateService gateService, ApiGateService apiGateService) {
		this.gateService = gateService;
		this.apiGateService = apiGateService;
	}
	/**
	 * [조회] 운영 정책 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getGateRuleInfoList")
	public Map<String, Object> getGateRuleInfoList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.getGateRuleInfoList(request, paramMap);
	}
	
	/**
	 * [수정] 운영 정책 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/updateGateRuleInfo")
	public Map<String, Object> updateGateRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.updateGateRuleInfo(request, paramMap);
	}
	
	/**
	 * [조회] 장비 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getGateList")
	public Map<String, Object> getGateList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.getGateList(request, paramMap);
	}
	
	/**
	 * [조회] 입력장치 분류 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getInputList")
	public Map<String, Object> getInputList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.getInputList(request, paramMap);
	}
	
	/**
	 * [등록/수정] 입력장치 분류 정보 등록/수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/upsertInputInfo")
	public Map<String, Object> upsertInputInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.upsertInputInfo(request, paramMap);
	}
	
	/**
	 * [삭제] 입력장치 분류 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/deleteInputInfo")
	public Map<String, Object> deleteInputInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.deleteInputInfo(request, paramMap);
	}
	
	/**
	 * [조회] 게이트 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getGateInfoList")
	public Map<String, Object> getGateInfoList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.getGateInfoList(request, paramMap);
	}
	
	/**
	 * [등록] 게이트 정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/insertGateInfo")
	public Map<String, Object> insertGateInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.insertGateInfo(request, paramMap);
	}
	
	/**
	 * [수정] 게이트 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @장비 ID 중복 확인
	 */
	@RequestMapping(value = "/gate/updateGateInfo")
	public Map<String, Object> updateGateInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.updateGateInfo(request, paramMap);
	}
	
	/**
	 * [삭제] 게이트 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/deleteGateInfo")
	public Map<String, Object> deleteGateInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.deleteGateInfo(request, paramMap);
	}
	
	/**
	 * [조회] 그룹 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getGroupList")
	public Map<String, Object> getGroupList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.getGroupList(request, paramMap);
	}
	
	/**
	 * [등록] 그룹 정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/insertGroupInfo")
	public Map<String, Object> insertGroupInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.insertGroupInfo(request, paramMap);
	}
	
	/**
	 * [수정] 그룹 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @장비 ID 중복 확인
	 */
	@RequestMapping(value = "/gate/updateGroupInfo")
	public Map<String, Object> updateGroupInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.updateGroupInfo(request, paramMap);
	}
	
	/**
	 * [삭제] 그룹 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/deleteGroupInfo")
	public Map<String, Object> deleteGroupInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.deleteGroupInfo(request, paramMap);
	}
	
	/**
	 * [조회] 서버 설정 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getGateDBConnInfo")
	public Map<String, Object> getGateDBConnInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.getGateDBConnInfo(request, paramMap);
	}
	
	/**
	 * [등록/수정] 서버 설정 등록/수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/upsertGateDBConn")
	public Map<String, Object> upsertGateDBConn(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.upsertGateDBConn(request, paramMap);
	}
	
	/**
	 * [조회] 게이트 스케줄 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getGateScheduleList")
	public Map<String, Object> getGateScheduleList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.getGateScheduleList(request, paramMap);
	}
	
	/**
	 * [등록] 게이트 스케줄 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/insertGateScheduleInfo")
	public Map<String, Object> insertGateScheduleInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.insertGateScheduleInfo(request, paramMap);
	}
	
	/**
	 * [수정] 게이트 스케줄 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/updateGateScheduleInfo")
	public Map<String, Object> updateGateScheduleInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.updateGateScheduleInfo(request, paramMap);
	}
	
	/**
	 * [삭제] 게이트 스케줄 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/deleteGateScheduleInfo")
	public Map<String, Object> deleteGateScheduleInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.deleteGateScheduleInfo(request, paramMap);
	}
	
	/**
	 * [조회] 게이트 생체인증 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getSecurityList")
	public Map<String, Object> getSecurityList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.getSecurityList(request, paramMap);
	}
	
	/**
	 * [등록] 게이트 생체인증 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/insertSecurityInfo")
	public Map<String, Object> insertsecurityInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.insertSecurityInfo(request, paramMap);
	}
	
	/**
	 * [수정] 게이트 생체인증 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/updateSecurityInfo")
	public Map<String, Object> updatesecurityInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.updateSecurityInfo(request, paramMap);
	}
	
	/**
	 * [삭제] 게이트 생체인증 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/deleteSecurityInfo")
	public Map<String, Object> deletesecurityInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.deleteSecurityInfo(request, paramMap);
	}
	
	/**
	 * [등록] command 설정 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/insertCommandInfo")
	public Map<String, Object> insertCommandInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.insertCommandInfo(request, paramMap);
	}
	
	/**
	 * [등록] command 설정 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/updateCommandInfo")
	public Map<String, Object> updateCommandInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.updateCommandInfo(request, paramMap);
	}
	
	/**
	 * [삭제] command 설정 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/deleteCommandInfo")
	public Map<String, Object> deleteCommandInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.deleteCommandInfo(request, paramMap);
	}
	
	/**
	 * [조회] 장비 설정 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getCfgRuleInfo")
	public Map<String, Object> getCfgRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.getCfgRuleInfo(request, paramMap);
	}
	
	/**
	 * [등록/수정] 장비 설정 등록/수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/upsertCfgRuleInfo")
	public Map<String, Object> upsertCfgRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.upsertCfgRuleInfo(request, paramMap);
	}
	
	/**
	 * [등록] 장비 설정 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/insertCfgRuleInfo")
	public Map<String, Object> insertCfgRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.insertCfgRuleInfo(request, paramMap);
	}
	
	/**
	 * [수정] 장비 설정 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/updateCfgRuleInfo")
	public Map<String, Object> updateCfgRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiGateService.updateCfgRuleInfo(request, paramMap);
	}
	
	/**
	 * [조회] 게이트 시리얼 포트 중복 여부
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/checkPortDuplicated")
	public Map<String, Object> checkPortDeplicated(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.checkPortDuplicated(request, paramMap);
	}
	
	/**
	 * [조회] 게이트 버튼 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/getBtnList")
	public Map<String, Object> getBtnList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.getBtnList(request, paramMap);
	}
	
	/**
	 * [등록] 게이트 버튼 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/insertBtnInfo")
	public Map<String, Object> insertBtnInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.insertBtnInfo(request, paramMap);
	}
	
	/**
	 * [수정] 게이트 버튼 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/updateBtnInfo")
	public Map<String, Object> updateBtnInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.updateBtnInfo(request, paramMap);
	}
	
	/**
	 * [삭제] 게이트 버튼 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/gate/deleteBtnInfo")
	public Map<String, Object> deleteBtnInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiGateService.deleteBtnInfo(request, paramMap);
	}

}
