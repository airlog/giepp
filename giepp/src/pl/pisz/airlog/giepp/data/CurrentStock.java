package pl.pisz.airlog.giepp.data;

import java.util.Comparator;

/** Obiekty tej klasy trzymają informacje potrzebne przy kupnie/sprzedazy akcji.
 * endPrice - jest aktualną ceną
 * startPrice, minPrice, maxPrice - mogą być przydatne przy podejmowaniu decyzji 
 * change - zmiana ceny od otwarcia, wyrażona w procentach, może być przydatna przy podejmowaniu decyzji 
 * time - czas ostatniej transakcji
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

