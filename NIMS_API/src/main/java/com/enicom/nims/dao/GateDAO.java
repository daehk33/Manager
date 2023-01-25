package com.enicom.nims.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.enicom.nims.Handler;

@Repository
public class GateDAO implements DaoInterface {
	@Autowired
	@Resource(name = "sqlSessionTemplate5")
	private SqlSession sqlSession;

	@Autowired
	@Resource(name = "sqlSessionFactoryTemplate")
	private SqlSession sqlSession2;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

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

	@Transactional
	public List<Map<String, Object>> selecteHandler(String statement, Map<String, Object> condition) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

		try {
			sqlSession2 = sqlSessionFactory.openSession();
			try {
				sqlSession2.select(statement, condition, new Handler(resultList));
			} finally {
				sqlSession2.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
}
