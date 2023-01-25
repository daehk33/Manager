package com.enicom.nims.resvLoan;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.api.service.ApiResvLoanService;
import com.enicom.nims.resvLoan.service.ResvLoanService;

@RestController
public class ResvLoanController {
	private ResvLoanService resvLoanService;
	private ApiResvLoanService apiResvLoanService;
	
	@Autowired
	public ResvLoanController(ResvLoanService resvLoanService, ApiResvLoanService apiResvLoanService) {
		this.resvLoanService = resvLoanService;
		this.apiResvLoanService = apiResvLoanService;
	}
	
	/**
	 * [조회] 인기도서/우수회원 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getResvLoan{gubun}RankList", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getResvLoanRankList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap, @PathVariable String gubun) throws Exception {
		paramMap.put("gubun", gubun);
		return resvLoanService.getResvLoanRankList(request, paramMap);
	}

	/**
	 * [조회] 비치 도서 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getResvLoanBookList")
	public Map<String, Object> getResvLoanBookList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return resvLoanService.getResvLoanBookList(request, paramMap);
	}

	/**
	 * [조회] 예약 도서 조회
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getReservedBookList")
	public Map<String, Object> getReservedBookList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return resvLoanService.getReservedBookList(request, paramMap);
	}

	/**
	 * [조회] 캐비닛 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getResvLoanCabinetList")
	public Map<String, Object> getResvLoanCabinetList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return resvLoanService.getResvLoanCabinetList(request, paramMap);
	}

	/**
	 * [조회] 모듈 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getResvLoanModuleInfoList")
	public Map<String, Object> getResvLoanModuleInfoList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiResvLoanService.getResvLoanModuleInfoList(request, paramMap);
	}

	/**
	 * [조회] 모듈 중복 여부 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/checkModuleDuplicated")
	public Map<String, Object> checkModuleDuplicated(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return resvLoanService.checkModuleDuplicated(request, paramMap);
	}

	/**
	 * [조회] 모듈 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/insertResvLoanModule")
	public Map<String, Object> insertResvLoanModule(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiResvLoanService.insertResvLoanModule(request, paramMap);
	}
	
	/**
	 * [조회] 모듈 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/deleteResvLoanModule")
	public Map<String, Object> deleteResvLoanModule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiResvLoanService.deleteResvLoanModule(request, paramMap);
	}
	
	/**
	 * [조회] 캐비닛 종합 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getResvLoanCabinetTotalInfo")
	public Map<String, Object> getResvLoanCabinetTotalInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return resvLoanService.getResvLoanCabinetTotalInfo(request, paramMap);
	}
	
	/**
	 * [수정] 캐비닛  수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/updateResvLoanCabinetType")
	public Map<String, Object> updateResvLoanCabinetType(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiResvLoanService.updateResvLoanCabinetType(request, paramMap); 
	}
	
	/**
	 * [조회] 캐비닛 타입 설정 불가 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getResvLoanUnmodifiableCabinetList")
	public Map<String, Object> getResvLoanUnmodifiableCabinetList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiResvLoanService.getResvLoanUnmodifiableCabinetList(request, paramMap); 
	}
	
	/**
	 * [조회] 캐비닛 운영정책  목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/getResvLoanRuleInfoList")
	public Map<String, Object> getResvLoanRuleInfoList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiResvLoanService.getResvLoanRuleInfoList(request, paramMap);
	}
	
	/**
	 * [수정] 캐비닛 운영정책  수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/resvLoan/updateResvLoanRuleInfo")
	public Map<String, Object> updateResvLoanRuleInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return apiResvLoanService.updateResvLoanRuleInfo(request, paramMap);
	}
}
