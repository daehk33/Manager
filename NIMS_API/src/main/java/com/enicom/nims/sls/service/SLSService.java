package com.enicom.nims.sls.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SLSService {
	
	public Map<String, Object> getSLSSlotList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSDrumTrayList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSUserLoanList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSRankList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSLSOverdueList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
