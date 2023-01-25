package com.enicom.nims.schedule;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.api.service.ApiResvLoanService;
import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.utils.ParamUtil;

@Service
public class ResvLoanScheduler extends Scheduler {
	private Logger logger = LoggerFactory.getLogger(ResvLoanScheduler.class);
	private static String modelKey = "8";
	
	private ApiResvLoanService apiResvLoanService;
	
	
	@Autowired
	public ResvLoanScheduler(ApiResvLoanService apiResvLoanService, DeviceService deviceService) {
		super(deviceService, modelKey);
		this.apiResvLoanService = apiResvLoanService;
	}
	

	@Override
	public void scheduleInit() throws Exception {
		logger.info("ResvLoanSchedulerInit is Running >> {}", LocalDateTime.now());
		
		syncUseLogInfo(0);
		Thread.sleep(1000);
		
		syncCabinetModuleInfo();
		Thread.sleep(1000);
		
		syncCabinetInfo();
		Thread.sleep(1000);
		
		syncModuleInfo();
		Thread.sleep(1000);
		
		syncRuleInfo();
		
		logger.info("ResvLoanSchedulerInit is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleDay() throws Exception {
		logger.info("ResvLoanSchedulerDay is Running >> {}", LocalDateTime.now());
		
		syncCabinetModuleInfo();
		Thread.sleep(1000);
		
		syncCabinetInfo();
		
		logger.info("ResvLoanSchedulerDay is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleHour() throws Exception {
		logger.info("ResvLoanSchedulerHour is Running >> {}", LocalDateTime.now());
		
		syncCabinetModuleInfo();
		Thread.sleep(1000);
		
		syncCabinetInfo();
		Thread.sleep(1000);
		
		syncModuleInfo();
		Thread.sleep(1000);
		
		syncRuleInfo();
		
		logger.info("ResvLoanSchedulerHour is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteTen() throws Exception {
		logger.info("ResvLoanScheduler10Min is Running >> {}", LocalDateTime.now());
		
		syncUseLogInfo(0);
		Thread.sleep(1000);
		
		syncCabinetModuleInfo();
		Thread.sleep(1000);
		
		syncCabinetInfo();
		Thread.sleep(1000);
		
		syncModuleInfo();
		
		logger.info("ResvLoanScheduler10Min is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteOne() throws Exception {
		logger.info("ResvLoanScheduler1Min is Running >> {}", LocalDateTime.now());
		
		sendModuleInfo("");
		Thread.sleep(1000);
		
		sendCabinetModuleInfo("");
		Thread.sleep(1000);
		
		sendCabinetInfo("");
		Thread.sleep(1000);
		
		sendRuleInfo("");
		
		logger.info("ResvLoanScheduler1Min is Ended >> {}", LocalDateTime.now());
	}

	public void syncUseLogInfo(int interval) {
		logger.info(">>> syncUseLogInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUseLogInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> paramMap = new HashMap<String, Object>();
				if (interval > 0) {
					paramMap.put("interval", interval);
				}

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> useLogInfoList = apiResvLoanService.getResvLoanUseLogInfoList(paramMap);
				if (useLogInfoList == null) {
					continue;
				}
				for (Map<String, Object> useLogInfo: useLogInfoList) {
					if (useLogInfo != null) {
						useLogInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						
						useLogInfo.put("library_key", library_key);
						useLogInfo.put("device_key", device_key);

						Map<String, Object> useLogInfoMap = apiResvLoanService.getResvLoanUseLogInfo(null,
								useLogInfo);

						if (useLogInfoMap != null) {
							useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanUseLogInfo(null, useLogInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanUseLogInfo(null, useLogInfo);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void syncCabinetModuleInfo() {
		logger.info(">>> syncCabinetModuleInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncCabinetModuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> cabinetModuleInfoList = apiResvLoanService.getResvLoanCabinetModuleInfoList(device);
				if (cabinetModuleInfoList == null) {
					continue;
				}
				for (Map<String, Object> cabinetModuleInfo: cabinetModuleInfoList) {
					if (cabinetModuleInfo != null) {
						cabinetModuleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						cabinetModuleInfo.put("library_key", library_key);
						cabinetModuleInfo.put("device_key", device_key);

						Map<String, Object> cabinetModuleInfoMap = apiResvLoanService.getResvLoanCabinetModuleInfo(null, cabinetModuleInfo);

						if (cabinetModuleInfoMap != null) {
							cabinetModuleInfo.put("rec_key", cabinetModuleInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanCabinetModuleInfo(null, cabinetModuleInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanCabinetModuleInfo(null, cabinetModuleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + cabinetModuleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncCabinetModuleInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void syncCabinetInfo() {
		logger.info(">>> syncCabinetInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncCabinetInfo End!! - {}", LocalDateTime.now());
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
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> cabinetInfoList = apiResvLoanService.getResvLoanCabinetInfoList(device);
				if (cabinetInfoList == null) {
					continue;
				}
				for (Map<String, Object> cabinetInfo : cabinetInfoList) {
					if (cabinetInfo != null) {
						cabinetInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						cabinetInfo.put("library_key", library_key);
						cabinetInfo.put("device_key", device_key);

						Map<String, Object> cabinetInfoMap = apiResvLoanService.getResvLoanCabinetInfo(null, cabinetInfo);

						if (cabinetInfoMap != null) {
							cabinetInfo.put("rec_key", cabinetInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanCabinetInfo(null, cabinetInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanCabinetInfo(null, cabinetInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + cabinetInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncCabinetInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void syncModuleInfo() {
		logger.info(">>> syncModeulInfo Start !! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncModeulInfo End!! - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> moduleInfoList = apiResvLoanService.getResvLoanModuleInfoList(device);
				if (moduleInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> moduleInfo : moduleInfoList) {
					if (moduleInfo != null) {
						moduleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						moduleInfo.put("library_key", library_key);
						moduleInfo.put("device_key", device_key);

						Map<String, Object> moduleInfoMap = apiResvLoanService.getResvLoanModuleInfo(null, moduleInfo);

						if (moduleInfoMap != null) {
							moduleInfo.put("rec_key", moduleInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanModuleInfo(null, moduleInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanModuleInfo(null, moduleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + moduleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> syncModeulInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void syncRuleInfo() {
		logger.info(">>> syncRuleInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> ruleInfoList = apiResvLoanService.getResvLoanRuleInfoList(device);
				if (ruleInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> ruleInfo : ruleInfoList) {
					if (ruleInfo != null) {
						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiResvLoanService.getResvLoanRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanRuleInfo(null, ruleInfo);
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
	
	public void sendModuleInfo(String device_key) {
		logger.info(">>> sendModuleInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> sendModuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("send_req_yn", "Y");

				List<Map<String, Object>> moduleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService.getResvLoanModuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 INSERT
				if (moduleInfoList.size() > 0) {
					for (Map<String, Object> moduleInfo : moduleInfoList) {
						moduleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiResvLoanService.insertResvLoanModuleInfoGallery(null, moduleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							moduleInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanModuleSendResult(null, moduleInfo);
						}
					}
				}
			}
			
			logger.info(">>> sendModuleInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendCabinetModuleInfo(String device_key) {
		logger.info(">>> sendCabinetModuleInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> sendCabinetModuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());


				device.put("device_key", rec_key);
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> cabinetModuleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService
						.getResvLoanCabinetModuleInfoList(null, device).get("result"))).get("items");

				if (cabinetModuleInfoList.size() > 0) {
					for (Map<String, Object> cabinetModuleInfo : cabinetModuleInfoList) {

						Map<String, Object> resultMap =	resultMap = (Map<String, Object>) apiResvLoanService.insertResvLoanCabinetModuleInfoGallery(null, cabinetModuleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							cabinetModuleInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanCabinetModuleSendResult(null, cabinetModuleInfo);
						}
					}
				}
			}
			logger.info(">>> sendCabinetModuleInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendCabinetInfo(String device_key) {
		logger.info(">>> sendCabinetInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> sendCabinetInfo End!! - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("device_key", rec_key);
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> cabinetInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService
						.getResvLoanCabinetInfoList(null, device).get("result"))).get("items");

				if (cabinetInfoList.size() > 0) {
					for (Map<String, Object> cabinetInfo : cabinetInfoList) {
						Map<String, Object> cabinetInfoMap = apiResvLoanService.getResvLoanCabinetInfo(cabinetInfo);
						Map<String, Object> resultMap = new HashMap<String, Object>();

						if (cabinetInfoMap != null) {
							resultMap = (Map<String, Object>) apiResvLoanService.updateResvLoanCabinetInfo(cabinetInfo).get("result");
						} else {
							resultMap = (Map<String, Object>) apiResvLoanService.insertResvLoanCabinetInfoGallery(null, cabinetInfo).get("result");
						}

						if(resultMap.get("CODE").toString().equals("200")) {
							cabinetInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanCabinetSendResult(null, cabinetInfo);
						}
					}
				}
			}
			
			logger.info(">>> sendCabinetInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendRuleInfo(String device_key) {
		logger.info(">>> sendRuleInfo Start!! - {}", LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", modelKey);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> sendRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());


				device.put("device_key", rec_key);
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService
						.getResvLoanRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 UPDATE
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						Map<String, Object> resultMap = (Map<String, Object>) apiResvLoanService.updateResvLoanRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanRuleSendResult(null, ruleInfo);
						}
					}
				}
			}
			
			logger.info(">>> sendRuleInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
