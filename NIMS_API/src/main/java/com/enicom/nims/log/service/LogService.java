package com.enicom.nims.log.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LogService {
	public Map<String, Object> getLogList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> insertLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> deleteLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
