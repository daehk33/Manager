package com.enicom.nims.dashboard;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.dashboard.service.DashboardService;
import com.enicom.nims.utils.JsonUtil;

@RestController
public class DashboardController {
	private DashboardService dashboardService;

	@Autowired
	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	/**
	 * [조회] 실시간 모니터링 이력 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getLiveHistoryList")
	public Map<String, Object> getLiveHistory(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.getLiveHistoryList(request, paramMap);
	}

	/**
	 * [조회] 장비 이벤트 현황 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getEventStatusList")
	public Map<String, Object> getEventStatusList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return dashboardService.getEventStatusList(request, paramMap);
	}

	/**
	 * [조회] 장비 이벤트 분석 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getEventAnalysisList")
	public Map<String, Object> getEventAnalysisList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return dashboardService.getEventAnalysisList(request, paramMap);
	}

	/**
	 * [조회] 장애 알림 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getAlarmList")
	public Map<String, Object> getAlarmList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.getAlarmList(request, paramMap);
	}

	/**
	 * [삭제] 장애 알림 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/deleteAlarmInfo")
	public Map<String, Object> deleteAlarmInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.deleteAlarmInfo(request, paramMap);
	}

	/**
	 * [조회] 스마트도서관 도서 대출 현황 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getBookLoanStatus")
	public Map<String, Object> getBookLoanStatus(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.getBookLoanStatus(request, paramMap);
	}

	/**
	 * [조회] 스마트도서관 금일/주간 현황 Chart 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getUseLogCntby{type}")
	public Map<String, Object> getUseLogCnt(HttpServletRequest request, @RequestParam Map<String, Object> paramMap,
			@PathVariable String type) throws Exception {
		type = type.toLowerCase();
		if ("book".equals(type)) {
			return dashboardService.getUseLogCntbyBook(request, paramMap);
		}
		else if ("user".equals(type)) {
			return dashboardService.getUseLogCntbyUser(request, paramMap);
		}
		else if ("week".equals(type)) {
			return dashboardService.getUseLogCntbyWeek(request, paramMap);
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}
	}

	/**
	 * [조회] 대출반납기 현황 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getLoanReturnStatus")
	public Map<String, Object> getLoanReturnStatus(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return dashboardService.getLoanReturnStatus(request, paramMap);
	}

	/**
	 * [조회] 대출반납기 금일/주간/시간별 현황 Chart 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getLoanReturnUseLogCntby{type}")
	public Map<String, Object> getLoanReturnLogCnt(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap, @PathVariable String type) throws Exception {
		type = type.toLowerCase();
		if ("book".equals(type)) {
			return dashboardService.getLoanReturnUseLogCntbyBook(request, paramMap);
		}
		else if ("user".equals(type)) {
			return dashboardService.getLoanReturnUseLogCntbyUser(request, paramMap);
		}
		else if ("week".equals(type)) {
			return dashboardService.getLoanReturnUseLogCntbyWeek(request, paramMap);
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}
	}

	/**
	 * [조회] 반납기 현황 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getReturnStatus")
	public Map<String, Object> getReturnStatus(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.getReturnStatus(request, paramMap);
	}

	/**
	 * [조회] 반납기 금일/주간/시간별 현황 Chart 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getReturnUseLogCntby{type}")
	public Map<String, Object> getReturnLogCnt(HttpServletRequest request, @RequestParam Map<String, Object> paramMap,
			@PathVariable String type) throws Exception {
		type = type.toLowerCase();
		if ("today".equals(type)) {
			return dashboardService.getReturnUseLogCntbyToday(request, paramMap);
		}
		else if ("week".equals(type)) {
			return dashboardService.getReturnUseLogCntbyWeek(request, paramMap);
		}
		else if ("time".equals(type)) {
			return dashboardService.getReturnUseLogCntbyTime(request, paramMap);
		} else {
			return JsonUtil.makeResultJSON("410");
		}
	}

	/**
	 * [조회] 예약대출기 대출 현황 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getResvLoanStatus")
	public Map<String, Object> getResvLoanStatus(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.getResvLoanStatus(request, paramMap);
	}

	/**
	 * [조회] 예약대출기 금일/주간 현황 Chart 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getResvLoanUseLogCntby{type}")
	public Map<String, Object> getResvLoanLogCnt(HttpServletRequest request, @RequestParam Map<String, Object> paramMap,
			@PathVariable String type) throws Exception {
		type = type.toLowerCase();
		if ("today".equals(type)) {
			return dashboardService.getResvLoanUseLogCntbyToday(request, paramMap);
		}
		else if ("week".equals(type)) {
			return dashboardService.getResvLoanUseLogCntbyWeek(request, paramMap);
		} else {
			return JsonUtil.makeResultJSON("410");
		}
	}

	/**
	 * [조회] 출입게이트 출입 현황 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getGateStatus")
	public Map<String, Object> getGateStatus(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.getGateStatus(request, paramMap);
	}

	/**
	 * [조회] 출입게이트 금일/주간/시간별 현황 Chart 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getGateUseLogCntby{type}")
	public Map<String, Object> getGateLogCnt(HttpServletRequest request, @RequestParam Map<String, Object> paramMap,
			@PathVariable String type) throws Exception {
		type = type.toLowerCase();
		if ("today".equals(type)) {
			return dashboardService.getGateUseLogCntbyToday(request, paramMap);
		}
		else if ("week".equals(type)) {
			return dashboardService.getGateUseLogCntbyWeek(request, paramMap);
		}
		else if ("time".equals(type)) {
			return dashboardService.getGateUseLogCntbyTime(request, paramMap);
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}
	}
	
	/**
	 * [조회] 분실방지기 현황 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getAntiLostStatus")
	public Map<String, Object> getAntiLostStatus(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return dashboardService.getAntiLostStatus(request, paramMap);
	}
	
	/**
	 * [조회] 분실방지기 금일/주간/시간별 현황 Chart 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/getAntiLostUseLogCntby{type}")
	public Map<String, Object> getAntiLostLogCnt(HttpServletRequest request, @RequestParam Map<String, Object> paramMap,
			@PathVariable String type) throws Exception {
		type = type.toLowerCase();
		if ("today".equals(type)) {
			return dashboardService.getAntiLostUseLogCntbyToday(request, paramMap);
		}
		else if ("week".equals(type)) {
			return dashboardService.getAntiLostUseLogCntbyWeek(request, paramMap);
		}
		else if ("time".equals(type)) {
			return dashboardService.getAntiLostUseLogCntbyTime(request, paramMap);
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}
	}
}
