package com.enicom.nims.aop;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enicom.nims.utils.JsonUtil;

@Aspect
@Component
public class ControllerAspect {
	private Logger logger = LoggerFactory.getLogger(ControllerAspect.class);

	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && "
			+ "@annotation(requestMapping) && execution(* *(..)) && !execution(* com.enicom.nims.*.*(..))")
	private void controller(RequestMapping requestMapping) {}

	@Before("controller(requestMapping)")
	public void logControllerBefore(JoinPoint jp, RequestMapping requestMapping) {
		Object[] args = jp.getArgs();
		Map<String, Object> paramMap = Collections.emptyMap();
		
		if(args.length > 1 && args[1] instanceof Map) {
			paramMap = (Map<String, Object>) args[1];
		}
		
		logger.info("REQUEST [url: {}, params: {}]", Arrays.toString(requestMapping.value()), paramMap);
	}
	
	@SuppressWarnings("unchecked")
	@Around("controller(requestMapping)")
	public Map<String, Object> errorHandlerControllerAround(ProceedingJoinPoint pjp, RequestMapping requestMapping) {
		Signature sig = pjp.getSignature();
		try {
			return (Map<String, Object>) pjp.proceed();
		}
		catch (DataAccessResourceFailureException e) {
			logger.error("Database와 연결할 수 없습니다." + e.getMessage());
			return JsonUtil.makeResultJSON("0002");
		}
		catch (Throwable e) {
			String msg = String.format("%s.%s > %s",sig.getDeclaringTypeName(), sig.getName(), e.getMessage());
			logger.error(msg);
			e.printStackTrace();
		}
		return JsonUtil.makeResultJSON("400");
	}
}
