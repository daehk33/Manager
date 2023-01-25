package com.enicom.nims.code.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service
public class CodeServiceImpl implements CodeService {
	private ServiceUtil serviceUtil;

	@Autowired
	public CodeServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getCodeGroupCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "code.getCodeGroupCount" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> checkCodeGrpDuplicated(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "code.checkCodeGrpDuplicated" };
		return serviceUtil.select(mappers, paramMap, Operation.getCheck);
	}

	@Override
	public Map<String, Object> getCodeGroupList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "code.getCodeGroupCount", "code.getCodeGroupList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> insertCodeGroupInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("code.insertCodeGroupInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteCodeGroupInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("code.deleteCodeGroupInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateCodeGroupInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("code.updateCodeGroupInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> checkCodeDuplicated(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "grp_key" };
		String[] mappers = { "code.checkCodeDuplicated" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCheck);
	}

	@Override
	public Map<String, Object> getCodeCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "code.getCodeCount" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getCodeList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = { "rec_key", "grp_key" };
		String[] mappers = { "code.getCodeCount", "code.getCodeList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> insertCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "grp_key" };
		return serviceUtil.service("code.insertCodeInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("code.deleteCodeInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "grp_key" };
		return serviceUtil.service("code.updateCodeInfo", paramMap, columns, Operation.update);
	}
}
