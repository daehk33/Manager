package com.enicom.nims.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PacketUtil {
	private static Logger logger = LoggerFactory.getLogger(PacketUtil.class);

	/**
	 * [Packet] Create XML data using Packet object
	 * 
	 * @param packet
	 * @return
	 */
	public static String toXML(Packet packet) {
		String xml = null;

		// NullpointException 예외 처리
		if (packet == null)
			return null;

		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version\"1.0\" encoding=\"UTF-8\" ?> \r\n");
		sb.append(String.format("<service id=\"%s\" type=\"%s\"> \r\n", packet.getService_id(),
				packet.getService_type()));

		// Param XML 처리
		List<LinkedHashMap<String, String>> params = packet.getParams();

		if (params.size() == 0) {
			sb.append("<params></params> \r\n");
		} else {
			Map<String, String> param = null;

			for (int i = 0; i < params.size(); i++) {
				param = params.get(i);
				Iterator<String> keys = param.keySet().iterator();

				sb.append("<params> \r\n");

				while (keys.hasNext()) {
					String key = keys.next();
					String value = param.get(key);
					sb.append(String.format("<param name=\"%s\">%s</param> \r\n", key, value));
				}
			}
		}
		sb.append("</service>");
		xml = sb.toString();

		packet.setXml(xml);

		return xml;
	}

	/**
	 * [Packet] Parse XML data to Packet object
	 * 
	 * @param xml
	 * @return
	 */
	public static Packet parseXMLtoPacket(String xml) {
		// NullpointException 예외 처리
		if (xml == null)
			return null;

		Packet packet = new Packet();
		packet.setXml(xml);

		try {
			// DOM
			Document doc = Utils.parseXmlString(xml);

			// Tag 요소
			Element root = (Element) doc.getElementsByTagName("service").item(0);

			packet.setService_id(root.getAttribute("id").toUpperCase());
			packet.setService_type(root.getAttribute("type").toUpperCase());

			List<Element> params = Utils.getChildElements(root, "params");
			Element e, e2;
			String key, value;

			LinkedHashMap<String, String> map = null;
			List<LinkedHashMap<String, String>> result = new ArrayList<LinkedHashMap<String, String>>();

			for (int i = 0; i < params.size(); i++) {
				e = params.get(i);
				List<Element> l = Utils.getChildElements(e, "param");
				map = new LinkedHashMap<String, String>();

				for (int j = 0; j < l.size(); j++) {
					e2 = l.get(j);

					key = e2.getAttribute("name");
					value = Utils.getText(e2);

					map.put(key, value);
				}
				result.add(map);
			}

			packet.setParams(result);
			return packet;
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}

		return null;
	}

	/**
	 * [Packet] Parse XML data to Packet object
	 * 
	 * @param xml
	 * @return
	 */
	public static Map<String, Object> parseXMLtoMap(String xml) {
		
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// NullpointException 예외 처리
		if (xml == null)
			return null;

		Packet packet = new Packet();
		packet.setXml(xml);

		try {
			// DOM
			Document doc = Utils.parseXmlString(xml);

			// Tag 요소
			Element root = (Element) doc.getElementsByTagName("service").item(0);

			returnMap.put("service_id",root.getAttribute("id").toUpperCase());
			returnMap.put("service_type",root.getAttribute("type").toUpperCase());

			List<Element> params = Utils.getChildElements(root, "params");
			Element e, e2;
			String key, value;

			LinkedHashMap<String, String> map = null;
			List<LinkedHashMap<String, String>> result = new ArrayList<LinkedHashMap<String, String>>();

			for (int i = 0; i < params.size(); i++) {
				e = params.get(i);
				List<Element> l = Utils.getChildElements(e, "param");
				map = new LinkedHashMap<String, String>();

				for (int j = 0; j < l.size(); j++) {
					e2 = l.get(j);

					key = e2.getAttribute("name");
					value = Utils.getText(e2);

					map.put(key, value);
				}
				result.add(map);
			}
			
			returnMap.put("param",result);

			return returnMap;
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}

		return null;
	}

	/**
	 * [Socket] Packet을 Byte 형식으로 Send 후, Receive 한 데이터를 반환
	 * 
	 * @param host
	 * @param port
	 * @param packet
	 * @return
	 * @throws SocketTimeoutException
	 */
	public static Packet sendPacket(String host, int port, Packet packet, int time) throws SocketTimeoutException {
		if (host == null || packet == null || (port <= 0 || port >= 655535))
			return null;

		Socket socket = new Socket();
		int timeout = time * 1000;
		SocketAddress socketAddress = new InetSocketAddress(host, port);
		try {
			// InputStream 에서 데이터 읽을 때의 Timeout
			socket.setSoTimeout(timeout);
			// Socket 연결 자체에 대한 Timeout
			socket.connect(socketAddress, timeout);

			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			bw.write(toXML(packet), 0, toXML(packet).length());
			bw.flush();

			// -----------------------------------
			InputStream input_data = socket.getInputStream();

			byte[] receivedBuffer = new byte[2048];
			input_data.read(receivedBuffer);
			// -----------------------------------

			String rev = new String(receivedBuffer);
			rev = rev.trim();
			rev = rev.replace("&", "&amp;");
			rev = rev.replace("'", "&apos;");

			socket.close();

			return parseXMLtoPacket(rev);
		} catch (Exception e) {
			logger.warn(e.getMessage());
			// Timeout Exception
			if (e instanceof SocketTimeoutException)
				throw new SocketTimeoutException();
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					logger.debug(e.getMessage());
				}
		}

		return null;
	}
}
