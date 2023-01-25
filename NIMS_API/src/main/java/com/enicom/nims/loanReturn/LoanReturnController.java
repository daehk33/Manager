package com.enicom.nims.loanReturn;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.api.service.ApiLoanReturnService;
import com.enicom.nims.loanReturn.service.LoanReturnService;

@RestController
public class LoanReturnController {
	private LoanReturnService loanReturnService;
	private ApiLoanReturnService apiLoanReturnService;

	@Autowired
	public LoanReturnController(LoanReturnService loanReturnService, ApiLoanReturnService apiLoanReturnService) {
		this.loanReturnService = loanReturnService;
		this.apiLoanReturnService = apiLoanReturnService;
	}

	/**
	 * [조회] 인기도서/우수회원 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturn{gubun}RankList")
	public Map<String, Object> getLoanReturnRankList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap, @PathVariable String gubun) throws Exception {
		paramMap.put("gubun", gubun);
		return loanReturnService.getLoanReturnRankList(request, paramMap);
	}

	/**
	 * [조회] 운영 정책 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturnRuleInfoList")
	public Map<String, Object> getLoanReturnRuleInfoList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.getLoanReturnRuleInfoList(request, paramMap);
	}

	/**
	 * [수정] 운영 정책 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/updateLoanReturnRuleInfo")
	public Map<String, Object> updateLoanReturnRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.updateLoanReturnRuleInfo(request, paramMap);
	}

	/**
	 * [조회] DID 설정 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturnDIDRuleInfoList")
	public Map<String, Object> getLoanReturnDIDRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.getLoanReturnDIDRuleInfo(request, paramMap);
	}

	/**
	 * [수정] DID 설정 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/updateLoanReturnDIDRuleInfo")
	public Map<String, Object> updateLoanReturnDIDRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.updateLoanReturnDIDRuleInfo(request, paramMap);
	}

	/**
	 * [조회] 배너 이미지 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturnBannerImageList")
	public Map<String, Object> getLoanReturnBannerImageList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.getLoanReturnBannerImageList(request, paramMap);
	}

	/**
	 * [조회] 배너 이용안내 설정 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturnBannerSettingInfo")
	public Map<String, Object> getLoanReturnBannerSettingInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.getLoanReturnBannerSettingInfo(request, paramMap);
	}

	/**
	 * [수정] 배너 이용안내 설정 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/updateLoanReturnBannerSettingInfo")
	public Map<String, Object> updateLoanReturnBannerSettingInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.updateLoanReturnBannerSettingInfo(request, paramMap);
	}

	/**
	 * [수정] 배너 변경 상태 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/updateLoanReturnBannerChanged")
	public Map<String, Object> updateLoanReturnBannerChagned(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.updateLoanReturnBannerChanged(paramMap);
	}

	/**
	 * [수정] 배너 설정 및 이미지 불러오기
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/loadLoanReturnBannerSettings")
	public Map<String, Object> loadLoanReturnBannerSettings(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.loadLoanReturnBannerSettings(request, paramMap);
	}

	/**
	 * [조회] 휴관일 설정 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturnHolidayRule")
	public Map<String, Object> getLoanReturnHolidayRule(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.getLoanReturnHolidayRule(request, paramMap);
	}

	/**
	 * [수정] 휴관일 설정 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/updateLoanReturnHolidayRule")
	public Map<String, Object> updateLoanReturnHolidayRule(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.updateLoanReturnHolidayRule(request, paramMap);
	}

	/**
	 * [조회] 휴관일 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturnHolidayList")
	public Map<String, Object> getLoanReturnHolidayList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.getLoanReturnHolidayList(request, paramMap);
	}

	/**
	 * [등록/수정] 휴관일 정보 등록/수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/upsertLoanReturnHolidayInfo")
	public Map<String, Object> upsertLoanReturnHolidayInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.upsertLoanReturnHolidayInfo(request, paramMap);
	}

	/**
	 * [수정/삭제] 휴관일 정보 수정/삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/deleteLoanReturnHolidayInfo")
	public Map<String, Object> deleteLoanReturnHolidayInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.deleteLoanReturnHolidayInfo(request, paramMap);
	}

	/**
	 * [조회] 장비 설정 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/getLoanReturnEquipRuleInfoList")
	public Map<String, Object> getLoanReturnEquipRuleInfoList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.getLoanReturnEquipRuleInfoList(request, paramMap);
	}

	/**
	 * [수정] 장비 설정 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loanReturn/updateLoanReturnEquipRuleInfo")
	public Map<String, Object> updateLoanReturnEquipRuleInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return apiLoanReturnService.updateLoanReturnEquipRuleInfo(request, paramMap);
	}
}
