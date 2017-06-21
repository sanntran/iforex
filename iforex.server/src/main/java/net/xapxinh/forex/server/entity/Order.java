package net.xapxinh.forex.server.entity;

import java.util.Date;

public class Order {
	
	public enum TYPE {
		BUY, SELL
	}
		
	private String symbol;
	private float size;
	private String type;
	private Date openTime;
	private Date closeTime;
	private double openPrice;
	private double closePrice;
	private double stopLoss;
	private double takeProfit;
	private double swap;
	private double spread;
	private String comment;
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public float getSize() {
		return size;
	}
	public void setSize(float size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public Date getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}
	public double getStopLoss() {
		return stopLoss;
	}
	public void setStopLoss(double stopLoss) {
		this.stopLoss = stopLoss;
	}
	public double getTakeProfit() {
		return takeProfit;
	}
	public void setTakeProfit(double takeProfit) {
		this.takeProfit = takeProfit;
	}
	public double getSwap() {
		return swap;
	}
	public void setSwap(double swap) {
		this.swap = swap;
	}
	public double getSpread() {
		return spread;
	}
	public void setSpread(double spread) {
		this.spread = spread;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}	
}
