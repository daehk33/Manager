package com.enicom.nims.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtils {
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);
	
	/**
	 * API 요청
	 *
	 * @param path
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private static Map<String, Object> getHttpServletRequest(String url, String path, HttpServletRequest request, String fail_message) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String data = "";
		
		logger.info("-----------------------------------------------------------");
		logger.info("* Connect with API -> url: {}", path);
		
		try {
			HttpGet httpGet = new HttpGet(url + path);
			
			Enumeration<?> keys = request.getParameterNames();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				if("".equals(ParamUtil.parseString(request.getParameter(key)))) continue;
				params.add(new BasicNameValuePair(key, StringEscapeUtils.escapeHtml4(StringEscapeUtils.unescapeHtml4(request.getParameter(key)))));
			}
			
			logger.info(">>> Params for API : {}", params);
			
			URI uri = new URIBuilder(httpGet.getURI())
					.addParameters(params).build();
			((HttpRequestBase) httpGet).setURI(uri);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
			
			try {
				data = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
				HttpEntity entity = httpResponse.getEntity();
				EntityUtils.consume(entity);
				
				String receiveMsg = "";
				if (data.length() > 8000) receiveMsg = ">>> Data from API Length : " + String.valueOf(data.length());
				else receiveMsg = ">>> Data from API : " + data;
				
				logger.info(receiveMsg);
				
				resultMap = new ObjectMapper().readValue(data, HashMap.class);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				httpResponse.close();
			}
		}
		catch (Exception e) {
			logger.error(">>> HTTP Client Error occured !!! Error: : {}", e.getMessage());
			if(fail_message != null) {
				return JsonUtil.makeResultJSON("0000", fail_message); 
			}
			return JsonUtil.makeResultJSON("0000");
		}
		
		return resultMap;
	}
	
	/**
	 * Path 별 API 요청
	 * 
	 * @param path
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> createRequest(String url, String path, HttpServletRequest request, String fail_message){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			// send request
			resultMap = getHttpServletRequest(url, path, request, fail_message);

			if (!resultMap.isEmpty() && resultMap.get("result") != null && !resultMap.get("result").equals("")) {
				return resultMap;
			}
		} 
		catch (Exception e) {
			String msg = "API 요청 실패";
			logger.error(msg);

			// Bad Request
			return JsonUtil.makeResultJSON("421", msg);
		}
		return resultMap;
	}
}
