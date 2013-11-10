package pl.pisz.airlog.giepp.data;

public class Stats{
	
	private Long money;
	private Integer restarts;
	
	public Stats(long money, int restarts){
		this.money = money;
		this.restarts = restarts;
	}
	
	public int getRestarts(){
		return restarts;
	}
	
	public long getMoney(){
		return money;
	}
}