package com.enicom.nims.schedule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.device.service.DeviceService;

@Service
public abstract class Scheduler {
	protected DeviceService deviceService;
	protected String model_key;

	@Autowired
	public Scheduler(DeviceService deviceService, String model_key) {
		this.deviceService = deviceService;
		this.model_key = model_key;
	}

	public void run(ScheduleType type) throws Exception {
		if (type.equals(ScheduleType.INIT)) {
			scheduleInit();
		}
		else if (type.equals(ScheduleType.DAY)) {
			scheduleDay();
		}
		else if (type.equals(ScheduleType.HOUR)) {
			scheduleHour();
		}
		else if (type.equals(ScheduleType.MIN_TEN)) {
			scheduleMinuteTen();
		}
		else if (type.equals(ScheduleType.MIN_ONE)) {
			scheduleMinuteOne();
		}
	}

	public abstract void scheduleInit() throws Exception;
	public abstract void scheduleDay() throws Exception;
	public abstract void scheduleHour() throws Exception;
	public abstract void scheduleMinuteTen() throws Exception;
	public abstract void scheduleMinuteOne() throws Exception;


	protected List<Map<String, Object>> getDeviceList() throws Exception {
		Map<String, Object> deviceParams = new HashMap<String, Object>();

		if(model_key.contains(",")) {
			int[] model_key_array = Arrays.stream(model_key.split(",")).mapToInt(Integer::parseInt).toArray();
			deviceParams.put("model_key_array", model_key_array);
		}
		else {
			deviceParams.put("model_key", model_key);
		}

		return deviceService.getDeviceList(deviceParams);
	}

	protected List<Map<String, Object>> getDeviceConnList() throws Exception {
		Map<String, Object> deviceParams = new HashMap<String, Object>();

		if(model_key.contains(",")) {
			int[] model_key_array = Arrays.stream(model_key.split(",")).mapToInt(Integer::parseInt).toArray();
			deviceParams.put("model_key_array", model_key_array);
		}
		else {
			deviceParams.put("model_key", model_key);
		}

		return deviceService.getDeviceDBConnList(deviceParams);
	}
	protected interface Operation {
		void run(Map<String, Object> device);
	}
}
