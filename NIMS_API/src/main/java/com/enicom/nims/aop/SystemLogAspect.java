package com.enicom.nims.aop;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enicom.nims.log.service.LogService;
import com.enicom.nims.manager.service.ManagerService;
import com.enicom.nims.utils.ParamUtil;

@Aspect
@Component
public class SystemLogAspect {
	private Logger logger = LoggerFactory.getLogger(SystemLogAspect.class);

	@Pointcut(value = "!execution(* com.enicom.nims.log.service.*Impl.insert*(..)) && "
			+ "!execution(* com.enicom.nims.api.service.*Impl.*(..)) &&"
			+ "!execution(* com.enicom.nims..service.DeviceServiceImpl.updateDeviceConnInfo(..)) &&"
			+ "(execution(* com.enicom.nims..service.*Impl.insert*(..)) ||"
			+ "execution(* com.enicom.nims..service.*Impl.update*(..)) ||"
			+ "execution(* com.enicom.nims..service.*Impl.delete*(..)))")
	private void actionService() {}

	@AfterReturning(pointcut = "actionService()", returning = "result")
	public void logServiceAfter(JoinPoint jp, Object result) {
		@SuppressWarnings("unchecked")
		Map<String, Object> returnMap = (Map<String, Object>) ((Map<String, Object>) result).get("result");
		
		// 시스템 호출시 HttpServletRequest ==> null
		if(jp.getArgs()[0] == null) return;
		
		logActionMethod(jp, translateMethodToMap(jp, returnMap));
	}

	/**
	 * Parsing method signature
	 * method(HttpServletRequest request, Map<String, Object> paramMap)
	 * 
	 * @param jp
	 * @param returnMap
	 * @return
	 */
	private Map<String, Object> translateMethodToMap(JoinPoint jp, Map<String, Object> returnMap) {
		Map<String, Object> result = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		Map<String, Object> paramMap = (Map<String, Object>) jp.getArgs()[1];

		// Method 이름 파싱
		String[] words = jp.getSignature().getName().split("(?=\\p{Upper})");
		String action = words[0];
		String target = String.join("", Arrays.copyOfRange(words, 1, words.length - 1));
		
		String title = null;
		if("insert".equalsIgnoreCase(action)) {
			title = String.format("신규 %s 추가", target);
		}
		else if("update".equalsIgnoreCase(action)) {
			title = String.format("%s 수정", target);
		}
		else if("delete".equalsIgnoreCase(action)) {
			title = String.format("%s 삭제", target);
		}
		else {
			title = String.format("%s 기타", target);
		}
		// logContent 생성
		String logContent = String.format("[%s] rec_key: %s, response status: %s, worker: %s", title,
				ParamUtil.parseString(paramMap.get("rec_key")), ParamUtil.parseString(returnMap.get("CODE")), ParamUtil.parseString(paramMap.get("worker")));
		
		// result에 사용자 및 도서관 정보 추가
		addUserInfo(jp, result, paramMap);
		
		// result param 추가
		result.put("manager_key", paramMap.get("manager_key"));
		result.put("menu_path", paramMap.get("menu_path"));
		result.put("log_type", action);
		result.put("log_content", logContent);

		return result;
	}

	@Autowired
	private LogService logService;

	private void logActionMethod(JoinPoint jp, Map<String, Object> paramMap) {
		try {
			logService.insertLogInfo((HttpServletRequest) jp.getArgs()[0], paramMap);
		} catch (Exception e) {
			logger.error(">>> System Log 생성 불가! - insertLogInfo");
			e.printStackTrace();
		}
	}
	
	@Autowired
	private ManagerService managerService;
	
	private void addUserInfo(JoinPoint jp, Map<String, Object> result, Map<String, Object> paramMap){
		try {
			result.putAll(managerService.getManagerInfoForLog((HttpServletRequest) jp.getArgs()[0], paramMap));
		} catch (Exception e) {
			logger.error(">>> System Log 생성 불가 - getManagerInfo");
			e.printStackTrace();
		}
	}
	
}
