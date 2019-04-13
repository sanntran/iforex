package net.ionoff.forex.ea.model;

import java.io.Serializable;
import java.util.Date;

public class Tick implements Serializable {
	
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
