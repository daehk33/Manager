package com.enicom.nims.model;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.model.service.ModelService;

@RestController
public class ModelController {
	private ModelService modelService;

	@Autowired
	public ModelController(ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * [조회] 장비 모델 아이디 중복 여부
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/model/checkModelDuplicated", method = { RequestMethod.POST })
	public Map<String, Object> checkModelDuplicated(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return modelService.checkModelDuplicated(request, paramMap);
	}

	/**
	 * [조회] 장비 모델 수량 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/model/getModelCount", method = { RequestMethod.POST })
	public Map<String, Object> getModelCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return modelService.getModelCount(request, paramMap);
	}

	/**
	 * [조회] 장비 모델 타입 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/model/getModelTypeList", method = { RequestMethod.POST })
	public Map<String, Object> getModelTypeList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return modelService.getModelTypeList(request, paramMap);
	}

	/**
	 * [조회] 장비 모델 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/model/getModelList", method = { RequestMethod.POST })
	public Map<String, Object> getModelList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return modelService.getModelList(request, paramMap);
	}

	/**
	 * [등록] 장비 모델 정보 추가
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @모델 ID 중복 여부 확인
	 */
	@RequestMapping(value = "/model/insertModelInfo", method = { RequestMethod.POST })
	public Map<String, Object> insertModelInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return modelService.insertModelInfo(request, paramMap);
	}

	/**
	 * [삭제] 장비 모델 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @해당 모델 정보로 등록된 장비가 있는경우 삭제 불가
	 */
	@RequestMapping(value = "/model/deleteModelInfo", method = { RequestMethod.POST })
	public Map<String, Object> deleteModelInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return modelService.deleteModelInfo(request, paramMap);
	}

	/**
	 * [수정] 장비 모델 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @모델 ID 중복 여부 확인
	 */
	@RequestMapping(value = "/model/updateModelInfo", method = { RequestMethod.POST })
	public Map<String, Object> updateModelInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return modelService.updateModelInfo(request, paramMap);
	}

}
