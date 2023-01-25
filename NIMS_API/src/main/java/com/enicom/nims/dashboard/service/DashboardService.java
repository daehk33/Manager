package com.enicom.nims.dashboard.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface DashboardService {
	
	Map<String, Object> getLiveHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getEventStatusList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getEventAnalysisList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getAlarmList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> deleteAlarmInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getBookLoanStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getUseLogCntbyBook(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getUseLogCntbyUser(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getLoanReturnUseLogCntbyBook(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLoanReturnUseLogCntbyUser(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLoanReturnUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLoanReturnStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getReturnStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getReturnUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getReturnUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getReturnUseLogCntbyTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getResvLoanUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getResvLoanUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getResvLoanStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getGateUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getGateUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getGateUseLogCntbyTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getGateStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getAntiLostUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getAntiLostUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getAntiLostUseLogCntbyTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getAntiLostStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
