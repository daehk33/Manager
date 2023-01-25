package com.enicom.nims.api.service;

import java.util.List;
import java.util.Map;

public interface ApiLibService {
	public List<Map<String, Object>> getApiLibMemberList(Map<String, Object> paramMap) throws Exception;
	public List<Map<String, Object>> getApiLibBookList(Map<String, Object> paramMap) throws Exception;
	
	public Map<String, Object> getlibMemberInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertLibMemberInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteLibMemberInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLibMemberInfo(Map<String, Object> paramMap) throws Exception;

	public Map<String, Object> getlibBookInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> insertLibBookInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> deleteLibBookInfo(Map<String, Object> paramMap) throws Exception;
	public Map<String, Object> updateLibBookInfo(Map<String, Object> paramMap) throws Exception;
	
}
