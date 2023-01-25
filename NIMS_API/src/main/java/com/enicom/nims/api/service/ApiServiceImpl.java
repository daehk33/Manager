package com.enicom.nims.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.schedule.ScheduleService;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;
import com.enicom.nims.utils.SFTPUtil;
import com.enicom.nims.utils.SerialUtil;
import com.enicom.nims.wol.MagicPacket;
import com.enicom.nims.ws.WebSocketUtil;

@Service
public class ApiServiceImpl implements ApiService {
	private Logger logger = LoggerFactory.getLogger(ScheduleService.class);
	
	@Autowired
	private SFTPUtil sftpUtil;
	
	@Autowired
	private WebSocketUtil wsUtil;
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ApiLibService apiLibService;
	
	@Autowired
	private ServiceUtil serviceUtil;
	
	@Override
	public Map<String, Object> service(HttpServletRequest request, Map<String, Object> requestMap) throws Exception {
		String id = "system";
		String ip = request.getRemoteHost();

		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("worker", String.format("%s@%s", id, ip));

		String serviceId = requestMap.get("service_id").toString();

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> params = (List<Map<String, Object>>) requestMap.get("Params");

		if (serviceId.equals("REG_MEMBER")) {
			for (Map<String, Object> param : params) {

				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("member_id", param.get("member_id"));
				paramMap.put("member_no", param.get("member_no"));
				paramMap.put("member_nm", param.get("member_nm"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
//				paramMap.put("device_key"	,ParamUtil.parseIntOne(deviceMap, "rec_key"));

				Map<String, Object> memberMap = apiLibService.getlibMemberInfo(paramMap);

				if (memberMap != null) {
					paramMap.put("rec_key", memberMap.get("rec_key"));
					result = apiLibService.updateLibMemberInfo(paramMap);
				} else {
					result = apiLibService.insertLibMemberInfo(paramMap);
				}
			}
		}
		else if (serviceId.equals("REG_BOOK")) {
			for (Map<String, Object> param : params) {

				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("book_isbn", param.get("book_isbn"));
				paramMap.put("book_title", param.get("book_title"));
				paramMap.put("book_cnt", param.get("book_cnt"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
				paramMap.put("device_key", ParamUtil.parseIntOne(deviceMap, "rec_key"));

				Map<String, Object> bookMap = apiLibService.getlibBookInfo(paramMap);

				if (bookMap != null) {
					paramMap.put("rec_key", bookMap.get("rec_key"));
					result = apiLibService.updateLibBookInfo(paramMap);
				} else {
					result = apiLibService.insertLibBookInfo(paramMap);
				}
			}
		}
		else if (serviceId.equals("REG_ENTER")) {

			for (Map<String, Object> param : params) {
				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("member_id", ParamUtil.parseString(param, "member_id"));
				paramMap.put("member_no", ParamUtil.parseString(param, "member_no"));
				paramMap.put("enter_date", param.get("enter_date"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);
				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
				paramMap.put("device_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
				paramMap.put("device_nm", ParamUtil.parseString(deviceMap, "device_nm"));
				paramMap.put("device_location", ParamUtil.parseString(deviceMap, "device_location"));

				Map<String, Object> memberMap = apiLibService.getlibMemberInfo(paramMap);
				paramMap.put("member_key", ParamUtil.parseIntOne(memberMap, "member_key"));
				paramMap.put("member_nm", ParamUtil.parseString(memberMap, "member_nm"));

				result = insertEnterInfo(request, paramMap);
			}
		}
		else if (serviceId.equals("REG_LOAN")) {

			for (Map<String, Object> param : params) {

				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("book_isbn", param.get("book_isbn"));
				paramMap.put("member_id", param.get("member_id"));
				paramMap.put("member_no", param.get("member_no"));
				paramMap.put("loan_date", param.get("loan_date"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
				paramMap.put("device_key", ParamUtil.parseIntOne(deviceMap, "rec_key"));

				Map<String, Object> bookMap = apiLibService.getlibBookInfo(paramMap);
				Map<String, Object> memberMap = apiLibService.getlibMemberInfo(paramMap);

				paramMap.put("book_key", ParamUtil.parseIntOne(bookMap, "rec_key"));
				paramMap.put("member_key", ParamUtil.parseIntOne(memberMap, "rec_key"));

				result = insertLoanInfo(request, paramMap);
			}
		}
		// 대출반납기용
		else if (serviceId.equals("REG_LOANRETURN_LOAN")) {
			result = createLoanReturnBookEvent(request, paramMap, params, "대출");
			scheduleService.scheduleLoanReturnUseLogInfoRun();
		}
		else if (serviceId.equals("REG_LOANRETURN_RETURN")) {
			result = createLoanReturnBookEvent(request, paramMap, params, "반납");
			scheduleService.scheduleLoanReturnUseLogInfoRun();
		}
		else if (serviceId.equals("REG_ANTILOST")) {
			for (Map<String, Object> param : params) {

				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("inoutdiv", param.get("inoutdiv"));
				paramMap.put("cnt", param.get("cnt"));
				paramMap.put("event_date", param.get("event_date"));
				paramMap.put("book_isbn", param.get("book_isbn"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
				paramMap.put("device_key", ParamUtil.parseIntOne(deviceMap, "rec_key"));

				result = insertAntiLostInfo(request, paramMap);
			}
		}
		else if (serviceId.equals("REG_EVENT")) {
			for (Map<String, Object> param : params) {

				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("event_type", param.get("event_type"));
				paramMap.put("event_code", param.get("event_code"));
				paramMap.put("event_date", param.get("event_date"));
				paramMap.put("event_msg", param.get("event_msg"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
				paramMap.put("device_key", ParamUtil.parseIntOne(deviceMap, "rec_key"));
				paramMap.put("model_key", ParamUtil.parseIntOne(deviceMap, "model_key"));
				paramMap.put("device_nm", ParamUtil.parseString(deviceMap, "device_nm"));

				Map<String, Object> codeMap = getEventCodeInfo(request, paramMap);
				paramMap.put("event_code_nm", ParamUtil.parseString(codeMap.get("event_code_nm")));

				result = insertEventInfo(request, paramMap);
			}
		}
		else if (serviceId.equals("DEL_MEMBER")) {
			for (Map<String, Object> param : params) {

				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("member_id", param.get("member_id"));
				paramMap.put("member_no", param.get("member_no"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));

				Map<String, Object> memberMap = apiLibService.getlibMemberInfo(paramMap);

				if (memberMap != null) {
					paramMap.put("rec_key", memberMap.get("rec_key"));
					result = apiLibService.deleteLibMemberInfo(paramMap);
				} else {
					return JsonUtil.makeResultJSON("400", paramMap.get("member_id") + " 사용자가 없습니다.");
				}
			}
		}
		else if (serviceId.equals("DEL_BOOK")) {
			for (Map<String, Object> param : params) {

				paramMap.put("device_id", param.get("device_id"));
				paramMap.put("book_isbn", param.get("book_isbn"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
				paramMap.put("device_key", ParamUtil.parseIntOne(deviceMap, "rec_key"));

				Map<String, Object> bookMap = apiLibService.getlibBookInfo(paramMap);

				if (bookMap != null) {
					paramMap.put("rec_key", bookMap.get("rec_key"));
					result = apiLibService.deleteLibBookInfo(paramMap);
				} else {
					return JsonUtil.makeResultJSON("400", paramMap.get("book_isbn") + " 도서 정보가 없습니다.");
				}
			}
		}
		else if (serviceId.equals("REG_SLS_LOAN")) {
			result = createSLSBookEvent(request, paramMap, params, "대출");
			scheduleService.scheduleSLSUseLogInfoRun(3);
		}
		else if (serviceId.equals("REG_SLS_RETURN")) {
			result = createSLSBookEvent(request, paramMap, params, "반납");
			scheduleService.scheduleSLSUseLogInfoRun(3);
		}
		else if (serviceId.equals("REG_SLS_IN")) {
			result = createSLSBookOutEvent(request, paramMap, params, "투입");

			for (Map<String, Object> param : params) {
				String device_id = param.get("device_id").toString();

				scheduleService.scheduleSLSBookInfoRun(device_id);
			}
		}
		else if (serviceId.equals("REG_SLS_OUT")) {
			result = createSLSBookOutEvent(request, paramMap, params, "배출");
			scheduleService.scheduleSLSBookOutInfoRun();
		}
		else if (serviceId.equals("REG_RETURN")) {
			result = createSLSBookEvent(request, paramMap, params, "무인반납");
			scheduleService.scheduleSLSReturnInfoRun(3);
		}
		else if (serviceId.equals("REG_RESV_LOAN")) {
			result = createSLSBookEvent(request, paramMap, params, "예약대출");
			scheduleService.scheduleSLSReservationInfoRun(3);
		}
		// 스마트도서관 도서 목록 동기화
		else if (serviceId.equals("SYNC_BOOK_INFO")) {
			for (Map<String, Object> param : params) {
				String device_id = param.get("device_id").toString();

				scheduleService.scheduleSLSBookInfoRun(device_id);
			}

			result = JsonUtil.makeResultJSON("200");
		}
		// 예약대출기
		else if (serviceId.equals("REG_RESVLOAN_LOAN")) {
			result = createResvLoanBookEvent(request, paramMap, params, "대출");
			scheduleService.scheduleResvLoanUseLogInfoRun(3);
		}
		// 장비 연결 상태 갱신
		else if (serviceId.equals("CHK_DEVICE_CONN")) {

			for (Map<String, Object> param : params) {
				paramMap.put("manager_key", 0);
				paramMap.put("device_id", param.get("device_id"));

				Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

				paramMap.put("connect_yn", "Y");
				paramMap.put("device_status", "1");
				paramMap.put("worker", paramMap.get("worker").toString());
				paramMap.put("rec_key", ParamUtil.parseIntOne(deviceMap, "rec_key"));

				result = deviceService.updateDeviceConnInfo(request, paramMap);
			}
		} else {
			return JsonUtil.makeResultJSON("400", "알수없는 Service 입니다.!");
		}
		return result;
	}

	private Map<String, Object> createSLSBookEvent(HttpServletRequest request, Map<String, Object> paramMap,
			List<Map<String, Object>> params, String type) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		for (Map<String, Object> param : params) {

			paramMap.put("device_id", param.get("device_id"));
			paramMap.put("book_no", param.get("book_no"));
			paramMap.put("book_title", param.get("book_title"));
			paramMap.put("user_no", param.get("user_no"));
			paramMap.put("user_name", param.get("user_name"));
			paramMap.put("event_date", param.get("event_date"));
			paramMap.put("type", type);
			Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

			paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
			paramMap.put("device_nm", ParamUtil.parseString(deviceMap, "device_nm"));

			result = getSLSBookEvent(request, paramMap);
		}

		return result;
	}

	private Map<String, Object> getSLSBookEvent(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String message = String.format("[%s] %s_\"%s\"님 도서 \"%s\" %s",
				ParamUtil.parseString(paramMap.get("device_nm"), "미등록 장비"), paramMap.get("type"),
				paramMap.get("user_name"), paramMap.get("book_title"), paramMap.get("type"));
		wsUtil.sendToDashboard("smart",
				wsUtil.createMessage(paramMap.get("event_date"), message, paramMap.get("library_key"), 2));
		return JsonUtil.makeResultJSON("200");
	}

	private Map<String, Object> createSLSBookOutEvent(HttpServletRequest request, Map<String, Object> paramMap,
			List<Map<String, Object>> params, String type) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		for (Map<String, Object> param : params) {
			paramMap.put("device_id", param.get("device_id"));
			paramMap.put("book_no", param.get("book_no"));
			paramMap.put("book_title", param.get("book_title"));
			paramMap.put("event_date", param.get("event_date"));
			paramMap.put("type", type);
			Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

			paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
			paramMap.put("device_nm", ParamUtil.parseString(deviceMap, "device_nm"));

			result = getSLSBookOutEvent(request, paramMap);
		}

		return result;
	}

	private Map<String, Object> getSLSBookOutEvent(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String message = String.format("[%s] %s_%s(%s) %s", ParamUtil.parseString(paramMap.get("device_nm"), "미등록 장비"),
				paramMap.get("type"), paramMap.get("book_title"), paramMap.get("book_no"), paramMap.get("type"));
		wsUtil.sendToDashboard("smart",
				wsUtil.createMessage(paramMap.get("event_date"), message, paramMap.get("library_key"), 2));
		return JsonUtil.makeResultJSON("200");
	}

	private Map<String, Object> createSLSBookInEvent(HttpServletRequest request, Map<String, Object> paramMap,
			List<Map<String, Object>> params, String type) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		for (Map<String, Object> param : params) {
			paramMap.put("device_id", param.get("device_id"));
			paramMap.put("book_no", param.get("book_no"));
			paramMap.put("book_title", param.get("book_title"));
			paramMap.put("event_date", param.get("event_date"));
			paramMap.put("type", type);
			Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

			paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
			paramMap.put("device_nm", ParamUtil.parseString(deviceMap, "device_nm"));

			result = getSLSBookOutEvent(request, paramMap);
		}

		return result;
	}

	private Map<String, Object> getSLSBookInEvent(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String message = String.format("[%s] %s_%s(%s) %s", ParamUtil.parseString(paramMap.get("device_nm"), "미등록 장비"),
				paramMap.get("type"), paramMap.get("book_title"), paramMap.get("book_no"), paramMap.get("type"));
		wsUtil.sendToDashboard("smart",
				wsUtil.createMessage(paramMap.get("event_date"), message, paramMap.get("library_key"), 2));
		return JsonUtil.makeResultJSON("200");
	}

	private Map<String, Object> createResvLoanBookEvent(HttpServletRequest request, Map<String, Object> paramMap,
			List<Map<String, Object>> params, String type) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		for (Map<String, Object> param : params) {

			paramMap.put("device_id", param.get("device_id"));
			paramMap.put("title", param.get("title"));
			paramMap.put("user_name", param.get("user_name"));
			paramMap.put("event_date", param.get("event_date"));
			paramMap.put("type", type);
			Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

			paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
			paramMap.put("device_nm", ParamUtil.parseString(deviceMap, "device_nm"));

			result = getResvLoanBookEvent(request, paramMap);
		}

		return result;
	}

	private Map<String, Object> getResvLoanBookEvent(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String message = String.format("[%s] %s_\"%s\"님 도서 \"%s\" %s",
				ParamUtil.parseString(paramMap.get("device_nm"), "미등록 장비"), paramMap.get("type"),
				paramMap.get("user_name"), paramMap.get("title"), paramMap.get("type"));
		wsUtil.sendToDashboard("resvLoan",
				wsUtil.createMessage(paramMap.get("event_date"), message, paramMap.get("library_key"), 2));
		return JsonUtil.makeResultJSON("200");
	}

	private Map<String, Object> createLoanReturnBookEvent(HttpServletRequest request, Map<String, Object> paramMap,
			List<Map<String, Object>> params, String type) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		for (Map<String, Object> param : params) {

			paramMap.put("device_id", param.get("device_id"));
			paramMap.put("title", param.get("title"));
			paramMap.put("user_name", param.get("user_name"));
			paramMap.put("event_date", param.get("event_date"));
			paramMap.put("type", type);
			Map<String, Object> deviceMap = getDeviceInfo(request, paramMap);

			paramMap.put("library_key", ParamUtil.parseIntOne(deviceMap, "library_key"));
			paramMap.put("device_nm", ParamUtil.parseString(deviceMap, "device_nm"));
		}

		result = getLoanReturnBookEvent(request, paramMap);

		return result;
	}

	private Map<String, Object> getLoanReturnBookEvent(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String message = String.format("[%s] %s_\"%s\"님 도서 \"%s\" %s",
				ParamUtil.parseString(paramMap.get("device_nm"), "미등록 장비"), paramMap.get("type"),
				paramMap.get("user_name"), paramMap.get("title"), paramMap.get("type"));

		wsUtil.sendToDashboard("loanReturn",
				wsUtil.createMessage(paramMap.get("event_date"), message, paramMap.get("library_key"), 2));

		return JsonUtil.makeResultJSON("200");
	}

	public Map<String, Object> getDeviceInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "device.getDeviceInfo" };
		return serviceUtil.select(mappers, paramMap, Operation.getOneOriMap);
	}

	private Map<String, Object> getEventCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "dashboard.getEventCodeInfo" };
		return serviceUtil.select(mappers, paramMap, Operation.getOneOriMap);
	}

	public Map<String, Object> getLoanInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "api.getLoanInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	public Map<String, Object> insertEnterInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "member_key" };

		String member_info = paramMap.get("member_id").toString().equals("")
				&& paramMap.get("member_no").toString().equals("")
						? "미등록 이용자"
						: String.format("%s (%s)", paramMap.get("member_nm"),
								paramMap.get("member_id").toString().equals("") ? paramMap.get("member_no")
										: paramMap.get("member_id"));

		String message = String.format("[%s] %s, \"%s\" 출입", paramMap.get("device_nm"), paramMap.get("device_location"),
				member_info);
		wsUtil.sendToDashboard("enter",
				wsUtil.createMessage(paramMap.get("enter_date"), message, paramMap.get("library_key"), 2));

		return serviceUtil.service("api.insertEnterInfo", paramMap, columns, Operation.insert);
	}

	public Map<String, Object> insertLoanInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "member_key", "book_key" };

		wsUtil.sendToDashboard("loan",
				wsUtil.createMessage(paramMap.get("loan_date"), "", paramMap.get("library_key"), 2));

		return serviceUtil.service("api.insertLoanInfo", paramMap, columns, Operation.insert);
	}

	public Map<String, Object> insertReturnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "member_key", "book_key" };

		wsUtil.sendToDashboard("return",
				wsUtil.createMessage(paramMap.get("return_date"), "", paramMap.get("library_key"), 2));

		return serviceUtil.service("api.insertReturnInfo", paramMap, columns, Operation.insert);
	}

