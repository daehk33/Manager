package com.enicom.nims.dao;

import java.util.List;
import java.util.Map;

public interface DaoInterface {
	public Map<String, Object> selectOne(String statement, Map<String, Object> condition);
	public int selectInt(String statement, Map<String, Object> condition);
	public String selectString(String statement, Map<String, Object> condition);
	public List<Map<String, Object>> selectList(String statement, Map<String, Object> condition);

	public int insert(String statement, Map<String, Object> condition);
	public int update(String statement, Map<String, Object> condition);
	public int delete(String statement, Map<String, Object> condition);
}