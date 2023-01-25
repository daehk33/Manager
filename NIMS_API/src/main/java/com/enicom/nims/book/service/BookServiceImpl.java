package com.enicom.nims.book.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service
public class BookServiceImpl implements BookService {
	private ServiceUtil serviceUtil;

	@Autowired
	public BookServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> getBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] mappers = {"book.getBookList"};
		return serviceUtil.select(request, paramMap, mappers, Operation.getList); 
	}
	
	@Override
	public Map<String, Object> insertBookInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = {};
		if(serviceUtil.selectInt("book.getBookCount", paramMap) > 0) {
			return serviceUtil.service(request, paramMap, Operation.update, columns, "book.updateBookInfo");
		}
		return serviceUtil.service(request, paramMap, Operation.insert, columns, "book.insertBookInfo");
	}

	@Override
	public Map<String, Object> insertBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception {
		String[] columns = {};
		return serviceUtil.service(request, paramMap, Operation.insert, columns, "book.insertBookList");
	}
	
	@Override
	public Map<String, Object> updateBookInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = {};
		
		return serviceUtil.service(request, paramMap, Operation.update, columns, "book.updateBookInfo");
	}
	
	@Override
	public Map<String, Object> deleteBookInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = {};
		serviceUtil.service(request, paramMap, Operation.update, columns, "book.initBookSeq");
		return serviceUtil.service(request, paramMap, Operation.update, columns, "book.deleteBookInfo");
	}
}
