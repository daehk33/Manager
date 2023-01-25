package com.enicom.nims.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Packet {
	private String xml = null;
	private String service_id = null;
	private String service_type = null;
	private String service_desc = null;

	private List<LinkedHashMap<String, String>> params;

	public final static String REQ = "REQ";
	public final static String RES = "RES";
	public final static String ERR = "ERR";

	/**
	 * [Packet] Packet 생성자 1
	 * 
	 * @param service_id
	 * @param service_type
	 * @param service_desc
	 * @param params
	 * @return
	 */
	public static Packet createPacket(String service_id, String service_type, String service_desc,
			List<LinkedHashMap<String, String>> params) {
		if (service_id == null || service_type == null) {
			return null;
		}

		Packet packet = new Packet();
		try {
			if (params == null)
				params = new ArrayList<LinkedHashMap<String, String>>();

			packet.setService_id(service_id);
			packet.setService_type(service_type);
			packet.setService_desc(service_desc);
			packet.setParams(params);
		} catch (Exception e) {
		}
		return packet;
	}

	/**
	 * [Packet] Packet 생성자 2
	 * 
	 * @param service_id
	 * @param service_type
	 * @param service_desc
	 * @return
	 */
	public static Packet createPacket(String service_id, String service_type, String service_desc) {
		return createPacket(service_id, service_type, service_desc, null);
	}

	/**
	 * [Packet] Packet 생성자 3
	 * 
	 * @param service_id
	 * @param service_type
	 * @param params
	 * @return
	 */
	public static Packet createPacket(String service_id, String service_type,
			List<LinkedHashMap<String, String>> params) {
		return createPacket(service_id, service_type, service_type, params);
	}

	@Override
	public String toString() {
		return String.format("Packet [service_id=%s, service_type=%s, params=%s]\nxml=%s", service_id, service_type,
				params, xml);
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getService_type() {
		return service_type;
	}

	public void setService_type(String service_type) {
		this.service_type = service_type;
	}

	public List<LinkedHashMap<String, String>> getParams() {
		return params;
	}

	public void setParams(List<LinkedHashMap<String, String>> params) {
		this.params = params;
	}

	public String getService_desc() {
		return service_desc;
	}

	public void setService_desc(String service_desc) {
		this.service_desc = service_desc;
	}
}
