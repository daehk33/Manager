package com.enicom.nims.loanReturn.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service(value = "loanReturnService")
public class LoanReturnServiceImpl implements LoanReturnService {
	private ServiceUtil serviceUtil;
	
	@Autowired
	public LoanReturnServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getLoanReturnRankList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {"library_key", "device_key", "limit"};
		String[] mappers = null;
		
		if (!ParamUtil.formatDateByType(paramMap, "type")) {
			return JsonUtil.makeResultJSON("410");
		}
		
		String gubun = ParamUtil.parseString(paramMap.get("gubun")).toLowerCase();
		if(gubun.equals("book")) {
			mappers = new String[] {"loanReturn.getLoanReturnBookRankListCnt", "loanReturn.getLoanReturnBookRankList"};
		}
		else if(gubun.equals("user")) {
			mappers = new String[] {"loanReturn.getLoanReturnUserRankListCnt", "loanReturn.getLoanReturnUserRankList"};
		}
		else if(gubun.equals("location")) {
			mappers = new String[] {"loanReturn.getLoanReturnLocationRankListCnt", "loanReturn.getLoanReturnLocationRankList"};
		}
		else {
			return JsonUtil.makeResultJSON("410");
		}
		
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}
}
