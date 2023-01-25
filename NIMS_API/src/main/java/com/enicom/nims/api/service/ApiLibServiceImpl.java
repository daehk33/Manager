package com.enicom.nims.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.DaoType;
import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service
public class ApiLibServiceImpl implements ApiLibService {
	private ServiceUtil serviceUtil;

	@Autowired
	public ApiLibServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public List<Map<String, Object>> getApiLibMemberList(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.Kolas, "kolas.getApiLibMemberList", paramMap);
	}

	@Override
	public List<Map<String, Object>> getApiLibBookList(Map<String, Object> paramMap) throws Exception {
		return serviceUtil.selectList(DaoType.Kolas, "kolas.getBookList", paramMap);
	}

	@Override
	public Map<String, Object> getlibMemberInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "libraryMember.getLibMemberInfo" };
		String[] columns = { "library_key", "lib_member_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertLibMemberInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key" };
		return serviceUtil.service("libraryMember.insertLibMemberInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateLibMemberInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "member_grade", "library_key" };
		return serviceUtil.service("libraryMember.updateLibMemberInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> deleteLibMemberInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("libraryMember.deleteLibMemberInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> getlibBookInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "libraryBook.getLibBookInfo" };
		String[] columns = { "library_key" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}

	@Override
	public Map<String, Object> insertLibBookInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "book_cnt" };
		return serviceUtil.service("libraryBook.insertLibBookInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> updateLibBookInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key", "book_cnt" };
		return serviceUtil.service("libraryBook.updateLibBookInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> deleteLibBookInfo(Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("libraryBook.deleteLibBookInfo", paramMap, columns, Operation.delete);
	}
}
