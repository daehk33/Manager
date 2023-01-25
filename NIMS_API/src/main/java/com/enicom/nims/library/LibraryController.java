package com.enicom.nims.library;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.library.service.LibraryService;

@RestController
public class LibraryController {
	private LibraryService libraryService;

	@Autowired
	public LibraryController(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	/**
	 * [조회] 도서관 아이디 중복여부 확인
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/checkLibraryDuplicated", method = { RequestMethod.POST })
	public Map<String, Object> checkLibraryDuplicated(HttpServletRequest request,
			@RequestParam Map<String, Object> paramMap) throws Exception {
		return libraryService.checkLibraryDuplicated(request, paramMap);
	}

	/**
	 * [조회] 도서관 정보 수량 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/getLibraryCount", method = { RequestMethod.POST })
	public Map<String, Object> getLibraryCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.getLibraryCount(request, paramMap);
	}

	/**
	 * [조회] 도서관 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/getLibraryList", method = { RequestMethod.POST })
	public Map<String, Object> getLibraryList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.getLibraryList(request, paramMap);
	}

	/**
	 * [등록] 도서관 정보 등록
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @도서관 ID 중복 여부 확인
	 */
	@RequestMapping(value = "/library/insertLibraryInfo", method = { RequestMethod.POST })
	public Map<String, Object> insertLibraryInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.insertLibraryInfo(request, paramMap);
	}

	/**
	 * [삭제] 도서관 정보 삭제
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @도서관 내 등록된 장비가 있으면 삭제 불가
	 */
	@RequestMapping(value = "/library/deleteLibraryInfo", method = { RequestMethod.POST })
	public Map<String, Object> deleteLibraryInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.deleteLibraryInfo(request, paramMap);
	}

	/**
	 * [수정] 도서관 정보 수정
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @도서관 ID 중복 여부 확인
	 */
	@RequestMapping(value = "/library/updateLibraryInfo", method = { RequestMethod.POST })
	public Map<String, Object> updateLibraryInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.updateLibraryInfo(request, paramMap);
	}

	/**
	 * [조회] 도서관 회원수 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/getLibMemberCount", method = { RequestMethod.POST })
	public Map<String, Object> getLibMemberCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.getLibMemberCount(request, paramMap);
	}

	/**
	 * [조회] 도서관 회원 정보 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/getLibMemberList", method = { RequestMethod.POST })
	public Map<String, Object> getLibMemberList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.getLibMemberList(request, paramMap);
	}

	/**
	 * [조회] 도서관 보유 도서 수량 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/getLibBookCount", method = { RequestMethod.POST })
	public Map<String, Object> getLibBookCount(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.getLibBookCount(request, paramMap);
	}

	/**
	 * [조회] 도서관 보유 도서 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/getLibBookList", method = { RequestMethod.POST })
	public Map<String, Object> getLibBookList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.getLibBookList(request, paramMap);
	}
	
	/**
	 * [조회] 도서관 서버 여부 조회
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/library/checkLibServer", method = { RequestMethod.POST })
	public Map<String, Object> checkLibraryServer(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return libraryService.checkLibServer(request, paramMap);
	}
}
