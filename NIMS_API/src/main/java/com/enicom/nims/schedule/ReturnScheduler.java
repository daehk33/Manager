package com.enicom.nims.schedule;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.api.service.ApiReturnService;
import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.utils.ParamUtil;

@Service
public class ReturnScheduler extends Scheduler {
	private static Logger logger = LoggerFactory.getLogger(ReturnScheduler.class);
	private static String MODEL_KEY = "3,11";

	private ApiReturnService apiReturnService;

	@Autowired
	public ReturnScheduler(DeviceService deviceService, ApiReturnService apiReturnService) {
		super(deviceService, MODEL_KEY);
		this.apiReturnService = apiReturnService;
	}

	@Override
	public void scheduleInit() throws Exception {
		logger.info("ReturnSchedulerInit is Running >> {}", LocalDateTime.now());

		syncUseLogInfo();
		syncRuleInfo();

		logger.info("ReturnSchedulerInit is Ended >> {}", LocalDateTime.now());

	}

	@Override
	public void scheduleDay() throws Exception {
		logger.info("ReturnSchedulerDay is Running >> {}", LocalDateTime.now());
		logger.info("ReturnSchedulerDay is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleHour() throws Exception {
		logger.info("ReturnSchedulerHour is Running >> {}", LocalDateTime.now());
		
		syncRuleInfo();
		
		logger.info("ReturnSchedulerHour is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteTen() throws Exception {
		logger.info("ReturnScheduler10Minute is Running >> {}", LocalDateTime.now());
		
		syncUseLogInfo();
		
		logger.info("ReturnScheduler10Minute is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteOne() throws Exception {
		logger.info("ReturnScheduler1Minute is Running >> {}", LocalDateTime.now());

		sendRuleInfo();

		logger.info("ReturnScheduler1Minute is Ended >> {}", LocalDateTime.now());
	}

	private void syncUseLogInfo() {
		logger.info(">>> syncUseLogInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUseLogInfo End - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if (device_id.equals("")) {
					continue;
				}
				
				// 마지막 로그 정보 조회
				Map<String, Object> lastLogMap = apiReturnService.getReturnLastLogInfo(null, device);
				
				if (lastLogMap != null) {
					device.put("book_state_date", ParamUtil.parseString(lastLogMap.get("book_state_date")));
					device.put("book_state_time", ParamUtil.parseString(lastLogMap.get("book_state_time")));
				} else {
					device.put("book_state_date", "");
					device.put("book_state_time", "");
				}
				
				Map<String, Object> result = apiReturnService.getReturnUseLogInfoList(device);

				if (result != null) {
					@SuppressWarnings("unchecked")
					List<Map<String, Object>> useLogInfoList = (List<Map<String, Object>>) result.get("LogList");
					
					int insertCnt = 0;
					int updateCnt = 0;
					int failCnt = 0;
					if (useLogInfoList == null) {
						continue;
					}
					
					for (Map<String, Object> useLogInfo : useLogInfoList) {
						if (useLogInfo != null) {
							useLogInfo.put("library_key", library_key);
							useLogInfo.put("device_key", device_key);
							
							Map<String, Object> useLogInfoMap = apiReturnService.getReturnUseLogInfo(null, useLogInfo);
							
							if (useLogInfoMap != null) {
								useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
								apiReturnService.updateReturnUseLogInfo(null, useLogInfo);
								updateCnt++;
							} else {
								apiReturnService.insertReturnUseLogInfo(null, useLogInfo);
								insertCnt++;
							}
						} else {
							failCnt++;
						}
					}
					logger.info("TOTAL >>> " + useLogInfoList.size());
					logger.info("INSERT >>> " + insertCnt);
					logger.info("UPDATE >>> " + updateCnt);
					logger.info("FAIL >>> " + failCnt);
				}
			}

			logger.info(">>> syncUseLogInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncUseLogInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	private void syncRuleInfo() {
		logger.info(">>> syncRuleInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncRuleInfo End - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if (device_id.equals("")) {
					continue;
				}

				int totalCnt = 0;
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				Map<String, Object> result = apiReturnService.getReturnRuleInfoList(device);
				
				Map<String, Object> ruleInfo = new HashMap<String, Object>();
				
				ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
				ruleInfo.put("library_key", library_key);
				ruleInfo.put("device_key", device_key);
				
				if(result.get("Type").equals("0")) {
					Map<String, Object> ruleMap = (Map<String, Object>) result.get("config");

					for (String key : ruleMap.keySet()) {
						if (key != null) {
							ruleInfo.put("rule_name", key);
							ruleInfo.put("rule_value", ruleMap.get(key));
							ruleInfo.put("editable", 'Y');

							Map<String, Object> ruleInfoMap = apiReturnService.getReturnRuleInfo(null, ruleInfo);

							if (ruleInfoMap != null) {
								ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));

								apiReturnService.updateReturnRuleInfo(null, ruleInfo);
								updateCnt++;
							} else {
								apiReturnService.insertReturnRuleInfo(null, ruleInfo);
								insertCnt++;
							}
						} else {
							failCnt++;
						}
						totalCnt++;
					}
				} else if(result.get("Type").equals("1")) {
					ArrayList<Map<String, Object>> ruleMap = (ArrayList<Map<String, Object>>) result.get("config2");
					
					for(Map<String, Object> rule : ruleMap) {
						if(rule != null) {
							Map<String, Object> option = (Map<String, Object>) rule.get("option");
							String fileName = rule.get("FileName").toString();
							String ruleName = rule.get("ConfigName").toString() + "_" + option.get("OPTION_NAME").toString();
							String editable = option.get("editable").toString();
							String ruleValue = null;
							
							if(option.get("OPTION_VALUE") != null) {
								ruleValue = option.get("OPTION_VALUE").toString();
							}
							
							ruleInfo.put("file_name", fileName);
							ruleInfo.put("rule_name", ruleName);
							ruleInfo.put("rule_value", ruleValue);
							ruleInfo.put("editable", editable);
							
							Map<String, Object> ruleInfoMap = apiReturnService.getReturnRuleInfo(null, ruleInfo);

							if (ruleInfoMap != null) {
								ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));

								apiReturnService.updateReturnRuleInfo(null, ruleInfo);
								updateCnt++;
							} else {
								apiReturnService.insertReturnRuleInfo(null, ruleInfo);
								insertCnt++;
							}
						} else {
							failCnt++;
						}
						totalCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + totalCnt);
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncRuleInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncRuleInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	private void sendRuleInfo() {
		logger.info(">>> sendRuleInfo Start - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			String server_ip = InetAddress.getLocalHost().getHostAddress();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> sendRuleInfo End - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				
				if (device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				String db_url = device.get("db_url").toString();

				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiReturnService
						.getReturnRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 반납기 설정 파일에 INSERT
				if (ruleInfoList.size() == 0) {
					continue;
				}

				for (Map<String, Object> ruleInfo : ruleInfoList) {
					ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));
					ruleInfo.put("db_url", db_url);

					Map<String, Object> resultMap = (Map<String, Object>) apiReturnService
							.updateReturnRuleInfo(ruleInfo);

					if (ParamUtil.parseString(resultMap.get("Success")).equals("true")) {
						ruleInfo.put("send_req_yn", "S");
						apiReturnService.updateReturnRuleSendResult(null, ruleInfo);
					}
				}
			}

			logger.info(">>> sendRuleInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> sendRuleInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}

	}
}
