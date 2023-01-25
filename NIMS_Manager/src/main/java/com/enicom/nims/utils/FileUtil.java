package com.enicom.nims.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
	private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * [FileUtil] 특정 경로(path)에 있는 파일 목록 불러오기
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Object> getFileList(String path, String searchName) throws IOException {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		File dir = new File(path);
		if (!dir.exists()) {
			// 경로가 없을 경우 생성
			dir.mkdirs();
			logger.error("잘못된 폴더 경로입니다. path: {}", path);
			return JsonUtil.makeResultJSON("400", "폴더 경로를 찾을 수 없습니다.");
		}

		File files[] = dir.listFiles(new FilenameFilter() {
			// 파일명 필터
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(searchName);
			}
		});
		BasicFileAttributes attrs;

		for (File file : files) {
			// 폴더인 경우 pass
			if (file.isDirectory())
				continue;

			Map<String, Object> result = new HashMap<String, Object>();

			attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			FileTime create_date = attrs.creationTime();
			FileTime edit_date = attrs.lastModifiedTime();
			FileTime access_date = attrs.lastAccessTime();

			result.put("name", file.getName());
			result.put("size", file.length());
			result.put("create_date", sdf.format(new Date(create_date.toMillis())));
			result.put("edit_date", sdf.format(new Date(edit_date.toMillis())));
			result.put("access_date", sdf.format(new Date(access_date.toMillis())));

			resultList.add(result);
		}

		return JsonUtil.makeJSON("result", JsonUtil.makeListJSON(resultList));
	}

	/**
	 * [FileUtil] 파일 업로드
	 * 
	 * @param path
	 * @param uploadFile
	 * @param fileName
	 * @param rec_key
	 * @return
	 * @throws Exception
	 */
	public static File uploadFile(String path, MultipartFile uploadFile, String fileName) throws Exception {
		// files 폴더는 직접 생성해주지 않으면 Not Found Error.
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
			logger.info("폴더가 생성되었습니다. folder path: {}", folder);
		}

		String newFileName = "";
		String originFileName = "";
		
		OutputStream out = null;
		PrintWriter printWriter = null;
		try {
			originFileName = uploadFile.getOriginalFilename();
			newFileName = fileName;

			logger.info("* originFileName: {}, newFileName: {}", originFileName, newFileName);
			String fileExt = originFileName.substring(originFileName.lastIndexOf('.') + 1);

			// 웹 쉘 확장자를 가진 경우
			String[] candidates = { "jpg", "png", "jpeg", "xlsx", "xls" };
			if (Arrays.stream(candidates).filter(ext -> ext.equalsIgnoreCase(fileExt)).count() < 1) {
				return null;
			}

			byte[] bytes = uploadFile.getBytes();
			File file = new File(path);

			// 파일명 중복체크
			if (newFileName != null && !newFileName.equals("")) {
				// 파일이 존재할 경우 덮어쓰기
				if (file.exists()) {
					file = new File(String.format("%s\\%s.%s", path, newFileName, fileExt));

					// 실행 가능한 파일이면 실행권한 제거
					if (file.canExecute()) {
						file.setExecutable(false);
					}
				}
			}

			out = new FileOutputStream(file);
			out.write(bytes);
			return file;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (out != null)
					out.close();
				if (printWriter != null)
					printWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Map<String, Object> renameFile(String path, String origin, String change) {
		File file = new File(String.format("%s\\%s", path, origin));
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			logger.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(),
					file.canRead());
			return JsonUtil.makeResultJSON("400", "파일이 없거나 읽을 수 없습니다");
		}

		File newFile = new File(String.format("%s\\%s", path, change));
		if (file.renameTo(newFile)) {
			return JsonUtil.makeResultJSON("200", "파일명이 성공적으로 수정되었습니다.");
		} else if (newFile.exists()) {
			return JsonUtil.makeResultJSON("400", "이미 있는 파일명으로 수정할 수 없습니다.");
		} else {
			return JsonUtil.makeResultJSON("400");
		}
	}

	public static Map<String, Object> writeFile(String fileName, String content) {
		OutputStream out = null;
		PrintWriter printWriter = null;

		File file = new File(fileName);

		// 파일이 존재할 경우 덮어쓰기
		if (file.exists()) {
			file = new File(fileName);

			// 실행 가능한 파일이면 실행권한 제거
			if (file.canExecute()) {
				file.setExecutable(false);
			}
		}
		try {
			out = new FileOutputStream(file);
			out.write(content.getBytes("UTF-8"));

		} catch (IOException e) {
			return JsonUtil.makeResultJSON("400", e.getMessage());
		} finally {
			try {
				if (out != null)
					out.close();
				if (printWriter != null)
					printWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return JsonUtil.makeResultJSON("200");
	}

	/**
	 * [FileUtil] 파일 삭제
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String path, String fileName) {
		boolean result = false;

		File file = new File(String.format("%s\\%s", path, fileName));
		if (file.exists()) {
			result = file.delete();
		}

		return result;
	}

	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String path,
			String fileName) throws Exception {
		File file = new File(String.format("%s\\%s", path, fileName));
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			logger.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(),
					file.canRead());
			return;
		}

		response.setContentType("application/octet-stream; charset=utf-8");
		response.setContentLength((int) file.length());

		String browser = getBrowser(request);
		String disposition = getDisposition(fileName, browser);

		response.setHeader("Content-Disposition", disposition);
		response.setHeader("Content-Transfer-Encoding", "binary");

		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		fis = new FileInputStream(file);
		FileCopyUtils.copy(fis, out);
		if (fis != null)
			fis.close();
		out.flush();
		out.close();
	}

	private static String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
			return "MSIE";
		else if (header.indexOf("Chrome") > -1)
			return "Chrome";
		else if (header.indexOf("Opera") > -1)
			return "Opera";
		return "Firefox";
	}

	private static String getDisposition(String filename, String browser) throws UnsupportedEncodingException {
		String dispositionPrefix = "attachment;filename=";
		String encodedFilename = null;
		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		}
		return dispositionPrefix + encodedFilename;
	}
}