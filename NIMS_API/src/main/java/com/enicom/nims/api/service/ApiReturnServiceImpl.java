package com.enicom.nims.api.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.ParamUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value = "apiReturnService")
public class ApiReturnServiceImpl implements ApiReturnService{
	private ServiceUtil serviceUtil;
	
	@Autowired
	public ApiReturnServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}
	
	@Override
	public Map<String, Object> getReturnUseLogInfoList(Map<String, Object> paramMap) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String api_url = ParamUtil.parseString(paramMap.get("db_url")) + "GetLogInfoList";
		JSONObject params = new JSONObject();
		
		params.put("book_state_date", ParamUtil.parseString(paramMap.get("book_state_date")));
		params.put("book_state_time", ParamUtil.parseString(paramMap.get("book_state_time")));
		
		try {
			URL url = new URL(api_url);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(params.toString());
			wr.flush();
			wr.close();
			
			int responseCode = conn.getResponseCode();
			
			BufferedReader br;
			
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			
			result = (Map<String, Object>) new ObjectMapper().readValue(response.toString(), HashMap.class).get("d");
			if(result != null) {
				result.remove("__type");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Map<String, Object> getReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiReturn.getReturnUseLogInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		return serviceUtil.service("apiReturn.insertReturnUseLogInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		return serviceUtil.service("apiReturn.updateReturnUseLogInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getReturnRuleInfoList(Map<String, Object> paramMap) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String api_url = paramMap.get("db_url").toString() + "GetConfigInfoList";
		
		try {
			URL url = new URL(api_url); 
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			
			wr.flush();
			wr.close();
			
			int responseCode = conn.getResponseCode();
			
			BufferedReader br;
			
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			
			result = (Map<String, Object>) new ObjectMapper().readValue(response.toString(), HashMap.class).get("d");
			
			result.remove("__type");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public Map<String, Object> getReturnRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiReturn.getReturnRuleInfoList"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> getReturnRuleInfo(HttpServletRequest request, Map<String ,Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiReturn.getReturnRuleInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		return serviceUtil.service("apiReturn.insertReturnRuleInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateReturnRuleInfo(Map<String, Object> paramMap) throws Exception {
		String api_url = ParamUtil.parseString(paramMap.get("db_url")) + "UpdateConfigInfo";
		Map<String, Object> result = new HashMap<String, Object>();
		
		JSONObject params = new JSONObject();
		params.put("fileName", ParamUtil.parseString(paramMap.get("file_name")));
		params.put("target", ParamUtil.parseString(paramMap.get("rule_name")));
		params.put("value", ParamUtil.parseString(paramMap.get("rule_value")));
		
		try {
			URL url = new URL(api_url); 
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setDoOutput(true);
			
			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(params.toString());
			wr.flush();
			wr.close();
			
			int responseCode = conn.getResponseCode();
			
			BufferedReader br;
			
			if(responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			
			result = (Map<String, Object>) new ObjectMapper().readValue(response.toString(), HashMap.class).get("d");
			if(result != null) {
				result.remove("__type");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Map<String, Object> updateReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		return serviceUtil.service("apiReturn.updateReturnRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateReturnRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		return serviceUtil.service("apiReturn.updateReturnRuleSendResult", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getReturnLastLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiReturn.getReturnLastLogInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
}