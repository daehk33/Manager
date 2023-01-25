package com.enicom.nims.history;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.history.service.HistoryService;

@RestController
public class HistoryController {
	private HistoryService historyService;

	@Autowired
	public HistoryController(HistoryService historyService) {
		this.historyService = historyService;
	}

	@RequestMapping("/history/getLoanHistoryList")
	public Map<String, Object> getLoanHistoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return historyService.getLoanHistoryList(request, paramMap);
	}

	@RequestMapping("/history/getUnmannedReturnHistoryList")
	public Map<String, Object> getUnmannedReturnHistoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return historyService.getUnmannedReturnHistoryList(request, paramMap);
	}

	@RequestMapping("/history/getLoanReturnHistoryList")
	public Map<String, Object> getLoanReturnHistoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return historyService.getLoanReturnHistoryList(request, paramMap);
	}

	@RequestMapping("/history/getReturnHistoryList")
	public Map<String, Object> getReturnHistoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return historyService.getReturnHistoryList(request, paramMap);
	}

	@RequestMapping("/history/getResvLoanHistoryList")
	public Map<String, Object> getResvLoanHistoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return historyService.getResvLoanHistoryList(request, paramMap);
	}

	@RequestMapping("/history/getGateHistoryList")
	public Map<String, Object> getGateHistoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return historyService.getGateHistoryList(request, paramMap);
	}

	@RequestMapping("/history/getAntilostHistoryList")
	public Map<String, Object> getAntilostHistoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return historyService.getAntilostHistoryList(request, paramMap);
	}
}
