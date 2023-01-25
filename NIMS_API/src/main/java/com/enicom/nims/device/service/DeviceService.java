package com.enicom.nims.device.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface DeviceService {
	Map<String, Object> getDeviceCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getDeviceList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> insertDeviceInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> deleteDeviceInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateDeviceInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceInfoCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getDeviceInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getDeviceWolInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceStatusList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getDeviceStatusTotal(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceDBConnList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateDeviceConnInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceRuleList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateDeviceRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceEventDetailList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getDeviceEventType(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceControlButtonList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateDeviceControllStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDrumRuleList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateDrumRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> updateBookOutWorkStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> getDeviceSftpInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	List<Map<String, Object>> getDeviceList(Map<String, Object> paramMap) throws Exception;
	List<Map<String, Object>> getDeviceDBConnList(Map<String, Object> paramMap) throws Exception;
	List<Map<String, Object>> getDeviceRuleList(Map<String, Object> paramMap) throws Exception;
}
