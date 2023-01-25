package com.enicom.nims.api.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiGateService {
	public Map<String, Object> getGateUseLogInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertBatchGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateLatestLogDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getGateUserInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateUserInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateUserInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateUserInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getGateCompanyCodeInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateCompanyCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateCompanyCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateCompanyCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getGateDeptCodeInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateDeptCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateDeptCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateDeptCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getGateMajorCodeInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateMajorCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateMajorCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateMajorCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getGatePositionCodeInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGatePositionCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGatePositionCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGatePositionCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getGateRuleInfoList() throws Exception;
	public Map<String, Object> getGateRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateRuleInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getInputList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> upsertInputInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteInputInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getGateList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteGateInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
		
	public Map<String, Object> getGroupList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGroupInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGroupInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteGroupInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getGateDBConnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> upsertGateDBConn(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getGateScheduleList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertGateScheduleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateGateScheduleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteGateScheduleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getSecurityList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSecurityInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSecurityInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteSecurityInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> insertCommandInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateCommandInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteCommandInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> upsertCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> checkPortDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getBtnList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertBtnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateBtnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteBtnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

}
