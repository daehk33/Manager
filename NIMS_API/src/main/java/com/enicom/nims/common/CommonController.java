package com.enicom.nims.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.common.service.CommonService;

@RestController
public class CommonController {
	private CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}

	/**
	 * [조회] 메뉴 카테고리 목록 조회
	 * 
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getCategoryList", method = { RequestMethod.POST })
	public Map<String, Object> getCategoryList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return commonService.getCategoryList(request, paramMap);
	}

	/**
	 * [조회] 메뉴 하위 카테고리 목록 조회
	 * 
	 * @param request
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getSubCategoryList", method = { RequestMethod.POST })
	public Map<String, Object> getSubCategoryList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return commonService.getSubCategoryList(request, paramMap);
	}

	/**
	 * [조회] 메뉴 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/getMenuList", method = { RequestMethod.POST })
	public Map<String, Object> getMenuList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return commonService.getMenuList(request, paramMap);
	}
}
