package com.enicom.nims.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class ExcelUtil {
	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
	
	public static List<Map<String, Object>> read(ExcelOption readOption) {
		Workbook wb = getWorkbook(readOption.getFilePath());
		Sheet sheet = wb.getSheetAt(0);

		int numOfRows = sheet.getPhysicalNumberOfRows();
		int numOfCells = 0;

		Row row = null;
		Cell cell = null;

		String cellName = "";

		Map<String, Object> map = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (int rowIndex = readOption.getStartRow() - 1; rowIndex < numOfRows; rowIndex++) {
			row = sheet.getRow(rowIndex);
			if (row != null) {
				numOfCells = row.getLastCellNum();
				map = new LinkedHashMap<String, Object>();

				for (int cellIndex = 0; cellIndex < numOfCells; cellIndex++) {
					cell = row.getCell(cellIndex);
					cellName = getCellName(cell, cellIndex);

					if (ParamUtil.parseString(getCellValue(cell)).equals("")) {
						continue;
					}

					map.put(cellName, getCellValue(cell));
				}

				result.add(map);
			}
		}

		return result;
	}

	/**
	 * [ExcelUtil] Workbook 반환
	 * 
	 * @param file
	 * @return
	 */
	public static Workbook getWorkbook(String filePath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		Workbook wb = null;

		if (filePath.endsWith(".xls")) {
			try {
				wb = new HSSFWorkbook(fis);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		} else if (filePath.endsWith(".xlsx")) {
			try {
				wb = new XSSFWorkbook(fis);
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}

		return wb;
	}

	/**
	 * [ExcelUtil] Cell Column Name - Cell의 Column Name 반환(A,B,C..), Cell이 Null이면
	 * cellIndex 반환
	 * 
	 * @param cell
	 * @param cellIndex
	 * @return
	 */
	public static String getCellName(Cell cell, int cellIndex) {
		int cellNum = 0;
		if (cell != null) {
			cellNum = cell.getColumnIndex();
		} else {
			cellNum = cellIndex;
		}

		return CellReference.convertNumToColString(cellNum);
	}

	/**
	 * [ExcelUtil] Cell 값 반환
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(Cell cell) {
		String value = "";

		if (cell == null) {
			value = "";
		} else {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_FORMULA:
				value = cell.getCellFormula();
				break;

			case Cell.CELL_TYPE_NUMERIC:
				value = (int) cell.getNumericCellValue() + "";
				break;

			case Cell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;

			case Cell.CELL_TYPE_BOOLEAN:
				value = cell.getBooleanCellValue() + "";
				break;

			case Cell.CELL_TYPE_BLANK:
				value = "";
				break;

			case Cell.CELL_TYPE_ERROR:
				value = cell.getErrorCellValue() + "";
				break;
			default:
				value = cell.getStringCellValue();
			}
		}
		return value;
	}

	/**
	 * [FileUtil] 서지정보 엑셀 파싱
	 * 
	 * @param request
	 * @param paramMap
	 * @param destFile
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> parseBookFile(MultipartHttpServletRequest request,
			Map<String, Object> paramMap, File destFile) throws Exception {
		Set<String> noSet = new HashSet<String>();
		int passCnt = 0;
		List<Map<String, Object>> bookList = new ArrayList<Map<String, Object>>();
		
		/**
		 * 엑셀정보
		 */
		ExcelOption excelOptions = new ExcelOption();
		excelOptions.setFilePath(destFile.getAbsolutePath());
		excelOptions.setStartRow(ParamUtil.parseInt(paramMap.get("start_row"), 1));

		List<Map<String, Object>> excelContent = ExcelUtil.read(excelOptions);
		if(excelContent.size() < 1) {
			throw new Exception("추가할 서지정보가 없습니다!");
		}
		
		Map<String, Object> header = excelContent.get(0);
		parseBookHeader(header);
		
		for (Map<String, Object> temp: excelContent.subList(1, excelContent.size() - 1)) {
			if (temp == null) {
				passCnt++;
				continue;
			}
			if(header.keySet().size() < temp.keySet().size()-3) {
				throw new Exception("컬럼 헤더의 올바른 시작위치를 입력해주세요!");
			}
			
			Map<String, Object> article = new HashMap<String, Object>();
			for (String key : temp.keySet()) {
				String headerKey = ParamUtil.parseString(header.get(key));
				if(!headerKey.equals("")) {
					article.put(headerKey, temp.get(key));
				}
			}
			
			if(article.isEmpty()) {
				passCnt++;
				continue;
			}
			
			String book_no = ParamUtil.parseString(article.get("book_no"));
			String status = ParamUtil.parseString(article.get("status")).trim();
			String create_date = ParamUtil.parseString(article.get("create_date"));
			
			if("".equals(book_no) || book_no.indexOf(" ") > -1 || noSet.contains(book_no)) {
				passCnt++;
				continue;
			}
			noSet.add(book_no);

			// get_type 데이터가공
			if (status.equals("대출가능")) {
				article.put("status", 0);
			}
			else if (status.equals("대출중")) {
				article.put("status", 1);
			}
			else {
				article.put("status", 2);
			}
			
			// creat_date 데이터가공
			if (create_date.contains(".")) {
				article.put("create_date", create_date.replace(".", "-"));
			}
			
			bookList.add(article);
		}
		
		logger.info("Excel Size [Origin: {}, Parsed: {}, Pass: {}]", excelContent.size(), bookList.size(), passCnt);
		if(bookList.isEmpty()) {
			throw new Exception("새로 등록할 서지 정보가 없습니다!");
		}
		
		destFile.delete();
		return bookList;
	}
	
	/**
	 * [ExcelUtil] Book Excel Header 파싱
	 * @param header
	 * @return
	 */
	public static Map<String, Object> parseBookHeader(Map<String, Object> header) {
		Map<String, String> words = new HashMap<String, String>();
		words.put("등록번호", "book_no");
		words.put("제목", "title");
		words.put("자료명", "title");
		words.put("도서명", "title");
		words.put("서명", "title");
		words.put("저자", "author");
		words.put("발행처", "publisher");
		words.put("출판사", "publisher");
		words.put("발행년도", "publish_year");
		words.put("발행연도", "publish_year");
		words.put("출판일", "publish_year");
		words.put("출간년도", "publish_year");
		words.put("출간연도", "publish_year");
		words.put("출판년도", "publish_year");
		words.put("출판연도", "publish_year");
		words.put("청구기호", "call_no");
		words.put("자료상태", "status");
		words.put("도서상태", "status");
		words.put("등록일", "create_date");
		words.put("소장처", "location");
		words.put("위치", "location");
		
		for (String key : header.keySet()) {
			String value = ParamUtil.parseString(header.get(key));
			for (String word : words.keySet()) {
				String afterWord = words.get(word);

				if (value.contains(word)) {
					value = afterWord;
					break;
				}
			}

			header.put(key, value);
		}

		return header;
	}
}
