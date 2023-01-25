package com.enicom.nims.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiLoanReturnService {
	public Map<String, Object> getLoanReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertLoanReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanReturnRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getLoanReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertLoanReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanReturnDIDRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnDIDRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanReturnBannerImageList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getLoanReturnHolidayRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnHolidayRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanReturnHolidayInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getLoanReturnHolidayList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanReturnHolidayInfo(HttpServletRequest rqeuest, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> upsertLoanReturnHolidayInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteLoanReturnHolidayInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnHolidaySendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> loadLoanReturnBannerSettings(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanReturnEquipRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnEquipRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> upsertLoanReturnEquipRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnEquipRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getLoanReturnBannerSettingInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getLoanReturnBannerSettingInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnBannerSettingInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> upsertLoanReturnBannerSettingInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnBannerSettingSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanReturnLastLogKey(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getLoanReturnRuleInfoList(Map<String, Object> paramMap) throws Exception;
	public List<Map<String, Object>> getLoanReturnUseLogInfoList(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> updateLoanReturnRuleInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnBannerChanged(Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getLoanReturnHolidayList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getLoanReturnHolidayInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> upsertLoanReturnHolidayInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteLoanReturnHolidayInfo(Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getLoanReturnEquipRuleInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLoanReturnEquipRuleInfo(Map<String, Object> paramMap) throws Exception;
	public int updateLoanReturnBannerSettingInfo(Map<String, Object> paramMap) throws Exception;
}
