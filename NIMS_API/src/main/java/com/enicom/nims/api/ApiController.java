package com.enicom.nims.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.api.service.ApiService;

@RestController
public class ApiController {
	private ApiService apiService;

	@Autowired
	public ApiController(ApiService apiService) {
		this.apiService = apiService;
	}

	/**
	 * [등록] 장비이벤트 정보 등록 Service API
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/service", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> service(HttpServletRequest request, @RequestBody Map<String, Object> paramMap)
			throws Exception {
		return apiService.service(request, paramMap);
	}

	/**
	 * [API] Wake On Lan API
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/wol", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> wakeOnLan(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiService.wakeOnLan(request, paramMap);
	}

	/**
	 * [API] SFTP 파일 제어 API
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/sftpService", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sftpService(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiService.sftpService(request, paramMap);
	}
	
	/**
	 * [API] Serial 포트 목록 호출 API
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/api/serialService", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> serialService()
			throws Exception {
		return apiService.serialService();
	}
}
