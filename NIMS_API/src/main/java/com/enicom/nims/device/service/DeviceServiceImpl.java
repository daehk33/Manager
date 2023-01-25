package com.enicom.nims.device.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.dao.DaoType;
import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service(value = "deviceService")
public class DeviceServiceImpl implements DeviceService {
	private ServiceUtil serviceUtil;

	@Autowired
	public DeviceServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getDeviceCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "model_key" };
		String[] mappers = { "device.getDeviceCount" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getDeviceList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "model_key" };
		String[] mappers = { "device.getDeviceCount", "device.getDeviceList" };
		
		String model_key = ParamUtil.parseString(paramMap.get("model_key"));
		
		if(!model_key.equals("")) {
			if(model_key.contains(",")) {
				int[] modelKeyArry = Arrays.stream(model_key.split(",")).mapToInt(Integer::parseInt).toArray();
				
				paramMap.put("model_key", null);
				paramMap.put("model_key_array", modelKeyArry);
			}
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getDeviceInfoCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "model_key" };
		String[] mappers = { "device.getDeviceInfoCount" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getDeviceInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "model_key" };
		String[] mappers = { "device.getDeviceInfoCount", "device.getDeviceInfoList" };
		
		String model_key = ParamUtil.parseString(paramMap.get("model_key"));
		
		if(!model_key.equals("")) {
			if(model_key.contains(",")) {
				int[] modelKeyArry = Arrays.stream(model_key.split(",")).mapToInt(Integer::parseInt).toArray();
				
				paramMap.put("model_key", null);
				paramMap.put("model_key_array", modelKeyArry);
			}
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getDeviceWolInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "device_key" };
		String[] mappers = { "device.getDeviceWolInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertDeviceInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "model_key", "library_key" };
		return serviceUtil.service("device.insertDeviceInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteDeviceInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "model_key" };
		return serviceUtil.service("device.deleteDeviceInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateDeviceInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "model_key" };
		return serviceUtil.service("device.updateDeviceInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateDeviceConnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "device_status" };
		return serviceUtil.service("device.updateDeviceConnInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getDeviceStatusList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "device.getDeviceStatusList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getDeviceStatusTotal(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "device.getDeviceStatusTotal" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getDeviceDBConnList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "model_key" };
		String[] mappers = { "device.getDeviceDBConnList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getDeviceBookList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		String[] mappers = { "device.getDeviceBookListCnt", "device.getDeviceBookList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getDeviceRuleList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		String[] mappers = { "device.getDeviceRuleList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> updateDeviceRule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };

		for (String strKey : paramMap.keySet()) {
			if (strKey.contains("opt")) {
				if (paramMap.get(strKey).toString().isEmpty()) {
					paramMap.put(strKey, null);
				}
			}
		}

		return serviceUtil.service("device.updateDeviceRule", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getDeviceEventDetailList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		String[] mappers = { "getDeviceEventDetailListCnt", "device.getDeviceEventDetailList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getDeviceEventType(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "device.getDeviceEventType" };
		return serviceUtil.select(mappers, paramMap, Operation.getList);
	}

	@Override
	public Map<String, Object> getDeviceControlButtonList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "device.getDeviceControlButtonList" };
		return serviceUtil.select(mappers, paramMap, Operation.getList);
	}

	@Override
	public Map<String, Object> updateDeviceControllStatus(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "device_key" };
		return serviceUtil.service("device.updateDeviceControllStatus", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getDrumRuleList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "volume" };
		String[] mappers = { "device.getDrumRuleList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> updateDrumRule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "rec_key", "volume" };
		paramMap = ParamUtil.emptyToNull(paramMap);

		int result = serviceUtil.serviceCount("device.updateDrumRule", paramMap, columns, Operation.update);

		if (result > 0) {
			serviceUtil.service("apiSlibrary.updateSLSCasType", paramMap, columns, Operation.update);
			return JsonUtil.makeResultJSON("200");
		}

		return JsonUtil.makeResultJSON("400");
	}

	@Override
	public Map<String, Object> updateBookOutWorkStatus(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };

		int result = serviceUtil.serviceCount("device.updateBookOutWorkStatus", paramMap, columns, Operation.update);
		if (result > 0) {
			String device_id = ParamUtil.parseString(paramMap.get("device_id"));

			if (!device_id.equals("")) {
				DBContextHolder.setDBType(device_id);
				return serviceUtil.service(DaoType.Smart, "smart.updateBookOutWorkStatus", paramMap, columns,
						Operation.update);
			} else {
				return JsonUtil.makeResultJSON("410");
			}
		} else {
			return JsonUtil.makeResultJSON("400");
		}
	}

	@Override
	public Map<String, Object> getDeviceSftpInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		String[] mappers = { "device.getDeviceSftpInfoList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public List<Map<String, Object>> getDeviceList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "rec_key", "library_key", "model_key" };
		return serviceUtil.selectList("device.getDeviceList", paramMap, columns);
	}

	@Override
	public List<Map<String, Object>> getDeviceDBConnList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "model_key" };
		return serviceUtil.selectList("device.getDeviceDBConnList", paramMap, columns);
	}

	@Override
	public List<Map<String, Object>> getDeviceRuleList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "library_key", "device_key" };
		
		return serviceUtil.selectList("device.getDeviceRuleList", paramMap, columns);
	}
}
