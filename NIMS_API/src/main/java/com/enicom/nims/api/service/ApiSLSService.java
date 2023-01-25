package com.enicom.nims.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiSLSService {
	public List<Map<String, Object>> getSLSBookInfoList() throws Exception;
	public Map<String, Object> getSLSBookInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSBookInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSBookInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> syncSLSBookStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public List<Map<String, Object>> getSLSCasInfoList() throws Exception;
	public int updateSLSCasInfo(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getSLSCasInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSCasInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSCasInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSCasEnable(HttpServletRequest request, Map<String, Object> parmaMap) throws Exception;
	public Map<String, Object> updateSLSCasBatchEnable(HttpServletRequest request, Map<String, Object> parmaMap) throws Exception;
	public Map<String, Object> updateSLSCasSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSCasInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;

	public List<Map<String, Object>> getSLSRuleInfoList() throws Exception;
	public Map<String, Object> getSLSRuleInfoList(Map<String, Object> paramMap) throws Exception;
	public int updateSLSRuleInfo(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getSLSRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSRuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSRuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getSLSUseLogInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSUseLogInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSUseLogInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;

	public List<Map<String, Object>> getSLSReservationInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSReservationInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSReservationInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSReservationInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;

	public List<Map<String, Object>> getSLSReturnInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSReturnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSReturnInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSReturnInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getSLSStatusInfoList() throws Exception;
	public Map<String, Object> updateSLSStatusInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getSLSDrumRuleInfoList() throws Exception;
	public Map<String, Object> getSLSDrumRuleInfoList(Map<String, Object> paramMap) throws Exception;
	public int updateSLSDrumRuleInfo(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getSLSDrumRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSDrumRuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSDrumRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSDrumRuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getEquipmentBookInner(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getSLSBookOutInfoList() throws Exception;
	public Map<String, Object> getSLSBookOutInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSBookOutInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> syncSLSBookOutInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSBookOutInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSLSBookOutInfoTotal(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSBookOutInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteSLSBookOutInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteSLSBookOutInfoTotal(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
