package com.enicom.nims.code;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.code.service.CodeService;

@RestController
public class CodeController {
	private CodeService codeService;

	@Autowired
	public CodeController(CodeService codeService) {
		this.codeService = codeService;
	}

	/**
	 * [조회] 코드 그룹 아이디 중복 여부
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/code/checkCodeGrpDuplicated", method = { RequestMethod.POST })
	public Map<String, Object> checkCodeGrpDuplicated(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return codeService.checkCodeGrpDuplicated(request, paramMap);
	}

	/**
	 * [조회] 코드 그룹 카운트 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/code/getCodeGroupCount", method = { RequestMethod.POST })
	public Map<String, Object> getModelCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return codeService.getCodeGroupCount(request, paramMap);
	}

	/**
	 * [조회] 코드 그룹 리스트 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/code/getCodeGroupList", method = { RequestMethod.POST })
	public Map<String, Object> getCodeGroupList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return codeService.getCodeGroupList(request, paramMap);
	}

	/**
	 * [등록] 코드 정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @그룹 ID 중복 방지 필요
	 */
	@RequestMapping(value = "/code/insertCodeGroupInfo", method = { RequestMethod.POST })
	public Map<String, Object> insertCodeGroupInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return codeService.insertCodeGroupInfo(request, paramMap);
	}

	/**
	 * [삭제] 코드 그룹 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @코드 그룹에 포함된 코드 정보가 있으면 삭제 불가
	 */
	@RequestMapping(value = "/code/deleteCodeGroupInfo", method = { RequestMethod.POST })
	public Map<String, Object> deleteCodeGroupInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return codeService.deleteCodeGroupInfo(request, paramMap);
	}

	/**
	 * [수정] 코드 그룹 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @그룹 ID 중복 방지 필요
	 */
	@RequestMapping(value = "/code/updateCodeGroupInfo", method = { RequestMethod.POST })
	public Map<String, Object> updateCodeGroupInfo(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return codeService.updateCodeGroupInfo(request, paramMap);
	}

	/**
	 * [조회] 코드 아이디 중복 여부
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/code/checkCodeDuplicated", method = { RequestMethod.POST })
	public Map<String, Object> checkCodeDuplicated(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return codeService.checkCodeDuplicated(request, paramMap);
	}

	/**
	 * [조회] 코드 카운트 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/code/getCodeCount", method = { RequestMethod.POST })
	public Map<String, Object> getCodeCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return codeService.getCodeGroupCount(request, paramMap);
	}

	/**
	 * [조회] 코드 리스트 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/code/getCodeList", method = { RequestMethod.POST })
	public Map<String, Object> getCodeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return codeService.getCodeList(request, paramMap);
	}

	/**
	 * [등록] 코드정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @코드값 중복 방지 로직 필요
	 */
	@RequestMapping(value = "/code/insertCodeInfo", method = { RequestMethod.POST })
	public Map<String, Object> insertCodeInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return codeService.insertCodeInfo(request, paramMap);
	}

	/**
	 * [삭제] 코드정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/code/deleteCodeInfo", method = { RequestMethod.POST })
	public Map<String, Object> deleteCodeInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return codeService.deleteCodeInfo(request, paramMap);
	}

	/**
	 * [수정] 코드정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @코드값 중복 방지 로직 필요
	 */
	@RequestMapping(value = "/code/updateCodeInfo", method = { RequestMethod.POST })
	public Map<String, Object> updateCodeInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return codeService.updateCodeInfo(request, paramMap);
	}
}
