package com.enicom.nims.system.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SystemService {
	public Map<String, Object> checkSystemRuleDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSystemRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSystemRuleList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertSystemRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteSystemRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateSystemRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
