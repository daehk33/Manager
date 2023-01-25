package com.enicom.nims.Return;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.Return.service.ReturnService;
import com.enicom.nims.api.service.ApiReturnService;

@RestController
public class ReturnController {
	private ReturnService ReturnService;
	private ApiReturnService apiReturnService;
	
	@Autowired
	public ReturnController(ReturnService ReturnService, ApiReturnService apiReturnService) {
		this.ReturnService = ReturnService;
		this.apiReturnService = apiReturnService;
	}
	
	/**
	 * [조회] 운영 정책 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/return/getReturnRuleInfoList")
	public Map<String, Object> getLoanReturnRuleInfoList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return apiReturnService.getReturnRuleInfoList(request, paramMap);
	}
	
	/**
	 * [수정] 운영 정책 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/return/updateReturnRuleInfo")
	public Map<String, Object> updateLoanReturnRuleInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		return apiReturnService.updateReturnRuleInfo(request, paramMap);
	}

}
