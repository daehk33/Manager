package com.enicom.nims.utils;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.regex.Pattern;

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

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Utils {
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
		String pattern = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		String ip = null;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("X-Forwarded-For");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("X-Real-IP");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("X-RealIP");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getHeader("REMOTE_ADDR");
			}
			if (ip == null || ip.length() == 0 || !Pattern.matches(pattern, ip)) {
				ip = request.getRemoteAddr();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
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

		Enumeration netInterfaces = null;

		Enumeration eHeader = request.getHeaderNames();
		while (eHeader.hasMoreElements()) {
			String hName = (String) eHeader.nextElement();
			String hValue = request.getHeader(hName);
		}

		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return getLocalIp();
		}

		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
			Enumeration address = ni.getInetAddresses();
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
}
