package com.enicom.nims.utils;

import java.util.ArrayList;
import com.fazecast.jSerialComm.*;

public class SerialUtil {	
	public static ArrayList<String> getPorts() {
		SerialPort[] ports = SerialPort.getCommPorts();	
		ArrayList<String> portList = new ArrayList<String>();
		
		for (SerialPort port : ports) {
			portList.add(port.getSystemPortName());
		}
		
		return portList;
	}
}