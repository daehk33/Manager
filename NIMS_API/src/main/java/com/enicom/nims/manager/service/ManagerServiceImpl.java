package com.enicom.nims.manager.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.CommonDAO;
import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;

@Service("managerService")
public class ManagerServiceImpl implements ManagerService {
	private ServiceUtil serviceUtil;

	@Autowired
	public ManagerServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> checkManagerDuplicated(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "manager.checkManagerDuplicated" };
		return serviceUtil.select(mappers, paramMap, Operation.getCheck);
	}

	@Override
	public Map<String, Object> getManagerCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "manager.getManagerCount" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getManagerList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "manager.getManagerCount", "manager.getManagerList" };
		return serviceUtil.select(mappers, paramMap, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getManagerInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "manager.getManagerInfo" };

		if (paramMap.containsKey("rec_key")) {
			return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
		}

		// 로그인시 사용자 정보 불러올 때
		Map<String, Object> manager = serviceUtil.selectOne(mappers[0], paramMap);
		if (manager == null || manager.isEmpty() || !manager.get("password").equals(paramMap.get("password"))) {
			return JsonUtil.makeResultJSON("420");
		} else {
			// 로그인 성공
			paramMap.put("rec_key", manager.get("rec_key").toString());
			serviceUtil.service("manager.updateManagerLogin", paramMap, columns, Operation.update);
		}
		manager.remove("password");

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("CODE", "220");
		result.put("DATA", manager);
		return JsonUtil.makeJSON("result", result);
	}

	@Override
	public Map<String, Object> getManagerInfoForLog(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "manager_key" };
		String[] mappers = { "manager.getManagerInfoForLog" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertManagerInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "manager_grade", "library_key" };
		return serviceUtil.service("manager.insertManagerInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteManagerInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("manager.deleteManagerInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateManagerInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "manager_grade", "library_key" };
		return serviceUtil.service("manager.updateManagerInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateManagerPassword(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "manager_grade", "library_key" };
		String[] managerMapper = { "manager.getManagerInfo" };

		Map<String, Object> manager = serviceUtil.select(managerMapper, paramMap, columns, Operation.getOneOriMap);
		if (manager != null) {
			if (!manager.get("password").equals(paramMap.get("password"))) {
				return JsonUtil.makeResultJSON("480", "기존 비밀번호가 일치하지 않습니다. \n 다시 입력해주세요.");
			}
			else {
				return serviceUtil.service("manager.updateManagerPassword", paramMap, columns, Operation.update);
			}
		}

		return JsonUtil.makeResultJSON("420");
	}

	@Override
	public Map<String, Object> resetManagerPassword(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("manager.resetManagerpassword", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateManagerTheme(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("manager.updateManagerTheme", paramMap, columns, Operation.update);
	}
}
