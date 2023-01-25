package com.enicom.nims.sls.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service(value = "slsService")
public class SLSServiceImpl implements SLSService {
	private ServiceUtil serviceUtil;
	
	@Autowired
	public SLSServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}
	
	@Override
	public Map<String, Object> getSLSSlotList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		String[] mappers = { "sls.getSLSSlotCount", "sls.getSLSSlotList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getSLSDrumTrayList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "sls.getSLSDrumTrayList" };
		return serviceUtil.select(mappers, paramMap, Operation.getList);
	}

	@Override
	public Map<String, Object> getSLSUserLoanList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "sls.getSLSUserLoanCount", "sls.getSLSUserLoanList" };
		String[] columns = { "library_key", "device_key" };
		
		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String, Object> getSLSRankList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "limit" };
		String[] mappers = null;
		
		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		String gubun = ParamUtil.parseString(paramMap.get("gubun")).toLowerCase();
		if("book".equals(gubun)) {
			mappers = new String[] { "sls.getSLSBookRankListCnt", "sls.getSLSBookRankList" };
		}
		else if("user".equals(gubun)) {
			mappers = new String[] { "sls.getSLSUserRankListCnt", "sls.getSLSUserRankList" };
		}
		else if("location".equals(gubun)) {
			mappers = new String[] { "sls.getSLSUserRankListByLocationCnt", "sls.getSLSUserRankListByLocation" };
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getSLSOverdueList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		String[] mappers  = { "sls.getSLSOverdueListCount", "sls.getSLSOverdueList" };
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
}
