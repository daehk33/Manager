package com.enicom.nims.api.service;

import java.util.List;
import java.util.Map;

public interface ApiAntiLostService {
	List<Map<String, Object>> getDBConnListInServer(Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getBookHistoryMaxInServer(Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getCountHistoryMaxInServer(Map<String, Object> paramMap) throws Exception;
	
	int insertBookHistoryInfoToServer(Map<String, Object> paramMap) throws Exception;
	int insertCountHistoryInfoToServer(Map<String, Object> paramMap) throws Exception;
	
	List<Map<String, Object>> getDeviceList(Map<String, Object> paramMap) throws Exception;
	
	int getBookHistoryCount(Map<String, Object> paramMap) throws Exception;
	List<Map<String, Object>> getBookHistoryList(Map<String, Object> paramMap) throws Exception;
	
	int getCountHistoryCount(Map<String, Object> paramMap) throws Exception;
	List<Map<String, Object>> getCountHistoryList(Map<String, Object> paramMap) throws Exception;
}
