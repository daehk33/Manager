package com.enicom.nims.Return.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ReturnService {
	
	public Map<String, Object> getLoanReturnRankList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