	public Map<String, Object> updateReturnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "member_key", "book_key" };
		return serviceUtil.service("api.updateReturnInfo", paramMap, columns, Operation.update);
	}

	public Map<String, Object> insertAntiLostInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "cnt", "device_key", "member_key" };
		return serviceUtil.service("api.insertAntiLostInfo", paramMap, columns, Operation.insert);
	}


	private Map<String, Object> insertEventInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "member_key", "model_key" };

		Object library_key = paramMap.get("library_key");
		String date = ParamUtil.parseString(paramMap.get("event_date"));

		String message = ParamUtil.parseString(paramMap.get("event_msg"));
		if (message.equals("")) {
			message = String.format("[%s] \"%s\" 이벤트 발생",
					ParamUtil.parseString(paramMap.get("device_nm"), (String) paramMap.get("device_id")),
					paramMap.get("event_code_nm"));
		} else {
			message = String.format("[%s] \"%s\" 이벤트 발생",
					ParamUtil.parseString(paramMap.get("device_nm"), (String) paramMap.get("deivce_id")), message);
		}

		Object type = ParamUtil.parseString(paramMap.get("event_type"), "2");

		Object model_key = paramMap.get("model_key");

		String[] alarmColumns = { "library_key", "model_key" };
		Map<String, Object> alarmMap = new HashMap<String, Object>();

		alarmMap.put("id", System.currentTimeMillis());
		alarmMap.put("library_key", library_key);
		alarmMap.put("date", date);
		alarmMap.put("message", message);
		alarmMap.put("type", type);
		alarmMap.put("model_key", model_key);

		serviceUtil.service("dashboard.insertAlarmInfo", alarmMap, alarmColumns, Operation.insert);

		wsUtil.sendToDashboard("event", wsUtil.createMessage(date, message, library_key, type));

		return serviceUtil.service("api.insertEventInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> wakeOnLan(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {

		String ipStr = "";
		String macStr = "";
		int port = 0;

		Map<String, Object> deviceMap = new HashMap<String, Object>();

		if (paramMap.get("device_key") != null) {
			deviceMap = deviceService.getDeviceWolInfo(request, paramMap);
			if (deviceMap == null)
				return JsonUtil.makeResultJSON("400");
		} else {
			return JsonUtil.makeResultJSON("410");
		}

		try {
			if (deviceMap.get("device_broadcast") == null || deviceMap.get("device_port") == null
					|| deviceMap.get("device_mac") == null)
				return JsonUtil.makeResultJSON("400");

			macStr = MagicPacket.cleanMac(deviceMap.get("device_mac").toString());
			ipStr = deviceMap.get("device_broadcast").toString();
			port = Integer.parseInt(deviceMap.get("device_port").toString());

			logger.info("Sending to: " + MagicPacket.send(macStr, ipStr, port));

			paramMap.put("device_control_status", "3");
			deviceService.updateDeviceControllStatus(request, paramMap);
		} catch (IllegalArgumentException e) {
			logger.info(e.getMessage());
		} catch (Exception e) {
			logger.info("Failed to send Wake-on-LAN packet:" + e.getMessage());
		}

		return JsonUtil.makeResultJSON("200");
	}

	@Override
	public Map<String, Object> sftpService(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String host = "";
		String id = "";
		int port = 0;

		String remotePath = "";
		String deviceId = "";
		String action = "";
		String type = "";

		String[] local = { "127.0.0.1", "localhost" };

		boolean isServer = false;

		Map<String, Object> sftpMap = new HashMap<String, Object>();

		if (paramMap.get("device_key") != null && paramMap.get("action") != null && paramMap.get("type") != null) {
			sftpMap = deviceService.getDeviceSftpInfoList(request, paramMap);
			if (sftpMap == null)
				return JsonUtil.makeResultJSON("400");
		} else {
			return JsonUtil.makeResultJSON("410");
		}

		try {
			if (sftpMap.get("host") == null && sftpMap.get("id") == null && sftpMap.get("port") == null
					&& sftpMap.get("banner_image_path") == null)
				return JsonUtil.makeResultJSON("400");

			// SFTP 접속 정보 설정
			host = sftpMap.get("host").toString();
			id = sftpMap.get("id").toString();
			port = Integer.parseInt(sftpMap.get("port").toString());

			type = paramMap.get("type").toString();

			// 이미지 경로
			remotePath = sftpMap.get("banner_image_path").toString();

			// 장비를 서버로 이용중인경우
			isServer = Arrays.stream(local).anyMatch(host::equals);

			// 대상 장비ID
			deviceId = sftpMap.get("device_id").toString();

			// 요청 동작
			action = paramMap.get("action").toString();

			// SFTP 연결
			if (!isServer) {
				sftpUtil.connect(host, id, port);
			}

			boolean isSuccess = false;

			if (action.equalsIgnoreCase("upload")) {
				isSuccess = sftpUtil.fileUpload(remotePath, deviceId, type, isServer);
			} else if (action.equalsIgnoreCase("download")) {
				isSuccess = sftpUtil.fileDownload(remotePath, deviceId, type, isServer);
			} else if (action.equalsIgnoreCase("remove") && type.equalsIgnoreCase("image")) {
				String fileName = paramMap.get("file_name").toString();

				isSuccess = sftpUtil.fileRemove(remotePath, deviceId, fileName, isServer);
			}

			if (isSuccess) {
				sftpUtil.disconnection();
				return JsonUtil.makeResultJSON("200");
			} else {
				sftpUtil.disconnection();
				return JsonUtil.makeResultJSON("400");
			}
		} catch (Exception e) {
			logger.info("Failed to SFTP : " + e.getMessage());

			sftpUtil.disconnection();
			return JsonUtil.makeResultJSON("400");
		}
	}

	@Override
	public Map<String, Object> serialService()
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		ArrayList<String> portList = SerialUtil.getPorts();
		
		if(portList.isEmpty()) {
			return JsonUtil.makeResultJSON("400", "조회된 시리얼 포트가 없습니다.");
		} 
		else {
			result.put("CODE", "200");
			result.put("result", portList);
			
			return result;
		}
	}
}