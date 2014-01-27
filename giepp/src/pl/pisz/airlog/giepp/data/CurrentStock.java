package pl.pisz.airlog.giepp.data;

import java.util.Comparator;

/** Obiekty tej klasy trzymają informacje potrzebne przy kupnie/sprzedazy akcji.
 * endPrice - jest aktualną ceną
 * startPrice, minPrice, maxPrice - mogą być przydatne przy podejmowaniu decyzji 
 * change - zmiana ceny od otwarcia, wyrażona w procentach, może być przydatna przy podejmowaniu decyzji 
 * time - czas ostatniej transakcji
 * @author Rafał, Joanna 
 * **/
public class CurrentStock extends ArchivedStock {

	private String time;
	private Integer startPrice;	
	private Integer endPrice;
	private Float change;
	
	/** Umożliwia porównanie dwóch elementów.
	 * Domyślnie porównywanie odbywa się po nazwie firmy.
	 * 
	 * @return komparator
	 */
	public static Comparator<CurrentStock> getComparator() {
        return CurrentStock.getByNameComparator();
    }
	
	/** Umożliwia porównanie dwóch obiektów po nazwie. 
     * @return komparator
     */
	public static Comparator<CurrentStock> getByNameComparator() {
        return new Comparator<CurrentStock>() {
            @Override
            public int compare(CurrentStock o1, CurrentStock o2) {
                return o1.getName().compareTo(o2.getName());
            }  
        };
    }
	
	/** Umożliwia porównanie dwóch obiektów po czasie. 
     * @return komparator
     */
	public static Comparator<CurrentStock> getByTimeComparator() {
	    return new Comparator<CurrentStock>() {
            @Override
            public int compare(CurrentStock o1, CurrentStock o2) {
                return o1.time.compareTo(o2.time);
            }  
	    };
	}
	
	/** Umożliwia porównanie dwóch obiektów po cenie rozpoczęcia. 
     * @return komparator
     */
	public static Comparator<CurrentStock> getByStartPriceComparator() {
        return new Comparator<CurrentStock>() {
            @Override
            public int compare(CurrentStock o1, CurrentStock o2) {
                return o1.startPrice.compareTo(o2.startPrice);
            }  
        };
    }
	
	/** Umożliwia porównanie dwóch obiektów po najmiejszej cenie. 
     * @return komparator
     */
	public static Comparator<CurrentStock> getByMinPriceComparator() {
        return new Comparator<CurrentStock>() {
            @Override
            public int compare(CurrentStock o1, CurrentStock o2) {
                return o1.getMinPrice().compareTo(o2.getMinPrice());
            }  
        };
    }
	
	/** Umożliwia porównanie dwóch obiektów po największej cenie. 
     * @return komparator
     */
	public static Comparator<CurrentStock> getByMaxPriceComparator() {
        return new Comparator<CurrentStock>() {
            @Override
            public int compare(CurrentStock o1, CurrentStock o2) {
                return o1.getMaxPrice().compareTo(o2.getMaxPrice());
            }  
        };
    }
	
	/** Umożliwia porównanie dwóch obiektów po cenie końcowej. 
     * @return komparator
     */
	public static Comparator<CurrentStock> getByEndPriceComparator() {
        return new Comparator<CurrentStock>() {
            @Override
            public int compare(CurrentStock o1, CurrentStock o2) {
                return o1.getEndPrice().compareTo(o2.getEndPrice());
            }  
        };
    }
	
	/** Umożliwia porównanie dwóch obiektów po zmianie. 
     * @return komparator
     */
	public static Comparator<CurrentStock> getByChangeComparator() {
        return new Comparator<CurrentStock>() {
            @Override
            public int compare(CurrentStock o1, CurrentStock o2) {
                return o1.change.compareTo(o2.change);
            }  
        };
    }
	
	/** Konstruktor, w którym ustawiane są wartości trzymane w tej klasie. Wywoływany jest w nim
	 * konstruktor z klasy {link ArchivedStock}.
	 * @param name - nazwa firmy
	 * @param time - czas ostatniej tranzakcji
	 * @param startPrice - cena z pierwszej tranzakcji lub cena odniesienia
	 * @param minPrice - minimalna cena w trakcie całego dnia
	 * @param maxPrice - maksymalna cena w trakcie całego dnia
	 * @param endPrice - cena z ostatniej tranzakcji
	 * @param change - zmiana ceny z ostatniej tranzakcji w stosunku do ceny odniesienia
	*/
	public CurrentStock(String name, String time, int startPrice, int minPrice, int maxPrice, int endPrice, float change) {
		super(name,maxPrice,minPrice);
	
		this.time = time;
		this.startPrice = startPrice; 
		this.endPrice = endPrice; 
		this.change = change;
	}
	
	/** Metoda zwracająca cenę z pierwszej tranzakcji lub odniesienia.
	 * @return cena z pierwszej tranzakcji lub odniesienia*/
	public Integer getStartPrice() {
	    return startPrice;
	}
	
	/** Metoda zwracająca cenę z ostatniej tranzakcji.
	 * @return cena z ostatniej tranzakcji */
	public Integer getEndPrice() {
	    return endPrice;
	}
	
	/** Metoda zwracająca zmianę ceny z ostatniej tranzakcji w stosunku do ceny odniesienia.
	 * @return zmiana ceny z ostatniej tranzakcji w stosunku do ceny odniesienia */
	public Float getChange() {
	    return change;
	}
	
	/** Metoda zwracająca czas ostatniej tranzakcji.
	 * @return czas ostatniej tranzakcji*/
	public String getTime() {
	    return time;
	}

}

