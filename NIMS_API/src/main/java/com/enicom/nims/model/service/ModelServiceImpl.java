package com.enicom.nims.model.service;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service(value = "modelService")
public class ModelServiceImpl implements ModelService {
	private ServiceUtil serviceUtil;

	@Autowired
	public ModelServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> checkModelDuplicated(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "model.checkModelDuplicated" };
		return serviceUtil.select(mappers, paramMap, Operation.getCheck);
	}

	@Override
	public Map<String, Object> getModelTypeList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "model.getModelTypeCount", "model.getModelTypeList" };

		if (!paramMap.get("manager_grade").toString().equals("0")) {
			if (paramMap.get("model_auth") != null && !paramMap.get("model_auth").toString().isEmpty()) {
				String modelAuth = paramMap.get("model_auth").toString();
				int[] modelAuthArry = Arrays.stream(modelAuth.split(",")).mapToInt(Integer::parseInt).toArray();

				paramMap.put("model_auth", modelAuthArry);
			}
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getModelCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "model.getModelCount" };
		String[] columns = { "rec_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getModelList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "model.getModelCount", "model.getModelList" };
		String[] columns = { "rec_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> insertModelInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("model.insertModelInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteModelInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("model.deleteModelInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateModelInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("model.updateModelInfo", paramMap, columns, Operation.update);
	}

}
