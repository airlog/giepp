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
	
	public int getRestarts() {
		return restarts;
	}
	
	public long getMoney() {
		return money;
	}
}
