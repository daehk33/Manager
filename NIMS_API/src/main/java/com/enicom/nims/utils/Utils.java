package com.enicom.nims.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
	private static Logger logger = LoggerFactory.getLogger(Utils.class);

	public static String toString(Document doc, boolean outputDocType) {
		StringWriter writer = new StringWriter();
		Transformer transformer = null;

		try {
			transformer = TransformerFactory.newInstance().newTransformer();

			if (outputDocType) {
				DocumentType type = doc.getDoctype();
				if (type != null) {
					String publicId = type.getPublicId();

					if (publicId != null) {
						transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_PUBLIC, publicId);
					}
					String systemId = type.getSystemId();

					if (systemId != null) {
						transformer.setOutputProperty(javax.xml.transform.OutputKeys.DOCTYPE_SYSTEM, systemId);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Could not create a transformer." + e);
		}

		try {
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
		} catch (Exception e) {
			System.out.println("Could not transform document." + e);
		}

		return writer.toString();
	}

	public static boolean saveXml(String filename, Document doc) {
		try {
			DOMSource source = new DOMSource(doc);

			File file = new File(filename);
			StreamResult result = new StreamResult(file);

			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");

			xformer.transform(source, result);

			return true;
		} catch (TransformerConfigurationException tce) {
			tce.printStackTrace();
		} catch (TransformerException te) {
			te.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public static Document createDomDocument() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(false);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();

			return doc;
		} catch (Exception e) {

		}

		return null;
	}

	public static Document parseXmlFile(String filename) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			factory.setValidating(false);
			// factory.setNamespaceAware(false);

			DocumentBuilder builder = factory.newDocumentBuilder();
			File f = new File(filename);

			if (!f.exists())
				return null;

			Document doc = builder.parse(f);

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Document parseXmlString(String xml) {
		if (xml == null || xml.trim().length() < 5)
			return null;

		System.out.println("parseXmlString ---------------");
		System.out.println(xml);
		System.out.println("parseXmlString ---------------");

		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			factory.setValidating(false);
			// factory.setNamespaceAware(false);

			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.trim().getBytes("UTF-8")));

			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public static Element getChildElement(Element parent, String child_name) {
		NodeList child = parent.getChildNodes();
		Node node = null;
		Element elem = null;

		for (int i = 0; i < child.getLength(); i++) {
			node = child.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elem = (Element) node;

				if (child_name.equalsIgnoreCase(elem.getNodeName())) {
					return elem;
				}
			}
		}

		return null;
	}

	public static java.util.List<Element> getChildElements(Element parent, String child_name) {
		NodeList child = parent.getChildNodes();
		java.util.List<Element> list = new ArrayList<Element>();
		Node node = null;
		Element elem = null;

		for (int i = 0; i < child.getLength(); i++) {
			node = child.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elem = (Element) node;

				if (child_name.equals(elem.getNodeName())) {
					list.add(elem);
				}
			}
		}

		return list;
	}

	public static List<Map<String, Object>> getChildElementsByTagName(String path, String tagName) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		System.out.println(path);

		File file = new File(path);
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			logger.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(),
					file.canRead());
			return list;
		}

		Document document = Utils.parseXmlFile(path);
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			NodeList childList = node.getChildNodes();

			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				if (child.getNodeType() != Node.ELEMENT_NODE)
					continue;

				Map<String, Object> childMap = new HashMap<String, Object>();

				childMap.put("name", child.getNodeName().toLowerCase());
				childMap.put("value", child.getTextContent());

				list.add(childMap);
			}
		}

		return list;
	}

	public static Map<String, Object> getValueByTagName(String path, String tagName) {
		Map<String, Object> result = new HashMap<String, Object>();
		File file = new File(path);
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			logger.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(),
					file.canRead());
			return JsonUtil.makeResultJSON("400", "파일이 없거나 읽을 수 없습니다");
		}

		Document document = Utils.parseXmlFile(path);
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			Map<String, Object> info = new HashMap<String, Object>();

			info.put("name", node.getNodeName().toLowerCase());
			info.put("value", node.getTextContent());

			result.put("info", info);
		}

		return result;
	}

	public static Map<String, Object> updateChildElementsByTagName(String path, String tagName,
			Map<String, Object> paramMap) {
		File file = new File(path);
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			logger.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(),
					file.canRead());
			return JsonUtil.makeResultJSON("400", "파일이 없거나 읽을 수 없습니다");
		}

		Document document = Utils.parseXmlFile(path);
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			NodeList childList = node.getChildNodes();

			for (int i = 0; i < childList.getLength(); i++) {
				Node child = childList.item(i);
				if (child.getNodeType() != Node.ELEMENT_NODE)
					continue;

				boolean update = paramMap.get(child.getNodeName().toLowerCase()) != null;
				String val = ParamUtil.parseString(paramMap.get(child.getNodeName().toLowerCase()));
				if (update) {
					child.setTextContent(val);
				}
			}
		}

		return FileUtil.writeFile(path, Utils.toString(document, false));
	}

	public static Map<String, Object> updateValueByTagName(String path, String tagName, Map<String, Object> paramMap) {
		File file = new File(path);
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			logger.error("File에 접근할 수 없습니다. 존재여부: {}, 파일여부: {}, 권한여부: {}", file.exists(), file.isFile(),
					file.canRead());
			return JsonUtil.makeResultJSON("400", "파일이 없거나 읽을 수 없습니다");
		}

		Document document = Utils.parseXmlFile(path);
		NodeList nodeList = document.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			Node node = nodeList.item(0);
			String val = ParamUtil.parseString(paramMap.get("value"));
			if (!val.equals("")) {
				node.setTextContent(val);
			}
		}

		return FileUtil.writeFile(path, Utils.toString(document, false));
	}

	public static java.util.List<Element> getChildElements(Element parent) {
		NodeList child = parent.getChildNodes();
		java.util.List<Element> list = new ArrayList<Element>();
		Node node = null;
		Element elem = null;

		for (int i = 0; i < child.getLength(); i++) {
			node = child.item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				elem = (Element) node;
				list.add(elem);
			}
		}

		return list;
	}

	public static String getChildText(Element parent, String child_name) {
		Element child = getChildElement(parent, child_name);

		if (child == null)
			return null;

		return getText(child);
	}

	public static String getText(Element node) {
		StringBuffer sb = new StringBuffer();
		NodeList list = node.getChildNodes();
		Node nod;

		for (int i = 0; i < list.getLength(); i++) {
			nod = list.item(i);

			switch (nod.getNodeType()) {
			case Node.CDATA_SECTION_NODE:
			case Node.TEXT_NODE:
				sb.append(nod.getNodeValue());
			}
		}

		return sb.toString();
	}

	public static String getClientIP(HttpServletRequest request) {
		String ip = null;
		ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}

		return ip;
	}

	public static String getClientIPTest(HttpServletRequest request) {
		String ip = null;
		ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		System.out.println("ip :" + ip);

		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			String strIpAdress = inetAddress.getHostAddress();

			System.out.println("inetAddress IP : " + strIpAdress);

			System.out.println("Inet4Address ip :" + Inet4Address.getLocalHost().getHostAddress());
			System.out.println("getCurrentEnvironmentNetworkIp ip :" + getCurrentEnvironmentNetworkIp(request));
			System.out.println("getIP ip :" + getIP(request));

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return ip;
	}

	public static String getIP(HttpServletRequest request) throws UnknownHostException {

		String remoteHost = request.getRemoteHost();
		String remoteAddr = request.getRemoteAddr();
		if (remoteAddr.equals("0:0:0:0:0:0:0:1")) {
			InetAddress localip = InetAddress.getLocalHost();
			remoteAddr = localip.getHostAddress();
			remoteHost = localip.getHostName();
		}
		int remotePort = request.getRemotePort();

		String msgoutput = remoteHost + " (" + remoteAddr + " : " + remotePort + ")";
		return msgoutput;
	}

	public static String getCurrentEnvironmentNetworkIp(HttpServletRequest request) {

		Enumeration<?> netInterfaces = null;

		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return getLocalIp();
		}

		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
			Enumeration<?> address = ni.getInetAddresses();
			if (address == null) {
				return getLocalIp();
			}
			while (address.hasMoreElements()) {
				InetAddress addr = (InetAddress) address.nextElement();
				if (!addr.isLoopbackAddress() && !addr.isSiteLocalAddress() && !addr.isAnyLocalAddress()) {
					String ip = addr.getHostAddress();
					if (ip.indexOf(".") != -1 && ip.indexOf(":") == -1) {
						return ip;
					}
				}
			}
		}
		return getLocalIp();
	}

	public static String getLocalIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return null;
		}
	}

	public static int getEndDay(String startDate) {
		Calendar cal = Calendar.getInstance();
		if (!startDate.equals("")) {
			cal.set(Integer.parseInt(startDate.substring(0, 4)), Integer.parseInt(startDate.substring(5, 7)) - 1, 01);
		}
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static String getNextDate(String startDate, String gubun) {
		String rtnVal = "";

		if (!startDate.equals("")) {
			Calendar cal = Calendar.getInstance();
			startDate = StringUtil.rpad(startDate, 10, '0');
			
			int year = ParamUtil.parseInt(startDate.substring(0, 4));
			int month = ParamUtil.parseInt(startDate.substring(5, 7));
			int day = ParamUtil.parseInt(startDate.substring(8, 10));
			
			if (gubun.equals("Day")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				cal.set(year, month - 1, day);
				cal.add(Calendar.DATE, 1); // 다음달

				rtnVal = df.format(cal.getTime());
			} else if (gubun.equals("Month")) {
				DateFormat df = new SimpleDateFormat("yyyy-MM");
				cal.set(year, month - 1, 01);
				cal.add(Calendar.MONTH, 1); // 다음달

				rtnVal = df.format(cal.getTime());
			} else if (gubun.equals("Year")) {
				DateFormat df = new SimpleDateFormat("yyyy");
				cal.set(year, 01, 01);
				cal.add(Calendar.YEAR, 1); // 다음년도

				rtnVal = df.format(cal.getTime());
			}
		}
		
		return rtnVal;
	}
	
	public static String getEndDate(String startDate, String gubun) {
		String rtnVal = "";

		if (!startDate.equals("")) {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			
			startDate = StringUtil.rpad(startDate, 10, '0');
			int year = ParamUtil.parseInt(startDate.substring(0, 4));
			int month = ParamUtil.parseInt(startDate.substring(5, 7));
			int day = ParamUtil.parseInt(startDate.substring(8, 10));
			
			if (gubun.equals("Day")) {
				cal.set(year, month - 1, day);
				cal.add(Calendar.DATE, 1); // 다음달

				rtnVal = df.format(cal.getTime());
			} else if (gubun.equals("Month")) {
				cal.set(year, month - 1, 01);
				cal.add(Calendar.MONTH, 1); // 다음달
				cal.add(Calendar.DATE, -1);
				
				rtnVal = df.format(cal.getTime());
			} else if (gubun.equals("Year")) {
				cal.set(year, 12, 31);

				rtnVal = df.format(cal.getTime());
			}
		}
		
		return rtnVal;
	}
	public static Set<String> getSetFromStr(String str) {
		String[] arr = str.split(",");
		Stream<String> stream = Arrays.stream(arr).map(item -> item.trim()).filter(item -> !item.equals(""));
		return stream.collect(Collectors.toSet());
	}

	public static SortedSet<String> getSortedSet() {
		SortedSet<String> set = new TreeSet<String>(new Comparator<String>() {
			@Override
			public int compare(String obj1, String obj2) {
				int val1 = ParamUtil.parseInt(obj1);
				int val2 = ParamUtil.parseInt(obj2);

				if (val1 > val2)
					return 1;
				// 0을 반환하지 않을 경우 삭제 안됨.
				else if (val1 == val2)
					return 0;
				else
					return -1;
			}
		});

		return set;
	}
}
