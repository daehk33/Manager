package com.enicom.nims.dao;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.enicom.nims.utils.ParamUtil;

@Configuration
public class DatabaseConfig {
	private Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

	@Value("#{config['jdbc.driverClass']}")
	private String driverClass;

	@Value("#{config['jdbc.url']}")
	private String url;

	@Value("#{config['jdbc.username']}")
	private String username;

	@Value("#{config['jdbc.password']}")
	private String password;

	@Value("#{config['schedule.Smart']}")
	private String scheduleOption_Smart;

	@Value("#{config['schedule.LoanReturn']}")
	private String scheduleOption_LoanReturn;

	@Value("#{config['schedule.ResvLoan']}")
	private String scheduleOption_ResvLoan;

	@Value("#{config['schedule.Gate']}")
	private String scheduleOption_Gate;

	@Value("#{config['schedule.AntiLost']}")
	private String scheduleOption_AntiLost;

	private CommonDAO commonDao;
	private DataSource dataSource;

	@Autowired
	public DatabaseConfig(CommonDAO commonDao) {
		this.commonDao = commonDao;
		this.dataSource = createDataSource(driverClass, url, username, password);
	}

	@Bean(name = "dataSource2")
	public DataSource createRouterDatasourceKolas() {
		try {
			AbstractRoutingDataSource routingDataSource = new RoutingDataSource();
			Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

			List<Map<String, Object>> libraryList = commonDao.selectList("library.getLibraryDBConnList",
					Collections.emptyMap());
			String db_key;
			String db_classname;
			String db_url;
			String db_user;
			String db_password;

			if (libraryList.size() > 0) {
				for (Map<String, Object> libInfo : libraryList) {
					db_key = ParamUtil.parseString(libInfo.get("library_id"));
					db_classname = ParamUtil.parseString(libInfo.get("db_classname").toString());
					db_url = ParamUtil.parseString(libInfo.get("db_url").toString());
					db_user = ParamUtil.parseString(libInfo.get("db_username").toString());
					db_password = ParamUtil.parseString(libInfo.get("db_password").toString());

					targetDataSources.put(db_key, createDataSource(db_classname, db_url, db_user, db_password));
				}
			}
			if (targetDataSources.size() > 0) {
				routingDataSource.setTargetDataSources(targetDataSources);
			} else {
				targetDataSources.put("0", dataSource);
				routingDataSource.setTargetDataSources(targetDataSources);
			}
			return routingDataSource;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Bean(name = "dataSource3")
	public DataSource createRouterDatasourceSmartDB() {
		return createAbstractRoutingDataSource("6", "스마트도서관", scheduleOption_Smart);
	}

	@Bean(name = "dataSource4")
	public DataSource createRouterDatasourceGallery() {
		return createAbstractRoutingDataSource("8", "예약대출기", scheduleOption_ResvLoan);
	}

	@Bean(name = "dataSource5")
	public DataSource createRouterDatasourceGate() {
		return createAbstractRoutingDataSource("2", "출입게이트", scheduleOption_Gate);
	}

	@Bean(name = "dataSource6")
	public DataSource createRouterDatasourceLoanReturn() {
		return createAbstractRoutingDataSource("1,9,10", "대출반납기", scheduleOption_LoanReturn);
	}

	@Bean(name = "dataSource7")
	public DataSource createRouterDatasourceAntiLost() {
		return createAbstractRoutingDataSource("5", "분실방지기", scheduleOption_AntiLost);
	}

	public DataSource createDataSource(String driverClass, String url, String user, String password) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setUrl(url);

		return dataSource;
	}

	public DataSource createAbstractRoutingDataSource(String modelKey, String modelName, String use_yn) {
		try {
			AbstractRoutingDataSource routingDataSource = new RoutingDataSource();
			Map<Object, Object> targetDataSources = new HashMap<Object, Object>();

			Map<String, Object> deviceParams = new HashMap<String, Object>();

			String query = "device.getDeviceDBConnList";
			if (modelKey.equals("5")) {
				// 분실 방지기면 쿼리 변경
				query = "apiAntilost.getDBConnList";
			}

			// Model Key 파싱
			if (!modelKey.contains(",")) {
				deviceParams.put("model_key", Integer.parseInt(modelKey));
			} else {
				int[] modelKeyArry = Arrays.stream(modelKey.split(",")).mapToInt(Integer::parseInt).toArray();
				deviceParams.put("model_key_array", modelKeyArry);
			}
			
			logger.info("<<< [{}] MODEL {} * CONECTION INFO >>>", modelName, modelKey);
			if (use_yn.equals("Y")) {
				List<Map<String, Object>> deviceList = commonDao.selectList(query, deviceParams);
				if(deviceList == null || deviceList.isEmpty()) {
					logger.info("> Result: connection not found");
				}
				else {
					logger.info("> Result: connection {} found", deviceList.size());
				}
				
				String db_key;
				String db_classname;
				String db_url;
				String db_user;
				String db_password;
				
				for (Map<String, Object> device : deviceList) {
					db_key = ParamUtil.parseString(device.get("device_id"));
					if (modelKey.equals("5")) {
						db_key = ParamUtil.parseString(device.get("rec_key"));
					}
					db_classname = ParamUtil.parseString(device.get("db_classname"));
					db_url = ParamUtil.parseString(device.get("db_url"));
					db_user = ParamUtil.parseString(device.get("db_username"));
					db_password = ParamUtil.parseString(device.get("db_password"));

					DataSource datasource = createDataSource(db_classname, db_url, db_user, db_password);
					Connection testconn = datasource.getConnection();

					logger.info("> [Connection] {}", testconn);

					targetDataSources.put(db_key, datasource);
				}
			}
			else {
				logger.info("> Result: scheduling not used");
			}
			if (targetDataSources.size() > 0) {
				routingDataSource.setTargetDataSources(targetDataSources);
			} else {
				targetDataSources.put("0", dataSource);
				routingDataSource.setTargetDataSources(targetDataSources);
			}
			return routingDataSource;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
