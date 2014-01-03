package pl.pisz.airlog.giepp.data;

import pl.pisz.airlog.giepp.game.Game;

public class Stats {
	
	private Long money;
	private Integer restarts;
	private Long maxMoney;
	private Long minMoney;
	
	public Stats() {
		this.money = Game.MONEY_ON_START;
		this.restarts = 0;
		this.maxMoney = Game.MONEY_ON_START;
		this.minMoney = Game.MONEY_ON_START;
	}
		
	public Stats setMoney(long money) {
	    this.money = money;
	    return this;
	}
	
	public Stats setRestarts(int num) {
	    this.restarts = num;
	    return this;
	}
	
	public Stats setMaxMoney(long maxMoney) {
		this.maxMoney = maxMoney;
		return this;
	}

	public Stats setMinMoney(long minMoney) {
		this.minMoney = minMoney;
		return this;
	}

	public Integer getRestarts() {
		return restarts;
	}
		
	public Long getMoney() {
		return money;
	}
	
	public Long getMaxMoney() {
		return maxMoney;
	}

	public Long getMinMoney() {
		return minMoney;
	}
	
}
