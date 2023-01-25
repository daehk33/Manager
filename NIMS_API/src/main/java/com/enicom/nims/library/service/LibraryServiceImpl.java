package com.enicom.nims.library.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.dao.Operation;
import com.enicom.nims.dao.ServiceUtil;

@Service(value = "libraryService")
public class LibraryServiceImpl implements LibraryService {
	private ServiceUtil serviceUtil;

	@Autowired
	public LibraryServiceImpl(ServiceUtil serviceUtil) {
		this.serviceUtil = serviceUtil;
	}

	@Override
	public Map<String, Object> checkLibraryDuplicated(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "library.checkLibraryDuplicated" };
		String[] columns = { "rec_key" };

		return serviceUtil.select(mappers, paramMap, columns, Operation.getCheck);
	}

	@Override
	public Map<String, Object> getLibraryCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "library.getLibraryCount" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getLibraryList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "library.getLibraryCount", "library.getLibraryList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public List<Map<String, Object>> getLibraryDBConnList(Map<String, Object> paramMap)
			throws Exception {
		return serviceUtil.selectList("library.getLibraryDBConnList", paramMap);
	}

	@Override
	public Map<String, Object> insertLibraryInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("library.insertLibraryInfo", paramMap, columns, Operation.insert);
	}

	@Override
	public Map<String, Object> deleteLibraryInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("library.deleteLibraryInfo", paramMap, columns, Operation.delete);
	}

	@Override
	public Map<String, Object> updateLibraryInfo(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		return serviceUtil.service("library.updateLibraryInfo", paramMap, columns, Operation.update);
	}

	@Override
	public Map<String, Object> getLibMemberCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "libraryMember.getLibMemberCount" };
		String[] columns = { "rec_key", "library_key", "device_key", "member_no" };

		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getLibMemberList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "libraryMember.getLibMemberCount", "libraryMember.getLibMemberList" };
		String[] columns = { "rec_key", "library_key", "device_key" };

		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> getLibBookCount(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] mappers = { "libraryBook.getLibBookCount" };
		String[] columns = { "rec_key", "library_key", "device_key" };

		return serviceUtil.select(mappers, paramMap, columns, Operation.getCount);
	}

	@Override
	public Map<String, Object> getLibBookList(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key", "library_key", "device_key" };
		String[] mappers = { "libraryBook.getLibBookCount", "libraryBook.getLibBookList" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getPagedList);
	}

	@Override
	public Map<String, Object> checkLibServer(HttpServletRequest request, Map<String, Object> paramMap)
			throws Exception {
		String[] columns = { "rec_key" };
		String[] mappers = { "library.checkLibServer" };
		return serviceUtil.select(mappers, paramMap, columns, Operation.getOneOriMap);
	}
}
