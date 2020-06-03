package net.ionoff.forex.ea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class Order {
	
	public enum TYPE {
		BUY, SELL
	}
		
	private String symbol;
	private double size;
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

}
