package com.enicom.nims.stats.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StatsService {
	public Map<String, Object> getStatsList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsDevice(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsCalendar(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getStatsSLSReservationLoanList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String ,Object> getStatsUnmannedReturnList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsUnmannedReturnDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsUnmannedReturnTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsUnmannedReturnDevice(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsUnmannedReturnCalendar(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getStatsLoanReturnList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsLoanReturnDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsLoanReturnTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsLoanReturnDevice(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsLoanReturnCalendar(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getStatsReturnList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsReturnDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsReturnTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsReturnDevice(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsReturnCalendar(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getStatsResvLoanList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsResvLoanTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsResvLoanDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsResvLoanDevice(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsResvLoanCalendar(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getStatsGateList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsGateTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsGateDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsGateDevice(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsGateCalendar(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getStatsAntiLostList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsAntiLostDay(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsAntiLostDate(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsAntiLostTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsAntiLostDevice(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getStatsAntiLostCalendar(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
