package com.enicom.nims.schedule;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.api.service.ApiLoanReturnService;
import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.utils.ParamUtil;

@Service
public class LoanReturnScheduler extends Scheduler {
	private static Logger logger = LoggerFactory.getLogger(LoanReturnScheduler.class);
	private static String MODEL_KEY = "1,9,10";

	private ApiLoanReturnService apiLoanReturnService;

	@Autowired
	public LoanReturnScheduler(ApiLoanReturnService apiLoanReturnService, DeviceService deviceService) {
		super(deviceService, MODEL_KEY);
		this.apiLoanReturnService = apiLoanReturnService;
	}

	@Override
	public void scheduleInit() throws Exception {
		logger.info("LoanReturnSchedulerInit is Running >> {}", LocalDateTime.now());

		syncUseLogInfo();
		Thread.sleep(1000);

		syncRuleInfo();
		Thread.sleep(1000);

		syncHolidayInfo();
		Thread.sleep(1000);

		syncEquipRuleInfo();
		Thread.sleep(1000);

		syncBannerSettinfInfo();

		logger.info("LoanReturnSchedulerInit is End >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleDay() throws Exception {
		logger.info("LoanReturnSchedulerDay is Running >> {}", LocalDateTime.now());

		syncHolidayInfo();

		logger.info("LoanReturnSchedulerDay is End >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleHour() throws Exception {
		logger.info("LoanReturnSchedulerHour is Running >> {}", LocalDateTime.now());

		syncRuleInfo();
		Thread.sleep(1000);

		syncHolidayInfo();

		logger.info("LoanReturnSchedulerHour is End >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteTen() throws Exception {
		logger.info("LoanReturnScheduler10Minute is Running >> {}", LocalDateTime.now());

		syncUseLogInfo();
		Thread.sleep(1000);

		syncHolidayInfo();
		Thread.sleep(1000);

		syncRuleInfo();
		Thread.sleep(1000);

		syncBannerSettinfInfo();

		logger.info("LoanReturnScheduler10Minute is End >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteOne() throws Exception {
		logger.info("LoanReturnScheduler1Minute is Running >> {}", LocalDateTime.now());

		sendRuleInfo();
		Thread.sleep(1000);

		sendHolidayInfo();
		Thread.sleep(1000);

		sendEquipRuleInfo();
		Thread.sleep(1000);

		sendBannerSettingInfo();

		logger.info("LoanReturnScheduler1Minute is End >> {}", LocalDateTime.now());
	}

	public void syncUseLogInfo() {
		logger.info(">>> syncUseLogInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUseLogInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> keyMap = new HashMap<String, Object>();

				keyMap.put("library_key", library_key);
				keyMap.put("device_key", device_key);

				Map<String, Object> lastKeyResult = apiLoanReturnService.getLoanReturnLastLogKey(null, keyMap);
				Map<String, Object> logMap = ((Map<String, Object>) ((Map<String, Object>) lastKeyResult.get("result")).get("info"));

				Map<String, Object> paramMap = new HashMap<String, Object>();

				if(logMap != null) {
					paramMap.put("log_key", logMap.get("log_key"));
				}

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> useLogInfoList = apiLoanReturnService.getLoanReturnUseLogInfoList(paramMap);
				if (useLogInfoList == null) {
					continue;
				}

				for (Map<String, Object> useLogInfo : useLogInfoList) {
					if (useLogInfo != null) {

						useLogInfo.put("library_key", library_key);
						useLogInfo.put("device_key", device_key);

						Map<String, Object> useLogInfoMap = apiLoanReturnService.getLoanReturnUseLogInfo(null, useLogInfo);

						if(useLogInfoMap != null) {
							useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
							apiLoanReturnService.updateLoanReturnUseLogInfo(null, useLogInfo);
							updateCnt++;
						} else {
							apiLoanReturnService.insertLoanReturnUseLogInfo(null, useLogInfo);
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

			logger.info(">>> syncUseLogInfo End!! - {}", LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncRuleInfo() {
		logger.info(">>> syncRuleInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				String model_key = ParamUtil.parseString(device.get("model_key"));

				Map<String, Object> ruleMap = new HashMap<String, Object>();

				ruleMap.put("model_key", model_key);

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> ruleInfoList = apiLoanReturnService.getLoanReturnRuleInfoList(ruleMap);
				if (ruleInfoList == null) {
					continue;
				}
				for (Map<String, Object> ruleInfo : ruleInfoList) {
					if (ruleInfo != null) {
						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiLoanReturnService.getLoanReturnRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiLoanReturnService.updateLoanReturnRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiLoanReturnService.insertLoanReturnRuleInfo(null, ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + ruleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncRuleInfo End!! - {}", LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncHolidayInfo() {
		logger.info(">>> syncHolidayInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncHolidayInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int successCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> holidayList = apiLoanReturnService.getLoanReturnHolidayList(Collections.emptyMap());
				if (holidayList == null) {
					continue;
				}

				for(Map<String, Object> holidayInfo: holidayList) {
					if(holidayInfo != null) {
						holidayInfo.put("library_key", library_key);
						holidayInfo.put("device_key", device_key);

						apiLoanReturnService.upsertLoanReturnHolidayInfo(null, holidayInfo);
						successCnt++;
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + holidayList.size());
				logger.info("SUCCESS >>> " + successCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncHolidayInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncEquipRuleInfo() {
		logger.info(">>> syncEquipRuleInfo Start!! - {}", LocalDateTime.now());

		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();

			String modelKeys = "1";
			int[] modelKeyArry = Arrays.stream(modelKeys.split(",")).mapToInt(Integer::parseInt).toArray();
			deviceParams.put("model_key_array", modelKeyArry);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncEquipRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int successCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> ruleList = apiLoanReturnService.getLoanReturnEquipRuleInfoList(Collections.emptyMap());
				if (ruleList == null) {
					continue;
				}

				for(Map<String, Object> ruleInfo: ruleList) {
					if(ruleInfo != null) {
						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						apiLoanReturnService.upsertLoanReturnEquipRuleInfo(null, ruleInfo);
						successCnt++;
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + ruleList.size());
				logger.info("SUCCESS >>> " + successCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncEquipRuleInfo End!! - {}", LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncBannerSettinfInfo() {
		logger.info(">>> syncBannerSettingInfo Start!! - {}", LocalDateTime.now());

		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();

			String modelKeys = "1,10";
			int[] modelKeyArry = Arrays.stream(modelKeys.split(",")).mapToInt(Integer::parseInt).toArray();
			deviceParams.put("model_key_array", modelKeyArry);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncBannerSettingInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int successCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> ruleList = apiLoanReturnService.getLoanReturnBannerSettingInfo(Collections.emptyMap());
				if (ruleList == null) {
					continue;
				}
				for(Map<String, Object> ruleInfo: ruleList) {
					if(ruleInfo != null) {
						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						apiLoanReturnService.upsertLoanReturnBannerSettingInfo(null, ruleInfo);
						successCnt++;
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + ruleList.size());
				logger.info("SUCCESS >>> " + successCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncBannerSettingInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendRuleInfo() {
		logger.info(">>> sendRuleInfo Start!! - {}", LocalDateTime.now());

		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> sendRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService
						.getLoanReturnRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 대출반납기DB에 INSERT
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						ruleInfo.put("model_key", device.get("model_key"));
						ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiLoanReturnService
								.updateLoanReturnRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiLoanReturnService.updateLoanReturnRuleSendResult(null, ruleInfo);
						}
					}
				}
			}
			logger.info(">>> sendRuleInfo End!! - {}", LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendHolidayInfo() {
		logger.info(">>> sendHolidayInfo Start!! - {}", LocalDateTime.now());

		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = getDeviceConnList();
			if (deviceList.size() == 0) {
				logger.info(">>> sendHolidayInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				if (device_id.equals("")) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("device_key", device.get("rec_key"));
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> holidayInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService.getLoanReturnHolidayInfoList(null, device)).get("result")).get("items");

				// 대상이 있으면 대출반납기DB에 INSERT
				if (holidayInfoList.size() == 0) {
					continue;
				}
				for (Map<String, Object> holidayInfo : holidayInfoList) {
					holidayInfo.put("worker", String.format("%s@%s", "nims", server_ip));

					Map<String, Object> resultMap = (Map<String, Object>) apiLoanReturnService.upsertLoanReturnHolidayInfo(holidayInfo).get("result");

					if(resultMap.get("CODE").toString().equals("200")) {
						holidayInfo.put("send_req_yn", "S");
						apiLoanReturnService.updateLoanReturnHolidaySendResult(null, holidayInfo);
					}
				}
			}

			logger.info(">>> sendHolidayInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendEquipRuleInfo() {
		logger.info(">>> sendEquipRuleInfo Start!! - {}", LocalDateTime.now());

		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = getDeviceConnList();
			if (deviceList.size() == 0) {
				logger.info(">>> sendEquipRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));

				if (device_id.equals("")) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("device_key", device.get("rec_key"));
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService
						.getLoanReturnEquipRuleInfoList(null, device)).get("result")).get("items");

				// 대상이 있으면 대출반납기DB에 INSERT
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiLoanReturnService
								.updateLoanReturnEquipRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiLoanReturnService.updateLoanReturnEquipRuleSendResult(null, ruleInfo);
						}
					}
				}
			}

			logger.info(">>> sendEquipRuleInfo End!! - {}", LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendBannerSettingInfo() {
		logger.info(">>> sendBannerSettingInfo Start!! - {}", LocalDateTime.now());

		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> sendBannerSettingInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("device_key", device.get("rec_key"));
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> settingInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService.getLoanReturnBannerSettingInfo(null, device)).get("result")).get("items");

				// 대상이 있으면 대출반납기DB에 UPDATE
				if (settingInfoList.size() > 0) {
					for(Map<String, Object> settingInfo : settingInfoList) {
						settingInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						int result = apiLoanReturnService.updateLoanReturnBannerSettingInfo(settingInfo);
						if(result > 0) {
							settingInfo.put("send_req_yn", "S");
							apiLoanReturnService.updateLoanReturnBannerSettingSendResult(null, settingInfo);
						}
					}
				}
			}

			logger.info(">>> sendBannerSettingInfo End!! - {}", LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
