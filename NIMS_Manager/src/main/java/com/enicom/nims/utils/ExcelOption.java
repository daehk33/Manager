package com.enicom.nims.utils;

public class ExcelOption {
	private String filePath; // 엑셀파일의 경로
	private int startRow; // 추출을 시작할 행 번호

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
}