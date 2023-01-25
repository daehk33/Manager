package com.enicom.nims.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
	private static Logger logger = LoggerFactory.getLogger(RoutingDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		String type = DBContextHolder.getDBType();
		logger.debug("접속한 DB Key: {}", type);
		
		return type;
	}
}