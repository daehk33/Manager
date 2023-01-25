package com.enicom.nims.stats;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.stats.service.StatsService;

@RestController
public class StatsController {
	@Autowired
	StatsService statsService;

	/**
	 * [조회] 통계 - 스마트도서관 기간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsList", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsList(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 스마트도서관 요일별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsDate", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsDate(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsDate(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 스마트도서관 시간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsTime", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsTime(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsTime(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 스마트도서관 장비별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsDevice", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsDevice(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsDevice(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 스마트도서관 이용 달력 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsCalendar", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsCalendar(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsCalendar(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 스마트도서관 예약대출 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsSLSReservationLoanList", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsSLSReservationLoanList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsSLSReservationLoanList(request, paramMap);
	}
	
	/***
	 * [조회] 통계 - 스마트도서관 요일별 무인 반납 통계 데이터 조회
	 * 
	 * @params paramMap
	 * @return
	 * @tthrows Exception
	 */
	@RequestMapping(value = "/stats/getStatsUnmannedReturnDate", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsUnmannedReturnDate(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsUnmannedReturnDate(request, paramMap);
	}
	
	/***
	 * [조회] 통계 - 스마트도서관 시간별 무인 반납 통계 데이터 조회
	 * 
	 * @params paramMap
	 * @return
	 * @tthrows Exception
	 */
	@RequestMapping(value = "/stats/getStatsUnmannedReturnTime", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsUnmannedReturnTime(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsUnmannedReturnTime(request, paramMap);
	}
	
	/***
	 * [조회] 통계 - 스마트도서관 장비별 무인 반납 통계 데이터 조회
	 * 
	 * @params paramMap
	 * @return
	 * @tthrows Exception
	 */
	@RequestMapping(value = "/stats/getStatsUnmannedReturnDevice", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsUnmannedReturnDevice(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsUnmannedReturnDevice(request, paramMap);
	}
	
	/***
	 * [조회] 통계 - 스마트도서관 무인 반납 이용 달력 데이터 조회
	 * 
	 * @params paramMap
	 * @return
	 * @tthrows Exception
	 */
	@RequestMapping(value = "/stats/getStatsUnmannedReturnCalendar", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsUnmannedReturnCalendar(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsUnmannedReturnCalendar(request, paramMap);
	}
	
	/***
	 * [조회] 통계 - 스마트도서관 기간별 무인 반납 통계 데이터 조회
	 * 
	 * @params paramMap
	 * @return
	 * @tthrows Exception
	 */
	@RequestMapping(value = "/stats/getStatsUnmannedReturnList", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsUnmannedReturnList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsUnmannedReturnList(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 대출반납기 기간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsLoanReturnList", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsLoanReturnList(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsLoanReturnList(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 대출반납기 요일별 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsLoanReturnDate", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsLoanReturnDate(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsLoanReturnDate(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 대출반납기 시간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsLoanReturnTime", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsLoanReturnTime(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsLoanReturnTime(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 대출반납기 장비별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsLoanReturnDevice", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsLoanReturnDevice(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsLoanReturnDevice(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 대출반납기 이용 달력 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsLoanReturnCalendar", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsLoanReturnCalendar(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsLoanReturnCalendar(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 반납기 기간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsReturnList", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsReturnList(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsReturnList(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 반납기 요일별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsReturnDate", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsReturnDate(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsReturnDate(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 반납기 시간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsReturnTime", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsReturnTime(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsReturnTime(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 반납기 시간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsReturnDevice", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsReturnDevice(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsReturnDevice(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 반납기 이용 달력 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsReturnCalendar", method = { RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getStatsReturnCalendar(HttpServletRequest request,  @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsReturnCalendar(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 예약대출기 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsResvLoanList", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsResvLoanList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsResvLoanList(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 예약대출기 시간별 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsResvLoanTime", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsResvLoanTime(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsResvLoanTime(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 예약대출기 요일별 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsResvLoanDate", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsResvLoanDate(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsResvLoanDate(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 예약대출기 장비별 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsResvLoanDevice", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsResvLoanDevice(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsResvLoanDevice(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 예약대출기 이용 달력 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsResvLoanCalendar", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsResvLoanCalendar(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsResvLoanCalendar(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 출입게이트 기간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsGateList", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsGateList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsGateList(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 출입게이트 시간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsGateTime", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsGateTime(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsGateTime(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 출입게이트 요일별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsGateDate", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsGateDate(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsGateDate(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 출입게이트 장비별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsGateDevice", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsGateDevice(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsGateDevice(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 출입게이트 이용 달력 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsGateCalendar", method = { RequestMethod.POST , RequestMethod.GET})
	public Map<String, Object> getStatsGateCalendar(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsGateCalendar(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 분실방지기 기간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsAntiLostList", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getStatsAntiLostList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsAntiLostList(request, paramMap);
	}

	/**
	 * [조회] 통계 - 분실방지기 시간별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsAntiLostTime", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getStatsAntiLostTime(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsAntiLostTime(request, paramMap);
	}

	/**
	 * [조회] 통계 - 분실방지기 날짜별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsAntiLostDay", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getStatsAntiLostDay(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsAntiLostDay(request, paramMap);
	}
	
	/**
	 * [조회] 통계 - 분실방지기 요일별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsAntiLostDate", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getStatsAntiLostDate(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsAntiLostDate(request, paramMap);
	}

	/**
	 * [조회] 통계 - 분실방지기 장비별 통계 데이터 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsAntiLostDevice", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getStatsAntiLostDevice(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsAntiLostDevice(request, paramMap);
	}

	/**
	 * [조회] 통계 - 분실방지기 이용 달력 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/stats/getStatsAntiLostCalendar", method = { RequestMethod.POST, RequestMethod.GET })
	public Map<String, Object> getStatsAntiLostCalendar(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return statsService.getStatsAntiLostCalendar(request, paramMap);
	}
}
