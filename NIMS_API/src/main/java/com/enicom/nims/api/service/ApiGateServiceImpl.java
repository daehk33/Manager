package com.enicom.nims.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.DaoType;
import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;
import com.enicom.nims.utils.Utils;

@Service(value = "apiGateService")
public class ApiGateServiceImpl implements ApiGateService {
	private ServiceUtil serviceUtil;
	
	@Autowired
	public ApiGateServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}
	
	@Override
	public Map<String, Object> getGateUseLogInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "gate.getGateUseLogInfoList" };
		return serviceUtil.select(null, paramMap, null, mappers, Operation.getHandler, "sqlSession5");
	}
	
	@Override
	public Map<String, Object> getGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "apiGate.getGateUseLogInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGateUseLogInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> insertBatchGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertBatchGateUseLogInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateUseLogInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "rec_key" };
		return serviceUtil.service("apiGate.updateGateUseLogInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getGateLatestLogDate(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "apiGate.getGateLatestLogDate" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOne);
	}
	
	@Override
	public Map<String, Object> getGateUserInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "gate.getGateUserInfoList" };
		return serviceUtil.select(null, paramMap, null, mappers, Operation.getList, "sqlSession5");
	}
	
	@Override
	public Map<String, Object> getGateUserInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "apiGate.getGateUserInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertGateUserInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGateUserInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateUserInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "rec_key" };
		return serviceUtil.service("apiGate.updateGateUserInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getGateCompanyCodeInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "gate.getGateCompanyCodeInfoList" };
		return serviceUtil.select(null, paramMap, null, mappers, Operation.getList, "sqlSession5");
	}
	
	@Override
	public Map<String, Object> getGateCompanyCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "apiGate.getGateCompanyCodeInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertGateCompanyCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGateCompanyCodeInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateCompanyCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "rec_key" };
		return serviceUtil.service("apiGate.updateGateCompanyCodeInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getGateDeptCodeInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "gate.getGateDeptCodeInfoList" };
		return serviceUtil.select(null, paramMap, null, mappers, Operation.getList, "sqlSession5");
	}
	
	@Override
	public Map<String, Object> getGateDeptCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "apiGate.getGateDeptCodeInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertGateDeptCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGateDeptCodeInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateDeptCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "rec_key" };
		return serviceUtil.service("apiGate.updateGateDeptCodeInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getGateMajorCodeInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "gate.getGateMajorCodeInfoList" };
		return serviceUtil.select(null, paramMap, null, mappers, Operation.getList, "sqlSession5");
	}
	
	@Override
	public Map<String, Object> getGateMajorCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "apiGate.getGateMajorCodeInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertGateMajorCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGateMajorCodeInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateMajorCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "rec_key" };
		return serviceUtil.service("apiGate.updateGateMajorCodeInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getGatePositionCodeInfoList(Map<String, Object> paramMap) throws Exception {
		String[] mappers = { "gate.getGatePositionCodeInfoList" };
		return serviceUtil.select(null, paramMap, null, mappers, Operation.getList, "sqlSession5");
	}
	
	@Override
	public Map<String, Object> getGatePositionCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		String[] mappers = { "apiGate.getGatePositionCodeInfo" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertGatePositionCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGatePositionCodeInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGatePositionCodeInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "rec_key" };
		return serviceUtil.service("apiGate.updateGatePositionCodeInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getGateRuleInfoList() throws Exception {
		String[] mappers = { "gate.getGateRuleInfoList" };
		return serviceUtil.select(null, null, null, mappers, Operation.getList, "sqlSession5");
	}
	
	@Override
	public Map<String, Object> getGateRuleInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getGateRuleInfoList" };
		String[] columns = { "library_key" };
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> getGateRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getGateRuleInfo" };
		String[] columns = { "library_key" };
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
	
	@Override
	public Map<String, Object> insertGateRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGateRuleInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateRuleInfo(Map<String, Object> paramMap) throws Exception {
		paramMap = ParamUtil.emptyToNull(paramMap);
		
		return serviceUtil.service(DaoType.Gate, "gate.updateGateRuleInfo", paramMap, Operation.update);
	}
	
	@Override
	public Map<String, Object> updateGateRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		paramMap = ParamUtil.emptyToNull(paramMap);
		
		return serviceUtil.service("apiGate.updateGateRuleInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> updateGateRuleSendResult(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.updateGateRuleSendResult", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getInputList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getInputList" };
		String[] columns = { "library_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> upsertInputInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key"};
		String[] mappers = { "apiGate.getInputList" };
		
		// 이미 사용중인 시리얼 포트인지 체크
		Map<String, Object> portMap = new HashMap<String, Object>();
		portMap.put("library_key", paramMap.get("library_key"));
		portMap.put("port", paramMap.get("port"));
		
		Map<String, Object> result = serviceUtil.select(mappers, portMap, columns, Operation.getOneOriMap);
		
		// 조회된 포트가 없는 경우 Insert
		if(result == null || result.isEmpty()) {
			return serviceUtil.service("apiGate.insertInputInfo", paramMap, columns, Operation.insert);
		}
		
		return serviceUtil.service("apiGate.updateInputInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteInputInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.deleteInputInfo", paramMap, columns, Operation.delete);
	}
	
	@Override
	public Map<String, Object> getGateList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getGateList" };
		String[] columns = { "library_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> getGateInfoList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getGateInfoList" };
		String[] columns = { "library_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> insertGateInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" };
		return serviceUtil.service("apiGate.insertGateInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key" };
		return serviceUtil.service("apiGate.updateGateInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteGateInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.deleteGateInfo", paramMap, columns, Operation.delete);
	}
	
	@Override
	public Map<String, Object> getGroupList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getGroupList" };
		String[] columns = { "library_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> insertGroupInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" , "gate" , "library_key" };
		return serviceUtil.service("apiGate.insertGroupInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGroupInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" , "gate" , "library_key" };
		return serviceUtil.service("apiGate.updateGroupInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteGroupInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.deleteGroupInfo", paramMap, columns, Operation.delete);
	}
	
	@Override
	public Map<String, Object> getGateDBConnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getGateDBConnInfo" };
		String[] columns = { "library_key"};
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> upsertGateDBConn(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key" , "port" ,"gate_port" , "smart_port"};
		String[] mappers = { "apiGate.getGateDBConnInfo" };
		Map<String, Object> gateDB = serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
		if(gateDB == null || gateDB.isEmpty()) {
			return serviceUtil.service("apiGate.insertGateDBConn", paramMap, columns, Operation.insert);
		}
		paramMap.put("library_key", gateDB.get("library_key"));
		paramMap.put("port", gateDB.get("port"));
		paramMap.put("gate_port", gateDB.get("gate_port"));
		paramMap.put("smart_port", gateDB.get("smart_port"));
		
		return serviceUtil.service("apiGate.updateGateDBConn", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> getGateScheduleList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getGateScheduleList" };
		String[] columns = { "library_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> insertGateScheduleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "equip_key" };
		return serviceUtil.service("apiGate.insertGateScheduleInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateGateScheduleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.updateGateScheduleInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteGateScheduleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.deleteGateScheduleInfo", paramMap, columns, Operation.delete);
	}
	
	@Override
	public Map<String, Object> getSecurityList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getSecurityList" };
		String[] columns = { "library_key" , "rec_key" , "group_no" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> insertSecurityInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "group_no" };
		return serviceUtil.service("apiGate.insertSecurityInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateSecurityInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" ,"group_no" };
		return serviceUtil.service("apiGate.updateSecurityInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteSecurityInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.deleteSecurityInfo", paramMap, columns, Operation.delete);
	}
	
	@Override
	public Map<String, Object> insertCommandInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "gate" };
		return serviceUtil.service("apiGate.insertCommandInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateCommandInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "gate" };
		return serviceUtil.service("apiGate.updateCommandInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteCommandInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.deleteCommandInfo", paramMap, columns, Operation.delete);
	}
	
	@Override
	public Map<String, Object> getCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getCfgRuleInfo" };
		String[] columns = { "library_key" , "rec_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> upsertCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getCfgRuleInfo" };
		String[] columns = { "library_key" };
		
		// 이미 사용중인 설정인지 체크
		Map<String, Object> cfgMap = new HashMap<String, Object>();
		cfgMap.put("library_key", paramMap.get("library_key"));
		cfgMap.put("category", paramMap.get("category"));
		cfgMap.put("rule_name", paramMap.get("rule_name"));
		
		Map<String, Object> result = serviceUtil.select(mappers, cfgMap, columns, Operation.getOneOriMap);
		
		// 조회된 설정이 없는 경우 Insert
		if(result == null || result.isEmpty()) {
			return serviceUtil.service("apiGate.insertCfgRuleInfo", paramMap, columns, Operation.insert);
		}
		
		return serviceUtil.service("apiGate.updateCfgRuleInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> insertCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key" };
		return serviceUtil.service("apiGate.insertCfgRuleInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateCfgRuleInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key" };
		return serviceUtil.service("apiGate.updateCfgRuleInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> checkPortDuplicated(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.checkPortDuplicated" };
		String[] columns = { "library_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCheck);
	}
	
	@Override
	public Map<String, Object> getBtnList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "apiGate.getBtnList" };
		String[] columns = { "library_key", "gate", "rec_key", "enable", "short_cut" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
		
	@Override
	public Map<String, Object> insertBtnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "gate", "rec_key", "enable", "short_cut"};
		return serviceUtil.service("apiGate.insertBtnInfo", paramMap, columns, Operation.insert);
	}
	
	@Override
	public Map<String, Object> updateBtnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "gate", "rec_key", "enable", "short_cut" };
		return serviceUtil.service("apiGate.updateBtnInfo", paramMap, columns, Operation.update);
	}
	
	@Override
	public Map<String, Object> deleteBtnInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("apiGate.deleteBtnInfo", paramMap, columns, Operation.delete);
	}

}
