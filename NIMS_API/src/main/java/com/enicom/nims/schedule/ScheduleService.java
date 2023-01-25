package com.enicom.nims.schedule;

import static java.lang.Math.min;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enicom.nims.api.service.ApiAntiLostService;
import com.enicom.nims.api.service.ApiGateService;
import com.enicom.nims.api.service.ApiLibService;
import com.enicom.nims.api.service.ApiLoanReturnService;
import com.enicom.nims.api.service.ApiResvLoanService;
import com.enicom.nims.api.service.ApiReturnService;
import com.enicom.nims.api.service.ApiSLSService;
import com.enicom.nims.dao.DBContextHolder;
import com.enicom.nims.device.service.DeviceService;
import com.enicom.nims.library.service.LibraryService;
import com.enicom.nims.utils.ParamUtil;

@Service
public class ScheduleService {
	private Logger logger = LoggerFactory.getLogger(ScheduleService.class);

	@Autowired
	private LibraryService librarayService;

	@Autowired
	private DeviceService deviceService;

	@Autowired
	private ApiLibService apiLibService;

	@Autowired
	private ApiSLSService apiSlsService;

	@Autowired
	private ApiResvLoanService apiResvLoanService;

	@Autowired
	private ApiGateService apiGateService;

	@Autowired
	private ApiLoanReturnService apiLoanReturnService;

	@Autowired
	private ApiReturnService apiReturnService;

	@Autowired
	private ApiAntiLostService apiAntiLostService;
	
	// 공통 스케줄러
	public void scheduleCommonInfoRun(String type) throws Exception {
		if (type.equals("10_sec")) {
			logger.info("scheduleCommonInfoRun10Second is Running >> " + LocalDateTime.now());

			scheduleDeviceNetworkCheck();
			Thread.sleep(1000);

			logger.info("scheduleCommonInfoRun10Second is Ended >> " + LocalDateTime.now());
		}
	}

	// 스마트도서관 관련 스케줄러
	public void scheduleSLSInfoRun(String type) throws Exception {
		if (type.equals("init")) {
			logger.info("scheduleSLSInfoRunInit is Running >> " + LocalDateTime.now());

			scheduleSLSBookInfoRun("");
			Thread.sleep(1000);

			scheduleSLSCasInfoRun();
			Thread.sleep(1000);

			scheduleSLSRuleInfoRun();
			Thread.sleep(1000);

			scheduleSLSDrumRuleInfoRun();
			Thread.sleep(1000);

			scheduleSLSUseLogInfoRun(0);
			Thread.sleep(1000);

			scheduleSLSReservationInfoRun(0);
			Thread.sleep(1000);

			scheduleSLSReturnInfoRun(0);
			Thread.sleep(1000);

			scheduleSLSBookOutInfoRun();

			logger.info("scheduleSLSInfoRunInit is Ended >> " + LocalDateTime.now());

		} else if (type.equals("day")) {
			logger.info("scheduleSLSInfoRunDay is Running >> " + LocalDateTime.now());

//			scheduleLibUsers();
//			Thread.sleep(1000);

//			scheduleLibBooks();
//			Thread.sleep(1000);

			scheduleSLSBookInfoRun("");

			logger.info("scheduleSLSInfoRunDay is Ended >> " + LocalDateTime.now());

		} else if (type.equals("hour")) {
			logger.info("scheduleSLSInfoRunHour is Running >> " + LocalDateTime.now());

			scheduleSLSCasInfoRun();
			Thread.sleep(1000);

			scheduleSLSRuleInfoRun();
			Thread.sleep(1000);

			scheduleSLSDrumRuleInfoRun();

			logger.info("scheduleSLSInfoRunHour is Ended >> " + LocalDateTime.now());

		} else if (type.equals("10_min")) {
			logger.info("scheduleSLSInfoRun10Minute is Running >> " + LocalDateTime.now());

			scheduleSLSUseLogInfoRun(0);
			Thread.sleep(1000);

			scheduleSLSReservationInfoRun(0);
			Thread.sleep(1000);

			scheduleSLSReturnInfoRun(0);

			logger.info("scheduleSLSInfoRun10Minute is Ended >> " + LocalDateTime.now());

		} else if (type.equals("1_min")) {
			logger.info("scheduleSLSInfoRun1Minute is Running >> " + LocalDateTime.now());

			scheduleDeviceRuleSendRun("");
			Thread.sleep(1000);

			scheduleDrumRuleSendRun("");
			Thread.sleep(1000);

			scheduleSLSCasInfoSendRun("");
			Thread.sleep(1000);

			scheduleSLSReservationInfoRun(0);
			Thread.sleep(1000);

			scheduleSLSBookOutInfoRun();
			Thread.sleep(1000);

			scheduleSLSBookInfoRun("");

			logger.info("scheduleSLSInfoRun1Minute is Ended >> " + LocalDateTime.now());

		} else if (type.contentEquals("10_sec")) {
			logger.info("scheduleSLSInfoRun10Second is Running >> " + LocalDateTime.now());

			scheduleDeviceStatus();

			logger.info("scheduleSLSInfoRun10Second is Ended >> " + LocalDateTime.now());

		}
	}

	// 예약대출기 관련 스케줄러
	public void scheduleResvLoanInfoRun(String type) throws Exception {
		if (type.contentEquals("init")) {
			logger.info("scheduleResvLoanInfoRunInit is Running >> " + LocalDateTime.now());

			scheduleResvLoanUseLogInfoRun(0);
			Thread.sleep(1000);

			scheduleResvLoanCabinetModuleInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanCabinetInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanModuleInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanRuleInfoRun();

			logger.info("scheduleResvLoanInfoRunInit is Ended >> " + LocalDateTime.now());

		} else if (type.equals("day")) {
			logger.info("scheduleResvLoanInfoRunDay is Running >> " + LocalDateTime.now());

			scheduleResvLoanCabinetModuleInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanCabinetInfoRun();

			logger.info("scheduleResvLoanInfoRunDay is Ended >> " + LocalDateTime.now());

		} else if (type.equals("hour")) {
			logger.info("scheduleResvLoanInfoRunHour is Running >> " + LocalDateTime.now());

			scheduleResvLoanCabinetModuleInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanCabinetInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanModuleInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanRuleInfoRun();

			logger.info("scheduleResvLoanInfoRunHour is Ended >> " + LocalDateTime.now());

		} else if (type.equals("10_min")) {
			logger.info("scheduleResvLoanInfoRun10Minute is Running >> " + LocalDateTime.now());

			scheduleResvLoanUseLogInfoRun(0);
			Thread.sleep(1000);

			scheduleResvLoanCabinetModuleInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanCabinetInfoRun();
			Thread.sleep(1000);

			scheduleResvLoanModuleInfoRun();

			logger.info("scheduleResvLoanInfoRun10Minute is Ended >> " + LocalDateTime.now());

		} else if (type.equals("1_min")) {
			logger.info("scheduleResvLoanInfoRun1Minute is Running >> " + LocalDateTime.now());

			scheduleResvLoanModuleSendRun("");
			Thread.sleep(1000);

			scheduleResvLoanCabinetModuleSendRun("");
			Thread.sleep(1000);

			scheduleResvLoanCabinetSendRun("");
			Thread.sleep(1000);

			scheduleResvLoanRuleSendRun("");

			logger.info("scheduleResvLoanInfoRun1Minute is Ended >> " + LocalDateTime.now());

		}
	}

	// 출입게이트 관련 스케줄러
	public void scheduleGateInfoRun(String type) throws Exception {
		if (type.contentEquals("init")) {
			logger.info("scheduleGateInfoRunInit is Running >> " + LocalDateTime.now());

			scheduleGateUseLogInfoRun();
			Thread.sleep(1000);

			scheduleGateUserInfoRun();
			Thread.sleep(1000);

			scheduleGateCompanyCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateDeptCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateMajorCodeInfoRun();
			Thread.sleep(1000);

			scheduleGatePosiitonCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateRuleInfoRun();
			Thread.sleep(1000);

			logger.info("scheduleGateInfoRunInit is Ended >> " + LocalDateTime.now());

		} else if (type.equals("day")) {
			logger.info("scheduleGateInfoRunDay is Running >> " + LocalDateTime.now());

			scheduleGateUserInfoRun();

			logger.info("scheduleGateInfoRunDay is Ended >> " + LocalDateTime.now());

		} else if (type.equals("hour")) {
			logger.info("scheduleGateInfoRunHour is Running >> " + LocalDateTime.now());

			scheduleGateCompanyCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateDeptCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateMajorCodeInfoRun();
			Thread.sleep(1000);

			scheduleGatePosiitonCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateRuleInfoRun();

			logger.info("scheduleGateInfoRunHour is Ended >> " + LocalDateTime.now());

		} else if (type.equals("10_min")) {
			logger.info("scheduleGateInfoRun10Minute is Running >> " + LocalDateTime.now());

			scheduleGateUseLogInfoRun();
			Thread.sleep(1000);

			scheduleGateCompanyCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateDeptCodeInfoRun();
			Thread.sleep(1000);

			scheduleGateMajorCodeInfoRun();
			Thread.sleep(1000);

			scheduleGatePosiitonCodeInfoRun();

			logger.info("scheduleGateInfoRun10Minute is Ended >> " + LocalDateTime.now());

		} else if (type.equals("1_min")) {
			logger.info("scheduleGateInfoRun1Minute is Running >> " + LocalDateTime.now());

			scheduleGateRuleSendRun();
			Thread.sleep(1000);

			logger.info("scheduleGateInfoRun1Minute is Ended >> " + LocalDateTime.now());

		}
	}

