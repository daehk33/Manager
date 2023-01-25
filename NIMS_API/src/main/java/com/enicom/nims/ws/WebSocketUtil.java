package com.enicom.nims.ws;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WebSocketUtil {
	private static Logger logger = LoggerFactory.getLogger(WebSocketUtil.class);
	private final SimpMessagingTemplate template;
	
	@Autowired
	public WebSocketUtil(SimpMessagingTemplate template) {
		this.template = template;
	}
	
	public void sendToDashboard(String type, String message) {
		String target = String.format("/topic/live/%s", type);
		logger.info(String.format("Target(%s)으로 WebSocket 전달", target));
		this.template.convertAndSend(target, message);
	}
	
	public String createMessage(Object date, Object message, Object library_key, Object type) {
		logger.info("WebSocket 메세지 생성");
		ObjectMapper objectMapper = new ObjectMapper();
		String result;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("library_key", library_key);
		paramMap.put("date", date);
		paramMap.put("message", message);
		paramMap.put("type", type);
		
		try {
			result = objectMapper.writeValueAsString(paramMap);
		}
		catch (JsonProcessingException e) {
			result = "";
			logger.error("WebSocket 메세지 생성 중 오류 발생");
			e.printStackTrace();
		}
		return result;
	}
}