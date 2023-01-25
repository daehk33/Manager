package com.enicom.nims.sls;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.api.service.ApiSLSService;
import com.enicom.nims.sls.service.SLSService;

@RestController
public class SLSController {
	private SLSService slsService;
	private ApiSLSService apiSlsService;

	@Autowired
	public SLSController(SLSService slsService, ApiSLSService apiSlsService) {
		this.slsService = slsService;
		this.apiSlsService = apiSlsService;
	}
	
	/**
	 * [조회] 선반 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getSLSSlotList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getSLSSlotList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return slsService.getSLSSlotList(request, paramMap);
	}

	/**
	 * [조회] 서가 위치 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getSLSDrumTrayList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getSLSDrumTrayList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return slsService.getSLSDrumTrayList(request, paramMap);
	}

	/**
	 * [조회] 사용자 대출 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getSLSUserLoanList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getSLSUserLoanList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return slsService.getSLSUserLoanList(request, paramMap);
	}

	/**
	 * [조회] 인기도서/우수회원 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getSLS{gubun}RankList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getSLSRankList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap, @PathVariable String gubun) throws Exception {
		paramMap.put("gubun",gubun);
		return slsService.getSLSRankList(request, paramMap);
	}

	/**
	 * [API] updateDeviceStatus
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/updateSLSStatus" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> updateSLSStatusInfo(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.updateSLSStatusInfo(request, paramMap);
	}

	/**
	 * [API] getEquipmentBookInner
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getEquipmentBookInner" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getEquipmentBookInner(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.getEquipmentBookInner(request, paramMap);
	}

	/**
	 * [API] getSLSCasInfoList
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getSLSCasInfoList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getSLSCasInfoList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.getSLSCasInfoList(request, paramMap);
	}
	/**
	 * [수정] updateSLSCasEnable
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/updateSLSCasEnable" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> updaetSLSCasEnable(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.updateSLSCasEnable(request, paramMap);
	}
	/**
	 * [수정] updateSLSCasBatchEnable
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/updateSLSCasBatchEnable" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> updaetSLSCasBatchEnable(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.updateSLSCasBatchEnable(request, paramMap);
	}

	/**
	 * [조회] 배출 도서 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getSLSBookOutList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getSLSBookOutList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.getSLSBookOutInfoList(request, paramMap);
	}

	/**
	 * [조회] 배출 도서 목록 동기화
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/syncSLSBookOutList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> syncSLSBookOutList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.syncSLSBookOutInfoList(request, paramMap);
	}

	/**
	 * [추가] insertSLSBookOutStatus
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/insertSLSBookOutInfo" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> insertSLSBookOutInfo(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.insertSLSBookOutInfoTotal(request, paramMap);
	}

	/**
	 * [수정] updateSLSBookOutStatus
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/updateSLSBookOutStatus" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> updateSLSBookOutStatus(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.updateSLSBookOutInfo(request, paramMap);
	}

	/**
	 * [삭제] deleteSLSBookOutStatus
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/deleteSLSBookOutInfo" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> deleteSLSBookOutStatus(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return apiSlsService.deleteSLSBookOutInfoTotal(request, paramMap);
	}

	/**
	 * [조회] 연체 도서 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/sls/getSLSOverdueList" , method = {RequestMethod.GET , RequestMethod.POST})
	public Map<String, Object> getSLSOverdueList(HttpServletRequest request, @RequestParam  Map<String, Object> paramMap) throws Exception {
		return slsService.getSLSOverdueList(request, paramMap);
   	}

}
