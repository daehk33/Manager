package com.enicom.nims.api.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiService {
	public Map<String, Object> service(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertEnterInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getLoanInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertLoanInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertReturnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateReturnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertAntiLostInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> wakeOnLan(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> sftpService(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> serialService() throws Exception;
}
