package pl.pisz.airlog.giepp.data;

/** Obiekty tej klasy trzymają informacje potrzebne do rysowania wykresu archiwalnych notowań.
 * Są to:
 * name - nazwa firmy, której dotyczą dane
 * date - data, z której są dane
 * minPrice, maxPrice - ceny minimalna i maksymalna w danym dniu.
 * @author Joanna**/
public class ArchivedStock {

	private String name;
	private String date;
	private Integer minPrice;
	private Integer maxPrice;
	
	/** Konstruktor, w którym ustawiane są wartości trzymane w tej klasie.
	 * @param name - nazwa firmy
	 * @param maxPrice - maksymalna cena w trakcie całego dnia
	 * @param minPrice - minimalna cena w trakcie całego dnia
	*/
	public ArchivedStock(String name, int maxPrice, int minPrice) {
		this.name = name; 
		this.maxPrice = maxPrice; 
		this.minPrice = minPrice; 
	}
	
	/** Metoda ustawiająca w obiekcie datę.
	 * @param date - data, powinna być w formacie YYYY-MM-DD.*/
	public void setDate(String date) {
	    this.date = date;
	}
	
	/** Metoda zwracająca nazwę firmy.
	 * @return nazwa firmy*/
	public String getName() {
	    return name;
	}
	
	/** Metoda zwracająca date.
	 * @return data*/
	public String getDate() {
	    return date;
	}
	
	/** Metoda zwracająca minimalną cenę w trakcie dnia.
	 * @return minimalna cena*/
	public Integer getMinPrice() {
	    return minPrice;
	}
	
	/** Metoda zwracająca maksymalną cenę w trakcie dnia.
	 * @return maksymalna cena*/
	public Integer getMaxPrice() {
	    return maxPrice;
	}

}

