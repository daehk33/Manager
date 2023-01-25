package com.enicom.nims.dashboard.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service("dashboardService")
public class DashboardServiceImpl implements DashboardService {
	private ServiceUtil serviceUtil;
	
	@Autowired
	public DashboardServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getLiveHistoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getLiveHistoryList"};
		
		String grade = ParamUtil.parseString(paramMap.get("grade"));
		String model_auth = ParamUtil.parseString(paramMap.get("model_auth"));
		
		if(!grade.equals("0") && model_auth.equals("")) {
			return JsonUtil.makeListJSON(Collections.emptyList());
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getEventStatusList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "model_key"};
		String[] mappers = {"dashboard.getEventStatusList"};

		if(!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getEventAnalysisList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getEventAnalysisList"};

		if(!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getAlarmList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "model_key"};
		String[] mappers = {"dashboard.getAlarmList"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> deleteAlarmInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		if(paramMap.get("id") == null) return JsonUtil.makeResultJSON("410");
		paramMap.put("id", Long.parseLong((String) paramMap.get("id")));

		return serviceUtil.service("dashboard.deleteAlarmInfo", paramMap, Operation.delete);
	}

	@Override
	public Map<String, Object> getBookLoanStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getBookLoanStatus"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
	}

	@Override
	public Map<String, Object> getUseLogCntbyBook(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getUseLogCntbyBook"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getUseLogCntbyUser(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getUseLogCntbyUser"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"dashboard.getUseLogCntbyWeek"};
		String[] rules = {"12", "13"};

		List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceList", Collections.emptyMap());

		if(deviceList.size() > 0) {
			for(String id: rules) {
				for(Map<String,Object> device : deviceList) {
					int device_key = ParamUtil.parseInt(device.get("rec_key"));

					device.put("device_key", device_key);
					device.put("id", id);
					
					Map<String, Object> ruleResult = serviceUtil.selectOne("device.getDeviceRuleList", device);
					if(ruleResult != null) {
						String rule = ruleResult.get("opt01").toString();
						
						String paramName = "unattended_return";
						if(id.equals("12")) {
							paramName = "reserve_loan";
						}
						if(rule.equals("Y")) {
							paramMap.put(paramName, "Y");
						} else {
							paramMap.put(paramName, "N");
						}
					}
				}
			}
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getLoanReturnStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getLoanReturnStatus"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
	}

	@Override
	public Map<String, Object> getLoanReturnUseLogCntbyBook(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getLoanReturnUseLogCntbyBook"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getLoanReturnUseLogCntbyUser(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getLoanReturnUseLogCntbyUser"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getLoanReturnUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getLoanReturnUseLogCntbyWeek"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getReturnStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getReturnStatus"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getReturnUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getReturnUseLogCntByToday"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getReturnUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getReturnUseLogCntbyWeek"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getReturnUseLogCntbyTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getReturnUseLogCntbyTime"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getResvLoanStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getResvLoanStatus"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
	}

	@Override
	public Map<String, Object> getResvLoanUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getResvLoanUseLogCntbyToday"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getResvLoanUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getResvLoanUseLogCntbyWeek"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getGateUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getGateUseLogCntbyToday"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getGateUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getGateUseLogCntbyWeek"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getGateUseLogCntbyTime(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getGateUseLogCntbyTime"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getGateStatus(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getGateStatus"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
	}

	@Override
	public Map<String, Object> getAntiLostUseLogCntbyToday(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getAntiLostUseLogCntbyToday"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getAntiLostUseLogCntbyWeek(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getAntiLostUseLogCntbyWeek"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getAntiLostUseLogCntbyTime(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getAntiLostUseLogCntbyTime"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getAntiLostStatus(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = {"library_key"};
		String[] mappers = {"dashboard.getAntiLostStatus"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
	}
}
