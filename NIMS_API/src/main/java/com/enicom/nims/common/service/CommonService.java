package com.enicom.nims.common.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CommonService {
	public Map<String, Object> getCategoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getSubCategoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getMenuList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
