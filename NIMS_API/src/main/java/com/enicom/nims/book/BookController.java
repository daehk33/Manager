package com.enicom.nims.book;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enicom.nims.book.service.BookService;

@RestController
public class BookController {
	private BookService service;

	@Autowired
	public BookController(BookService service) {
		this.service = service;
	}

	/**
	 * [조회] 서지 정보 조회
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/book/getBookList", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getBookList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return service.getBookList(request, paramMap);
	}

	/**
	 * [추가] 서지 정보 추가
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/book/add", method = RequestMethod.POST)
	public Map<String, Object> insertBookInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return service.insertBookInfo(request, paramMap);
	}

	/**
	 * [조회] 서지 정보 삭제
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/book/delete", method = RequestMethod.POST)
	public Map<String, Object> deleteBookInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return service.deleteBookInfo(request, paramMap);
	}

	/**
	 * [수정] 서지 정보 수정
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/book/update", method = RequestMethod.POST)
	public Map<String, Object> updateBookInfo(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		return service.updateBookInfo(request, paramMap);
	}

	/**
	 * [추가] 엑셀로 서지 정보 추가
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/book/addList", method = RequestMethod.POST)
	public Map<String, Object> insertBookList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) 
			throws Exception {
		
		return service.insertBookList(request, paramMap);
	}

}
