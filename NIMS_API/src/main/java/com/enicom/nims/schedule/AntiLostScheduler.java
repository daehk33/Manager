package com.enicom.nims.schedule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.api.service.ApiAntiLostService;
import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.utils.ParamUtil;

@Service
public class AntiLostScheduler extends Scheduler {
	private static Logger logger = LoggerFactory.getLogger(AntiLostScheduler.class);
	private static String MODEL_KEY = "5";

	private ApiAntiLostService apiAntiLostService;

	@Autowired
	public AntiLostScheduler(DeviceService deviceService, ApiAntiLostService apiAntiLostService) {
		super(deviceService, MODEL_KEY);
		this.apiAntiLostService = apiAntiLostService;
	}

	@Override
	public void scheduleInit() throws Exception {
		syncDeviceInfo();
		Thread.sleep(1000);
	}

	@Override
	public void scheduleDay() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void scheduleHour() throws Exception {
		syncDeviceInfo();

	}

	@Override
	public void scheduleMinuteTen() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void scheduleMinuteOne() throws Exception {
		syncCountLogInfo();
		Thread.sleep(1000);
		
		syncBookLogInfo();
		Thread.sleep(1000);
	}
	
	private Map<String, String> syncDeviceInfo() {
		Map<String, String> ipHash = new HashMap<String, String>();
		
		logger.info(">>> syncDeviceInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceConnList = apiAntiLostService.getDBConnListInServer(Collections.emptyMap());
			List<Map<String, Object>> deviceServerList = getDeviceList();
			if (deviceConnList.size() == 0 || deviceConnList.isEmpty()) {
				logger.info(">>> syncDeviceInfo End - {}", LocalDateTime.now());
				return ipHash;
			}
			
			/**
			 * ConnKey가 없는 경우 장비가 존재하지 않으므로 삭제 
			 */
			int garbageCnt = 0;
			List<String> connKeyList = deviceConnList.stream().map(conn -> ParamUtil.parseString(conn.get("rec_key"))).collect(Collectors.toList());
			for(Map<String, Object> server: deviceServerList) {
				String device_id = ParamUtil.parseString(server.get("device_id"));
				String[] arr = device_id.split("_");
				if(arr.length > 0 && connKeyList.indexOf(arr[1]) > -1) {
					continue;
				}
				
				garbageCnt ++;
				deviceService.deleteDeviceInfo(null, server);
			}
			
			logger.info("GARBAGE DELETE >>> {}", garbageCnt);
			
			/**
			 * 장비 목록 동기화
			 */
			for (Map<String, Object> conn: deviceConnList) {
				String conn_key = ParamUtil.parseString(conn.get("rec_key"));
				String library_key = ParamUtil.parseString(conn.get("library_key"));
				if(conn_key.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(conn_key);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int deleteCnt = 0;
				
				List<Map<String, Object>> deviceList = apiAntiLostService.getDeviceList(Collections.emptyMap());
				if (deviceList == null) {
					continue;
				}
				
				for (Map<String, Object> device: deviceList) {
					String rec_key = ParamUtil.parseString(device.get("rec_key"));
					String name = ParamUtil.parseString(device.get("name"));
					String ip = ParamUtil.parseString(device.get("ip"));
					
					String deviceId = String.format("antilost_%s_%s", conn_key, rec_key);
					device.put("worker", String.format("%s@%s", "schedule", "localhost"));
					device.put("library_key", library_key);
					device.put("model_key", "5");
					device.put("device_id", deviceId);
					device.put("device_nm", "분실방지기_0"+conn_key+rec_key);
					device.put("device_ip", ip);
					device.put("device_location", name);
					device.put("device_desc", name);
					device.put("config_yn", "N");
					device.put("did_yn", "N");
					device.put("schedule", "Y");
					
					ipHash.put(ip, deviceId);
					
					if(deviceServerList.stream().filter(server -> {
						return ParamUtil.parseString(server.get("device_id")).equals(deviceId);
					}).count() == 0) {
						insertCnt ++;
						deviceService.insertDeviceInfo(null, device);
					} else {
						updateCnt ++;
						deviceService.updateDeviceInfo(null, device);
					}
				}
				
				for(Map<String, Object> server: deviceServerList) {
					String device_id = ParamUtil.parseString(server.get("device_id"));
					String[] arr = device_id.split("_");
					if(arr.length > 0 && !arr[1].equals(conn_key)) {
						continue;
					}
					
					if(arr.length > 1 && deviceList.stream().filter(device -> {
						return arr[2].equals(ParamUtil.parseString(device.get("rec_key")));
					}).count() > 0) {
						continue;
					}
					
					deleteCnt ++;
					deviceService.deleteDeviceInfo(null, server);
				}
				
				logger.info("Result [insert: {}, update: {}, delete: {}]", insertCnt, updateCnt, deleteCnt);
			}
			
			logger.info(">>> syncDeviceInfo End - {}", LocalDateTime.now());
		}
		catch (Exception e) {
			logger.error(">>> syncDeviceInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
		
		return ipHash;
	}
	
	private void syncCountLogInfo() {
		logger.info(">>> syncCountLogInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceConnList = apiAntiLostService.getDBConnListInServer(Collections.emptyMap());
			
			if (deviceConnList.size() == 0 || deviceConnList.isEmpty()) {
				logger.info(">>> syncCountLogInfo End - {}", LocalDateTime.now());
				return;
			}
			
			Map<String, String> ipHash = syncDeviceInfo();
			for (Map<String, Object> conn: deviceConnList) {
				String conn_key = ParamUtil.parseString(conn.get("rec_key"));
				String library_key = ParamUtil.parseString(conn.get("library_key"));
				if(conn_key.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(conn_key);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("conn_key", ParamUtil.parseInt(conn_key));
				paramMap.put("library_key", ParamUtil.parseInt(library_key));
				
				/**
				 * Server DB의 가장 최신의 log_date를 가져옴
				 * 1. count 체크 - count/100 만큼 반복. 초기 sync의 경우 데이터가 많을 경우가 있어서 100건씩 잘라서 가져옴.
				 */
				Map<String, Object> itemMax = apiAntiLostService.getCountHistoryMaxInServer(paramMap);
				int itemCount = apiAntiLostService.getCountHistoryCount(itemMax != null ? itemMax : Collections.emptyMap());
				List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
				
				if(itemCount == 0) {
					logger.info("INSERT >>> {}", itemCount);
					continue;
				}
				
				for(int i=0; i<=Math.ceil(itemCount/100.0); i++) {
					itemMax = apiAntiLostService.getCountHistoryMaxInServer(paramMap);
					itemList = apiAntiLostService.getCountHistoryList(itemMax != null ? itemMax : Collections.emptyMap());
					System.err.println(itemList);
					int insertCount = 0;
					if(itemList.size() > 0) {
						itemList.forEach(item -> {
							String equip_id = ParamUtil.parseString(item.get("equip_id"));
							item.put("device_id", ParamUtil.parseString(ipHash.get(equip_id), equip_id));
						});
						
						paramMap.put("countList", itemList);
						insertCount = apiAntiLostService.insertCountHistoryInfoToServer(paramMap);
					}
					
					logger.info("INSERT >>> {}", insertCount);
				}
			}
			
			logger.info(">>> syncCountLogInfo End - {}", LocalDateTime.now());
		}
		catch (Exception e) {
			logger.error(">>> syncCountLogInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}
	
	private void syncBookLogInfo() {
		logger.info(">>> syncBookLogInfo Start - {}", LocalDateTime.now());
		try {
			List<Map<String, Object>> deviceConnList = apiAntiLostService.getDBConnListInServer(Collections.emptyMap());
			
			if (deviceConnList.size() == 0 || deviceConnList.isEmpty()) {
				logger.info(">>> syncBookLogInfo End - {}", LocalDateTime.now());
				return;
			}
			
			Map<String, String> ipHash = syncDeviceInfo();
			for (Map<String, Object> conn: deviceConnList) {
				String conn_key = ParamUtil.parseString(conn.get("rec_key"));
				String library_key = ParamUtil.parseString(conn.get("library_key"));
				if(conn_key.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(conn_key);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("conn_key", ParamUtil.parseInt(conn_key));
				paramMap.put("library_key", ParamUtil.parseInt(library_key));
				
				/**
				 * Server DB의 가장 최신의 log_date를 가져옴
				 * 1. count 체크 - count/100 만큼 반복. 초기 sync의 경우 데이터가 많을 경우가 있어서 100건씩 잘라서 가져옴.
				 */
				Map<String, Object> itemMax = apiAntiLostService.getBookHistoryMaxInServer(paramMap);
				int itemCount = apiAntiLostService.getBookHistoryCount(itemMax != null ? itemMax : Collections.emptyMap());
				List<Map<String, Object>> itemList = new ArrayList<Map<String,Object>>();
				
				if(itemCount == 0) {
					logger.info("INSERT >>> {}", itemCount);
					continue;
				}
				
				for(int i=0; i<Math.ceil(itemCount/100.0); i++) {
					itemMax = apiAntiLostService.getBookHistoryMaxInServer(paramMap);
					itemList = apiAntiLostService.getBookHistoryList(itemMax != null ? itemMax : Collections.emptyMap());
					
					int insertCount = 0;
					if(itemList.size() > 0) {
						itemList.forEach(item -> {
							String equip_id = ParamUtil.parseString(item.get("equip_id"));
							item.put("device_id", ParamUtil.parseString(ipHash.get(equip_id), equip_id));
						});
						
						paramMap.put("bookList", itemList);
						insertCount = apiAntiLostService.insertBookHistoryInfoToServer(paramMap);
					}
					
					logger.info("INSERT >>> {}", insertCount);
				}
			}
			
			logger.info(">>> syncBookLogInfo End - {}", LocalDateTime.now());
		}
		catch (Exception e) {
			logger.error(">>> syncBookLogInfo End - {}", LocalDateTime.now());
			e.printStackTrace();
		}
	}
}
