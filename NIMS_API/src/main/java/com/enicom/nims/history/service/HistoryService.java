package com.enicom.nims.history.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface HistoryService {
	public Map<String, Object> getLoanHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getUnmannedReturnHistoryList(HttpServletRequest request, Map<String ,Object> paramMap) throws Exception;
	public Map<String, Object> getLoanReturnHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getReturnHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getGateHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getAntilostHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
