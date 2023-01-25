package com.enicom.nims.schedule;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.api.service.ApiSLSService;
import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.utils.ParamUtil;

@Service
public class SLSScheduler extends Scheduler {
	private static Logger logger = LoggerFactory.getLogger(SLSScheduler.class);

	private ApiSLSService apiSlsService;
	private static String MODEL_KEY = "6";

	@Autowired
	public SLSScheduler(ApiSLSService apiSlsService, DeviceService deviceService) {
		super(deviceService, MODEL_KEY);
		this.apiSlsService = apiSlsService;
	}

	@Override
	public void scheduleInit() throws Exception {
		logger.info("SLSSchedulerInit is Running >> {}", LocalDateTime.now());

		syncBookInfo("");
		Thread.sleep(1000);

		syncCasInfo();
		Thread.sleep(1000);

		syncRuleInfo();
		Thread.sleep(1000);

		syncDrumRuleInfo();
		Thread.sleep(1000);

		syncUseLogInfo(0);
		Thread.sleep(1000);

		syncReservationInfo(0);
		Thread.sleep(1000);

		syncReturnInfo(0);
		Thread.sleep(1000);

		syncBookOutInfo();

		logger.info("SLSSchedulerInit is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleDay() throws Exception {
		logger.info("SLSSchedulerDay is Running >> {}", LocalDateTime.now());

		syncBookInfo("");

		logger.info("SLSSchedulerDay is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleHour() throws Exception {
		logger.info("SLSSchedulerHour is Running >> {}", LocalDateTime.now());

		syncCasInfo();
		Thread.sleep(1000);

		syncRuleInfo();
		Thread.sleep(1000);

		syncDrumRuleInfo();

		logger.info("SLSSchedulerHour is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteTen() throws Exception {
		logger.info("SLSScheduler10Min is Running >> {}", LocalDateTime.now());

		syncUseLogInfo(0);
		Thread.sleep(1000);

		syncReservationInfo(0);
		Thread.sleep(1000);

		syncReturnInfo(0);

		logger.info("SLSScheduler10Min is Ended >> {}", LocalDateTime.now());
	}

	@Override
	public void scheduleMinuteOne() throws Exception {
		logger.info("SLSScheduler1Min is Running >> {}", LocalDateTime.now());

		sendRuleInfo("");
		Thread.sleep(1000);

		sendDrumRuleInfo("");
		Thread.sleep(1000);

		sendCasInfo("");
		Thread.sleep(1000);

		syncReservationInfo(0);
		Thread.sleep(1000);

		syncBookOutInfo();
		Thread.sleep(1000);

		syncBookInfo("");

		logger.info("SLSScheduler1Min is Ended >> {}", LocalDateTime.now());
	}

	public void syncBookInfo(String paramDeviceId) {
		logger.info(">>> syncBookInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncBookInfo End!! - {}", LocalDateTime.now());
				return;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			String now = sdf.format(new Date());

			for (Map<String, Object> device : deviceList) {
				String deviceId = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				// device id가 없을 경우 pass
				if ("".equals(deviceId))
					continue;

				// parameter로 받은 device id가 빈값이 아니고, 해당 id가 아닐때 pass
				if (!"".equals(paramDeviceId) && !paramDeviceId.equals(deviceId))
					continue;

				device.put("now", now);

				// Device 정보에 따른 NIMS DB 도서 목록
				@SuppressWarnings("unchecked")
				Map<String, Object> nimsResult = (Map<String, Object>) apiSlsService.getSLSBookList(null, device)
						.get("result");
				List<Map<String, Object>> nimsBookList = (List<Map<String, Object>>) nimsResult.get("items");
				if (nimsBookList == null) {
					logger.warn("NIMS의 도서 목록을 받아올수 없습니다");
					continue;
				}

				// Device 정보에 따른 SmartDB 연결
				DBContextHolder.setDBType(deviceId);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				// 해당 장비의 도서 목록(SmartDB)
				List<Map<String, Object>> deviceBookList = apiSlsService.getSLSBookInfoList();
				if (deviceBookList == null || deviceBookList.isEmpty()) {
					logger.warn("해당 장비에 따른 도서목록이 없습니다.");
					continue;
				}

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				for (Map<String, Object> book : deviceBookList) {
					if (book == null) {
						continue;
					}
					String book_id = ParamUtil.parseString(book.get("id"));

					book.put("worker", String.format("%s@%s", "schedule", "localhost"));
					book.put("library_key", library_key);
					book.put("device_key", device_key);
					book.put("book_status", "1");
					book.put("now", now);

					List<Map<String, Object>> candidates = nimsBookList.stream()
							.filter(nimsBook -> nimsBook.get("id").toString().equals(book_id))
							.collect(Collectors.toList());

					if (!candidates.isEmpty()) {
						book.put("rec_key", candidates.get(0).get("rec_key"));
						apiSlsService.updateSLSBookInfo(null, book);
						updateCnt++;
					} else {
						apiSlsService.insertSLSBookInfo(null, book);
						insertCnt++;
					}
				}

				// 동기화 되지 않은 도서 배출 처리
				apiSlsService.syncSLSBookStatus(null, device);

				logger.info("TOTAL >>> " + deviceBookList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncBookInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncCasInfo() {
		logger.info(">>> syncCasInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncCasInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if (device_id.equals("")) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				List<Map<String, Object>> casInfoList = apiSlsService.getSLSCasInfoList();

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (casInfoList == null) {
					continue;
				}

				for (Map<String, Object> casInfo : casInfoList) {
					if (casInfo != null) {
						casInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						casInfo.put("library_key", library_key);
						casInfo.put("device_key", device_key);

						Map<String, Object> casInfoMap = apiSlsService.getSLSCasInfo(null, casInfo);

						if (casInfoMap != null) {
							casInfo.put("rec_key", casInfoMap.get("rec_key"));
							apiSlsService.updateSLSCasInfo(null, casInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSCasInfo(null, casInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}

				logger.info("TOTAL >>> " + casInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncCasInfo End!! - {}", LocalDateTime.now());
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

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if ("".equals(device_id)) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				List<Map<String, Object>> ruleInfoList = apiSlsService.getSLSRuleInfoList();

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
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiSlsService.getSLSRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiSlsService.updateSLSRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSRuleInfo(null, ruleInfo);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncDrumRuleInfo() {
		logger.info(">>> syncDrumRuleInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncDrumRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if (device_id.equals("")) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> drumRuleInfoList = apiSlsService.getSLSDrumRuleInfoList();
				if (drumRuleInfoList == null) {
					continue;
				}
				for (Map<String, Object> ruleInfo : drumRuleInfoList) {
					if (ruleInfo != null) {

						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiSlsService.getSLSDrumRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiSlsService.updateSLSDrumRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSDrumRuleInfo(null, ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}

				logger.info("TOTAL >>> " + drumRuleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncDrumRuleInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncUseLogInfo(int interval) {
		logger.info(">>> syncUseLogInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncUseLogInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if (!"".equals(device_id)) {
					DBContextHolder.setDBType(device_id);
					logger.info("DBtype > {}", DBContextHolder.getDBType());

					Map<String, Object> paramMap = new HashMap<String, Object>();
					if (interval > 0) {
						paramMap.put("interval", interval);
					}
					List<Map<String, Object>> useLogInfoList = apiSlsService.getSLSUseLogInfoList(paramMap);

					int insertCnt = 0;
					int updateCnt = 0;
					int failCnt = 0;
					if (useLogInfoList == null) {
						continue;
					}

					for (Map<String, Object> useLogInfo : useLogInfoList) {
						if (useLogInfo != null) {
							useLogInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

							useLogInfo.put("library_key", library_key);
							useLogInfo.put("device_key", device_key);

							Map<String, Object> useLogInfoMap = apiSlsService.getSLSUseLogInfo(null, useLogInfo);

							if (useLogInfoMap != null) {
								useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
								apiSlsService.updateSLSUseLogInfo(null, useLogInfo);
								updateCnt++;
							} else {
								apiSlsService.insertSLSUseLogInfo(null, useLogInfo);
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

			logger.info(">>> syncUseLogInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncReservationInfo(int interval) {
		logger.info(">>> syncReservationInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncReservationInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if ("".equals(device_id)) {
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

				List<Map<String, Object>> reservationInfoList = apiSlsService.getSLSReservationInfoList(paramMap);
				if (reservationInfoList == null) {
					continue;
				}

				for (Map<String, Object> reservationInfo : reservationInfoList) {
					if (reservationInfo != null) {
						reservationInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						reservationInfo.put("library_key", library_key);
						reservationInfo.put("device_key", device_key);

						Map<String, Object> casInfoMap = apiSlsService.getSLSReservationInfo(null, reservationInfo);

						if (casInfoMap != null) {
							reservationInfo.put("rec_key", casInfoMap.get("rec_key"));
							apiSlsService.updateSLSReservationInfo(null, reservationInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSReservationInfo(null, reservationInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + reservationInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncReservationInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncReturnInfo(int interval) {
		logger.info(">>> syncReturnInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncReturnInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if ("".equals(device_id)) {
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

				List<Map<String, Object>> returnInfoList = apiSlsService.getSLSReturnInfoList(paramMap);
				if (returnInfoList == null) {
					continue;
				}

				for (Map<String, Object> returnInfo : returnInfoList) {
					if (returnInfo != null) {
						String id = ParamUtil.parseString(returnInfo.get("id"));

						returnInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						returnInfo.put("library_key", library_key);
						returnInfo.put("device_key", device_key);

						Map<String, Object> returnInfoMap = apiSlsService.getSLSReturnInfo(null, returnInfo);

						if (returnInfoMap != null) {
							returnInfo.put("rec_key", returnInfoMap.get("rec_key"));
							apiSlsService.updateSLSReturnInfo(null, returnInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSReturnInfo(null, returnInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + returnInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncReturnInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void syncBookOutInfo() {
		logger.info(">>> syncBookOutInfo Start!! - {}", LocalDateTime.now());

		try {
			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncBookOutInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if ("".equals(device_id)) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int insertCnt = 0;
				int updateCnt = 0;
				int deleteCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> slsBookOutInfoList = apiSlsService.getSLSBookOutInfoList();
				if (slsBookOutInfoList == null) {
					continue;
				}

				for (Map<String, Object> slsBookOutInfo : slsBookOutInfoList) {
					if (slsBookOutInfo != null) {
						slsBookOutInfo.put("library_key", library_key);
						slsBookOutInfo.put("device_key", device_key);

						Map<String, Object> slsBookOutInfoMap = apiSlsService.getSLSBookOutInfo(null, slsBookOutInfo);

						if (slsBookOutInfoMap != null) {
							slsBookOutInfo.put("rec_key", slsBookOutInfoMap.get("rec_key"));

							apiSlsService.updateSLSBookOutInfo(null, slsBookOutInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSBookOutInfo(null, slsBookOutInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}

				logger.info("TOTAL >>> " + slsBookOutInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("DELETE >>> " + deleteCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> syncBookOutInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendRuleInfo(String paramDeviceKey) {
		logger.info(">>> sendRuleInfo Start!! - {}", LocalDateTime.now());

		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> sendRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));

				if ("".equals(device_id) || !rec_key.equals(paramDeviceKey)) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				// 각 장비에 해당되는 RULE 정보 조회
				device.put("send_req_yn", "Y");
				Map<String, Object> ruleResult = apiSlsService.getSLSRuleInfoList(device);

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (ruleResult
						.get("result"))).get("items");

				// 대상이 있으면 SmartDB UPDATE
				if (ruleInfoList.size() > 0) {
					DBContextHolder.setDBType(device_id);

					for (Map<String, Object> ruleInfoMap : ruleInfoList) {
						ruleInfoMap.put("worker", String.format("%s@%s", "nims", server_ip));
						int result = apiSlsService.updateSLSRuleInfo(ruleInfoMap);
						if (result > 0) {
							ruleInfoMap.put("send_req_yn", "S");
							apiSlsService.updateSLSRuleSendResult(null, ruleInfoMap);
						}
					}
				}
			}

			logger.info(">>> sendRuleInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendDrumRuleInfo(String paramDeviceKey) {
		logger.info(">>> sendDrumRuleInfo Start!! - {}", LocalDateTime.now());

		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty() || paramDeviceKey.equals("")) {
				logger.info(">>> sendDrumRuleInfo End!! - {}", LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("device_id"));

				if ("".equals(device_id) || !rec_key.equals(paramDeviceKey)) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				// 각 장비에 해당되는 RULE 정보 조회
				device.put("send_req_yn", "Y");

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiSlsService
						.getSLSDrumRuleInfoList(device).get("result"))).get("items");

				// 대상이 있으면 SmartDB UPDATE
				if (ruleInfoList.size() > 0) {
					DBContextHolder.setDBType(device.get("device_id").toString());

					for (Map<String, Object> ruleInfoMap : ruleInfoList) {
						ruleInfoMap.put("worker", String.format("%s@%s", "nims", server_ip));
						int result = apiSlsService.updateSLSDrumRuleInfo(ruleInfoMap);
						if (result > 0) {
							ruleInfoMap.put("send_req_yn", "S");
							apiSlsService.updateSLSDrumRuleSendResult(null, ruleInfoMap);
						}
					}
				}
			}

			logger.info(">>> sendDrumRuleInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendCasInfo(String paramDeviceKey) {
		logger.info(">>> sendCasInfo Start!! - {}", LocalDateTime.now());

		try {
			if (paramDeviceKey.equals("")) {
				logger.info(">>> sendCasInfo End - Device Key를 입력해주세요.");
				return;
			}

			List<Map<String, Object>> deviceList = getDeviceConnList();

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSCasInfoSend End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				if ("".equals(device_id) || !device_key.equals(paramDeviceKey)) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("send_req_yn", "Y");

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> casInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiSlsService
						.getSLSCasInfoList(null, device).get("result"))).get("items");

				if (casInfoList.size() == 0) {
					continue;
				}

				for (Map<String, Object> casInfoMap : casInfoList) {
					DBContextHolder.setDBType(device_id);
					casInfoMap.put("worker", String.format("%s@%s", "schedule", "localhost"));

					int result = apiSlsService.updateSLSCasInfo(casInfoMap);
					if (result > 0) {
						casInfoMap.put("send_req_yn", "S");
						apiSlsService.updateSLSCasInfo(null, casInfoMap);
					}
				}
			}

			logger.info(">>> sendCasInfo End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkDeviceStatus() {
		logger.info(">>> checkDeviceStatus Start!! - {}", LocalDateTime.now());

		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", MODEL_KEY);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> checkDeviceStatus End!! - {}", LocalDateTime.now());
				return;
			}

			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if (device_id.equals("")) {
					continue;
				}

				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> statusInfo = apiSlsService.getSLSStatusInfoList();
				if (statusInfo != null && !statusInfo.get("status_info").toString().equals("")) {
					statusInfo.put("device_key", device_key);
					statusInfo.put("device_control_status", statusInfo.get("status_info"));
					deviceService.updateDeviceControllStatus(null, statusInfo);
				}
			}

			logger.info(">>> checkDeviceStatus End!! - {}", LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
