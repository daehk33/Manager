package com.enicom.nims.loanReturn.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LoanReturnService {
	
	public Map<String, Object> getLoanReturnRankList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
