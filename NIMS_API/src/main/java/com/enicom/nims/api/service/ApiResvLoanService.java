package com.enicom.nims.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface ApiResvLoanService {
	public List<Map<String, Object>> getResvLoanUseLogInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanUseLogInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanUseLogInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getResvLoanCabinetModuleInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanCabinetModuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanCabinetModuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanCabinetModuleInfoGallery(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanCabinetModuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanCabinetModuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteResvLoanCabinetModuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanCabinetModuleSendResult(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getResvLoanCabinetInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanCabinetInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanCabinetInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanCabinetInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanCabinetInfoGallery(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanCabinetInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanCabinetInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanCabinetInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteResvLoanCabinetInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanCabinetType(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanCabinetSendResult(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanUnmodifiableCabinetList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public List<Map<String, Object>> getResvLoanModuleInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanModuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanModuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanModuleInfoGallery(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanModuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanModule(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanModuleInfo(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteResvLoanModule(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanModuleSendResult(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;	
	
	public List<Map<String, Object>> getResvLoanRuleInfoList(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> getResvLoanRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertResvLoanRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanRuleInfo(Map<String, Object> paramMap) throws Exception;	
	public Map<String, Object> updateResvLoanRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateResvLoanRuleSendResult(HttpServletRequest request,  Map<String, Object> paramMap) throws Exception;
}
