package com.enicom.nims.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.DaoType;
import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service
public class ApiAntiLostServiceImpl implements ApiAntiLostService {
	private static String[] columns = {"library_key", "device_key", "conn_key"};
	private ServiceUtil serviceUtil;
	
	@Autowired
	public ApiAntiLostServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}
	
	@Override
	public List<Map<String, Object>> getDBConnListInServer(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList("apiAntilost.getDBConnList", paramMap, columns);
	}
	
	@Override
	public Map<String, Object> getBookHistoryMaxInServer(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectOne("apiAntilost.getBookHistoryMax", paramMap, columns);
	}
	
	@Override
	public Map<String, Object> getCountHistoryMaxInServer(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectOne("apiAntilost.getCountHistoryMax", paramMap, columns);
	}
	
	@Override
	public int insertBookHistoryInfoToServer(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.serviceCount("apiAntilost.insertBookHistoryInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public int insertCountHistoryInfoToServer(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.serviceCount("apiAntilost.insertCountHistoryInfo", paramMap, columns, Operation.insert);
	}

	
	
	@Override
	public int getBookHistoryCount(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectInt(DaoType.AntiLost, "antilost.getBookHistoryCount", paramMap, columns);
	}
	
	@Override
	public List<Map<String, Object>> getBookHistoryList(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.AntiLost, "antilost.getBookHistoryList", paramMap, columns);
	}

	@Override
	public int getCountHistoryCount(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectInt(DaoType.AntiLost, "antilost.getCountHistoryCount", paramMap, columns);
	}
	
	@Override
	public List<Map<String, Object>> getCountHistoryList(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.AntiLost, "antilost.getCountHistoryList", paramMap, columns);
	}

	@Override
	public List<Map<String, Object>> getDeviceList(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.AntiLost, "antilost.getDeviceList", paramMap, columns);
	}
}
