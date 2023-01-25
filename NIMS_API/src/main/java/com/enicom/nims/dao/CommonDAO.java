package com.enicom.nims.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDAO implements DaoInterface {
	@Autowired
	@Resource(name = "sqlSessionTemplate")
	private SqlSession sqlSession;

	@Override
	public Map<String, Object> selectOne(String statement, Map<String, Object> condition) {
		return sqlSession.selectOne(statement, condition);
	}

	@Override
	public int selectInt(String statement, Map<String, Object> condition) {
		return sqlSession.selectOne(statement, condition);
	}

	@Override
	public String selectString(String statement, Map<String, Object> condition) {
		return sqlSession.selectOne(statement, condition);
	}

	@Override
	public List<Map<String, Object>> selectList(String statement, Map<String, Object> condition) {
		return sqlSession.selectList(statement, condition);
	}

	@Override
	public int insert(String statement, Map<String, Object> condition) {
		return sqlSession.insert(statement, condition);
	}

	@Override
	public int update(String statement, Map<String, Object> condition) {
		return sqlSession.update(statement, condition);
	}

	@Override
	public int delete(String statement, Map<String, Object> condition) {
		return sqlSession.delete(statement, condition);
	}

}
