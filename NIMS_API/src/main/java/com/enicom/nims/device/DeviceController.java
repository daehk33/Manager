package com.enicom.nims.device;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.schedule.ScheduleService;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@RestController
public class DeviceController {
	private DeviceService deviceService;
	private ScheduleService scheduleService;
	
	@Autowired
	public DeviceController(DeviceService deviceService, ScheduleService scheduleService) {
		this.deviceService = deviceService;
		this.scheduleService = scheduleService;
	}

	/**
	 * [조회] 장비 수량 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceCount")
	public Map<String, Object> getDeviceCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.getDeviceCount(request, paramMap);
	}

	/**
	 * [조회] 장비 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceList")
	public Map<String, Object> getDeviceList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.getDeviceList(request, paramMap);
	}

	/**
	 * [조회] 장비 정보 수량 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceInfoCount")
	public Map<String, Object> getDeviceInfoCount(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return deviceService.getDeviceInfoCount(request, paramMap);
	}

	/**
	 * [조회] 장비 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceInfoList")
	public Map<String, Object> getDeviceInfoList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.getDeviceInfoList(request, paramMap);
	}

	/**
	 * [등록] 장비 정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception @ 장비 ID 중복 여부 확인
	 */
	@RequestMapping(value = "/device/insertDeviceInfo")
	public Map<String, Object> insertDeviceInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.insertDeviceInfo(request, paramMap);
	}

	/**
	 * [삭제] 장비 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/deleteDeviceInfo")
	public Map<String, Object> deleteDeviceInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.deleteDeviceInfo(request, paramMap);
	}

	/**
	 * [수정] 장비 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @장비 ID 중복 확인
	 */
	@RequestMapping(value = "/device/updateDeviceInfo")
	public Map<String, Object> updateDeviceInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.updateDeviceInfo(request, paramMap);
	}

	/**
	 * [조회] 장비 상태 목록 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceStatusList")
	public Map<String, Object> getDeviceStatusList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return deviceService.getDeviceStatusList(request, paramMap);
	}

	/**
	 * [조회] 장비 상태 목록 조회(합계)
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceStatusTotal")
	public Map<String, Object> getDeviceStatusTotal(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return deviceService.getDeviceStatusTotal(request, paramMap);
	}

	/**
	 * [조회] 장비 비치 도서 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceBookList")
	public Map<String, Object> getDeviceBookList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.getDeviceBookList(request, paramMap);
	}

	/**
	 * [조회] 장비 정책 동기화
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/syncDeviceBookList")
	public Map<String, Object> syncDeviceBookList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		scheduleService.scheduleSLSBookInfoRun("");
		
		return JsonUtil.makeResultJSON("200");
	}

	/**
	 * [조회] 장비 정책 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceRuleList")
	public Map<String, Object> getDeviceRuleList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.getDeviceRuleList(request, paramMap);
	}

	/**
	 * [수정] 장비 정책 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/updateDeviceRule", method = { RequestMethod.POST })
	public Map<String, Object> updateDeviceRule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.updateDeviceRule(request, paramMap);
	}

	/**
	 * [조회] 장비 이벤트 상세 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceEventDetailList")
	public Map<String, Object> getDeviceEventDetailList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return deviceService.getDeviceEventDetailList(request, paramMap);
	}

	/**
	 * [조회] 장비 이벤트 타입 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceEventType")
	public Map<String, Object> getDeviceEventType(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return deviceService.getDeviceEventType(request, paramMap);
	}

	/**
	 * [조회] 장비 제어 목록(버튼) 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDeviceControlButtonList")
	public Map<String, Object> getDeviceControlButtonList(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return deviceService.getDeviceControlButtonList(request, paramMap);
	}

	/**
	 * [조회] 장비 정책 동기화
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/sendDeviceRule")
	public Map<String, Object> sendDeviceRule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		
		String device_key = ParamUtil.parseString(paramMap.get("device_key"));
		scheduleService.scheduleDeviceRuleSendRun(device_key);
		scheduleService.scheduleResvLoanRuleSendRun(device_key);
		
		return JsonUtil.makeResultJSON("200");
	}

	/**
	 * [조회] 서가 설정 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/getDrumRuleList")
	public Map<String, Object> getDrumRuleList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.getDrumRuleList(request, paramMap);
	}

	/**
	 * [수정] 서가 설정 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/updateDrumRule", method = { RequestMethod.POST })
	public Map<String, Object> updateDrumRule(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return deviceService.updateDrumRule(request, paramMap);
	}

	/**
	 * [수정] 도서 배출 작업 상태 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/device/updateBookOutWorkStatus", method = { RequestMethod.POST })
	public Map<String, Object> updateBookOutWorkStatus(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return deviceService.updateBookOutWorkStatus(request, paramMap);
	}
}
