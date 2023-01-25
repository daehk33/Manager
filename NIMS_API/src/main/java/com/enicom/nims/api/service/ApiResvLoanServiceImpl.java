package com.enicom.nims.api.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.enicom.nims.resvLoan.service.ResvLoanService;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service(value = "apiResvLoanService")
public class ApiResvLoanServiceImpl implements ApiResvLoanService {
	private ServiceUtil serviceUtil;
	private ResvLoanService resvLoanService;
	
	@Autowired
	public ApiResvLoanServiceImpl(ServiceUtil serviceUtil, ResvLoanService resvLoanService) {
		this.serviceUtil = serviceUtil;
		this.resvLoanService = resvLoanService;
	}
	
	@Override
	public List<Map<String, Object>> getResvLoanUseLogInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "interval" };
		return serviceUtil.selectList(DaoType.Gallery, "gallery.getResvLoanUseLogInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getResvLoanUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanUseLogInfo" };
		String[] columns = { "library_key", "device_key", "idx" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertResvLoanUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "id", "loan_success", "return_success", "point" };
		return serviceUtil.service("apiResvLoan.insertResvLoanUseLogInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateResvLoanUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "id", "loan_success", "return_success", "point" };
		return serviceUtil.service("apiResvLoan.updateResvLoanUseLogInfo", paramMap, columns, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getResvLoanCabinetModuleInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "cabinet" };
		return serviceUtil.selectList(DaoType.Gallery, "gallery.getResvLoanCabinetModuleInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getResvLoanCabinetModuleInfoList(HttpServletRequest request,
			Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanCabinetModuleInfo" };
		String[] columns = { "library_key", "device_key", "cabinet", "insert_no" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getResvLoanCabinetModuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanCabinetModuleInfo" };
		String[] columns = { "library_key", "device_key", "cabinet", "insert_no" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertResvLoanCabinetModuleInfoGallery(HttpServletRequest request,
			Map<String, Object> paramMap) throws Exception {
		String[] columns = { "cabinet", "insert_no" };
		return serviceUtil.service(DaoType.Gallery, "gallery.insertResvLoanCabinetModuleInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> insertResvLoanCabinetModuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "cabinet", "insert_no" };
		return serviceUtil.service("apiResvLoan.insertResvLoanCabinetModuleInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateResvLoanCabinetModuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "cabinet", "insert_no" };
		return serviceUtil.service("apiResvLoan.updateResvLoanCabinetModuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> deleteResvLoanCabinetModuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "cabinet" };
		return serviceUtil.service("apiResvLoan.deleteResvLoanCabinetModuleInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateResvLoanCabinetModuleSendResult(HttpServletRequest request,
			Map<String, Object> paramMap) throws Exception {
		String[] columns = { "library_key", "device_key", "cabinet" };
		return serviceUtil.service("apiResvLoan.updateResvLoanCabinetModuleSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getResvLoanCabinetInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "module" };
		return serviceUtil.selectList(DaoType.Gallery, "gallery.getResvLoanCabinetInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getResvLoanCabinetInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanCabinetInfo" };
		String[] columns = { "library_key", "device_key", "cabinet", "module", "sub_cabinet" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getResvLoanCabinetInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "cabinet", "module" };
		return serviceUtil.selectOne(DaoType.Gallery, "gallery.getResvLoanCabinetInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getResvLoanCabinetInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanCabinetInfo" };
		String[] columns = { "library_key", "device_key", "cabinet", "module", "sub_cabinet" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertResvLoanCabinetInfoGallery(HttpServletRequest request,
			Map<String, Object> paramMap) throws Exception {
		String[] columns = { "cabinet", "module", "sub_cabinet", "book_count", "cabinet_type" };
		return serviceUtil.service(DaoType.Gallery, "gallery.insertResvLoanCabinetInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> insertResvLoanCabinetInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "cabinet", "module", "sub_cabinet", "book_count",
				"cabinet_type" };
		return serviceUtil.service("apiResvLoan.insertResvLoanCabinetInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateResvLoanCabinetInfo(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "cabinet", "cabinet_type" };
		return serviceUtil.service(DaoType.Gallery, "gallery.updateResvLoanCabinetInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateResvLoanCabinetInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "cabinet", "module", "sub_cabinet", "book_count",
				"cabinet_type" };
		return serviceUtil.service("apiResvLoan.updateResvLoanCabinetInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> deleteResvLoanCabinetInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "cabinet", "module" };
		return serviceUtil.service("apiResvLoan.deleteResvLoanCabinetInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateResvLoanCabinetType(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key" };
		String[] keyList = paramMap.get("rec_key").toString().split(",");
		String[] subKeyList = paramMap.get("sub_cabinet").toString().split(",");

		Map<String, Object> cabinetMap = new HashMap<String, Object>();

		String id = "system";
		String ip = request.getRemoteHost();

		cabinetMap.put("library_key", paramMap.get("library_key").toString());
		cabinetMap.put("device_key", paramMap.get("device_key").toString());
		cabinetMap.put("worker", String.format("%s@%s", id, ip));

		for (int i = 0; i < keyList.length; i++) {
			cabinetMap.put("rec_key", keyList[i]);
			cabinetMap.put("sub_cabinet", subKeyList[i]);
			cabinetMap.put("send_req_yn", 'Y');

			serviceUtil.service("apiResvLoan.updateResvLoanCabinetType", cabinetMap, columns, Operation.update);
		}

		return JsonUtil.makeResultJSON("200");
	}

	@Override
	public Map<String, Object> updateResvLoanCabinetSendResult(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "cabinet" };
		return serviceUtil.service("apiResvLoan.updateResvLoanCabinetSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getResvLoanUnmodifiableCabinetList(HttpServletRequest request,
			Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanUnmodifiableCabinetList" };
		String[] columns = { "library_key", "device_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public List<Map<String, Object>> getResvLoanModuleInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "module_id" };
		return serviceUtil.selectList(DaoType.Gallery, "gallery.getResvLoanModuleInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getResvLoanModuleInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "direction", "module_id", "cabinet_no" };
		String[] mappers = { "apiResvLoan.getResvLoanModuleInfoList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getResvLoanModuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanModuleInfo" };
		String[] columns = { "library_key", "device_key", "module_id" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertResvLoanModuleInfoGallery(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "module_id", "cabinet_no", "direction", "column_cnt", "row_cnt", "start_left", "start_top",
				"width", "height", "out_type" };
		return serviceUtil.service(DaoType.Gallery, "gallery.insertResvLoanModule", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> insertResvLoanModuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "module_id", "cabinet_no", "direction", "column_cnt",
				"row_cnt", "start_left", "start_top", "width", "height", "out_type" };
		return serviceUtil.service("apiResvLoan.insertResvLoanModuleInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateResvLoanModuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "module_id", "cabinet_no", "direction",
				"column_cnt", "row_cnt", "start_left", "start_top", "width", "height", "out_type" };
		paramMap = ParamUtil.emptyToNull(paramMap);

		return serviceUtil.service("apiResvLoan.updateResvLoanModuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateResvLoanModuleSendResult(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "module_id", "cabinet_no", "direction",
				"column_cnt", "row_cnt", "start_left", "start_top", "width", "height", "out_type" };
		return serviceUtil.service("apiResvLoan.updateResvLoanModuleSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> insertResvLoanModule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "module_id", "cabinet_no", "direction", "column_cnt",
				"row_cnt", "start_left", "start_top", "width", "height", "out_type" };

		int result = serviceUtil.serviceCount("apiResvLoan.insertResvLoanModule", paramMap, columns, Operation.insert);
		if (result > 0) {
			List<Map<String, Object>> moduleList = serviceUtil.selectList("getResvLoanModuleInfoList", paramMap, columns);

			for (Map<String, Object> moduleInfo: moduleList) {
				int library_key = ParamUtil.parseInt(moduleInfo.get("library_key"));
				int device_key = ParamUtil.parseInt(moduleInfo.get("device_key"));
				int cabinet_no = ParamUtil.parseInt(moduleInfo.get("cabinet_no"));
				int column = ParamUtil.parseInt(moduleInfo.get("column_cnt"));
				int row = ParamUtil.parseInt(moduleInfo.get("row_cnt"));
				int endCabinetNo = cabinet_no + (column * row) - 1;
				int sub_cabinet = 1;

				// cabinet_module, cabinet 테이블 데이터 추가
				for (int i = cabinet_no; i <= endCabinetNo; i++) {
					Map<String, Object> cabinetMap = new HashMap<String, Object>();

					// cabinet 데이터 입력
					cabinetMap.put("library_key", library_key);
					cabinetMap.put("device_key", device_key);
					cabinetMap.put("cabinet", i);
					cabinetMap.put("module", ParamUtil.parseInt(moduleInfo.get("module_id")));
					cabinetMap.put("sub_cabinet", sub_cabinet);
					cabinetMap.put("book_count", 0);
					cabinetMap.put("status", 0);
					cabinetMap.put("error", 0);
					cabinetMap.put("send_req_yn", "Y");

					insertResvLoanCabinetInfo(null, cabinetMap);

					sub_cabinet++;

					// cabinet_module 데이터 입력
					for (int j = 1; j <= 3; j++) {
						Map<String, Object> cabinetModuleMap = new HashMap<String, Object>();

						cabinetModuleMap.put("library_key", library_key);
						cabinetModuleMap.put("device_key", device_key);
						cabinetModuleMap.put("cabinet", i);
						cabinetModuleMap.put("insert_no", j);
						cabinetModuleMap.put("status", 0);
						cabinetModuleMap.put("send_req_yn", "Y");

						insertResvLoanCabinetModuleInfo(null, cabinetModuleMap);
					}
				}
			}
		}

		return JsonUtil.makeResultJSON("200");
	}

	@Override
	public Map<String, Object> deleteResvLoanModule(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "module_id" };
		List<String> defaultModuleList = new ArrayList<>(Arrays.asList("1", "2", "3"));

		Map<String, Object> lastModuleResult = (Map<String, Object>) (resvLoanService.getResvLoanLastModule(request,
				paramMap));

		// 삭제 대상 모듈이 기본 모듈(1~3)인지 체크
		if (defaultModuleList.contains(paramMap.get("module_id").toString())) {
			return JsonUtil.makeResultJSON("451");
		}

		// 삭제 대상 모듈의 module_id가 마지막에 추가된 module_id와 동일한지 체크
		String lastModuleId = ParamUtil.parseString(lastModuleResult.get("module_id"));

		if (lastModuleId.equals(paramMap.get("module_id").toString())) {
			int result = serviceUtil.serviceCount("apiResvLoan.deleteResvLoanModule", paramMap, columns,
					Operation.delete);

			// cabinet, cabinet_module 데이터 삭제
			int library_key = ParamUtil.parseInt(paramMap.get("library_key"));
			int device_key = ParamUtil.parseInt(paramMap.get("device_key"));
			int cabinet_no = ParamUtil.parseInt(lastModuleResult.get("cabinet_no"));
			int column = ParamUtil.parseInt(lastModuleResult.get("column_cnt"));
			int row = ParamUtil.parseInt(lastModuleResult.get("row_cnt"));
			int endCabinetNo = cabinet_no + (column * row) - 1;

			Map<String, Object> cabinetMap = new HashMap<String, Object>();

			cabinetMap.put("library_key", library_key);
			cabinetMap.put("device_key", device_key);
			cabinetMap.put("module", lastModuleId);

			// cabinet 삭제
			deleteResvLoanCabinetInfo(null, cabinetMap);

			for (int i = cabinet_no; i <= endCabinetNo; i++) {
				Map<String, Object> cabinetModuleMap = new HashMap<String, Object>();

				cabinetModuleMap.put("library_key", library_key);
				cabinetModuleMap.put("device_key", device_key);
				cabinetModuleMap.put("cabinet", i);

				// cabinet_module 삭제
				deleteResvLoanCabinetModuleInfo(null, cabinetModuleMap);
			}

			// NIMS쪽 DB에서 삭제 성공 시 예약대출기쪽 DB에서도 삭제 진행
			if (result > 0) {
				String device_id = ParamUtil.parseString(paramMap.get("device_id"));

				Map<String, Object> deviceParams = new HashMap<String, Object>();
				deviceParams.put("model_key", 8);
				deviceParams.put("rec_key", device_id);

				List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceDBConnList",
						deviceParams);
				if (deviceList.size() > 0) {
					for (Map<String, Object> device : deviceList) {
						if (device.get("rec_key").toString().equals(paramMap.get("device_key").toString())) {
							DBContextHolder.setDBType(device.get("device_id").toString());

							// cabinet 삭제
							List<Map<String, Object>> cabinetList = getResvLoanCabinetInfoList(cabinetMap);

							if (cabinetList.size() > 0) {
								serviceUtil.service(DaoType.Gallery, "gallery.deleteResvLoanCabinetInfo", cabinetMap, columns, Operation.delete);
							}

							// cabinet_module 삭제
							for (int i = cabinet_no; i <= endCabinetNo; i++) {
								Map<String, Object> cabinetModuleMap = new HashMap<String, Object>();

								cabinetModuleMap.put("cabinet", i);

								List<Map<String, Object>> cabinetModuleList = getResvLoanCabinetModuleInfoList(cabinetModuleMap);

								if (cabinetModuleList.size() > 0) {
									serviceUtil.service(DaoType.Gallery, "gallery.deleteResvLoanCabinetModule", cabinetModuleMap, columns, Operation.delete);
								}
							}

							List<Map<String, Object>> moduleList = (List<Map<String, Object>>) getResvLoanModuleInfoList(paramMap);

							if (moduleList.size() > 0) {
								serviceUtil.service(DaoType.Gallery, "gallery.deleteResvLoanModule", paramMap, columns, Operation.delete);
							}

							return JsonUtil.makeResultJSON("200");
						}
					}
				}
				return JsonUtil.makeResultJSON("200");

			} else {
				return JsonUtil.makeResultJSON("400");
			}
		} else {
			return JsonUtil.makeResultJSON("450");
		}
	}

	@Override
	public List<Map<String, Object>> getResvLoanRuleInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = { "manage_id" };
		return serviceUtil.selectList(DaoType.Gallery, "gallery.getResvLoanRuleInfoList", paramMap, columns);
	}

	@Override
	public Map<String, Object> getResvLoanRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanRuleList" };
		String[] columns = { "library_key", "device_key" };

		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> getResvLoanRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiResvLoan.getResvLoanRuleInfo" };
		String[] columns = { "library_key", "device_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertResvLoanRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key" };
		return serviceUtil.service("apiResvLoan.insertResvLoanRuleInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateResvLoanRuleInfo(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.service(DaoType.Gallery, "gallery.updateResvLoanRuleInfo", paramMap, Operation.update);
	}

	@Override
	public Map<String, Object> updateResvLoanRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		paramMap = ParamUtil.emptyToNull(paramMap);

		return serviceUtil.service("apiResvLoan.updateResvLoanRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateResvLoanRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiResvLoan.updateResvLoanRuleSendResult", paramMap, columns, Operation.update);
	}
}
