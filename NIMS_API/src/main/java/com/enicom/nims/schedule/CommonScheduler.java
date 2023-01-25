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

import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.utils.ParamUtil;

@Service
public class CommonScheduler {
	private static Logger logger = LoggerFactory.getLogger(CommonScheduler.class);
	private DeviceService deviceService;
	
	@Autowired
	public CommonScheduler(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	public void scheduleSecondTen() {
		syncDeviceNetworkStatus();
	}
	
	private void syncDeviceNetworkStatus() {
		logger.info(">>> syncDeviceNetworkStatus Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			List<Map<String, Object>> deviceList = deviceService.getDeviceList(params);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> syncDeviceNetworkStatus End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				String device_ip = ParamUtil.parseString(device.get("device_ip"));
				
				if (!rec_key.equals("") && !device_ip.equals("")) {
					InetAddress pingcheck = InetAddress.getByName(device_ip);
					params.put("rec_key", rec_key);
					
					if (pingcheck.isReachable(4000)) {
						params.put("device_status", 1);
						params.put("connect_yn", "Y");
					} else {
						if (ParamUtil.parseDouble(device.get("connect_interval")) > 10) {
							params.put("device_status", 0);
						}
						params.put("connect_yn", "N");
					}
					
					pingcheck = null;
					params.put("worker", String.format("%s@%s", "nims", InetAddress.getLocalHost().getHostAddress()));
					deviceService.updateDeviceConnInfo(null, params);
				}
			}
			
			logger.info(">>> syncDeviceNetworkStatus End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
