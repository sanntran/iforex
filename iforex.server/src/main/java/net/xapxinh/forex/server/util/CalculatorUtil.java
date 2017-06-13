package net.xapxinh.forex.server.util;

public class CalculatorUtil {
	
	public static long priceToPip(double price) {
		return (long) (price * 10000);
	}
}
