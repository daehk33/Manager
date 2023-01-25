package com.enicom.nims.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.utils.HttpUtil;
import com.enicom.nims.utils.JsonUtil;

@RestController
public class LoginController {
	private RSAUtil rsa;
	
	@Value("#{config['api.url']}")
	private String api_url;
	
	@Autowired
	public LoginController(RSAUtil rsa) {
		this.rsa = rsa;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login/loginCheck")
	public Map<String, Object> loginCheck(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			request = (HttpServletRequest) rsa.decrypt(request);
		} catch (Exception e) {
			return JsonUtil.makeResultJSON("422");
		}
		
		paramMap.put("manager_id", request.getParameter("manager_id"));
		paramMap.put("password", request.getParameter("password"));
		
		resultMap = HttpUtil.send(api_url, "/manager/getManagerInfo", paramMap);

		if (null != resultMap && !resultMap.isEmpty()) {
			if (null != resultMap.get("result") && !"".equals(resultMap.get("result"))) {
				Map<String, Object> result = (Map<String, Object>) resultMap.get("result");

				if (result.get("CODE") != null && result.get("CODE").equals("220")) {

					Map<String, Object> manager = (Map<String, Object>) result.get("DATA");
					SessionManager.setSession(request, manager);

					if (null != manager.get("theme") && !"".equals(manager.get("theme"))) {
						String theme = manager.get("theme").toString();
						SessionManager.setTheme(request, theme);
					}

					return resultMap;
				}
				return resultMap;
			}
		}
		return JsonUtil.makeResultJSON("400");
	}
}
