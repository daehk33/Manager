package com.enicom.nims.dao;

public enum Operation {
	getList(1), getCount(1), getOne(1), getOneOriMap(1), getPagedList(2), getCheck(1), insert(1), delete(1), update(1), getHandler(1);
	private int mapperCnt;
	
	Operation(int mapperCnt){
		this.mapperCnt = mapperCnt;
	}
	public int getMapperCnt() {
		return mapperCnt;
	}
}