	// 대출반납기 관련 스케줄러
	public void scheduleLoanReturnInfoRun(String type) throws Exception {
		if (type.equals("init")) {
			logger.info("scheduleLoanReturnInfoRunInit is Running >> " + LocalDateTime.now());

			scheduleLoanReturnUseLogInfoRun();
			Thread.sleep(1000);

			scheduleLoanReturnRuleInfoRun();
			Thread.sleep(1000);

			scheduleLoanReturnHolidayInfoRun();
			Thread.sleep(1000);

			scheduleLoanReturnEquipRuleInfoRun();
			Thread.sleep(1000);

			scheduleLoanReturnBannerSettingInfoRun();
			Thread.sleep(1000);

			logger.info("scheduleLoanReturnInfoRunInit is Ended >> " + LocalDateTime.now());

		} else if (type.equals("day")) {
			logger.info("scheduleLoanReturnInfoRunDay is Running >> " + LocalDateTime.now());

			logger.info("scheduleLoanReturnInfoRunDay is Ended >> " + LocalDateTime.now());

		} else if (type.equals("hour")) {
			logger.info("scheduleLoanReturnInfoRunHour is Running >> " + LocalDateTime.now());

			scheduleLoanReturnRuleInfoRun();

			logger.info("scheduleLoanReturnInfoRunHour is Ended >> " + LocalDateTime.now());

		} else if (type.equals("10_min")) {
			logger.info("scheduleLoanReturnInfoRun10Minute is Running >> " + LocalDateTime.now());

			scheduleLoanReturnUseLogInfoRun();

			logger.info("scheduleLoanReturnInfoRun10Minute is Ended >> " + LocalDateTime.now());

		} else if (type.equals("1_min")) {
			logger.info("scheduleLoanReturnInfoRun1Minute is Running >> " + LocalDateTime.now());

			scheduleLoanReturnRuleSendRun();
			Thread.sleep(1000);

			scheduleLoanReturnHolidaySendRun();
			Thread.sleep(1000);

			scheduleLoanReturnEquipRuleSendRun();
			Thread.sleep(1000);

			scheduleLoanReturnBannerSettingSendRun();
			Thread.sleep(1000);

			logger.info("scheduleLoanReturnInfoRun1Minute is Ended >> " + LocalDateTime.now());

		}
	}

	// 반납기 관련 스케줄러
	public void schedulerReturnInfoRun(String type) throws Exception {
		if (type.contentEquals("init")) {
			logger.info("scheduleReturnInfoRunInit is Running >> " + LocalDateTime.now());

			scheduleReturnUseLogInfoRun();
			Thread.sleep(1000);

			scheduleReturnRuleInfoRun();

			logger.info("scheduleReturnInfoRunInit is Ended >> " + LocalDateTime.now());
		} else if (type.equals("day")) {
			logger.info("scheduleReturnInfoRunDay is Running >> " + LocalDateTime.now());

			logger.info("scheduleReturnInfoRunDay is Ended >> " + LocalDateTime.now());

		} else if (type.equals("hour")) {
			logger.info("scheduleReturnInfoRunHour is Running >> " + LocalDateTime.now());

			scheduleReturnRuleInfoRun();

			logger.info("scheduleReturnInfoRunHour is Ended >> " + LocalDateTime.now());

		} else if (type.equals("10_min")) {
			logger.info("scheduleReturnInfoRun10Minute is Running >> " + LocalDateTime.now());

			scheduleReturnUseLogInfoRun();

			logger.info("scheduleReturnInfoRun10Minute is Ended >> " + LocalDateTime.now());

		} else if (type.equals("1_min")) {
			logger.info("scheduleReturnInfoRun1Minute is Running >> " + LocalDateTime.now());

			scheduleReturnRuleSendRun();

			logger.info("scheduleReturnInfoRun1Minute is Ended >> " + LocalDateTime.now());

		}
	}

	public void schedulerAntiLost(String type) throws Exception {
		
	}
	
