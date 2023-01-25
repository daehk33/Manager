package com.enicom.nims.api.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiReturnService {
	public Map<String, Object> getReturnUseLogInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getReturnRuleInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getReturnRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getReturnRuleInfo(HttpServletRequest request, Map<String ,Object> paramMap) throws Exception;
	public Map<String, Object> insertReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateReturnRuleInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateReturnRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getReturnLastLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}