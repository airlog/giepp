package pl.pisz.airlog.giepp.data;

/** Obiekty tej klasy trzymają informacje potrzebne do rysowania wykresu archiwalnych notowań.**/
public class ArchivedStock {

	protected String name;
	private String date;
	protected int minPrice;
	protected int maxPrice;
	
	public ArchivedStock(String name, int maxPrice, int minPrice) {
		this.name = name; 
		this.maxPrice = maxPrice; 
		this.minPrice = minPrice; 
	}
	public void setDate(String date) {	this.date = date; }
	public String getName() { return name; }
	public String getDate() { return date; }
	public int getMinPrice() { return minPrice; }
	public int getMaxPrice() { return maxPrice; }

}

