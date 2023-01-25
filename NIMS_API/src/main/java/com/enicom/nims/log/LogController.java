package com.enicom.nims.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.log.service.LogService;

@RestController
public class LogController {
	private LogService logService;

	@Autowired
	public LogController(LogService logService) {
		this.logService = logService;
	}

	@RequestMapping("/log/getLogList")
	public Map<String, Object> getLogList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return logService.getLogList(request, paramMap);
	}

	@RequestMapping("/log/insertLogInfo")
	public Map<String, Object> insertLogInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return logService.insertLogInfo(request, paramMap);
	}

	@RequestMapping("/log/deleteLogInfo")
	public Map<String, Object> deleteLogInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return logService.deleteLogInfo(request, paramMap);
	}
}
