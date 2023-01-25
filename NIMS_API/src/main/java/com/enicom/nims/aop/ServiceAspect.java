package com.enicom.nims.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.enicom.nims.utils.JsonUtil;
import com.enicom.nims.utils.ParamUtil;

@Aspect
@Component
public class ServiceAspect {
	private Logger logger = LoggerFactory.getLogger(ServiceAspect.class);
	
	@Pointcut(value = "!execution(* com.enicom.nims.api.service.*Impl.*(..)) &&"
			+ "execution(* com.enicom.nims..service.*Impl.*(..))")
	private void allService() {}
	
	@Around("allService()")
	public Object logServiceAround(ProceedingJoinPoint pjp) {
		Object[] args = pjp.getArgs();
		Signature sig = pjp.getSignature();
		String[] methods = sig.getDeclaringTypeName().split("\\.");
		
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("> Service Started\n> Method: %s.%s, Params: %s\n", methods[methods.length-1], sig.getName(), args.length > 1 ? args[1] : "{}"));
		
		try {
			Object result = pjp.proceed();
			
			String data = ParamUtil.parseString(result);
			if (data.length() > 100) sb.append("> Data from API Length: " + String.valueOf(data.length()));
			else sb.append("> Returned Data: " + data);
			
			logger.debug(sb.toString());
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			logger.error("{}\n> Error Occured: {}", sb.toString(), e.getMessage());
			return JsonUtil.makeResultJSON("400");
		}
	}
}