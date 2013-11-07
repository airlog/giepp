package pl.pisz.airlog.giepp.data;

/** Obiekty tej klasy trzymają informacje potrzebne do rysowania wykresu archiwalnych notowań.**/
public class ArchivedStock {

	private String name;
	private String date;
	private Integer minPrice;
	private Integer maxPrice;
	
	public ArchivedStock(String name, int maxPrice, int minPrice) {
		this.name = name; 
		this.maxPrice = maxPrice; 
		this.minPrice = minPrice; 
	}
	
	public void setDate(String date) {
	    this.date = date;
	}
	
	public String getName() {
	    return name;
	}
	
	public String getDate() {
	    return date;
	}
	
	public Integer getMinPrice() {
	    return minPrice;
	}
	
	public Integer getMaxPrice() {
	    return maxPrice;
	}

}

