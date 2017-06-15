package net.xapxinh.forex.server.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Wave extends Pojo {
	
	public static final int MIN_SIZE = 3;
	
	private static final long serialVersionUID = 1L;
	
	private List<Candle> candles;

	public abstract Wave newInstance();
	
	public List<Candle> getCandles() {
		return candles;
	}

	public void setCandles(List<Candle> candles) {
		this.candles = candles;
	}
	
	public synchronized void addCandle(Candle candle) {
		if (candles == null) {
			candles = new ArrayList<>();
		}
		candles.add(candle);
	}
	
	public boolean isUp() {
		Candle lowestCandle = getLowestCandle();
		Candle highestCandle = getHighestCandle();
		Candle firstCandle = getFirstCandle();
		if (lowestCandle == null || firstCandle == null) {
			return false;
		}
		return lowestCandle.getTime().before(highestCandle.getTime()) && lowestCandle.getTime().equals(firstCandle);
	}
	
	public boolean isDown() {
		Candle lowestCandle = getLowestCandle();
		Candle highestCandle = getHighestCandle();
		Candle firstCandle = getFirstCandle();
		if (highestCandle == null || firstCandle == null) {
			return false;
		}
		return highestCandle.getTime().before(lowestCandle.getTime()) && highestCandle.equals(firstCandle);
	}
	
	public boolean hasDirection() {
		return isUp() || isDown();
	}
	
	public boolean isHesitant() {
		Candle lowestCandle = getLowestCandle();
		Candle highestCandle = getHighestCandle();
		Candle firstCandle = getFirstCandle();
		if (lowestCandle == null || highestCandle == null || firstCandle == null) {
			return false;
		}
		return lowestCandle.equals(highestCandle) && lowestCandle.equals(firstCandle);
	}
	
	
	public Candle getHighestCandle() {
		Candle highestCandle = getCandles().get(0);
		for (Candle candle : getCandles()) {
			if (candle.getHigh() > highestCandle.getHigh()) {
				highestCandle = candle;
			}
		}
		return highestCandle;
	}
	
	public Candle getLowestCandle() {
		Candle lowestCandle = getCandles().get(0);
		for (Candle candle : getCandles()) {
			if (candle.getLow() < lowestCandle.getLow()) {
				lowestCandle = candle;
			}
		}
		return lowestCandle;
	}
	
	public double getLowestPrice() {
		return getLowestCandle().getLow();
	}
	
	public double getHighestPrice() {
		return getHighestCandle().getHigh();
	}
	
	public Date getDate() {
		if (candles == null || candles.isEmpty()) {
			return null;
		}
		return candles.get(0).getTime();
	}

	public Candle getFirstCandle() {
		if (candles == null || candles.isEmpty()) {
			return null;
		}
		return candles.get(0);
	}

	public Candle getLastCandle() {
		if (candles == null || candles.isEmpty()) {
			return null;
		}
		return candles.get(candles.size() - 1);
	}

	public int getPeriod() {
		return 0;
	}

	public double getMinHeigh() {
		return 0;
	}
}
