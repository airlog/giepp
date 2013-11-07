package pl.pisz.airlog.giepp.data;

/** Obiekty tej klasy trzymają informacje potrzebne przy kupnie/sprzedazy akcji.
 * endPrice - jest aktualną ceną
 * startPrice, minPrice, maxPrice - mogą być przydatne przy podejmowaniu decyzji 
 * change - zmiana ceny od otwarcia, wyrażona w procentach, może być przydatna przy podejmowaniu decyzji 
 * time - czas ostatniej transakcji
 * **/

public class CurrentStock extends ArchivedStock{

	private String time;
	private Integer startPrice;	
	private Integer endPrice;
	private Float change;
	
	public CurrentStock(String name, String time, int startPrice, int minPrice, int maxPrice, int endPrice, float change) {
		super(name,maxPrice,minPrice);
	
		this.time = time;
		this.startPrice = startPrice; 
		this.endPrice = endPrice; 
		this.change = change;
	}
	
	public Integer getStartPrice() {
	    return startPrice;
	}
	
	public Integer getEndPrice() {
	    return endPrice;
	}
	
	public Float getChange() {
	    return change;
	}
	
	public String getTime() {
	    return time;
	}

}

