package com.enicom.nims.library.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LibraryService {
	Map<String, Object> checkLibraryDuplicated(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLibraryCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLibraryList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> insertLibraryInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> deleteLibraryInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> updateLibraryInfo(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLibMemberCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLibMemberList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLibBookCount(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
	Map<String, Object> getLibBookList(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;

	List<Map<String, Object>> getLibraryDBConnList(Map<String, Object> paramMap) throws Exception;
	
	Map<String, Object> checkLibServer(HttpServletRequest request, Map<String, Object> paramMap) throws Exception;
}
