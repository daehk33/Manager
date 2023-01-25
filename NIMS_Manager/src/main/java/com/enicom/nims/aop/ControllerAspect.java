package com.enicom.nims.aop;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;
import com.enicom.nims.utils.Utils;

@Aspect
@Component
public class ControllerAspect {
	private static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
	
	@Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && "
			+ "@annotation(requestMapping) && execution(* *(..))")
	private void controller(RequestMapping requestMapping) {}
	
	@SuppressWarnings("unchecked")
	@Around("controller(requestMapping)")
	public Object errorHandlerControllerAround(ProceedingJoinPoint pjp, RequestMapping requestMapping) {
		Object[] args = pjp.getArgs();
		
		HttpServletRequest request = (HttpServletRequest) args[0];
		Map<String, Object> paramMap = (Map<String, Object>) args[1];
		
		String caller = ParamUtil.parseString(paramMap.get("caller"));
		
		String uri = request.getRequestURI();
		String ip = Utils.getClientIP(request);
		StringBuffer sb = new StringBuffer();
		
		/**
		 * [로그인] 현재 로그인한 사용자 아이디 및 주소 체크
		 * 
		 * > 무시하는 경우
		 * - caller가 nicom
		 * - 요청한 path가 /api로 시작하지 않는 경우
		 */
		// request를 할 때 manager와 ip 정보를 같이 보냄.
		HttpSession session = request.getSession();
		Map<String, Object> manager = (Map<String, Object>) session.getAttribute("MANAGER");

		if(caller.equalsIgnoreCase("nicom")) {}
		else if(!uri.startsWith("/api")) {
			// /api로 시작하지 않는 경우
		}
		else if(manager != null) {
			paramMap.put("ip", ip);
			paramMap.put("menu_path", request.getHeader("referer").replace(request.getHeader("origin"), ""));
			
			paramMap.put("manager_id", manager.get("manager_id"));
			paramMap.put("manager_key", manager.get("rec_key"));
			paramMap.put("worker", String.format("%s@%s",  manager.get("manager_id").toString(), ip));
		}
		else {
			sb.append(String.format("\n* Status: Warn, Message: 로그인 세션이 없음"));
			logger.warn(sb.toString());
			return JsonUtil.makeResultJSON("421");
		}
		
		logger.info("* REQUEST [url: {}, ip: {}, params: {}]", uri, ip, paramMap);
		
		try {
			return pjp.proceed();
		}
		catch (Throwable e) {
			sb.append(String.format("* Status: Error, Message: %s", e.getMessage()));
			logger.error(sb.toString());
			e.printStackTrace();
			
			return JsonUtil.makeResultJSON("400", e.getMessage());
		}
	}
}
