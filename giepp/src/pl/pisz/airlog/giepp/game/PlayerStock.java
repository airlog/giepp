package pl.pisz.airlog.giepp.game;

public class PlayerStock {
	
	private String companyName;
	private Integer amount;
	private Integer startPrice;
	
	public PlayerStock(String companyName, int amount, int startPrice) {
		this.companyName = companyName;
		this.amount = amount;
		this.startPrice = startPrice;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public Integer getAmount() {
		return amount;
	}
	public Integer getStartPrice() {
		return startPrice;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}
}
		
		