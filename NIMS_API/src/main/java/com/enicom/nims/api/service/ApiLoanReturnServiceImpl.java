package com.enicom.nims.api.service;

import java.io.File;
import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.dao.DaoType;
import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;
import com.enicom.nims.utils.Utils;

@Service(value = "apiLoanReturnService")
public class ApiLoanReturnServiceImpl implements ApiLoanReturnService{
	private ServiceUtil serviceUtil;
	private ApiService apiService;
	
	@Autowired
	public ApiLoanReturnServiceImpl(ServiceUtil serviceUtil, ApiService apiService) {
		this.serviceUtil = serviceUtil;
		this.apiService = apiService;
	}
	
	@Value("#{config['sftp.localImagePath']}")
	private String localImagePath;
	
	@Override
	public List<Map<String, Object>> getLoanReturnUseLogInfoList(Map<String, Object> paramMap) throws Exception {
		String[] columns = {"log_key"};
		return serviceUtil.selectList(DaoType.LoanReturn, "multiplus.getLoanReturnUseLogInfoList", paramMap, columns);
	}
	
	@Override
	public Map<String, Object> getLoanReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnUseLogInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	@Override
	public Map<String, Object> insertLoanReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key", "log_key"};
		return serviceUtil.service("apiLoanReturn.insertLoanReturnUseLogInfo", paramMap, columns, Operation.insert);
	}
	@Override
	public Map<String, Object> updateLoanReturnUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key", "rec_key"};
		return serviceUtil.service("apiLoanReturn.updateLoanReturnUseLogInfo", paramMap, columns, Operation.update);
	}
	@Override
	public List<Map<String, Object>> getLoanReturnRuleInfoList(Map<String, Object> paramMap) throws Exception {
		String mapper = null;
		String model_key = ParamUtil.parseString(paramMap.get("model_key"));
		
		if(!model_key.equals("9")) {
			mapper = "multiplus.getLoanReturnRuleInfoList";
		}
		else {
			mapper = "multiplus.getLoanReturnRuleInfoList2";
		}
		
		return serviceUtil.selectList(DaoType.LoanReturn, mapper, paramMap);
	}
	@Override
	public Map<String, Object> getLoanReturnRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnRuleInfoList"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	@Override
	public Map<String, Object> getLoanReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnRuleInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	@Override
	public Map<String, Object> insertLoanReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key"};
		return serviceUtil.service("apiLoanReturn.insertLoanReturnRuleInfo", paramMap, columns, Operation.insert);
	}
	@Override
	public Map<String, Object> updateLoanReturnRuleInfo(Map<String, Object> paramMap) throws Exception {
		paramMap = ParamUtil.emptyToNull(paramMap);
		String mapper = null;
		String model_key = ParamUtil.parseString(paramMap.get("model_key"));
		
		if(!model_key.equals("9")) {
			mapper = "multiplus.updateLoanReturnRuleInfo";
		}
		else {
			mapper = "multiplus.updateLoanReturnRuleInfo2";
		}
		
		return serviceUtil.service(DaoType.LoanReturn, mapper, paramMap, Operation.update);
	}
	@Override
	public Map<String, Object> updateLoanReturnRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		return serviceUtil.service("apiLoanReturn.updateLoanReturnRuleInfo", paramMap, columns, Operation.update);
	}
	@Override
	public Map<String, Object> updateLoanReturnRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		return serviceUtil.service("apiLoanReturn.updateLoanReturnRuleSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getLoanReturnDIDRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnDIDRuleInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> updateLoanReturnDIDRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		
		String id = paramMap.get("manager_id").toString();
		String ip = request.getRemoteHost();
		
		paramMap.put("worker", String.format("%s@%s", id, ip));
		
		return serviceUtil.service("apiLoanReturn.updateLoanReturnDIDRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateLoanReturnBannerChanged(Map<String, Object> paramMap) throws Exception {
		String deviceId = paramMap.get("device_id").toString();
		
		DBContextHolder.setDBType(deviceId);
		
		return serviceUtil.service(DaoType.LoanReturn, "multiplus.updateLoanReturnBannerChanged", paramMap, Operation.update);
	}

	@Override
	public Map<String, Object> getLoanReturnBannerImageList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		String[] mappers = {"device.getDeviceInfo"};
		
		Map<String, Object> deviceMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> fileListMap = new ArrayList<Map<String, Object>>();
		
		String path = "";
		String imagePath = "";
		
		if(paramMap.get("rec_key") != null) {
			try {
				deviceMap = serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
				
				// 조회된 장비가 없을 경우
				if(deviceMap == null) {
					resultMap.put("result", JsonUtil.makeListJSON(fileListMap));
					
					return resultMap;
				}
			} 
			catch(Exception e) {
				e.printStackTrace();
				return JsonUtil.makeResultJSON("400");
			}
			
			String device_id = deviceMap.get("device_id").toString();
			
			path = localImagePath + device_id + "/";
			imagePath = "/resources/banner/img/" + device_id + "/";
			
			File dir = new File(path);
			
			// 폴더 유무 확인
			if(!dir.exists()) {
				try {
					dir.mkdir();
				}
				catch(Exception e) {
					e.printStackTrace();
					return JsonUtil.makeResultJSON("400");
				}
			}
			
			// 파일 목록 Map 생성
			File[] fileList = {};
			if(dir.listFiles() != null) {
				fileList = dir.listFiles();
			}
			for(File file : fileList) {
				Map<String, Object> fileMap = new HashMap<String, Object>();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
				Date editDate = new Date(file.lastModified());
				
				String fileSize = "0";
				long size = file.length();
				
				if(size > (1024 * 1024)) {
					fileSize = ((size / 1024) / 1024) + " MB";
				}
				else if (size > 1024) {
					fileSize = (size / 1024) + " KB";
				}
				else {
					fileSize = size + " BYTE";
				}
				
				fileMap.put("file_name", file.getName());
				fileMap.put("file_path", imagePath + file.getName());
				fileMap.put("file_size", fileSize);
				fileMap.put("edit_date", dateFormat.format(editDate));
				
				fileListMap.add(fileMap);
			}
			
			resultMap.put("result", JsonUtil.makeListJSON(fileListMap));
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}
		
		return resultMap;
	}
	
	@Override
	public Map<String, Object> getLoanReturnHolidayRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnDIDHolidayRule"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> updateLoanReturnHolidayRule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		
		String id = paramMap.get("manager_id").toString();
		String ip = Inet4Address.getLocalHost().getHostAddress();
		
		paramMap.put("worker", String.format("%s@%s", id, ip));
		paramMap.put("send_req_yn", "Y");
		
		return serviceUtil.service("apiLoanReturn.updateLoanReturnDIDHolidayUseRule", paramMap, columns, Operation.update);
	}
	
	@Override
	public List<Map<String, Object>> getLoanReturnHolidayList(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.LoanReturn, "multiplus.getLoanReturnHolidayList", paramMap);
	}
	
	@Override
	public Map<String, Object> getLoanReturnHolidayInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnHolidayList"};
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> getLoanReturnHolidayList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnHolidayList"};
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> holidays = (List<Map<String, Object>>) ((Map<String, Object>) serviceUtil.select(mappers, paramMap, columns, Operation.getList).get("result")).get("items");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		holidays.forEach(holiday -> {
			String days = ParamUtil.parseString(holiday.get("days"));
			if(!days.equals("")) {
				String[] daysArr = days.split(",");
				
				for(String day: daysArr) {
					if(day.equals("")) continue;
					
					Calendar c = Calendar.getInstance();
					c.set(ParamUtil.parseInt(holiday.get("year")), ParamUtil.parseInt(holiday.get("month")) - 1, ParamUtil.parseInt(day));
					
					Map<String, Object> result = new HashMap<String, Object>();
					result.put("date", sdf.format(c.getTime()));
					resultList.add(result);
				}
			}
		});
		
		return JsonUtil.makeListJSON(resultList);
	}
	
	@Override
	public Map<String, Object> getLoanReturnHolidayInfo(Map<String, Object> paramMap) throws Exception {
		String mapper = "multiplus.getLoanReturnHolidayInfo";
		return serviceUtil.selectOne(DaoType.LoanReturn, mapper, paramMap);
	}
	
	@Override
	public Map<String, Object> getLoanReturnHolidayInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"multiplus.getLoanReturnHolidayInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap); 
	}

	@Override
	public Map<String, Object> upsertLoanReturnHolidayInfo(Map<String, Object> paramMap) throws Exception {
		String mapper = "multiplus.getLoanReturnHolidayInfo";
		Map<String, Object> holiday = serviceUtil.selectOne(DaoType.LoanReturn, mapper, paramMap);
		
		if(holiday == null || holiday.isEmpty()) {
			return serviceUtil.service(DaoType.LoanReturn, "multiplus.insertLoanReturnHolidayInfo", paramMap, Operation.insert);
		}
		
		String daysHoliday = ParamUtil.parseString(holiday.get("days"));
		String daysParam = ParamUtil.parseString(paramMap.get("days"));
		
		SortedSet<String> set = Utils.getSortedSet();
		set.addAll(Utils.getSetFromStr(daysHoliday));
		set.addAll(Utils.getSetFromStr(daysParam));
		
		paramMap.put("days", String.join(",", set));
		paramMap.put("rec_key", holiday.get("rec_key"));
		
		return serviceUtil.service(DaoType.LoanReturn, "multiplus.updateLoanReturnHolidayInfo", paramMap, Operation.update);
	}

	@Override
	public Map<String, Object> upsertLoanReturnHolidayInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = {"apiLoanReturn.getLoanReturnHolidayInfo"};
		String[] columns = {"rec_key", "library_key", "device_key"};
		Map<String, Object> holiday = serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
		
		if(holiday == null || holiday.isEmpty()) {
			return serviceUtil.service("apiLoanReturn.insertLoanReturnHolidayInfo", paramMap, columns, Operation.insert);
		}
		
		String daysHoliday = ParamUtil.parseString(holiday.get("days"));
		String daysParam = ParamUtil.parseString(paramMap.get("days"));
		
		SortedSet<String> set = Utils.getSortedSet();
		set.addAll(Utils.getSetFromStr(daysHoliday));
		set.addAll(Utils.getSetFromStr(daysParam));
		
		paramMap.put("days", String.join(",", set));
		paramMap.put("rec_key", holiday.get("rec_key").toString());
		return serviceUtil.service("apiLoanReturn.updateLoanReturnHolidayInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteLoanReturnHolidayInfo(Map<String, Object> paramMap) throws Exception {
		String mapper = "multiplus.getLoanReturnHolidayInfo";
		Map<String, Object> holiday = serviceUtil.selectOne(DaoType.LoanReturn, mapper, paramMap);
		
		if(holiday == null || holiday.isEmpty()) {
			return JsonUtil.makeResultJSON("200");
		}
		
		String daysHoliday = ParamUtil.parseString(holiday.get("days"));
		String daysParam = ParamUtil.parseString(paramMap.get("days"));
		
		SortedSet<String> set = Utils.getSortedSet();
		set.addAll(Utils.getSetFromStr(daysHoliday));
		set.removeAll(Utils.getSetFromStr(daysParam));
		
		paramMap.put("rec_key", holiday.get("rec_key"));
		
		if(set.isEmpty()) {
			return serviceUtil.service(DaoType.LoanReturn, "multiplus.deleteLoanReturnHolidayInfo", paramMap, Operation.delete);
		}
		else {
			paramMap.put("days", String.join(",", set));
			return serviceUtil.service(DaoType.LoanReturn, "multiplus.updateLoanReturnHolidayInfo", paramMap, Operation.update);
		}
	}

	@Override
	public Map<String, Object> deleteLoanReturnHolidayInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key", "library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnHolidayInfo"};
		Map<String, Object> holiday = serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
		
		if(holiday == null || holiday.isEmpty()) {
			return JsonUtil.makeResultJSON("200");
		}
		
		String daysHoliday = ParamUtil.parseString(holiday.get("days"));
		String daysParam = ParamUtil.parseString(paramMap.get("days"));
		
		SortedSet<String> set = Utils.getSortedSet();
		set.addAll(Utils.getSetFromStr(daysHoliday));
		set.removeAll(Utils.getSetFromStr(daysParam));
		
		paramMap.put("rec_key", holiday.get("rec_key").toString());
		
		if(set.isEmpty()) {
			int result = serviceUtil.serviceCount("apiLoanReturn.deleteLoanReturnHolidayInfo", paramMap, columns, Operation.delete);
			
			// NIMS쪽 DB에서 삭제 성공 시 대출반납기쪽 DB에서도 삭제 진행
			if(result > 1) {
				Map<String, Object> deviceParams = new HashMap<String, Object>();
				
				deviceParams.put("model_key", "1");
				deviceParams.put("rec_key", paramMap.get("device_key").toString());
				
				List<Map<String, Object>> deviceList = serviceUtil.selectList("device.getDeviceList", deviceParams);
				
				if(deviceList.size() > 0) {
					String device_key = ParamUtil.parseString(paramMap.get("device_key"));
					
					for (Map<String, Object> device: deviceList) {
						String rec_key = ParamUtil.parseString(device.get("rec_key"));
						String device_id = ParamUtil.parseString(device.get("device_id"));
						
						if(rec_key.equals(device_key)) {
							DBContextHolder.setDBType(device_id);
							
							return deleteLoanReturnHolidayInfo(paramMap);
						}
					}
				}
				else {
					return JsonUtil.makeResultJSON("400");
				}
			}
			else {
				return JsonUtil.makeResultJSON("400", "휴관일 삭제 중 오류가 발생하였습니다.");
			}
		}
		else {
			paramMap.put("days", String.join(",", set));
			return serviceUtil.service("apiLoanReturn.updateLoanReturnHolidayInfo", paramMap, columns, Operation.update);
		}
		
		return JsonUtil.makeResultJSON("200");
	}
	
	@Override
	public Map<String, Object> updateLoanReturnHolidaySendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnHolidayInfo"};
		
		Map<String, Object> holiday = serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
		
		if(holiday == null || holiday.isEmpty()) {
			return JsonUtil.makeResultJSON("200");
		}
		
		return serviceUtil.service("apiLoanReturn.updateLoanReturnHolidaySendResult", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> loadLoanReturnBannerSettings(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		List<Boolean> resultList = new ArrayList<Boolean>();
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			Map<String, Object> sftpMap = new HashMap<String, Object>();
			String device_key = paramMap.get("device_key").toString();
			String[] types = paramMap.get("types").toString().split(",");
			
			sftpMap.put("device_key", device_key);
			sftpMap.put("action", "download");
			
			for(String type: types) {
				sftpMap.put("type", type.trim());
				
				resultMap = apiService.sftpService(request, sftpMap);
				result = ((Map<String, Object>) resultMap.get("result"));
				
				if(result.get("CODE") == "200") {
					resultList.add(true);
				}
				else {
					resultList.add(false);
				}
			}
			
			if(!resultList.contains(false)) {
				return JsonUtil.makeResultJSON("200");
			}
			else {
				return JsonUtil.makeResultJSON("400", "배너 설정 및 이미지 불러오기 중 오류가 발생하였습니다.");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return JsonUtil.makeResultJSON("400", "배너 설정 및 이미지 불러오기 중 오류가 발생하였습니다.");
		}
	}

	@Override
	public List<Map<String, Object>> getLoanReturnEquipRuleInfoList(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.LoanReturn, "multiplus.getLoanReturnEquipRuleInfoList", paramMap);
	}

	@Override
	public Map<String, Object> getLoanReturnEquipRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnEquipRuleInfoList"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public Map<String, Object> updateLoanReturnEquipRuleInfo(Map<String, Object> paramMap) throws Exception {
		paramMap = ParamUtil.emptyToNull(paramMap);
		return serviceUtil.service(DaoType.LoanReturn, "multiplus.updateLoanReturnEquipRuleInfo", paramMap, Operation.update);
	}
	
	@Override
	public Map<String, Object> updateLoanReturnEquipRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		return serviceUtil.service("apiLoanReturn.updateLoanReturnEquipRuleInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> upsertLoanReturnEquipRuleInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key", "library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnEquipRuleInfo"};
		Map<String, Object> info = serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
		
		if(info == null || info.isEmpty()) {
			return serviceUtil.service("apiLoanReturn.insertLoanReturnEquipRuleInfo", paramMap, columns, Operation.insert);
		}
		
		paramMap.put("rec_key", info.get("rec_key").toString());
		return serviceUtil.service("apiLoanReturn.updateLoanReturnEquipRuleInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> updateLoanReturnEquipRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		return serviceUtil.service("apiLoanReturn.updateLoanReturnEquipRuleSendResult", paramMap, columns, Operation.update);
	}

	@Override
	public List<Map<String, Object>> getLoanReturnBannerSettingInfo(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.LoanReturn, "multiplus.getLoanReturnBannerSettingInfo", paramMap);
	}

	@Override
	public Map<String, Object> getLoanReturnBannerSettingInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnBannerSettingInfo"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}

	@Override
	public int updateLoanReturnBannerSettingInfo(Map<String, Object> paramMap) throws Exception {
		paramMap = ParamUtil.emptyToNull(paramMap);
		return serviceUtil.serviceCount(DaoType.LoanReturn, "multiplus.updateLoanReturnBannerSettingInfo", paramMap, Operation.update);
	}

	@Override
	public Map<String, Object> updateLoanReturnBannerSettingInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key"};
		return serviceUtil.service("apiLoanReturn.updateLoanReturnBannerSettingInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> upsertLoanReturnBannerSettingInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"rec_key", "library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnBannerSettingInfo"};
		Map<String, Object> info = serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
		
		if(info == null || info.isEmpty()) {
			return serviceUtil.service("apiLoanReturn.insertLoanReturnBannerSettingInfo", paramMap, columns, Operation.insert);
		}
		
		paramMap.put("rec_key", info.get("rec_key").toString());
		return serviceUtil.service("apiLoanReturn.updateLoanReturnBannerSettingInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> updateLoanReturnBannerSettingSendResult(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns= {"rec_key"};
		return serviceUtil.service("apiLoanReturn.updateLoanReturnBannerSettingSendResult", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getLoanReturnLastLogKey(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key"};
		String[] mappers = {"apiLoanReturn.getLoanReturnLastLogKey"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
	}
}