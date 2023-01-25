package com.enicom.nims.manager.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ManagerService {

	Map<String, Object> checkManagerDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getManagerInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception ;
	Map<String, Object> getManagerInfoForLog(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getManagerCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getManagerList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> insertManagerInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> deleteManagerInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateManagerInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateManagerPassword(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> resetManagerPassword(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateManagerTheme(HttpServletRequest request, Map<String, Object> paramMap) throws Exception; 
}
