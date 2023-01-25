package com.enicom.nims.code.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CodeService {

	Map<String, Object> checkCodeGrpDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getCodeGroupList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getCodeGroupCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> insertCodeGroupInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> deleteCodeGroupInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateCodeGroupInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	Map<String, Object> checkCodeDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getCodeList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getCodeCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> insertCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> deleteCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateCodeInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
