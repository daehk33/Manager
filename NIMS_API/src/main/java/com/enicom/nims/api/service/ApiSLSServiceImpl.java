package com.enicom.nims.api.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.dao.DaoType;
import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.schedule.ScheduleService;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service(value = "apislsService")
public class ApiSLSServiceImpl implements ApiSLSService {
	private ServiceUtil serviceUtil;
	private DeviceService deviceService;
	private ScheduleService scheduleService;

	@Autowired
	public ApiSLSServiceImpl(ServiceUtil serviceUtil, DeviceService deviceService, ScheduleService scheduleService) {
		this.serviceUtil = serviceUtil;
		this.deviceService = deviceService;
		this.scheduleService = scheduleService;
	}

	@Override
	public List<Map<String, Object>> getSLSBookInfoList() throws Exception {
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSBookInfoList", Collections.emptyMap());
	}

	@Override
	public Map<String, Object> getSLSBookList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiSlibrary.getSLSBookList" };
		String[] columns = { "library_key", "device_key" };

		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getSLSBookInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "id" };
		String[] mappers = { "apiSlibrary.getSLSBookInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertSLSBookInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id" };
		return serviceUtil.service("apiSlibrary.insertSLSBookInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateSLSBookInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id" };
		return serviceUtil.service("apiSlibrary.updateSLSBookInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> syncSLSBookStatus(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		return serviceUtil.service("apiSlibrary.syncSLSBookStatus", paramMap, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getSLSCasInfoList() throws Exception {
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSCasInfoList", Collections.emptyMap());
	}

	@Override
	public Map<String, Object> getSLSCasInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "no" };
		String[] mappers = { "apiSlibrary.getSLSCasInfoList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getSLSCasInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "no" };
		String[] mappers = { "apiSlibrary.getSLSCasInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	public Map<String, Object> insertSLSCasInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "book_cnt" };
		return serviceUtil.service("apiSlibrary.insertSLSCasInfo", paramMap, columns, Operation.insert);
	}

	public int updateSLSCasInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "no" };
		return serviceUtil.serviceCount(DaoType.Smart, "smart.updateSLSCasInfo", paramMap, columns, Operation.update);
	}

	public Map<String, Object> updateSLSCasInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "no", "usage" };
		paramMap = ParamUtil.emptyToNull(paramMap);

		return serviceUtil.service("apiSlibrary.updateSLSCasInfo", paramMap, columns, Operation.update);
	}

	public Map<String, Object> updateSLSCasEnable(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiSlibrary.updateSLSCasEnable", paramMap, columns, Operation.update);
	}

	public Map<String, Object> updateSLSCasBatchEnable(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		return serviceUtil.service("apiSlibrary.updateSLSCasBatchEnable", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateSLSCasSendResult(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiSlibrary.updateSLSCasSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getSLSRuleInfoList() throws Exception {
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSRuleInfoList", Collections.emptyMap());
	}

	@Override
	public int updateSLSRuleInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "id" };
		return serviceUtil.serviceCount(DaoType.Smart, "smart.updateSLSRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getSLSRuleInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "apiSlibrary.getSLSRuleInfo" };
		String[] columns = { "library_key", "device_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getSLSRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiSlibrary.getSLSRuleInfo" };
		String[] columns = { "library_key", "device_key", "id" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertSLSRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id" };
		return serviceUtil.service("apiSlibrary.insertSLSRuleInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateSLSRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id" };
		return serviceUtil.service("apiSlibrary.updateSLSRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateSLSRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id" };
		return serviceUtil.service("apiSlibrary.updateSLSRuleSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getSLSUseLogInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "interval" };
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSUseLogInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getSLSUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiSlibrary.getSLSUseLogInfo" };
		String[] columns = { "library_key", "device_key", "id" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertSLSUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id", "loan_success", "return_success", "point" };
		return serviceUtil.service("apiSlibrary.insertSLSUseLogInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateSLSUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id", "loan_success", "return_success", "point" };
		return serviceUtil.service("apiSlibrary.updateSLSUseLogInfo", paramMap, columns, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getSLSReservationInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "interval" };
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSReservationInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getSLSReservationInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiSlibrary.getSLSReservationInfo" };
		String[] columns = { "library_key", "device_key", "id" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertSLSReservationInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id" };
		return serviceUtil.service("apiSlibrary.insertSLSReservationInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateSLSReservationInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id" };
		return serviceUtil.service("apiSlibrary.updateSLSReservationInfo", paramMap, columns, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getSLSReturnInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "interval" };
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSReturnInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getSLSReturnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiSlibrary.getSLSReturnInfo" };
		String[] columns = { "library_key", "device_key", "id" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertSLSReturnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id", "return_success" };
		return serviceUtil.service("apiSlibrary.insertSLSReturnInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateSLSReturnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id", "return_success" };
		return serviceUtil.service("apiSlibrary.updateSLSReturnInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getSLSStatusInfoList() throws Exception {
		return serviceUtil.selectOne(DaoType.Smart, "smart.getSLSStatusInfoList", Collections.emptyMap());
	}

	@Override
	public Map<String, Object> updateSLSStatusInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String device_id = ParamUtil.parseString(paramMap.get("device_id"));
		if (!device_id.equals("")) {
			DBContextHolder.setDBType(device_id);
			serviceUtil.service(DaoType.Smart, "smart.updateDeviceStatus", paramMap, Operation.update);

			paramMap.put("device_control_status", paramMap.get("status_info"));
			return deviceService.updateDeviceControllStatus(null, paramMap);
		}
		return JsonUtil.makeResultJSON("410");
	}

	@Override
	public List<Map<String, Object>> getSLSDrumRuleInfoList() throws Exception {
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSDrumRuleInfoList", Collections.emptyMap());
	}

	@Override
	public int updateSLSDrumRuleInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "no" };
		return serviceUtil.serviceCount(DaoType.Smart, "smart.updateSLSDrumRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getSLSDrumRuleInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "apiSlibrary.getSLSDrumRuleInfo" };
		String[] columns = { "library_key", "device_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getSLSDrumRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "no" };
		String[] mappers = { "apiSlibrary.getSLSDrumRuleInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertSLSDrumRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "no" };
		return serviceUtil.service("apiSlibrary.insertSLSDrumRuleInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateSLSDrumRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "no" };
		return serviceUtil.service("apiSlibrary.updateSLSDrumRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateSLSDrumRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "no" };
		return serviceUtil.service("apiSlibrary.updateSLSDrumRuleSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getEquipmentBookInner(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "smart.getEquipmentBookInner" };
		String device_id = ParamUtil.parseString(paramMap.get("device_id"));

		Map<String, Object> deviceParams = new HashMap<String, Object>();
		deviceParams.put("model_key", 6);

		List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceDBConnList", deviceParams);
		if (deviceList.size() > 0) {
			for (Map<String, Object> device : deviceList) {
				if (!device_id.equals("")) {
					if (!ParamUtil.parseString(device.get("device_id")).equals(device_id))
						continue;
				}

				DBContextHolder.setDBType(device_id);

				return serviceUtil.select(DaoType.Smart, mappers, paramMap, Operation.getList);
			}
		}

		return JsonUtil.makeResultJSON("400", "조회된 도서가 없습니다.");
	}

	@Override
	public List<Map<String, Object>> getSLSBookOutInfoList() throws Exception {
		return serviceUtil.selectList(DaoType.Smart, "smart.getSLSBookOutInfoList", Collections.emptyMap());
	}

	@Override
	public Map<String, Object> getSLSBookOutInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "smart.getSLSBookOutInfoList" };
		return serviceUtil.select(DaoType.Smart, mappers, paramMap, Operation.getOneOriMap);
	}

	public Map<String, Object> getSLSBookOutInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		String[] mappers = { "apiSlibrary.getSLSBookOutInfoListCnt", "apiSlibrary.getSLSBookOutInfoList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "id" };
		String[] mappers = { "apiSlibrary.getSLSBookOutInfoList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> syncSLSBookOutInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		scheduleService.scheduleSLSBookOutInfoRun();

		return JsonUtil.makeResultJSON("200");
	}

	@Override
	public Map<String, Object> insertSLSBookOutInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "book_key" };
		String device_id = ParamUtil.parseString(paramMap.get("device_id"));

		Map<String, Object> deviceParams = new HashMap<String, Object>();
		deviceParams.put("model_key", 6);

		List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceDBConnList", deviceParams);
		if (deviceList.size() > 0) {
			for (Map<String, Object> device : deviceList) {
				if (!device_id.equals("")) {
					if (!ParamUtil.parseString(device.get("device_id")).equals(device_id))
						continue;
				}

				DBContextHolder.setDBType(device_id);

				return serviceUtil.service(DaoType.Smart, "smart.insertSLSBookOutInfo", paramMap, columns,
						Operation.insert);
			}
		}

		return JsonUtil.makeResultJSON("400", "배출 도서 등록 작업 중 오류가 발생하였습니다.");
	}

	@Override
	public Map<String, Object> insertSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "book_key", "id" };
		return serviceUtil.service("apiSlibrary.insertSLSBookOutInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> insertSLSBookOutInfoTotal(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "book_key" };
		String device_id = ParamUtil.parseString(paramMap.get("device_id"));

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = sdf.format(new Date());

		paramMap.put("add_date", now);

		Map<String, Object> deviceParams = new HashMap<String, Object>();
		deviceParams.put("model_key", 6);

		List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceDBConnList", deviceParams);
		if (deviceList.size() > 0) {
			for (Map<String, Object> device : deviceList) {
				if (!device_id.equals("")) {
					if (!ParamUtil.parseString(device.get("device_id")).equals(device_id))
						continue;
				}

				paramMap.put("library_key", device.get("library_key"));
				paramMap.put("device_key", device.get("device_key"));

				DBContextHolder.setDBType(device_id);

				int result = serviceUtil.serviceCount(DaoType.Smart, "smart.insertSLSBookOutInfo", paramMap, columns, Operation.insert);
				if (result > 0) {
					Map<String, Object> outInfo = (Map<String, Object>) getSLSBookOutInfoList(paramMap);
					paramMap.put("id", outInfo.get("id").toString());

					return insertSLSBookOutInfo(request, paramMap);
				}
				else {
					return JsonUtil.makeResultJSON("400", "배출 도서 등록 작업 중 오류가 발생하였습니다.");
				}
			}
		}

		return JsonUtil.makeResultJSON("400", "배출 도서 등록 작업 중 오류가 발생하였습니다.");
	}

	@Override
	public Map<String, Object> updateSLSBookOutInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiSlibrary.updateSLSBookOutStatus", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		return serviceUtil.service("apiSlibrary.updateSLSBookOutInfo", paramMap, Operation.update);
	}

	@Override
	public Map<String, Object> deleteSLSBookOutInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "id" };
		return serviceUtil.service(DaoType.Smart, "smart.deleteSLSBookOutInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> deleteSLSBookOutInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "id" };
		return serviceUtil.service("apiSlibrary.deleteSLSBookOutInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> deleteSLSBookOutInfoTotal(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] sColumns = { "id" };
		String[] nColumns = { "rec_key", "id" };

		String device_id = ParamUtil.parseString(paramMap.get("device_id"));

		Map<String, Object> deviceParams = new HashMap<String, Object>();
		deviceParams.put("model_key", 6);

		List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceDBConnList", deviceParams);
		if (deviceList.size() > 0) {
			for (Map<String, Object> device : deviceList) {
				if (!device_id.equals("")) {
					if (!ParamUtil.parseString(device.get("device_id")).equals(device_id))
						continue;
				}

				DBContextHolder.setDBType(device_id);

				int result = serviceUtil.serviceCount(DaoType.Smart, "smart.deleteSLSBookOutInfo", paramMap, sColumns, Operation.delete);
				if (result > 0) {
					return serviceUtil.service("apiSlibrary.deleteSLSBookOutInfo", paramMap, nColumns, Operation.delete);
				} else {
					return JsonUtil.makeResultJSON("400", "배출 도서 등록 작업 중 오류가 발생하였습니다.");
				}
			}
		}

		return JsonUtil.makeResultJSON("400", "배출 도서 등록 작업 중 오류가 발생하였습니다.");
	}
}