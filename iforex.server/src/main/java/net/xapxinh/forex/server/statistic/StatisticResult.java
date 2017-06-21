package net.xapxinh.forex.server.statistic;

public class StatisticResult {
	
	public static double getEuUsSessionM15AvgHeigh() {
		return 0.00034;
	}
	
	public static double getEuUsSessionHighCandlePercent() {
		return 0.38;
	}
	
	/*
	
	public static void analyze() {

		List<EurUsdM15Candle> eurUsdM15Candles = candleService.loadAll(EurUsdM15Candle.class);
		long euUsHighCandleCount = 0;
		long euUsCandleCount = 0;
		double totalEuUsCandleHeigh = 0;
		
		for (EurUsdM15Candle candle : eurUsdM15Candles) {
			if (candle.isInEuUsSession()) {
				euUsCandleCount ++;
				totalEuUsCandleHeigh += candle.getHeigh();
				if (candle.getHeigh() > EUUS_SESION_EURUSD_M15_AVG_HEIGH) {
					euUsHighCandleCount ++;
				}
			}
		}
		
		System.out.println("EUUS_SESION_EURUSD_M15_AVG_HEIGH " + totalEuUsCandleHeigh/euUsCandleCount);
		System.out.println("EUUS_SESION_EURUSD_HIGH_CANDLE_PERCENT " + (double)euUsHighCandleCount/euUsCandleCount);
	}
	
	*/
}
