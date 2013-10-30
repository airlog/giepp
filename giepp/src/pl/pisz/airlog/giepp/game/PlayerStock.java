package pl.pisz.airlog.giepp.game;

public class PlayerStock {
	
	private String companyName;
	private int amount;
	private int startPrice;
	
	public PlayerStock(String companyName, int amount, int startPrice){
		this.companyName = companyName;
		this.amount = amount;
		this.startPrice = startPrice;
	}
	
	public String getCompanyName(){
		return companyName;
	}
	public int getAmount(){
		return amount;
	}
	public int getStartPrice(){
		return startPrice;
	}
}
		
		