package net.ionoff.forex.ea.util;

public class CalculatorUtil {
	
	public static long priceToPip(double price) {
		return (long) (price * 10000);
	}
}
