package com.enicom.nims.schedule;

import static java.lang.Math.min;

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

import com.enicom.nims.api.service.ApiGateService;
import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.utils.ParamUtil;
import com.enicom.nims.ws.WebSocketUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GateScheduler extends Scheduler {
	private static Logger logger = LoggerFactory.getLogger(GateScheduler.class);
	
	private ApiGateService apiGateService;
	private WebSocketUtil wsUtil;
	private static String MODEL_KEY = "2";

	@Autowired
	public GateScheduler(DeviceService deviceService, ApiGateService apiGateService, WebSocketUtil wsUtil) {
		super(deviceService, MODEL_KEY);
		this.apiGateService = apiGateService;
		this.wsUtil = wsUtil;
	}

	@Override
	public void scheduleInit() throws Exception {
		logger.info("GateSchedulerInit is Running >> {}", LocalDateTime.now());

		syncUseLogInfo();
		Thread.sleep(1000);

		syncUserInfo();
		Thread.sleep(1000);

		syncCompanyCodeInfo();
		Thread.sleep(1000);

		syncDeptCodeInfo();
		Thread.sleep(1000);

		syncMajorCodeInfo();
		Thread.sleep(1000);

		syncPositionCodeInfo();
		Thread.sleep(1000);

		syncRuleInfo();
		Thread.sleep(1000);

		logger.info("GateSchedulerInit is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleDay() throws Exception {
		logger.info("GateSchedulerDay is Running >> {}", LocalDateTime.now());

		syncUserInfo();

		logger.info("GateSchedulerDay is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleHour() throws Exception {
		logger.info("GateSchedulerHour is Running >> {}", LocalDateTime.now());

		syncCompanyCodeInfo();
		Thread.sleep(1000);

		syncDeptCodeInfo();
		Thread.sleep(1000);

		syncMajorCodeInfo();
		Thread.sleep(1000);

		syncPositionCodeInfo();
		Thread.sleep(1000);

		syncRuleInfo();
		Thread.sleep(1000);

		logger.info("GateSchedulerHour is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteTen() throws Exception {
		logger.info("GateScheduler10Minute is Running >> {}", LocalDateTime.now());

		syncUseLogInfo();
		Thread.sleep(1000);

		syncCompanyCodeInfo();
		Thread.sleep(1000);

		syncDeptCodeInfo();
		Thread.sleep(1000);

		syncMajorCodeInfo();
		Thread.sleep(1000);

		syncPositionCodeInfo();
		Thread.sleep(1000);

		logger.info("GateScheduler10Minute is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteOne() throws Exception {
		logger.info("GateScheduler1Minute is Running >> {}", LocalDateTime.now());

		sendRuleInfo();
		Thread.sleep(1000);
		
		checkSchedule();

		logger.info("GateScheduler1Minute is Ended >> {}", LocalDateTime.now());
	}
	
	/**
	 * [출입 게이트] 출입 정보 연계
	 */
	private void syncUseLogInfo() {
		logger.info(">>> syncUseLogInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUseLogInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> paramMap = new HashMap<String, Object>();
				Map<String, Object> logParamMap = new HashMap<String, Object>();

				logParamMap.put("library_key", library_key);

				List<Map<String, Object>> useLogInfoList = new ArrayList<Map<String, Object>>();

				// NIMS 테이블에 있는 Gate Log 데이터 유무 확인하여 최신 데이터 날짜 확인
				@SuppressWarnings("unchecked")
				Map<String, Object> logResult = (Map<String, Object>) ((Map<String, Object>) apiGateService.getGateLatestLogDate(null, logParamMap)
						.get("result")).get("info");

				// 데이터가 있을 경우 INSERT/UPDATE 진행
				if (logResult != null) {
					paramMap.put("latest_date", logResult.get("latest_date"));

					Map<String, Object> result = apiGateService.getGateUseLogInfoList(paramMap);
					useLogInfoList = (List<Map<String, Object>>) result.get("data");

					int insertCnt = 0;
					int updateCnt = 0;
					int failCnt = 0;
					if (useLogInfoList != null) {
						for (Map<String, Object> useLogInfo : useLogInfoList) {
							if (useLogInfo != null) {
								useLogInfo.put("library_key", library_key);

								Map<String, Object> useLogInfoMap = apiGateService.getGateUseLogInfo(null, useLogInfo);

								if(useLogInfoMap != null) {
									useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
									apiGateService.updateGateUseLogInfo(null, useLogInfo);
									updateCnt++;
								} else {
									apiGateService.insertGateUseLogInfo(null, useLogInfo);
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
				// 데이터가 없을 경우 insertBatch 진행
				else {
					Map<String, Object> result = apiGateService.getGateUseLogInfoList(paramMap);
					useLogInfoList = (List<Map<String, Object>>) result.get("data");

					Map<String, Object> insertMap = new HashMap<String, Object>();
					insertMap.put("library_key", library_key);

					// 1000개씩 나눠서 Insert 진행
					int limit = 1000;

					for(int i = 0; i < useLogInfoList.size(); i += limit) {
						insertMap.put("infoList", new ArrayList<Map<String, Object>>(useLogInfoList.subList(i, min(i + limit, useLogInfoList.size()))));

						apiGateService.insertBatchGateUseLogInfo(null, insertMap);
					}
				}
			}
			
			logger.info(">>> syncUseLogInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncUseLogInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	/**
	 * [출입게이트] 사용자 정보 연계
	 */
	private void syncUserInfo() {
		logger.info(">>> syncUserInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUserInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateUserInfoList(null);
				List<Map<String, Object>> userInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (userInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> userInfo : userInfoList) {
					if (userInfo != null) {
						userInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						userInfo.put("library_key", library_key);

						Map<String, Object> userInfoMap = apiGateService.getGateUserInfo(null, userInfo);

						if(userInfoMap != null) {
							userInfo.put("rec_key", userInfoMap.get("rec_key"));
							apiGateService.updateGateUserInfo(null, userInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateUserInfo(null, userInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + userInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncUserInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncUserInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	/**
	 * [출입게이트] 소속 코드 정보 연계
	 */
	private void syncCompanyCodeInfo() {
		logger.info(">>> syncCompanyCodeInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUseLogInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateCompanyCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (codeInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGateCompanyCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGateCompanyCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateCompanyCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncCompanyCodeInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncCompanyCodeInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	/**
	 * [출입게이트] 부서 코드 정보 연계
	 */
	private void syncDeptCodeInfo() {
		logger.info(">>> syncDeptCodeInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUseLogInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateDeptCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (codeInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGateDeptCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGateDeptCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateDeptCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncDeptCodeInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncDeptCodeInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	/**
	 * [출입게이트] 전공 코드 정보 연계
	 */
	private void syncMajorCodeInfo() {
		logger.info(">>> syncMajorCodeInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncMajorCodeInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateMajorCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (codeInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGateMajorCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGateMajorCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateMajorCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncMajorCodeInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncMajorCodeInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	/**
	 * [출입게이트] 신분 코드 정보 연계
	 */
	private void syncPositionCodeInfo() {
		logger.info(">>> syncPositionCodeInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncPositionCodeInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGatePositionCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				if (codeInfoList == null) {
					continue;
				}
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGatePositionCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGatePositionCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGatePositionCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncPositionCodeInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncPositionCodeInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}

	/**
	 * [출입게이트] 운영 정책 정보 연계
	 */
	private void syncRuleInfo() {
		logger.info(">>> syncRuleInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncRuleInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateRuleInfoList();

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				if (ruleInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> ruleInfo : ruleInfoList) {
					if (ruleInfo != null) {
						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);

						Map<String, Object> ruleInfoMap = apiGateService.getGateRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiGateService.updateGateRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateRuleInfo(null, ruleInfo);
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
			
			logger.info(">>> syncRuleInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> syncRuleInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}
	
	/**
	 * [출입게이트] 운영 정책 동기화
	 */
	private void sendRuleInfo() {
		logger.info(">>> sendRuleInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			String server_ip = InetAddress.getLocalHost().getHostAddress();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> sendRuleInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiGateService
						.getGateRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 INSERT
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiGateService
								.updateGateRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiGateService.updateGateRuleSendResult(null, ruleInfo);
						}
					}
				}
			}
			
			logger.info(">>> sendRuleInfo End - {}", LocalDateTime.now());
		} catch (Exception e) {
			logger.error(">>> sendRuleInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}
	
	/**
	 * [출입게이트] 스케줄 목록 체크
	 */
	private void checkSchedule() {
		logger.info(">>> checkSchedule Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();
			
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncRuleInfo End - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				// 현재 등록된 스케줄 목록 조회
				List<Map<String, Object>> scheduleList = (List<Map<String, Object>>)((Map<String, Object>)apiGateService.getGateScheduleList(null, null).get("result")).get("items");
				
				if (scheduleList == null) {
					continue;
				}
				
				for (Map<String, Object> scheduleInfo : scheduleList) {
					if (scheduleInfo != null) {
						// 요일 비교
						LocalDateTime now = LocalDateTime.now();
						int week = now.getDayOfWeek().getValue();
						
						// 일요일인 경우 0으로 변경
						if(week == 7) {
							week = 0;
						}
						
						String date = scheduleInfo.get("date").toString();
						
						// 스케줄러에 등록한 요일 비교
						if(date.equals(String.valueOf(week)) || date.equals("ALL")) {
							// 시간 비교
							int nowTime = Integer.parseInt(String.format("%s%s", now.getHour(), now.getMinute()));
							int time = ParamUtil.parseInt(scheduleInfo.get("time"));
							
							if(nowTime == time) {
								ObjectMapper objectMapper = new ObjectMapper();
								Map<String, Object> paramMap = new HashMap<String, Object>();
								paramMap.put("EQUIP_ID", scheduleInfo.get("equip_id"));
								paramMap.put("IP", scheduleInfo.get("equip_ip"));
								paramMap.put("OPERATION_TYPE", scheduleInfo.get("operation_type"));
								
								wsUtil.sendToDashboard("monitoring", objectMapper.writeValueAsString(paramMap));
							}
						} 
					}
				}
			}
			
			logger.info(">>> checkSchedule End - {}", LocalDateTime.now());
		}
		catch (Exception e) {
			logger.error(">>> checkSchedule End - {}", LocalDateTime.now());
		}
	}
}
