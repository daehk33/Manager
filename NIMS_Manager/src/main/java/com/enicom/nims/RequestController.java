package com.enicom.nims;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.enicom.nims.login.SessionManager;
import com.enicom.nims.utils.ExcelUtil;
import com.enicom.nims.utils.FileUtil;
import com.enicom.nims.utils.HttpUtil;
import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@RestController
public class RequestController {
	@Value("#{config['api.url']}")
	private String api_url;

	@Value("#{config['file.path.book']}")
	private String bookPath;
	
	@Value("#{config['sftp.localImagePath']}")
	private String localImagePath;
	
	/**
	 * [공통] Request Mapping
	 * 
	 * @param request
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/api/{category}/{page}", method = RequestMethod.POST)
	public Map<String, Object> apiRequestManager(HttpServletRequest request, @RequestParam Map<String, Object> paramMap,
			@PathVariable String category, @PathVariable String page) throws Exception {
		return HttpUtil.send(api_url, String.format("/%s/%s", category, page), paramMap);
	}
	
	@RequestMapping(value = "/api/book/excel", method = RequestMethod.POST)
	public Map<String, Object> insertBookList(MultipartHttpServletRequest request, @RequestParam Map<String, Object> paramMap) 
			throws Exception {
		
		MultipartFile excelFile = request.getFile("file");
		if (excelFile == null || excelFile.isEmpty()) {
			throw new RuntimeException("엑셀파일을 선택 해 주세요.");
		}
		File file = FileUtil.uploadFile(bookPath, excelFile, "temp");
		if(file == null) {
			throw new RuntimeException("올바르지 않은 파일입니다.");
		}
		if("Y".equals(ParamUtil.parseString(paramMap.get("rewrite")))) {
			HttpUtil.send(api_url, "/book/delete", paramMap);
		}
		
		List<Map<String, Object>> bookList = ExcelUtil.parseBookFile(request, paramMap, file);
		FileUtil.deleteFile(bookPath, file.getName());
		
		for(Map<String, Object> book: bookList) {
			book.putAll(HttpUtil.getBasicInfoForApi(paramMap));
			HttpUtil.send(api_url, "/book/add", book);
		}
		
		return JsonUtil.makeResultJSON("200");
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/api/common/getMenuList", method = RequestMethod.POST)
	public Map<String, Object> getMenuList(HttpServletRequest request, @RequestParam Map<String, Object> paramMap) throws Exception {
		Map<String, Object> menuList = HttpUtil.send(api_url, "/common/getMenuList", paramMap);

		SessionManager.setMenuList(request, ((Map<String, Object>) menuList.get("result")).get("items"));
		return menuList;
	}

	/**
	 * @Desc Theme 지정
	 */
	@RequestMapping(value = "/api/manager/updateManagerTheme")
	public Map<String, Object> setTheme(HttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {
		String theme = ParamUtil.parseString(paramMap.get("theme"));
		SessionManager.setTheme(request, theme);
		
		return HttpUtil.send(api_url, "/manager/updateManagerTheme", paramMap);
	}

	/**
	 * @Desc 대출반납기 배너 이미지 등록
	 */
	@RequestMapping(value = "/api/loanReturn/saveBannerImage", method = RequestMethod.POST)
	public Map<String, Object> saveBannerImage(MultipartHttpServletRequest request, @RequestParam Map<String, Object> paramMap)
			throws Exception {

		Iterator<String> fileNames = null;
		fileNames = request.getFileNames();

		String device_id = request.getHeader("device_id");
		String path = localImagePath + device_id + "/";

		File dir = new File(path);

		if (!dir.exists()) {
			try {
				dir.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return JsonUtil.makeResultJSON("400");
			}
		}

		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = request.getFile(fileName);

			InputStream inputStream = null;
			OutputStream outputStream = null;

			try {
				if (mFile.getSize() > 0) {
					inputStream = mFile.getInputStream();
					outputStream = new FileOutputStream(path + mFile.getOriginalFilename());

					int readByte = 0;
					byte[] buffer = new byte[8192];

					while ((readByte = inputStream.read(buffer, 0, 8120)) != -1) {
						outputStream.write(buffer, 0, readByte);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return JsonUtil.makeResultJSON("400");
			} finally {
				inputStream.close();
				outputStream.close();
			}
		}

		return JsonUtil.makeResultJSON("200");
	}
}
