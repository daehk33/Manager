package com.enicom.nims.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	/**
	 * API 요청
	 *
	 * @param path
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> send(String url, String path, Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		String data = "";
		StringBuilder sb = new StringBuilder();

		try {
			// API URL 설정
			HttpPost httpPost = new HttpPost(url + path);

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("caller", "manager"));

			for (String key : paramMap.keySet()) {
				String value = ParamUtil.parseString(paramMap.get(key));

				/**
				 * & 특수문자와 같은 경우 escapeHtml4 구문을 반복해서 적용하게되면 &amp;amp; 등과 같이 반복적용되는 문제가 존재하여
				 * unescapeHtml4를 이용 원래 문자열로 되돌린 이후 재설정
				 */
				params.add(new BasicNameValuePair(key, StringEscapeUtils.escapeHtml4(value)));
			}

			sb.append(String.format("* Connect With NIMS API\n> Request [url: %s, params: %s]\n", path, params));

			httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			CloseableHttpClient httpClient = HttpClients.createDefault(); // HTTP Client 생성
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost); // 데이터 전송

			try {
				data = EntityUtils.toString(httpResponse.getEntity());
				HttpEntity entity = httpResponse.getEntity();
				EntityUtils.consume(entity);

				resultMap = new ObjectMapper().readValue(data, HashMap.class);

				long length = resultMap.toString().length();
				if (length > 3000) {
					sb.append(String.format("> Status: Success, Response: [length: %s]\n", length));
				} else {
					sb.append(
							String.format("> Status: Success, Response: [length: %s, data: %s]\n", length, resultMap));
				}

				logger.info(sb.toString());

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				httpResponse.close();
			}
		} catch (Exception e) {
			sb.append(String.format("> Status: Error, Error: Cannot access to NIMS API; %s > %s\n", e.getClass(),
					e.getMessage()));
			logger.error(sb.toString());
			return JsonUtil.makeResultJSON("0000");
		}

		return resultMap;
	}

	public static Map<String, Object> getBasicInfoForApi(Map<String, Object> paramMap) {
		Map<String, Object> apiParams = new HashMap<String, Object>();

		apiParams.put("ip", paramMap.get("ip"));
		apiParams.put("menu_path", paramMap.get("menu_path"));
		apiParams.put("manger_id", paramMap.get("manager_id"));
		apiParams.put("manager_key", paramMap.get("manager_key"));

		return apiParams;
	}
}
