package pl.pisz.airlog.giepp.data;

import pl.pisz.airlog.giepp.game.Game;

public class Stats {
	
	private Long money;
	private Integer restarts;
	
	public Stats() {
		this.money = Game.MONEY_ON_START;
		this.restarts = 0;
	}
	
	public Stats(long money, int restarts) {
		this.money = money;
		this.restarts = restarts;
	}
	
	public Stats setMoney(long money) {
	    this.money = money;
	    return this;
	}
	
	public Stats setRestarts(int num) {
	    this.restarts = num;
	    return this;
	}
	
	public Integer getRestarts() {
		return restarts;
	}
	
	public Long getMoney() {
		return money;
	}
	
}
