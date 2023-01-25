package com.enicom.nims.system.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service
public class SystemServiceImpl implements SystemService {
	private ServiceUtil serviceUtil;

	@Autowired
	public SystemServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> checkSystemRuleDuplicated(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "system.checkSystemRuleDuplicated" };
		return serviceUtil.select(mappers, paramMap, Operation.getCheck);
	}

	@Override
	public Map<String, Object> getSystemRule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "getSystemRuleList" };
		return serviceUtil.select(mappers, paramMap, Operation.getOne);
	}

	@Override
	public Map<String, Object> getSystemRuleList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "getSystemRuleList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> insertSystemRule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		return serviceUtil.service("system.insertSystemRule", paramMap, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteSystemRule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("system.deleteSystemRule", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateSystemRule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("system.updateSystemRule", paramMap, columns, Operation.update);
	}
}
