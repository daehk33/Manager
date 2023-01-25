package com.enicom.nims.model.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ModelService {

	Map<String, Object> checkModelDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getModelCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getModelTypeList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getModelList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> insertModelInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> deleteModelInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateModelInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
