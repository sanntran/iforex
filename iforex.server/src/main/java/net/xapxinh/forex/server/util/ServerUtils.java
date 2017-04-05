package net.xapxinh.forex.server.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import org.apache.log4j.Logger;

public class ServerUtils {

	static Logger logger = Logger.getLogger(ServerUtils.class.getName());

	static final int TIME_OUT = 3000;

	public static String getLocalIP() throws SocketException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		String localIp = "";

		for (NetworkInterface netInterface : Collections.list(nets)) {
			// String displayName = netInterface.getDisplayName();
			// String name = netInterface.getName();
			Enumeration<InetAddress> inetAddresses = netInterface.getInetAddresses();
			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				String inetAddr = inetAddress.toString().replaceAll("/", "");
				if (ServerUtils.isIPAddress(inetAddr) && !inetAddr.contains("127.0.0.1")) {
					return inetAddr;
				}
			}
		}
		return localIp;
	}

	public static boolean isIPAddress(String username) {
		String d[] = username.split("\\.");
		if (d != null && d.length == 4) {
			for (int i = 0; i < d.length; i++) {
				if (!isIntNumber(d[i])) {
					return false;
				}
			}
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isIntNumber(String num) {
		try {
			Integer.parseInt(num);
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static boolean isIntNumber(char num) {
		try {
			Integer.parseInt(String.valueOf(num));
		}
		catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	

	public static String toString(String[] strArr) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < strArr.length; i++) {
			builder.append(strArr[i]);
			if (i < (strArr.length - 1)) {
				builder.append(",");
			}
		}
		return builder.toString();
	}
}
