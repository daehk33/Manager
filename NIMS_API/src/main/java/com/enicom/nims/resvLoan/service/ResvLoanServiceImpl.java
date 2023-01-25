package com.enicom.nims.resvLoan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.HttpUtils;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service(value = "resvLoanService")
public class ResvLoanServiceImpl implements ResvLoanService {
	private ServiceUtil serviceUtil;
	
	@Autowired
	public ResvLoanServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}
	
	@Value("#{config['api.resv_loan']}")
	private String api_url;
	
	@Override
	public Map<String, Object> getResvLoanRankList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "library_key", "device_key", "limit" };
		String[] mappers = null;
		
		if(!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		String gubun = ParamUtil.parseString(paramMap.get("gubun")).toLowerCase();
		if(gubun.equals("book")) {
			mappers = new String[] { "resvLoan.getResvLoanBookRankListCnt", "resvLoan.getResvLoanBookRankList" };
		}
		else if(gubun.equals("user")) {
			mappers = new String[] { "resvLoan.getResvLoanUserRankListCnt", "resvLoan.getResvLoanUserRankList" };
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String, Object> getResvLoanBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = {"resvLoan.getResvLoanBookListCnt", "resvLoan.getResvLoanBookList"};
		
		return serviceUtil.select(mappers, paramMap, Operation.getPagedList);
	}
	
	@Override
	public Map<String, Object> getReservedBookList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		int pageSize = ParamUtil.parseInt(paramMap.get("pageSize"));
		int pageIdx = ParamUtil.parseInt(paramMap.get("pageIdx"));
		
		int first_row = pageSize * (pageIdx - 1) + 1;
		int last_row = pageSize * pageIdx;
		
		/*
		 * API Call
		 */
		Map<String, Object> mapCnt = HttpUtils.createRequest(api_url, "/GetReservationListCnt", request, "예약 목록 수 조회에 실패하였습니다.");
		
		String url_list = String.format("/GetReservationList?first_row=%d&last_row=%d", first_row, last_row);
		Map<String, Object> mapList = HttpUtils.createRequest(api_url, url_list, request, "예약 목록 수 조회에 실패하였습니다.");
		
		
		/*
		 * API Result 처리
		 */
		int totalCount = ParamUtil.parseInt(mapCnt.get("TOTAL_CNT"));
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) mapList.get("DATA");
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> count = new HashMap<String, Object>();
		
		String[] mappers = {"apiResvLoan.getReservedBookStatus"};
		if(dataList != null) {
			for(Map<String, Object> temp: dataList) {
				temp = ParamUtil.keyChangeLowerMap(temp);
				count = serviceUtil.select(mappers, temp, Operation.getCount);
				temp.putAll((Map<String, Object>) count.get("result"));
				resultList.add(temp);
			}
		}
		
		result.put("last_page", Math.ceil((double) totalCount / pageSize));
		result.put("count", totalCount);
		result.put("data", resultList);
		
		return JsonUtil.makeJSON("result", result);
	}

	@Override
	public Map<String, Object> getResvLoanCabinetList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = {"resvLoan.getResvLoanCabinetListCnt", "resvLoan.getResvLoanCabinetList"};
		String[] columns = {"library_key", "device_key", "cabinet", "insert_no"};

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
	
	@Override
	public Map<String, Object> getResvLoanCabinetTotalInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = {"resvLoan.getResvLoanCabinetTotalInfo"};
		String[] columns = {"library_key", "device_key"};

		return serviceUtil.select(mappers, paramMap, columns, Operation.getList);
	}
	
	@Override
	public Map<String, Object> checkModuleDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = {"resvLoan.checkModuleDuplicated"};
		String[] columns = {"library_key", "device_key", "module_id"};
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCheck);
	}
	
	@Override
	public Map<String, Object> getResvLoanLastModule(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = {"resvLoan.getResvLoanLastModule"};
		String[] columns = {"library_key", "device_key"};
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
}
