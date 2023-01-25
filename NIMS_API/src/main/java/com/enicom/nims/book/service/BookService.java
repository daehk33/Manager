package com.enicom.nims.book.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface BookService {
	public Map<String, Object> getBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> insertBookInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> updateBookInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteBookInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

}