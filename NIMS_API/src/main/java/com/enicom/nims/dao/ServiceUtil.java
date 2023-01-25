package com.enicom.nims.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Service("serviceUtil")
public class ServiceUtil {
	private DaoInterface dao;
	
	private CommonDAO commonDAO;
	private KolasDAO kolasDAO;
	private SmartDAO smartDAO;
	private DaoInterface galleryDAO;
	private GateDAO gateDAO;
	private LoanReturnDAO loanReturnDAO;
	private AntiLostDAO antiLostDAO;
	
	@Autowired
	public ServiceUtil(CommonDAO commonDAO, KolasDAO kolasDAO, SmartDAO smartDAO, DaoInterface galleryDAO, GateDAO gateDAO,
			LoanReturnDAO loanReturnDAO, AntiLostDAO antiLostDAO) {
		this.commonDAO = commonDAO;
		this.kolasDAO = kolasDAO;
		this.smartDAO = smartDAO;
		this.galleryDAO = galleryDAO;
		this.gateDAO = gateDAO;
		this.loanReturnDAO = loanReturnDAO;
		this.antiLostDAO = antiLostDAO;
	}
	
	public void setDao(DaoType dbType) {
		if(dbType.equals(DaoType.Kolas)) {
			dao = kolasDAO;
		}
		else if(dbType.equals(DaoType.Smart)) {
			dao = smartDAO;
		}
		else if(dbType.equals(DaoType.Gallery)) {
			dao = galleryDAO;
		}
		else if(dbType.equals(DaoType.Gate)) {
			dao = gateDAO;
		}
		else if(dbType.equals(DaoType.LoanReturn)) {
			dao = loanReturnDAO;
		}
		else if(dbType.equals(DaoType.AntiLost)) {
			dao = antiLostDAO;
		}
		else {
			dao = commonDAO;
		}
	}
	/**
	 * [조회] 원본
	 * @param request
	 * @param paramMap
	 * @param mappers
	 * @return
	 */
	public List<Map<String, Object>> selectList(DaoType type, String mappers, Map<String, Object> paramMap){
		setDao(type);
		return dao.selectList(mappers, paramMap);
	}
	public List<Map<String, Object>> selectList(DaoType type, String mappers, Map<String, Object> paramMap, String[] columns){
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		return selectList(type, mappers, paramMap);
	}
	
	public List<Map<String, Object>> selectList(String mappers, Map<String, Object> paramMap){
		return commonDAO.selectList(mappers, paramMap);
	}
	public List<Map<String, Object>> selectList(String mappers, Map<String, Object> paramMap, String[] columns){
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		return selectList(mappers, paramMap);
	}
	
	/**
	 * [조회] 원본
	 * @param request
	 * @param paramMap
	 * @param mappers
	 * @return
	 */
	public int selectInt(DaoType type, String mappers, Map<String, Object> paramMap, String[] columns){
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		return selectInt(type, mappers, paramMap);
	}
	
	public int selectInt(DaoType type, String mappers, Map<String, Object> paramMap){
		setDao(type);
		return dao.selectInt(mappers, paramMap);
	}
	
	public int selectInt(String mappers, Map<String, Object> paramMap){
		return commonDAO.selectInt(mappers, paramMap);
	}
	
	/**
	 * [조회] 원본
	 * @param request
	 * @param paramMap
	 * @param mappers
	 * @return
	 */
	public String selectString(DaoType type, String mappers, Map<String, Object> paramMap){
		setDao(type);
		return dao.selectString(mappers, paramMap);
	}
	public String selectString(String mappers, Map<String, Object> paramMap){
		return commonDAO.selectString(mappers, paramMap);
	}
	
	/**
	 * [조회] 원본
	 * @param request
	 * @param paramMap
	 * @param mappers
	 * @return
	 */
	public Map<String, Object> selectOne(DaoType type, String mappers, Map<String, Object> paramMap, String[] columns) {
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		return selectOne(type, mappers, paramMap);
	}
	public Map<String, Object> selectOne(DaoType type, String mappers, Map<String, Object> paramMap) {
		setDao(type);
		return dao.selectOne(mappers, paramMap);
	}
	public Map<String, Object> selectOne(String mappers, Map<String, Object> paramMap, String[] columns) {
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		return commonDAO.selectOne(mappers, paramMap);
	}
	public Map<String, Object> selectOne(String mappers, Map<String, Object> paramMap) {
		return commonDAO.selectOne(mappers, paramMap);
	}
	
	/**
	 * [DML] select table for paging
	 * 
	 * @param request
	 * @param paramMap
	 * @param columns  - the param required to parse String value to Integer
	 * @param mappers  - mybatis mappers(count, list)
	 * @param type     - {1: list, 2: count, 3: one, 4: paged list, 5: check}
	 * @return JsonMap
	 */
	public Map<String, Object> select(DaoType daoType, String[] mappers, Map<String, Object> paramMap, Operation type) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(type.getMapperCnt() != mappers.length) {
			throw new IllegalArgumentException("mapper의 개수가 올바르지 않습니다.");
		}
		
