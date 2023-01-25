package com.enicom.nims.history.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service("historyService")
public class HistoryServiceImpl implements HistoryService {
	private ServiceUtil serviceUtil;

	@Autowired
	public HistoryServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getLoanHistoryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "history.getLoanHistoryCount", "history.getLoanHistoryList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByTypeHistory(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getUnmannedReturnHistoryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "history.getUnmannedReturnHistoryCount", "history.getUnmannedReturnHistoryList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByTypeHistory(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getLoanReturnHistoryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "history.getLoanReturnHistoryCount", "history.getLoanReturnHistoryList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByTypeHistory(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getReturnHistoryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "history.getReturnHistoryCount", "history.getReturnHistoryList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByTypeHistory(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getResvLoanHistoryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "history.getResvLoanHistoryCount", "history.getResvLoanHistoryList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByTypeHistory(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getGateHistoryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "history.getGateHistoryCount", "history.getGateHistoryList" };
		String[] columns = { "library_key", "device_key", "manager_key" };

		if (!ParamUtil.formatDateByTypeHistory(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getAntilostHistoryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "history.getAntiLostHistoryCount", "history.getAntiLostHistoryList" };
		String[] columns = { "library_key", "device_key" };

		if (!ParamUtil.formatDateByTypeHistory(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
}
