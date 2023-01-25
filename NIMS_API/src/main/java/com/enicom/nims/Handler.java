package com.enicom.nims;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class Handler implements ResultHandler {
	
	public List<Map<String, Object>> resultList = null;
	
	public Handler() {}
	
	public Handler(List<Map<String, Object>> resultList) {
		this.resultList = resultList;
	}

	@Override
	public void handleResult(ResultContext context) {
		Object object = context.getResultObject();
		resultList.add((Map<String, Object>) object);
	}

}