		setDao(daoType);
		
		// general list
		if (type == Operation.getList) {
			resultList = dao.selectList(mappers[0], paramMap);
			result = JsonUtil.makeListJSON(resultList);
		}

		// get count
		else if (type == Operation.getCount) {
			int count = dao.selectInt(mappers[0], paramMap);
			result.put("count", count);
		}

		// get one
		else if (type == Operation.getOne) {
			Map<String, Object> info = dao.selectOne(mappers[0], paramMap);
			result.put("info", info);
		}

		// paged list
		else if (type == Operation.getPagedList) {
			int pageSize = ParamUtil.parseInt(paramMap.get("pageSize"));
			int totalCount = dao.selectInt(mappers[0], paramMap);
			
			resultList = dao.selectList(mappers[1], paramMap);
			
			result.put("last_page", Math.ceil((double) totalCount / pageSize));
			result.put("count", totalCount);
			result.put("data", resultList);
			
			if(resultList.size() > 0) {
				result.put("CODE", "200");
			}
			else {
				result.put("CODE", "210");
			}
		}

		// check
		else if (type == Operation.getCheck) {
			int count = dao.selectInt(mappers[0], paramMap);
			if (count > 0) {
				result.put("CODE", "201");
				result.put("DESC", "이미 사용 중입니다.");
			} 
			else {
				result.put("CODE", "200");
				result.put("DESC", "사용 가능합니다.");
			}
		}
		
		// json이 아닌 일반 HashMap Return (내부 사용용)
		else if (type == Operation.getOneOriMap) {
			return dao.selectOne(mappers[0], paramMap);
		}

