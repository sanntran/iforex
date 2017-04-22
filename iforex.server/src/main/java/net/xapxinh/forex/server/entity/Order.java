package net.xapxinh.forex.server.entity;

public class Order {
	
	private Tick firstTick;
	private Tick secondTick;
	
	public Tick getFirstTick() {
		return firstTick;
	}
	public void setFirstTick(Tick firstTick) {
		this.firstTick = firstTick;
	}
	
	public Tick getSecondTick() {
		return secondTick;
	}
	public void setSecondTick(Tick secondTick) {
		this.secondTick = secondTick;
	}
	
	public boolean isBreakoutTrend(Tick tick) {
		return false; // TODO
	}
}
