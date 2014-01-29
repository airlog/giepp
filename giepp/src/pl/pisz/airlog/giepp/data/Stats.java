package pl.pisz.airlog.giepp.data;

import pl.pisz.airlog.giepp.game.Game;

/** Obiekt tej klasy przechowuje informację o wynikach gracza.
 *  money - pieniądze, kótre gracz posiada (nie licząc akcji), domyślnie Game.MONEY_ON_START
 *  restarts - ile razy gracz zaczynał od nowa, domyślnie 0
 *  maxMoney - ile pieniędzy gracz miał najwięcej (wliczając pieniądze w akcjach), domyślnie Game.MONEY_ON_START
 *  minMoney - ile pieniędzy gracz miał najmniej (wliczając pieniądze w akcjach), domyślnie Game.MONEY_ON_START
 * @author Joanna
 * */
public class Stats {
	
	private Long money;
	private Integer restarts;
	private Long maxMoney;
	private Long minMoney;
	
	/** Tworzy obiekt tej klasy. Ustawia domyślne wartości pól.*/
	public Stats() {
		this.money = Game.MONEY_ON_START;
		this.restarts = 0;
		this.maxMoney = Game.MONEY_ON_START;
		this.minMoney = Game.MONEY_ON_START;
	}
	
	/** Ustawia ilość posiadanych pieniędzy (nie licząc akcji).
	 * @param money - ilość na jaką mają być ustawione posiadane pieniędze (nie licząc akcji)
	 * @return aktualny stan obiektu Stats*/
	public Stats setMoney(long money) {
	    this.money = money;
	    return this;
	}
	
	/** Ustawia ile razy gracz zaczynał grę od nowa.
	 * @param restarts - liczbna, na którą ma być ustawione ile razy gracz zaczynał grę od nowa.
	 * @return aktualny stan obiektu Stats*/
	public Stats setRestarts(int restarts) {
	    this.restarts = restarts;
	    return this;
	}
	
	/** Ustawia największą liczbę posiadanych pieniędzy (licząc akcje) w trakcie gry.
	 * @param maxMoney - nowa wartość największej liczby pięniędzy (licząc akcje)
	 * @return aktualny stan obiektu Stats*/
	public Stats setMaxMoney(long maxMoney) {
		this.maxMoney = maxMoney;
		return this;
	}

	/** Ustawia najmniejszą liczbę posiadanych pieniędzy (licząc akcje) w trakcie gry.
	 * @param minMoney - nowa wartość najmniejszej liczby pięniędzy (licząc akcje)
	 * @return aktualny stan obiektu Stats*/
	public Stats setMinMoney(long minMoney) {
		this.minMoney = minMoney;
		return this;
	}

	/** Zwraca ile razy gracz zaczynał grę od nowa
	 * @return ile razy gracz zaczynał grę od nowa*/
	public Integer getRestarts() {
		return restarts;
	}
		
	/** Zwraca liczbę pieniędzy gracza (nie licząc akcji)
	 * @return pieniądze jakie posiada gracz (nie licząc akcji)*/
	public Long getMoney() {
		return money;
	}
	
	/** Zwraca największą liczbę pieniędzy jakie miał gracz (licząc akcje)
	 * @return największa liczba pieniędzy jakie miał gracz (licząc akcje)*/
	public Long getMaxMoney() {
		return maxMoney;
	}

	/** Zwraca najmniejszą liczbę pieniędzy jakie miał gracz (licząc akcje)
	 * @return najmniejsza liczba pieniędzy jakie miał gracz (licząc akcje)*/
	public Long getMinMoney() {
		return minMoney;
	}
	
}