		return JsonUtil.makeJSON("result", result);
	}
	
	
	public Map<String, Object> select(DaoType daoType, String[] mappers, Map<String, Object> paramMap, String[] columns, Operation type) throws Exception{
		// parameter 숫자 parsing 처리
		if (columns != null) {
			if(paramMap != null) paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		
		return select(daoType, mappers, paramMap, type);
	}
	
	public Map<String, Object> select(String[] mappers, Map<String, Object> paramMap, String[] columns, Operation type) throws Exception {
		return select(DaoType.System, mappers, paramMap, columns, type);
	}
	
	public Map<String, Object> select(String[] mappers, Map<String, Object> paramMap, Operation type) throws Exception {
		return select(DaoType.System, mappers, paramMap, type);
	}
	
	public Map<String, Object> select(HttpServletRequest request, Map<String, Object> paramMap, String[] columns,
			String[] mappers, Operation type, String sqlSession) throws Exception{
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();
		
		if(sqlSession.equals("sqlSession")){
			result = select(mappers, paramMap, columns ,type);
		}else if (sqlSession.equals("sqlSession5")) {
			// parameter 숫자 parsing 처리
			if (columns != null)
				paramMap = ParamUtil.parseInt(paramMap, columns);
			// json이 아닌 일반 HashMap Return (내부 사용용)
			if (type == Operation.getOneOriMap) {
				return gateDAO.selectOne(mappers[0], paramMap);
			}else if (type == Operation.getList) {
				resultList =  gateDAO.selectList(mappers[0], paramMap);
				result.put("data", resultList);
				return result;
			}else if (type == Operation.getPagedList) {
				resultList = gateDAO.selectList(mappers[0], paramMap);
				
				return JsonUtil.makeListJSON(resultList);
			}else if (type == Operation.getHandler) {
				resultList = gateDAO.selecteHandler(mappers[0], paramMap);
				result.put("data", resultList);
				return result;
			}
		}
		return JsonUtil.makeJSON("result", result);
	}
	
	/**
	 * [DML] select table
	 * 
	 * @param request
	 * @param paramMap
	 * @param mappers  - mybatis mappers(count, list)
	 * @param type     - Operation {getList, getCound, getOne, getCheck, getPagedList}
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> select(HttpServletRequest request, Map<String, Object> paramMap, String[] mappers,
			Operation type) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> result = new HashMap<String, Object>();

		if (type.getMapperCnt() > mappers.length) {
			throw new IllegalArgumentException("mapper의 개수가 올바르지 않습니다.");
		}

		// general list
		if (type == Operation.getList) {
			resultList = dao.selectList(mappers[0], paramMap);
			result.put("itemsCount", resultList.size());
			result.put("items", resultList);
			result.put("CODE", "200");
		}
		// get count
		else if (type == Operation.getCount) {
			int count = dao.selectInt(mappers[0], paramMap);
			result.put("count", count);
			result.put("CODE", "200");
		}

		// get one
		else if (type == Operation.getOne) {
			Map<String, Object> info = dao.selectOne(mappers[0], paramMap);
			result.put("info", info);
			result.put("CODE", "200");
		}

		// get paged list (custom for tabulator)
		else if (type == Operation.getPagedList) {
			int pageSize = ParamUtil.parseInt(paramMap.get("pageSize"));
			int pageIdx = ParamUtil.parseInt(paramMap.get("pageIdx"));
			
			int count = dao.selectInt(mappers[0], paramMap);
			if(count > 0) {
				resultList = dao.selectList(mappers[1], paramMap);
			}

			result.put("last_page", Math.ceil((double) count / pageSize));
			result.put("pageIdx", pageIdx);
			result.put("pageSize", pageSize);
			result.put("count", count);
			result.put("data", resultList);
			result.put("CODE", "200");
		}

		// check
		else if (type == Operation.getCheck) {
			int count = dao.selectInt(mappers[0], paramMap);
			if (count > 0) {
				result.put("CODE", "201");
				result.put("DESC", "이미 사용 중입니다.");
			} else {
				result.put("CODE", "200");
				result.put("DESC", "사용 가능합니다.");
			}
		}

		return JsonUtil.makeJSON("result", result);
	}


	/**
	 * [DML] Create, Update, Delete
	 * 
	 * @param type     - insert, delete, update
	 * @param paramMap
	 * @param columns  - the param required to parse String value to Integer
	 * @param mapper   - mybatis mapper
	 * @return
	 */
	public int serviceCount(DaoType dbType, String mapper, Map<String, Object> paramMap, Operation type)
			throws Exception {
		int cnt = 0;
		
		setDao(dbType);
		
		if (type == Operation.insert) {
			cnt = dao.insert(mapper, paramMap);
		} 
		else if (type == Operation.update) {
			cnt = dao.update(mapper, paramMap);
		} 
		else if (type == Operation.delete) {
			cnt = dao.delete(mapper, paramMap);
		}
		else {
			throw new IllegalArgumentException("지정된 type을 사용하세요!");
		}
		return cnt;
	}
	public int serviceCount(String mapper, Map<String, Object> paramMap, Operation type) throws Exception {
		return serviceCount(DaoType.System, mapper, paramMap, type);
	}
	public int serviceCount(String mapper, Map<String, Object> paramMap, String[] columns, Operation type) throws Exception {
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		return serviceCount(DaoType.System, mapper, paramMap, type);
	}
	public int serviceCount(DaoType dbType, String mapper, Map<String, Object> paramMap, String[] columns, Operation type) throws Exception {
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		return serviceCount(dbType, mapper, paramMap, type);
	}
	
	public Map<String, Object> service(DaoType dbType, String mapper, Map<String, Object> paramMap, Operation type)
			throws Exception {
		setDao(dbType);
		
		int cnt = serviceCount(dbType, mapper, paramMap, type);
		if (cnt > 0) {
			return JsonUtil.makeResultJSON("200");
		}
		
		return JsonUtil.makeResultJSON("400");
	}
	
	public Map<String, Object> service(DaoType dbType, String mapper, Map<String, Object> paramMap, String[] columns, Operation type)
			throws Exception {
		// parameter 숫자 parsing 처리
		if (columns != null) {
			paramMap = ParamUtil.parseInt(paramMap, columns);
		}
		
		return service(dbType, mapper, paramMap, type);
	}
	
	public Map<String, Object> service(String mapper, Map<String, Object> paramMap, String[] columns, Operation type) throws Exception {
		return service(DaoType.System, mapper, paramMap, columns, type);
	}
	public Map<String, Object> service(String mapper, Map<String, Object> paramMap, Operation type) throws Exception {
		return service(DaoType.System, mapper, paramMap, type);
	}
	
	/**
	 * [DML] Create, Update, Delete
	 * 
	 * @param type     - Operation {INSERT, DELETE, UPDATE}
	 * @param paramMap
	 * @param columns  - the param required to parse String value to Integer
	 * @param mapper   - mybatis mapper
	 * @return
	 */
	public Map<String, Object> service(HttpServletRequest request, Map<String, Object> paramMap, Operation type,
			String[] columns, String mapper) throws Exception {
		int cnt = 0;

		// parameter 숫자 parsing 처리
		if (columns != null)
			paramMap = ParamUtil.parseInt(paramMap, columns);

		if (type == Operation.insert) {
			cnt = dao.insert(mapper, paramMap);
		}
		else if (type == Operation.delete) {
			cnt = dao.delete(mapper, paramMap);
		}
		else if (type == Operation.update) {
			cnt = dao.update(mapper, paramMap);
		}
		else {
			throw new IllegalArgumentException("지정된 type을 사용하세요!");
		}

		if (cnt > 0) {
			return JsonUtil.makeResultJSON("200");
		}
		return JsonUtil.makeResultJSON("400");

	}
}
