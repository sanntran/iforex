package net.xapxinh.forex.server.entity;

import java.util.Date;

public class Tick extends Pojo {
	
	private static final long serialVersionUID = 1L;
	
	private Date time;
	private double price;
	private String symbol;
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
