package com.enicom.nims.common.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service
public class CommonServiceImpl implements CommonService {
	private ServiceUtil serviceUtil;
	
	@Autowired
	public CommonServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}
	
	public Map<String, Object> getCategoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"manager_grade"};
		String[] mappers = {"common.getCategoryList"};
		
		String manager_grade = ParamUtil.parseString(paramMap.get("manager_grade"));
		String model_auth = ParamUtil.parseString(paramMap.get("model_auth"));
		
		if(!manager_grade.equals("0")) {
			if(!model_auth.equals("")) {
				String modelAuth = paramMap.get("model_auth").toString();
				int[] modelAuthArry = Arrays.stream(modelAuth.split(",")).mapToInt(Integer::parseInt).toArray();
				
				paramMap.put("model_auth", modelAuthArry);
			}
			else {
				return JsonUtil.makeResultJSON("210");
			}
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	public Map<String, Object> getSubCategoryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"manager_grade"};
		String[] mappers = {"common.getSubCategoryList"};
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	public Map<String, Object> getMenuList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"manager_grade", "device_key", "library_key"}; 
		String[] mappers = {"common.getMenuList"};
		
		List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceList", paramMap, columns);
		
		if(deviceList.size() > 0) {
			for(Map<String,Object> device: deviceList) {
				device.put("device_key", device.get("rec_key"));
				device.put("id", "12");
				
				Map<String, Object> rule = serviceUtil.selectOne("device.getDeviceRuleList", device, columns);
				
				if(rule != null) {
					String opt01 = ParamUtil.parseString(rule.get("opt01"));
					
					if(opt01.equals("Y")) {
						paramMap.put("reserve_loan", "Y");
					} else {
						paramMap.put("reserve_loan", "N");
					}
				}
			}
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
}
