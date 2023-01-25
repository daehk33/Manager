package com.enicom.nims.log.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service
public class LogServiceImpl implements LogService {
	private ServiceUtil serviceUtil;

	@Autowired
	public LogServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getLogList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "log.getLogCount", "log.getLogList" };
		String[] columns = { "admin_key", "library_key" };

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> insertLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "admin_key" };
		return serviceUtil.service("log.insertLogInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "admin_key" };
		return serviceUtil.service("log.insertLogInfo", paramMap, columns, Operation.delete);
	}
}
