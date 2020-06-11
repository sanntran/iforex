package net.ionoff.forex.ea.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Pattern implements Entity {
	private static final long serialVersionUID = 1L;

	public static final String MT4_CSV_DATE_FORMAT = "yyyy.MM.dd,HH:mm";

	@Id
	private long id;

	@Column(name = "last_1candle_price")
	private double last1candlePrice;

	@Column(name = "last_2candle_price")
	private double last2candlePrice;

	@Column(name = "last_3candle_price")
	private double last3candlePrice;

	@Column(name = "last_15candle_price")
	private double last15candlePrice;

	@Column(name = "last_lowest_price")
	private double lastLowestPrice;

	@Column(name = "last_lowest_candle")
	private double lastLowestCandle;

	@Column(name = "last_highest_price")
	private double lastHighestPrice;

	@Column(name = "last_highest_candle")
	private double lastHighestCandle;

	@Column(name = "next_1candle_price")
	private double next1candlePrice;

	@Column(name = "next_2candle_price")
	private double next2candlePrice;

    @Column(name = "next_3candle_price")
    private double next3candlePrice;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

    public double getLast1candlePrice() {
        return last1candlePrice;
    }

    public void setLast1candlePrice(double last1candlePrice) {
        this.last1candlePrice = last1candlePrice;
    }

    public double getLast2candlePrice() {
        return last2candlePrice;
    }

    public void setLast2candlePrice(double last2candlePrice) {
        this.last2candlePrice = last2candlePrice;
    }

    public double getLast3candlePrice() {
        return last3candlePrice;
    }

    public void setLast3candlePrice(double last3candlePrice) {
        this.last3candlePrice = last3candlePrice;
    }

    public double getLast15candlePrice() {
        return last15candlePrice;
    }

    public void setLast15candlePrice(double last15candlePrice) {
        this.last15candlePrice = last15candlePrice;
    }

    public double getLastLowestPrice() {
        return lastLowestPrice;
    }

    public void setLastLowestPrice(double lastLowestPrice) {
        this.lastLowestPrice = lastLowestPrice;
    }

    public double getLastLowestCandle() {
        return lastLowestCandle;
    }

    public void setLastLowestCandle(double lastLowestCandle) {
        this.lastLowestCandle = lastLowestCandle;
    }

    public double getLastHighestPrice() {
        return lastHighestPrice;
    }

    public void setLastHighestPrice(double lastHighestPrice) {
        this.lastHighestPrice = lastHighestPrice;
    }

    public double getLastHighestCandle() {
        return lastHighestCandle;
    }

    public void setLastHighestCandle(double lastHighestCandle) {
        this.lastHighestCandle = lastHighestCandle;
    }

    public double getNext1candlePrice() {
        return next1candlePrice;
    }

    public void setNext1candlePrice(double next1candlePrice) {
        this.next1candlePrice = next1candlePrice;
    }

    public double getNext2candlePrice() {
        return next2candlePrice;
    }

    public void setNext2candlePrice(double next2candlePrice) {
        this.next2candlePrice = next2candlePrice;
    }

    public double getNext3candlePrice() {
        return next3candlePrice;
    }

    public void setNext3candlePrice(double next3candlePrice) {
        this.next3candlePrice = next3candlePrice;
    }
}
