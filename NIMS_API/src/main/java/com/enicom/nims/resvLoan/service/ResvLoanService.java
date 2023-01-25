package com.enicom.nims.resvLoan.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ResvLoanService {
	public Map<String, Object> getResvLoanRankList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanCabinetList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getReservedBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getResvLoanCabinetTotalInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> checkModuleDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanLastModule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
