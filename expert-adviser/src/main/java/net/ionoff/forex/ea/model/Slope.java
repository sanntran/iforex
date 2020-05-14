package net.ionoff.forex.ea.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Slope implements Entity {
	private static final long serialVersionUID = 1L;

	public static final String MT4_CSV_DATE_FORMAT = "yyyy.MM.dd,HH:mm";

	@Id
	private long id;

	@Column(name = "g1_slope")
	private double g1Slope;

	@Column(name = "g2_low_slope")
	private double g2LowSlope;

	@Column(name = "g2_high_slope")
	private double g2HighSlope;

	@Column(name = "g3_low_slope")
	private double g3LowSlope;

	@Column(name = "g3_high_slope")
	private double g3HighSlope;

	@Column(name = "g4_low_slope")
	private double g4LowSlope;

	@Column(name = "g4_high_slope")
	private double g4HighSlope;

	@Column(name = "g5_low_slope")
	private double g5LowSlope;

	@Column(name = "g5_high_slope")
	private double g5HighSlope;

	@Column(name = "next_g1_low")
	private double nextG1Low;

	@Column(name = "next_g1_high")
	private double nextG1High;

	@Column(name = "next_low")
	private double nextLow;

	@Column(name = "next_high")
	private double nextHigh;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public double getG1Slope() {
		return g1Slope;
	}

	public void setG1Slope(double g1Slope) {
		this.g1Slope = g1Slope;
	}

	public double getG2HighSlope() {
		return g2HighSlope;
	}

	public void setG2HighSlope(double g2HighSlope) {
		this.g2HighSlope = g2HighSlope;
	}

	public double getG2LowSlope() {
		return g2LowSlope;
	}

	public void setG2LowSlope(double g2LowSlope) {
		this.g2LowSlope = g2LowSlope;
	}

	public double getG3HighSlope() {
		return g3HighSlope;
	}

	public void setG3HighSlope(double g3HighSlope) {
		this.g3HighSlope = g3HighSlope;
	}

	public double getG3LowSlope() {
		return g3LowSlope;
	}

	public void setG3LowSlope(double g3LowSlope) {
		this.g3LowSlope = g3LowSlope;
	}

	public double getG4HighSlope() {
		return g4HighSlope;
	}

	public void setG4HighSlope(double g4HighSlope) {
		this.g4HighSlope = g4HighSlope;
	}

	public double getG4LowSlope() {
		return g4LowSlope;
	}

	public void setG4LowSlope(double g4LowSlope) {
		this.g4LowSlope = g4LowSlope;
	}

	public double getG5HighSlope() {
		return g5HighSlope;
	}

	public void setG5HighSlope(double g5HighSlope) {
		this.g5HighSlope = g5HighSlope;
	}

	public double getG5LowSlope() {
		return g5LowSlope;
	}

	public void setG5LowSlope(double g5LowSlope) {
		this.g5LowSlope = g5LowSlope;
	}

	public double getNextG1High() {
		return nextG1High;
	}

	public void setNextG1High(double nextG1High) {
		this.nextG1High = nextG1High;
	}

	public void setNextG1High(long nextG1High) {
		this.nextG1High = Double.valueOf(nextG1High);
	}

	public double getNextG1Low() {
		return nextG1Low;
	}

	public void setNextG1Low(double nextG1Low) {
		this.nextG1Low = nextG1Low;
	}

	public double getNextLow() {
		return nextLow;
	}

	public void setNextLow(double nextLow) {
		this.nextLow = nextLow;
	}

	public double getNextHigh() {
		return nextHigh;
	}

	public void setNextHigh(double nextHigh) {
		this.nextHigh = nextHigh;
	}

	public void setNextG1Low(long nextG1Low) {
		this.nextG1Low = Double.valueOf(nextG1Low);
	}
}