	public void scheduleSLSBookInfoRun(String device_id) throws Exception {
		logger.info("scheduleSLSBookInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSBookInfo(device_id);
	}

	public void scheduleSLSCasInfoRun() throws Exception {
		logger.info("scheduleSLSCasInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSCasInfo();
	}

	public void scheduleSLSRuleInfoRun() throws Exception {
		logger.info("scheduleSLSRuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSRuleInfo();
	}

	public void scheduleSLSDrumRuleInfoRun() throws Exception {
		logger.info("scheduleSLSDrumRuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSDrumRuleInfo();
	}

	/**
	 * [스마트도서관] 대출/반납정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleSLSUseLogInfoRun(int interval) throws Exception {
		logger.info("scheduleSLSUseLogInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSUseLogInfo(interval);
	}

	/**
	 * [스마트도서관] 예약 대출 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleSLSReservationInfoRun(int interval) throws Exception {
		logger.info("scheduleSLSReservationInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSReservationInfo(interval);
	}

	/**
	 * [스마트도서관] 무인 반납도서 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleSLSReturnInfoRun(int interval) throws Exception {
		logger.info("scheduleSLSReturnInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSReturnInfo(interval);
	}

	/**
	 * [스마트도서관] 무인 반납도서 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleSLSBookOutInfoRun() throws Exception {
		logger.info("scheduleSLSBookOutInfoRun is Running >> " + LocalDateTime.now());
		scheduleSLSBookOutInfo();
	}

	/**
	 * [스마트도서관] 장비 정책 정보 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleSLSCasInfoSendRun(String device_key) throws Exception {
		logger.info("scheduleSLSCasInfoSendRun is Running >> " + LocalDateTime.now());
		scheduleSLSCasInfoSend(device_key);
	}

	/**
	 * [스마트도서관] 장비 정책 정보 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleDeviceRuleSendRun(String device_key) throws Exception {
		logger.info("scheduleDeviceRuleSendRun is Running >> " + LocalDateTime.now());
		scheduleDeviceRuleSend(device_key);
	}

	/**
	 * [스마트도서관] 서가 설정 정보 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleDrumRuleSendRun(String device_key) throws Exception {
		logger.info("scheduleDrumRuleSendRun is Running >> " + LocalDateTime.now());
		scheduleDrumRuleSend(device_key);
	}

	/**
	 * [예약대출기] 대출 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanUseLogInfoRun(int interval) throws Exception {
		logger.info("scheduleResvLoanUseLogInfoRun is Running >> " + LocalDateTime.now());
		scheduleResvLoanUseLogInfo(interval);
	}

	/**
	 * [예약대출기] 도서 투입(도서 정보 포함) 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanCabinetModuleInfoRun() throws Exception {
		logger.info("scheduleResvLoanCabinetModuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleResvLoanCabinetModuleInfo();
	}

	/**
	 * [예약대출기] 도서 투입(사용자 정보 포함) 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanCabinetInfoRun() throws Exception {
		logger.info("scheduleResvLoanCabinetInfoRun is Running >> " + LocalDateTime.now());
		scheduleResvLoanCabinetInfo();
	}

	/**
	 * [예약대출기] 모듈 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanModuleInfoRun() throws Exception {
		logger.info("scheduleResvLoanModuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleResvLoanModuleInfo();
	}

	/**
	 * [예약대출기] 운영 정책  연계
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanRuleInfoRun() throws Exception {
		logger.info("scheduleResvLoanRuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleResvLoanRuleInfo();
	}

	/**
	 * [예약대출기] 모듈 정보 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanModuleSendRun(String device_key) throws Exception {
		logger.info("scheduleResvLoanModuleSend is Running >> " + LocalDateTime.now());
		scheduleResvLoanModuleSend(device_key);
	}

	/**
	 * [예약대출기] 도서 투입 테이블 [도서 정보] 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanCabinetModuleSendRun(String device_key) throws Exception {
		logger.info("scheduleResvLoanCabinetModuleSend is Running >> " + LocalDateTime.now());
		scheduleResvLoanCabinetModuleSend(device_key);
	}

	/**
	 * [예약대출기] 도서 투입 테이블 [사용자 정보] 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanCabinetSendRun(String device_key) throws Exception {
		logger.info("scheduleResvLoanCabinetSend is Running >> " + LocalDateTime.now());
		scheduleResvLoanCabinetSend(device_key);
	}

	/**
	 * [예약대출기] 운영정책 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleResvLoanRuleSendRun(String device_key) throws Exception {
		logger.info("scheduleResvLoanRuleSend is Running >> " + LocalDateTime.now());
		scheduleResvLoanRuleSend(device_key);
	}

	/**
	 * [출입게이트] 출입 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleGateUseLogInfoRun() throws Exception {
		logger.info("scheduleGateUseLogInfoRun is Runnig >> " + LocalDateTime.now());
		scheduleGateUseLogInfo();
	}

	/**
	 * [출입게이트] 사용자 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleGateUserInfoRun() throws Exception {
		logger.info("scheduleGateUserInfoRun is Running >> " + LocalDateTime.now());
		scheduleGateUserInfo();
	}

	/**
	 * [출입게이트] 소속 코드 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleGateCompanyCodeInfoRun() throws Exception {
		logger.info("scheduleGateCompanyCodeInfoRun is Running >> " + LocalDateTime.now());
		scheduleGateCompanyCodeInfo();
	}

	/**
	 * [출입게이트] 부서 코드 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleGateDeptCodeInfoRun() throws Exception {
		logger.info("scheduleGateDeptCodeInfoRun is Running >> " + LocalDateTime.now());
		scheduleGateDeptCodeInfo();
	}

	/**
	 * [출입게이트] 전공 코드 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleGateMajorCodeInfoRun() throws Exception {
		logger.info("scheduleGateMajorCodeInfoRun is Running >> " + LocalDateTime.now());
		scheduleGateMajorCodeInfo();
	}

	/**
	 * [출입게이트] 신분 코드 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleGatePosiitonCodeInfoRun() throws Exception {
		logger.info("scheduleGatePositionCodeInfoRun is Running >> " + LocalDateTime.now());
		scheduleGatePositionCodeInfo();
	}

	/**
	 * [출입게이트] 운영 정책 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleGateRuleInfoRun() throws Exception {
		logger.info("scheduleGateRuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleGateRuleInfo();
	}

	/**
	 * [출입게이트] 운영 정책 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleGateRuleSendRun() throws Exception {
		logger.info("scheduleGateRuleSend is Running >> " + LocalDateTime.now());
		scheduleGateRuleSend();
	}

	/**
	 * [대출반납기] 대출 반납 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnUseLogInfoRun() throws Exception {
		logger.info("scheduleLoanReturnUseLogInfoRun is Running >> " + LocalDateTime.now());
		scheduleLoanReturnUseLogInfo();
	}

	/**
	 * [대출반납기] 운영 정책 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnRuleInfoRun() throws Exception {
		logger.info("scheduleLoanReturnRuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleLoanReturnRuleInfo();
	}

	/**
	 * [대출반납기] 운엉 정책 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnRuleSendRun() throws Exception {
		logger.info("scheduleLoanReturnRuleSend is Running >> " + LocalDateTime.now());
		scheduleLoanReturnRuleSend();
	}

	/**
	 * [대출반납기] 휴관일 설정 연계
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnHolidayInfoRun() throws Exception {
		logger.info("scheduleLoanReturnHolidayInfoRun is Running >> " + LocalDateTime.now());
		scheduleLoanReturnHolidayInfo();
	}

	/**
	 * [대출반납기] 휴관일 설정 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnHolidaySendRun() throws Exception {
		logger.info("scheduleLoanReturnHolidaySend is Running >> " + LocalDateTime.now());
		scheduleLoanReturnHolidaySend();
	}

	/**
	 * [대출반납기] 장비 설정 연계
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnEquipRuleInfoRun() throws Exception {
		logger.info("scheduleLoanReturnEquipRuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleLoanReturnEquipRuleInfo();
	}

	/**
	 * [대출반납기] 장비 설정 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnEquipRuleSendRun() throws Exception {
		logger.info("scheduleLoanReturnEquipRuleSendRun is Running >> " + LocalDateTime.now());
		scheduleLoanReturnEquipRuleSend();
	}

	/**
	 * [대출반납기] 배너 설정 연계
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnBannerSettingInfoRun() throws Exception {
		logger.info("scheduleLoanReturnBannerSettingInfoRun is Running >> " + LocalDateTime.now());
		scheduleLoanReturnBannerSettingInfo();
	}

	/**
	 * [대출반납기] 배너 설정 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleLoanReturnBannerSettingSendRun() throws Exception {
		logger.info("scheduleLoanReturnBannerSEttingSendRun is Running >> " + LocalDateTime.now());
		scheduleLoanReturnBannerSettingSend();
	}

	/**
	 * [반납기] 반납 정보 연계
	 *
	 * @throws Exception
	 */
	public void scheduleReturnUseLogInfoRun() throws Exception {
		logger.info("scheduleReturnUseLogInfoRun is Running >> " + LocalDateTime.now());
		scheduleReturnUseLogInfo();
	}

	/**
	 * [반납기] 운영 정책
	 *
	 * @throws Exception
	 */
	public void scheduleReturnRuleInfoRun() throws Exception {
		logger.info("scheduleReturnRuleInfoRun is Running >> " + LocalDateTime.now());
		scheduleReturnRuleInfo();
	}

	/**
	 * [반납기] 운영 정책 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleReturnRuleSendRun() throws Exception {
		logger.info("scheduleReturnRuleSendRun is Running >> " + LocalDateTime.now());
		scheduleReturnRuleSend();
	}
	
	/**
	 * [분실방지] 운영 정책 동기화
	 *
	 * @throws Exception
	 */
	public void scheduleAntiLostDeviceInfoRun() throws Exception {
		logger.info("scheduleAntiLostDeviceInfoRun is Running >> " + LocalDateTime.now());
	}
	
	private void scheduleLibUsers() throws Exception {
		logger.info(">>> scheduleLibUsers Start!! - " + LocalDateTime.now());
		
		try {
			List<Map<String, Object>> libraryList = librarayService.getLibraryDBConnList(Collections.emptyMap());
			if(libraryList.size() == 0 || libraryList.isEmpty()) {
				logger.info(">>> scheduleLibUsers End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> library: libraryList) {
				String library_id = ParamUtil.parseString(library.get("library_id"));
				String library_key = ParamUtil.parseString(library.get("library_key"));
				
				if(library_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(library_id);

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> libMemberList = apiLibService.getApiLibMemberList(Collections.emptyMap());
				if (libMemberList == null) {
					continue;
				}
				
				for (Map<String, Object> member: libMemberList) {
					if (member != null) {
						String lib_member_key = ParamUtil.parseString(member.get("REC_KEY"));
						String member_no = ParamUtil.parseString(member.get("USER_NO"));
						String member_id = ParamUtil.parseString(member.get("USER_ID"));
						String member_name = ParamUtil.parseString(member.get("NAME"));
						String user_class = ParamUtil.parseString(member.get("USER_CLASS"));
						String member_class = ParamUtil.parseString(member.get("MEMBER_CLASS"));
						String reg_date = ParamUtil.parseString(member.get("REG_DATE"));

						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("worker", String.format("%s@%s", "schedule", "localhost"));
						paramMap.put("lib_member_key", lib_member_key);
						paramMap.put("member_id", member_id);
						paramMap.put("member_no", member_no);
						paramMap.put("member_nm", member_name);
						paramMap.put("user_class", user_class);
						paramMap.put("member_class", member_class);
						paramMap.put("reg_date", reg_date);

						paramMap.put("library_key", library_key);

						Map<String, Object> memberMap = apiLibService.getlibMemberInfo(paramMap);
						if (memberMap != null) {
							paramMap.put("rec_key", memberMap.get("rec_key"));
							apiLibService.updateLibMemberInfo(paramMap);
							updateCnt++;
						}
						else {
							apiLibService.insertLibMemberInfo(paramMap);
							insertCnt++;
						}
						paramMap = null;
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + libMemberList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleLibUsers End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleLibBooks() throws Exception {
		logger.info(">>> scheduleLibBooks Start!! - " + LocalDateTime.now());
		
		try {
			List<Map<String, Object>> libraryList = librarayService.getLibraryDBConnList(Collections.emptyMap());
			if(libraryList.size() == 0 || libraryList.isEmpty()) {
				logger.info(">>> scheduleLibBooks End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> library : libraryList) {
				String library_key = ParamUtil.parseString(library.get("library_key"));
				String library_id = ParamUtil.parseString(library.get("library_id"));
				if(library_id.equals("") || library_key.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(library_id);

				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> libBookList = apiLibService.getApiLibBookList(Collections.emptyMap());
				if (libBookList == null) {
					continue;
				}
				
				for (Map<String, Object> book: libBookList) {
					if (book != null) {
						String book_isbn = ParamUtil.parseString(book.get("ST_CODE"));
						String book_title = ParamUtil.parseString(book.get("TITLE_INFO"));

						Map<String, Object> paramMap = new HashMap<String, Object>();
						paramMap.put("worker", String.format("%s@%s", "schedule", "localhost"));
						paramMap.put("book_isbn", book_isbn);
						paramMap.put("book_title", book_title);

						paramMap.put("library_key", library_key);

						Map<String, Object> bookMap = apiLibService.getlibBookInfo(paramMap);

						if (bookMap != null) {
							paramMap.put("rec_key", bookMap.get("rec_key"));
							apiLibService.updateLibBookInfo(paramMap);
							updateCnt++;
						} else {
							apiLibService.insertLibBookInfo(paramMap);
							insertCnt++;
						}
						paramMap = null;
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + libBookList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleLibBooks End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void scheduleSLSBookInfo(String paramDeviceId) throws Exception {
		logger.info(">>> scheduleSLSBookInfo Start!! - " + LocalDateTime.now());

		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSBookInfo End!! - " + LocalDateTime.now());
				return;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
			String now = sdf.format(new Date());
			
			for (Map<String, Object> device : deviceList) {
				String deviceId = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				// device id가 없을 경우 pass
				if ("".equals(deviceId))
					continue;

				// parameter로 받은 device id가 빈값이 아니고, 해당 id가 아닐때 pass
				if (!"".equals(paramDeviceId) && !paramDeviceId.equals(deviceId))
					continue;

				device.put("now", now);

				// Device 정보에 따른 NIMS DB 도서 목록
				@SuppressWarnings("unchecked")
				Map<String, Object> nimsResult = (Map<String, Object>) apiSlsService.getSLSBookList(null, device).get("result");
				List<Map<String, Object>> nimsBookList = (List<Map<String, Object>>) nimsResult.get("items");
				if (nimsBookList == null) {
					logger.warn("NIMS의 도서 목록을 받아올수 없습니다");
					continue;
				}

				// Device 정보에 따른 SmartDB 연결
				DBContextHolder.setDBType(deviceId);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				// 해당 장비의 도서 목록(SmartDB)
				List<Map<String, Object>> deviceBookList = apiSlsService.getSLSBookInfoList();
				if (deviceBookList == null || deviceBookList.isEmpty()) {
					logger.warn("해당 장비에 따른 도서목록이 없습니다.");
					continue;
				}

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				for (Map<String, Object> book: deviceBookList) {
					if (book == null) {
						continue;
					}
					String book_id = ParamUtil.parseString(book.get("id"));

					book.put("worker", String.format("%s@%s", "schedule", "localhost"));
					book.put("library_key", library_key);
					book.put("device_key", device_key);
					book.put("book_status", "1");
					book.put("now", now);

					List<Map<String, Object>> candidates = nimsBookList.stream()
							.filter(nimsBook -> nimsBook.get("id").toString().equals(book_id))
							.collect(Collectors.toList());

					if (!candidates.isEmpty()) {
						book.put("rec_key", candidates.get(0).get("rec_key"));
						apiSlsService.updateSLSBookInfo(null, book);
						updateCnt++;
					}
					else {
						apiSlsService.insertSLSBookInfo(null, book);
						insertCnt++;
					}
				}

				// 동기화 되지 않은 도서 배출 처리
				apiSlsService.syncSLSBookStatus(null, device);

				logger.info("TOTAL >>> " + deviceBookList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}

			logger.info(">>> scheduleSLSBookInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleSLSCasInfo() throws Exception {
		logger.info(">>> scheduleSLSCasInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSCasInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				List<Map<String, Object>> casInfoList = apiSlsService.getSLSCasInfoList();

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (casInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> casInfo: casInfoList) {
					if (casInfo != null) {
						casInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						casInfo.put("library_key", library_key);
						casInfo.put("device_key", device_key);

						Map<String, Object> casInfoMap = apiSlsService.getSLSCasInfo(null, casInfo);

						if (casInfoMap != null) {
							casInfo.put("rec_key", casInfoMap.get("rec_key"));
							apiSlsService.updateSLSCasInfo(null, casInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSCasInfo(null, casInfo);
							insertCnt++;
						}
					}
					else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + casInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleSLSCasInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleSLSRuleInfo() {
		logger.info(">>> scheduleSLSRuleInfo Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSRuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if ("".equals(device_id)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				List<Map<String, Object>> ruleInfoList = apiSlsService.getSLSRuleInfoList();

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (ruleInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> ruleInfo: ruleInfoList) {
					if (ruleInfo != null) {
						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiSlsService.getSLSRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiSlsService.updateSLSRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSRuleInfo(null, ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + ruleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleSLSRuleInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleSLSUseLogInfo(int interval) throws Exception {
		logger.info(">>> scheduleSLSUseLogInfo Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSUseLogInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if (!"".equals(device_id)) {
					DBContextHolder.setDBType(device_id);
					logger.info("DBtype > {}", DBContextHolder.getDBType());

					Map<String, Object> paramMap = new HashMap<String, Object>();
					if (interval > 0) {
						paramMap.put("interval", interval);
					}
					List<Map<String, Object>> useLogInfoList = apiSlsService.getSLSUseLogInfoList(paramMap);

					int insertCnt = 0;
					int updateCnt = 0;
					int failCnt = 0;
					if (useLogInfoList == null) {
						continue;
					}
					
					for (Map<String, Object> useLogInfo : useLogInfoList) {
						if (useLogInfo != null) {
							useLogInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

							useLogInfo.put("library_key", library_key);
							useLogInfo.put("device_key", device_key);

							Map<String, Object> useLogInfoMap = apiSlsService.getSLSUseLogInfo(null, useLogInfo);

							if (useLogInfoMap != null) {
								useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
								apiSlsService.updateSLSUseLogInfo(null, useLogInfo);
								updateCnt++;
							} else {
								apiSlsService.insertSLSUseLogInfo(null, useLogInfo);
								insertCnt++;
							}
						} else {
							failCnt++;
						}
					}
					
					logger.info("TOTAL >>> " + useLogInfoList.size());
					logger.info("INSERT >>> " + insertCnt);
					logger.info("UPDATE >>> " + updateCnt);
					logger.info("FAIL >>> " + failCnt);
				}
			}
			
			logger.info(">>> scheduleSLSUseLogInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleSLSReservationInfo(int interval) throws Exception {
		logger.info(">>> scheduleSLSReservationInfo Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSReservationInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if ("".equals(device_id)) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> paramMap = new HashMap<String, Object>();
				if (interval > 0) {
					paramMap.put("interval", interval);
				}

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> reservationInfoList = apiSlsService.getSLSReservationInfoList(paramMap);
				if (reservationInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> reservationInfo : reservationInfoList) {
					if (reservationInfo != null) {
						reservationInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						reservationInfo.put("library_key", library_key);
						reservationInfo.put("device_key", device_key);

						Map<String, Object> casInfoMap = apiSlsService.getSLSReservationInfo(null,
								reservationInfo);

						if (casInfoMap != null) {
							reservationInfo.put("rec_key", casInfoMap.get("rec_key"));
							apiSlsService.updateSLSReservationInfo(null, reservationInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSReservationInfo(null, reservationInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + reservationInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleSLSReservationInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleSLSReturnInfo(int interval) throws Exception {
		logger.info(">>> scheduleSLSReturnInfo Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSReturnInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if ("".equals(device_id)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> paramMap = new HashMap<String, Object>();
				if (interval > 0) {
					paramMap.put("interval", interval);
				}
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> returnInfoList = apiSlsService.getSLSReturnInfoList(paramMap);
				if (returnInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> returnInfo : returnInfoList) {
					if (returnInfo != null) {
						String id = ParamUtil.parseString(returnInfo.get("id"));

						returnInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						returnInfo.put("library_key", library_key);
						returnInfo.put("device_key", device_key);

						Map<String, Object> returnInfoMap = apiSlsService.getSLSReturnInfo(null, returnInfo);

						if (returnInfoMap != null) {
							returnInfo.put("rec_key", returnInfoMap.get("rec_key"));
							apiSlsService.updateSLSReturnInfo(null, returnInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSReturnInfo(null, returnInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + returnInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleSLSReturnInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleSLSBookOutInfo() throws Exception {
		logger.info(">>> scheduleSLSBookOutInfo Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSBookOutInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if ("".equals(device_id)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int insertCnt = 0;
				int updateCnt = 0;
				int deleteCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> slsBookOutInfoList = apiSlsService.getSLSBookOutInfoList();
				if (slsBookOutInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> slsBookOutInfo: slsBookOutInfoList) {
					if (slsBookOutInfo != null) {
						slsBookOutInfo.put("library_key", library_key);
						slsBookOutInfo.put("device_key", device_key);

						Map<String, Object> slsBookOutInfoMap = apiSlsService.getSLSBookOutInfo(null, slsBookOutInfo);

						if (slsBookOutInfoMap != null) {
							slsBookOutInfo.put("rec_key", slsBookOutInfoMap.get("rec_key"));

							apiSlsService.updateSLSBookOutInfo(null, slsBookOutInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSBookOutInfo(null, slsBookOutInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + slsBookOutInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("DELETE >>> " + deleteCnt);
				logger.info("FAIL >>> " + failCnt);
			}
				
			logger.info(">>> scheduleSLSBookOutInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 선반 설정 정보 중 UPDATE 된 내역을 확인한뒤 SmartDB에 UPDATE 한다.
	 */
	private void scheduleSLSCasInfoSend(String paramDeviceKey) throws Exception {
		logger.info(">>> scheduleSLSCasInfoSend Start!! - " + LocalDateTime.now());
		try {
			if(paramDeviceKey.equals("")) {
				logger.info(">>> scheduleSLSCasInfoSend End - Device Key를 입력해주세요.");
				return;
			}
			
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);
			
			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSCasInfoSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				if ("".equals(device_id) || !device_key.equals(paramDeviceKey)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				device.put("send_req_yn", "Y");

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> casInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiSlsService
						.getSLSCasInfoList(null, device).get("result"))).get("items");

				if (casInfoList.size() == 0) {
					continue;
				}
				
				for (Map<String, Object> casInfoMap : casInfoList) {
					DBContextHolder.setDBType(device_id);
					casInfoMap.put("worker", String.format("%s@%s", "schedule", "localhost"));
					
					int result = apiSlsService.updateSLSCasInfo(casInfoMap);
					if (result > 0) {
						casInfoMap.put("send_req_yn", "S");
						apiSlsService.updateSLSCasInfo(null, casInfoMap);
					}
				}
			}
		
			logger.info(">>> scheduleSLSCasInfoSend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 장비 정책 정보 중 UPDATE 된 내역을 확인한뒤 SmartDB에 UPDATE 한다.
	 */
	private void scheduleDeviceRuleSend(String paramDeviceKey) throws Exception {

		logger.info(">>> scheduleDeviceRuleSend Start!! - " + LocalDateTime.now());
		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();
			
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleDeviceRuleSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if ("".equals(device_id) || !rec_key.equals(paramDeviceKey)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				// 각 장비에 해당되는 RULE 정보 조회
				device.put("send_req_yn", "Y");
				Map<String, Object> ruleResult = apiSlsService.getSLSRuleInfoList(device);
				
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (ruleResult.get("result"))).get("items");

				// 대상이 있으면 SmartDB UPDATE
				if (ruleInfoList.size() > 0) {
					DBContextHolder.setDBType(device_id);
					
					for (Map<String, Object> ruleInfoMap : ruleInfoList) {
						ruleInfoMap.put("worker", String.format("%s@%s", "nims", server_ip));
						int result = apiSlsService.updateSLSRuleInfo(ruleInfoMap);
						if (result > 0) {
							ruleInfoMap.put("send_req_yn", "S");
							apiSlsService.updateSLSRuleSendResult(null, ruleInfoMap);
						}
					}
				}
			}
			
			logger.info(">>> scheduleDeviceRuleSend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 서가 설정 정보 중 UPDATE 된 내역을 확인한뒤 SmartDB에 UPDATE 한다.
	 */
	private void scheduleDrumRuleSend(String paramDeviceKey) throws Exception {
		logger.info(">>> scheduleDrumRuleSend Start!! - " + LocalDateTime.now());
		
		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();
			
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty() || paramDeviceKey.equals("")) {
				logger.info(">>> scheduleDrumRuleSend End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("device_id"));
				
				if ("".equals(device_id) || !rec_key.equals(paramDeviceKey)) {
					continue;
				}
			
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				// 각 장비에 해당되는 RULE 정보 조회
				device.put("send_req_yn", "Y");
				
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiSlsService.getSLSDrumRuleInfoList(device).get("result"))).get("items");

				// 대상이 있으면 SmartDB UPDATE
				if (ruleInfoList.size() > 0) {
					DBContextHolder.setDBType(device.get("device_id").toString());

					for (Map<String, Object> ruleInfoMap : ruleInfoList) {
						ruleInfoMap.put("worker", String.format("%s@%s", "nims", server_ip));
						int result = apiSlsService.updateSLSDrumRuleInfo(ruleInfoMap);
						if (result > 0) {
							ruleInfoMap.put("send_req_yn", "S");
							apiSlsService.updateSLSDrumRuleSendResult(null, ruleInfoMap);
						}
					}
				}
			}
				
			logger.info(">>> scheduleDrumRuleSend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 등록된 장비의 네트워크 연결 상태를 확인한다.
	 */
	private void scheduleDeviceNetworkCheck() throws Exception {

		logger.info(">>> scheduleDeviceNetworkCheck Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			List<Map<String, Object>> deviceList = deviceService.getDeviceList(params);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleDeviceNetworkCheck End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				String device_ip = ParamUtil.parseString(device.get("device_ip"));
				
				if (!rec_key.equals("") && !device_ip.equals("")) {
					InetAddress pingcheck = InetAddress.getByName(device_ip);
					params.put("rec_key", rec_key);
					
					if (pingcheck.isReachable(4000)) {
						params.put("device_status", 1);
						params.put("connect_yn", "Y");
					} else {
						if (ParamUtil.parseDouble(device.get("connect_interval")) > 10) {
							params.put("device_status", 0);
						}
						params.put("connect_yn", "N");
					}
					
					pingcheck = null;
					params.put("worker", String.format("%s@%s", "nims", InetAddress.getLocalHost().getHostAddress()));
					deviceService.updateDeviceConnInfo(null, params);
				}
			}
			
			logger.info(">>> scheduleDeviceNetworkCheck End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 등록된 장비 상태정보를 확인한다.
	 */
	private void scheduleDeviceStatus() throws Exception {
		logger.info(">>> scheduleDeviceStatus Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> statusInfo = apiSlsService.getSLSStatusInfoList();
				if (statusInfo != null && !statusInfo.get("status_info").toString().equals("")) {
					statusInfo.put("device_key", device_key);
					statusInfo.put("device_control_status", statusInfo.get("status_info"));
					deviceService.updateDeviceControllStatus(null, statusInfo);
				}
			}
			
			logger.info(">>> scheduleDeviceStatus End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleSLSDrumRuleInfo() {
		logger.info(">>> scheduleSLSDrumRuleInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 6);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleSLSDrumRuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> drumRuleInfoList = apiSlsService.getSLSDrumRuleInfoList();
				if (drumRuleInfoList == null) {
					continue;
				}
				for (Map<String, Object> ruleInfo : drumRuleInfoList) {
					if (ruleInfo != null) {

						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiSlsService.getSLSDrumRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiSlsService.updateSLSDrumRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiSlsService.insertSLSDrumRuleInfo(null, ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + drumRuleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleSLSDrumRuleInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void scheduleResvLoanUseLogInfo(int interval) throws Exception {
		logger.info(">>> scheduleResvLoanUseLogInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				return;
			}
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> paramMap = new HashMap<String, Object>();
				if (interval > 0) {
					paramMap.put("interval", interval);
				}

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> useLogInfoList = apiResvLoanService.getResvLoanUseLogInfoList(paramMap);
				if (useLogInfoList == null) {
					continue;
				}
				for (Map<String, Object> useLogInfo: useLogInfoList) {
					if (useLogInfo != null) {
						useLogInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						
						useLogInfo.put("library_key", library_key);
						useLogInfo.put("device_key", device_key);

						Map<String, Object> useLogInfoMap = apiResvLoanService.getResvLoanUseLogInfo(null,
								useLogInfo);

						if (useLogInfoMap != null) {
							useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanUseLogInfo(null, useLogInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanUseLogInfo(null, useLogInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + useLogInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleResvLoanUseLogInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleResvLoanCabinetModuleInfo() throws Exception {
		logger.info(">>> scheduleResvLoanCabinetModuleInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleResvLoanCabinetModuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> cabinetModuleInfoList = apiResvLoanService.getResvLoanCabinetModuleInfoList(device);
				if (cabinetModuleInfoList == null) {
					continue;
				}
				for (Map<String, Object> cabinetModuleInfo: cabinetModuleInfoList) {
					if (cabinetModuleInfo != null) {
						cabinetModuleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						cabinetModuleInfo.put("library_key", library_key);
						cabinetModuleInfo.put("device_key", device_key);

						Map<String, Object> cabinetModuleInfoMap = apiResvLoanService.getResvLoanCabinetModuleInfo(null, cabinetModuleInfo);

						if (cabinetModuleInfoMap != null) {
							cabinetModuleInfo.put("rec_key", cabinetModuleInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanCabinetModuleInfo(null, cabinetModuleInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanCabinetModuleInfo(null, cabinetModuleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + cabinetModuleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleResvLoanCabinetModuleInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleResvLoanCabinetInfo() throws Exception {
		logger.info(">>> scheduleResvLoanCabinetInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleResvLoanCabinetInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> cabinetInfoList = apiResvLoanService.getResvLoanCabinetInfoList(device);
				if (cabinetInfoList == null) {
					continue;
				}
				for (Map<String, Object> cabinetInfo : cabinetInfoList) {
					if (cabinetInfo != null) {
						cabinetInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						cabinetInfo.put("library_key", library_key);
						cabinetInfo.put("device_key", device_key);

						Map<String, Object> cabinetInfoMap = apiResvLoanService.getResvLoanCabinetInfo(null, cabinetInfo);

						if (cabinetInfoMap != null) {
							cabinetInfo.put("rec_key", cabinetInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanCabinetInfo(null, cabinetInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanCabinetInfo(null, cabinetInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + cabinetInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleResvLoanCabinetInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleResvLoanModuleInfo() throws Exception {
		logger.info(">>> scheduleResvLoanModuleInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleResvLoanModuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> moduleInfoList = apiResvLoanService.getResvLoanModuleInfoList(device);
				if (moduleInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> moduleInfo : moduleInfoList) {
					if (moduleInfo != null) {
						moduleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						moduleInfo.put("library_key", library_key);
						moduleInfo.put("device_key", device_key);

						Map<String, Object> moduleInfoMap = apiResvLoanService.getResvLoanModuleInfo(null, moduleInfo);

						if (moduleInfoMap != null) {
							moduleInfo.put("rec_key", moduleInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanModuleInfo(null, moduleInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanModuleInfo(null, moduleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + moduleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleResvLoanModuleInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleResvLoanRuleInfo() throws Exception {
		logger.info(">>> scheduleResvLoanRuleInfo Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleResvLoanRuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> ruleInfoList = apiResvLoanService.getResvLoanRuleInfoList(device);
				if (ruleInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> ruleInfo : ruleInfoList) {
					if (ruleInfo != null) {
						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiResvLoanService.getResvLoanRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiResvLoanService.updateResvLoanRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiResvLoanService.insertResvLoanRuleInfo(null, ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				
				logger.info("TOTAL >>> " + ruleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleResvLoanRuleInfo End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 모듈 설정 정보에 전송 요청 상태인 데이터를 확인한 뒤 예약대출기 DB에 반영한다.
	 */
	private void scheduleResvLoanModuleSend(String device_key) throws Exception {
		logger.info(">>> scheduleResvLoanModuleSend Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> scheduleResvLoanModuleSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("send_req_yn", "Y");

				List<Map<String, Object>> moduleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService.getResvLoanModuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 INSERT
				if (moduleInfoList.size() > 0) {
					for (Map<String, Object> moduleInfo : moduleInfoList) {
						moduleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiResvLoanService.insertResvLoanModuleInfoGallery(null, moduleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							moduleInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanModuleSendResult(null, moduleInfo);
						}
					}
				}
			}
			
			logger.info(">>> scheduleResvLoanModuleSend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 도서 투입 테이블 [도서 정보]에 전송 요청 상태인 데이터를 확인한 뒤 예약대출기 DB에 반영한다.
	 */
	private void scheduleResvLoanCabinetModuleSend(String device_key) throws Exception {

		logger.info(">>> scheduleResvLoanCabinetModuleSend Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> scheduleResvLoanCabinetModuleSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());


				device.put("device_key", rec_key);
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> cabinetModuleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService
						.getResvLoanCabinetModuleInfoList(null, device).get("result"))).get("items");

				if (cabinetModuleInfoList.size() > 0) {
					for (Map<String, Object> cabinetModuleInfo : cabinetModuleInfoList) {

						Map<String, Object> resultMap =	resultMap = (Map<String, Object>) apiResvLoanService.insertResvLoanCabinetModuleInfoGallery(null, cabinetModuleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							cabinetModuleInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanCabinetModuleSendResult(null, cabinetModuleInfo);
						}
					}
				}
			}
			logger.info(">>> scheduleResvLoanCabinetModuleSend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 도서 투입 테이블 [사용자 정보]에 전송 요청 상태인 데이터를 확인한 뒤 예약대출기 DB에 반영한다.
	 */
	private void scheduleResvLoanCabinetSend(String device_key) throws Exception {
		logger.info(">>> scheduleResvLoanCabinetSend Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> scheduleResvLoanCabinetSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());


				device.put("device_key", rec_key);
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> cabinetInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService
						.getResvLoanCabinetInfoList(null, device).get("result"))).get("items");

				if (cabinetInfoList.size() > 0) {
					for (Map<String, Object> cabinetInfo : cabinetInfoList) {
						Map<String, Object> cabinetInfoMap = apiResvLoanService.getResvLoanCabinetInfo(cabinetInfo);
						Map<String, Object> resultMap = new HashMap<String, Object>();

						if (cabinetInfoMap != null) {
							resultMap = (Map<String, Object>) apiResvLoanService.updateResvLoanCabinetInfo(cabinetInfo).get("result");
						} else {
							resultMap = (Map<String, Object>) apiResvLoanService.insertResvLoanCabinetInfoGallery(null, cabinetInfo).get("result");
						}

						if(resultMap.get("CODE").toString().equals("200")) {
							cabinetInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanCabinetSendResult(null, cabinetInfo);
						}
					}
				}
			}
			
			logger.info(">>> scheduleResvLoanCabinetModuleSend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 운영정책 테이블에 전송 요청 상태인 데이터를 확인한 뒤 예약대출기 DB에 반영한다.
	 */
	private void scheduleResvLoanRuleSend(String device_key) throws Exception {
		logger.info(">>> scheduleResvLoanRuleSend Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 8);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> scheduleResvLoanRuleSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String rec_key = ParamUtil.parseString(device.get("rec_key"));
				
				if (device_id.equals("") || !device_key.equals(rec_key)) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());


				device.put("device_key", rec_key);
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiResvLoanService
						.getResvLoanRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 UPDATE
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						Map<String, Object> resultMap = (Map<String, Object>) apiResvLoanService.updateResvLoanRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiResvLoanService.updateResvLoanRuleSendResult(null, ruleInfo);
						}
					}
				}
			}
			
			logger.info(">>> scheduleResvLoanCabinetModuleSend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleGateUseLogInfo() throws Exception {
		logger.info(">>> scheduleGateUseLogInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGateUseLogInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> paramMap = new HashMap<String, Object>();
				Map<String, Object> logParamMap = new HashMap<String, Object>();

				logParamMap.put("library_key", library_key);

				List<Map<String, Object>> useLogInfoList = new ArrayList<Map<String, Object>>();

				// NIMS 테이블에 있는 Gate Log 데이터 유무 확인하여 최신 데이터 날짜 확인
				@SuppressWarnings("unchecked")
				Map<String, Object> logResult = (Map<String, Object>) ((Map<String, Object>) apiGateService.getGateLatestLogDate(null, logParamMap)
						.get("result")).get("info");

				// 데이터가 있을 경우 INSERT/UPDATE 진행
				if (logResult != null) {
					paramMap.put("latest_date", logResult.get("latest_date"));

					Map<String, Object> result = apiGateService.getGateUseLogInfoList(paramMap);
					useLogInfoList = (List<Map<String, Object>>) result.get("data");

					int insertCnt = 0;
					int updateCnt = 0;
					int failCnt = 0;
					if (useLogInfoList != null) {
						for (Map<String, Object> useLogInfo : useLogInfoList) {
							if (useLogInfo != null) {
								useLogInfo.put("library_key", library_key);

								Map<String, Object> useLogInfoMap = apiGateService.getGateUseLogInfo(null, useLogInfo);

								if(useLogInfoMap != null) {
									useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
									apiGateService.updateGateUseLogInfo(null, useLogInfo);
									updateCnt++;
								} else {
									apiGateService.insertGateUseLogInfo(null, useLogInfo);
									insertCnt++;
								}
							} else {
								failCnt++;
							}
						}
						logger.info("TOTAL >>> " + useLogInfoList.size());
						logger.info("INSERT >>> " + insertCnt);
						logger.info("UPDATE >>> " + updateCnt);
						logger.info("FAIL >>> " + failCnt);
					}
				}
				// 데이터가 없을 경우 insertBatch 진행
				else {
					Map<String, Object> result = apiGateService.getGateUseLogInfoList(paramMap);
					useLogInfoList = (List<Map<String, Object>>) result.get("data");

					Map<String, Object> insertMap = new HashMap<String, Object>();
					insertMap.put("library_key", library_key);

					// 1000개씩 나눠서 Insert 진행
					int limit = 1000;

					for(int i = 0; i < useLogInfoList.size(); i += limit) {
						insertMap.put("infoList", new ArrayList<Map<String, Object>>(useLogInfoList.subList(i, min(i + limit, useLogInfoList.size()))));

						apiGateService.insertBatchGateUseLogInfo(null, insertMap);
					}
				}
			}
			logger.info(">>> scheduleGateUseLogInfo End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleGateUserInfo() throws Exception {
		logger.info(">>> scheduleGateUserInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGateUserInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> result = apiGateService.getGateUserInfoList(null);
				List<Map<String, Object>> userInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (userInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> userInfo : userInfoList) {
					if (userInfo != null) {
						userInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						userInfo.put("library_key", library_key);

						Map<String, Object> userInfoMap = apiGateService.getGateUserInfo(null, userInfo);

						if(userInfoMap != null) {
							userInfo.put("rec_key", userInfoMap.get("rec_key"));
							apiGateService.updateGateUserInfo(null, userInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateUserInfo(null, userInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + userInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleGateUserInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleGateCompanyCodeInfo() {
		logger.info(">>> scheduleGateCompanyCodeInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGateCompanyCodeInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateCompanyCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (codeInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGateCompanyCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGateCompanyCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateCompanyCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleGateCompanyCodeInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleGateDeptCodeInfo() {
		logger.info(">>> scheduleGateDeptCodeInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGateDeptCodeInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateDeptCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (codeInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGateDeptCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGateDeptCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateDeptCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleGateDeptCodeInfo End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleGateMajorCodeInfo() {
		logger.info(">>> scheduleGateMajorCodeInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGateMajorCodeInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGateMajorCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (codeInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGateMajorCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGateMajorCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateMajorCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleGateMajorCodeInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleGatePositionCodeInfo() {
		logger.info(">>> scheduleGatePositionCodeInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGatePositionCodeInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				Map<String, Object> result = apiGateService.getGatePositionCodeInfoList(null);
				List<Map<String, Object>> codeInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				if (codeInfoList == null) {
					continue;
				}
				for (Map<String, Object> codeInfo : codeInfoList) {
					if (codeInfo != null) {
						codeInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
						codeInfo.put("library_key", library_key);

						Map<String, Object> codeInfoMap = apiGateService.getGatePositionCodeInfo(null, codeInfo);

						if(codeInfoMap != null) {
							codeInfo.put("rec_key", codeInfoMap.get("rec_key"));
							apiGateService.updateGatePositionCodeInfo(null, codeInfo);
							updateCnt++;
						} else {
							apiGateService.insertGatePositionCodeInfo(null, codeInfo);
							insertCnt++;
						}
					} else {
						failCnt ++;
					}
				}
				logger.info("TOTAL >>> " + codeInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleGatePositionCodeInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleGateRuleInfo() {
		logger.info(">>> scheduleGateRuleInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGateRuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				Map<String, Object> result = apiGateService.getGateRuleInfoList();

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) result.get("data");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				if (ruleInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> ruleInfo : ruleInfoList) {
					if (ruleInfo != null) {
						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);

						Map<String, Object> ruleInfoMap = apiGateService.getGateRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiGateService.updateGateRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiGateService.insertGateRuleInfo(null, ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + ruleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			logger.info(">>> scheduleGateRuleInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 출입게이트 정책에 전송 요청 상태인 데이터를 확인한 뒤 출입게이트 DB에 반영한다.
	 */
	private void scheduleGateRuleSend() throws Exception {
		logger.info(">>> scheduleGateRuleSend Start!! - " + LocalDateTime.now());
		
		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();
			
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 2);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleGateRuleSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiGateService
						.getGateRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 INSERT
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiGateService
								.updateGateRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiGateService.updateGateRuleSendResult(null, ruleInfo);
						}
					}
				}
			}
			logger.info(">>> scheduleGateRuleSend End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleLoanReturnUseLogInfo() throws Exception {
		logger.info(">>> scheduleLoanReturnUseLogInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleLoanReturnUseLogInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				Map<String, Object> keyMap = new HashMap<String, Object>();

				keyMap.put("library_key", library_key);
				keyMap.put("device_key", device_key);

				Map<String, Object> lastKeyResult = apiLoanReturnService.getLoanReturnLastLogKey(null, keyMap);
				Map<String, Object> logMap = ((Map<String, Object>) ((Map<String, Object>) lastKeyResult.get("result")).get("info"));

				Map<String, Object> paramMap = new HashMap<String, Object>();

				if(logMap != null) {
					paramMap.put("log_key", logMap.get("log_key"));
				}

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> useLogInfoList = apiLoanReturnService.getLoanReturnUseLogInfoList(paramMap);
				if (useLogInfoList == null) {
					continue;
				}
				
				for (Map<String, Object> useLogInfo : useLogInfoList) {
					if (useLogInfo != null) {

						useLogInfo.put("library_key", library_key);
						useLogInfo.put("device_key", device_key);

						Map<String, Object> useLogInfoMap = apiLoanReturnService.getLoanReturnUseLogInfo(null, useLogInfo);

						if(useLogInfoMap != null) {
							useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
							apiLoanReturnService.updateLoanReturnUseLogInfo(null, useLogInfo);
							updateCnt++;
						} else {
							apiLoanReturnService.insertLoanReturnUseLogInfo(null, useLogInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + useLogInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleLoanReturnUseLogInfo End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleLoanReturnRuleInfo() {
		logger.info(">>> scheduleLoanReturnRuleInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleLoanReturnRuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));

				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> ruleInfoList = apiLoanReturnService.getLoanReturnRuleInfoList(Collections.emptyMap());
				if (ruleInfoList == null) {
					continue;
				}
				for (Map<String, Object> ruleInfo : ruleInfoList) {
					if (ruleInfo != null) {
						ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));

						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						Map<String, Object> ruleInfoMap = apiLoanReturnService.getLoanReturnRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));
							apiLoanReturnService.updateLoanReturnRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiLoanReturnService.insertLoanReturnRuleInfo(null, ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + ruleInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleLoanReturnRuleInfo End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 대출반납기 정책에 전송 요청 상태인 데이터를 확인한 뒤 대출반납기 DB에 반영한다.
	 */
	private void scheduleLoanReturnRuleSend() throws Exception {
		logger.info(">>> scheduleLoanReturnRuleSend Start!! - " + LocalDateTime.now());
		
		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();
			
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleLoanReturnHolidayInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				
				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService
						.getLoanReturnRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 INSERT
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiLoanReturnService
								.updateLoanReturnRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiLoanReturnService.updateLoanReturnRuleSendResult(null, ruleInfo);
						}
					}
				}
			}
			logger.info(">>> scheduleLoanReturnRuleSend End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleLoanReturnHolidayInfo() throws Exception {
		logger.info(">>> scheduleLoanReturnHolidayInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleLoanReturnHolidayInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int successCnt = 0;
				int failCnt = 0;

				List<Map<String, Object>> holidayList = apiLoanReturnService.getLoanReturnHolidayList(Collections.emptyMap());
				if (holidayList == null) {
					continue;
				}
				
				for(Map<String, Object> holidayInfo: holidayList) {
					if(holidayInfo != null) {
						holidayInfo.put("library_key", library_key);
						holidayInfo.put("device_key", device_key);

						apiLoanReturnService.upsertLoanReturnHolidayInfo(null, holidayInfo);
						successCnt++;
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + holidayList.size());
				logger.info("SUCCESS >>> " + successCnt);
				logger.info("FAIL >>> " + failCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 대출반납기 휴관일 설정에 전송 요청 상태인 데이터를 확인한 뒤 대출반납기 DB에 반영한다.
	 */
	private void scheduleLoanReturnHolidaySend() throws Exception {
		logger.info(">>> scheduleLoanReturnHolidaySend Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> scheduleLoanReturnHolidaySend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				if (device_id.equals("")) {
					continue;
				}
			
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("device_key", device.get("rec_key"));
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> holidayInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService.getLoanReturnHolidayInfoList(null, device)).get("result")).get("items");

				// 대상이 있으면 대출반납기DB에 INSERT
				if (holidayInfoList.size() == 0) {
					continue;
				}
				for (Map<String, Object> holidayInfo : holidayInfoList) {
					holidayInfo.put("worker", String.format("%s@%s", "nims", server_ip));

					Map<String, Object> resultMap = (Map<String, Object>) apiLoanReturnService.upsertLoanReturnHolidayInfo(holidayInfo).get("result");

					if(resultMap.get("CODE").toString().equals("200")) {
						holidayInfo.put("send_req_yn", "S");
						apiLoanReturnService.updateLoanReturnHolidaySendResult(null, holidayInfo);
					}
				}
			}
			
			logger.info(">>> scheduleLoanReturnHolidaySend End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleLoanReturnEquipRuleInfo() throws Exception {
		logger.info(">>> scheduleLoanReturnEquipRuleInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleLoanReturnEquipRuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int successCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> ruleList = apiLoanReturnService.getLoanReturnEquipRuleInfoList(Collections.emptyMap());
				if (ruleList == null) {
					continue;
				}
				
				for(Map<String, Object> ruleInfo: ruleList) {
					if(ruleInfo != null) {
						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						apiLoanReturnService.upsertLoanReturnEquipRuleInfo(null, ruleInfo);
						successCnt++;
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + ruleList.size());
				logger.info("SUCCESS >>> " + successCnt);
				logger.info("FAIL >>> " + failCnt);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 대출반납기 장비 설정에 전송 요청 상태인 데이터를 확인한 뒤 대출반납기 DB에 반영한다.
	 */
	private void scheduleLoanReturnEquipRuleSend() throws Exception {
		logger.info(">>> scheduleLoanReturnEquipRuleSend Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			if (deviceList.size() == 0) {
				logger.info(">>> scheduleLoanReturnEquipRuleSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				
				if (device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("device_key", device.get("rec_key"));
				device.put("send_req_yn", "Y");

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService
						.getLoanReturnEquipRuleInfoList(null, device)).get("result")).get("items");

				// 대상이 있으면 대출반납기DB에 INSERT
				if (ruleInfoList.size() > 0) {
					for (Map<String, Object> ruleInfo : ruleInfoList) {
						ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						Map<String, Object> resultMap = (Map<String, Object>) apiLoanReturnService
								.updateLoanReturnEquipRuleInfo(ruleInfo).get("result");

						if(resultMap.get("CODE").toString().equals("200")) {
							ruleInfo.put("send_req_yn", "S");
							apiLoanReturnService.updateLoanReturnEquipRuleSendResult(null, ruleInfo);
						}
					}
				}
			}
			
			logger.info(">>> scheduleLoanReturnEquipRuleSend End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleLoanReturnBannerSettingInfo() throws Exception {
		logger.info(">>> scheduleLoanReturnBannerSettingInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleLoanReturnBannerSettingInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				int successCnt = 0;
				int failCnt = 0;
				
				List<Map<String, Object>> ruleList = apiLoanReturnService.getLoanReturnBannerSettingInfo(Collections.emptyMap());
				if (ruleList == null) {
					continue;
				}
				for(Map<String, Object> ruleInfo: ruleList) {
					if(ruleInfo != null) {
						ruleInfo.put("library_key", library_key);
						ruleInfo.put("device_key", device_key);

						apiLoanReturnService.upsertLoanReturnBannerSettingInfo(null, ruleInfo);
						successCnt++;
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + ruleList.size());
				logger.info("SUCCESS >>> " + successCnt);
				logger.info("FAIL >>> " + failCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @throws Exception 대출반납기 장비 설정에 전송 요청 상태인 데이터를 확인한 뒤 대출반납기 DB에 반영한다.
	 */
	private void scheduleLoanReturnBannerSettingSend() throws Exception {
		logger.info(">>> scheduleLoanReturnBannerSettingSend Start!! - " + LocalDateTime.now());
		
		try {
			String server_ip = InetAddress.getLocalHost().getHostAddress();
			
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 1);

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleLoanReturnBannerSettingSend End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				
				if(device_id.equals("")) {
					continue;
				}
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());
				
				device.put("device_key", device.get("rec_key"));
				device.put("send_req_yn", "Y");
				
				List<Map<String, Object>> settingInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiLoanReturnService.getLoanReturnBannerSettingInfo(null, device)).get("result")).get("items");

				// 대상이 있으면 대출반납기DB에 UPDATE
				if (settingInfoList.size() > 0) {
					for(Map<String, Object> settingInfo : settingInfoList) {
						settingInfo.put("worker", String.format("%s@%s", "nims", server_ip));

						int result = apiLoanReturnService.updateLoanReturnBannerSettingInfo(settingInfo);
						if(result > 0) {
							settingInfo.put("send_req_yn", "S");
							apiLoanReturnService.updateLoanReturnBannerSettingSendResult(null, settingInfo);
						}
					}
				}
			}
			
			logger.info(">>> scheduleLoanReturnBannerSettingSend End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleReturnUseLogInfo() throws Exception {
		logger.info(">>> scheduleReturnUseLogInfo Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 3);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleReturnUseLogInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}
				
				Map<String, Object> result = apiReturnService.getReturnUseLogInfoList(device);

				@SuppressWarnings("unchecked")
				List<Map<String, Object>> useLogInfoList = (List<Map<String, Object>>) result.get("LogList");

				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				if (useLogInfoList == null) {
					continue;
				}
			
				for (Map<String, Object> useLogInfo : useLogInfoList) {
					if (useLogInfo != null) {
						useLogInfo.put("library_key", library_key);
						useLogInfo.put("device_key", device_key);

						Map<String, Object> useLogInfoMap = apiReturnService.getReturnUseLogInfo(null, useLogInfo);

						if (useLogInfoMap != null) {
							useLogInfo.put("rec_key", useLogInfoMap.get("rec_key"));
							apiReturnService.updateReturnUseLogInfo(null, useLogInfo);
							updateCnt++;
						} else {
							apiReturnService.insertReturnUseLogInfo(null, useLogInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
				}
				logger.info("TOTAL >>> " + useLogInfoList.size());
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
			
			logger.info(">>> scheduleReturnUseLogInfo End!! - " + LocalDateTime.now());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleReturnRuleInfo() {

		logger.info(">>> scheduleReturnRuleInfo Start!! - " + LocalDateTime.now());
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 3);

			List<Map<String, Object>> deviceList = deviceService.getDeviceDBConnList(deviceParams);

			// device 연결 정보가 없으면 종료
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleReturnRuleInfo End!! - " + LocalDateTime.now());
				return;
			}
			for (Map<String, Object> device: deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				String library_key = ParamUtil.parseString(device.get("library_key"));
				String device_key = ParamUtil.parseString(device.get("device_key"));
				
				if(device_id.equals("")) {
					continue;
				}

				int totalCnt = 0;
				int insertCnt = 0;
				int updateCnt = 0;
				int failCnt = 0;
				
				Map<String, Object> result = (Map<String, Object>) apiReturnService.getReturnRuleInfoList(device).get("config");
				Map<String, Object> ruleInfo = new HashMap<String, Object>();

				ruleInfo.put("worker", String.format("%s@%s", "schedule", "localhost"));
				ruleInfo.put("library_key", library_key);
				ruleInfo.put("device_key", device_key);

				for (String key : result.keySet()) {
					if (key != null) {
						ruleInfo.put("rule_name", key);
						ruleInfo.put("rule_value", result.get(key));

						Map<String, Object> ruleInfoMap = apiReturnService.getReturnRuleInfo(null, ruleInfo);

						if (ruleInfoMap != null) {
							ruleInfo.put("rec_key", ruleInfoMap.get("rec_key"));

							apiReturnService.updateReturnRuleInfo(null, ruleInfo);
							updateCnt++;
						} else {
							apiReturnService.insertReturnRuleInfo(null,  ruleInfo);
							insertCnt++;
						}
					} else {
						failCnt++;
					}
					totalCnt++;
				}
				
				logger.info("TOTAL >>> " + totalCnt);
				logger.info("INSERT >>> " + insertCnt);
				logger.info("UPDATE >>> " + updateCnt);
				logger.info("FAIL >>> " + failCnt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scheduleReturnRuleSend() throws Exception {
		logger.info(">>> scheduleReturnRuleSend Start!! - " + LocalDateTime.now());
		
		try {
			Map<String, Object> deviceParams = new HashMap<String, Object>();
			deviceParams.put("model_key", 3);

			String server_ip = InetAddress.getLocalHost().getHostAddress();

			List<Map<String, Object>> deviceList = deviceService.getDeviceList(deviceParams);
			List<Map<String, Object>> dbConnList = deviceService.getDeviceDBConnList(deviceParams);
			if (deviceList.size() == 0 || deviceList.isEmpty()) {
				logger.info(">>> scheduleReturnRuleSend End!! - " + LocalDateTime.now());
				return;
			}
			
			for (Map<String, Object> device : deviceList) {
				String device_id = ParamUtil.parseString(device.get("device_id"));
				if (device_id.equals("")) {
					continue;
				}
				
				DBContextHolder.setDBType(device_id);
				logger.info("DBtype > {}", DBContextHolder.getDBType());

				device.put("send_req_yn", "Y");

				String db_url = "";
				for(Map<String, Object> dbConnInfo : dbConnList) {
					if(dbConnInfo.get("device_key").toString().equals(device.get("rec_key").toString())) {
						db_url = dbConnInfo.get("db_url").toString();
					}
				}

				List<Map<String, Object>> ruleInfoList = (List<Map<String, Object>>) ((Map<String, Object>) (apiReturnService
						.getReturnRuleInfoList(null, device).get("result"))).get("items");

				// 대상이 있으면 예약대출기DB에 INSERT
				if (ruleInfoList.size() == 0) {
					continue;
				}
				for (Map<String, Object> ruleInfo : ruleInfoList) {
					ruleInfo.put("worker", String.format("%s@%s", "nims", server_ip));
					ruleInfo.put("db_url", db_url);

					Map<String, Object> resultMap = (Map<String, Object>) apiReturnService
							.updateReturnRuleInfo(ruleInfo);

					if(resultMap.get("Success").toString().equals("true")) {
						ruleInfo.put("send_req_yn", "S");
						apiReturnService.updateReturnRuleSendResult(null, ruleInfo);
					}
				}
			}
			
			logger.info(">>> scheduleReturnRuleSend End!! - " + LocalDateTime.now());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
