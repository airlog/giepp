package pl.pisz.airlog.giepp.data;

import java.util.Comparator;

/** W klasie tej są trzymane informacje o posiadanych
 * przez gracza akcjach.
 * companyName - nazwa firmy, której gracz posiada akcje
 * amount - liczba posiadanych przez gracza akcji tej firmy 
 * startPrice - pieniądze wydane przez gracza na zakup akcji tej firmy (łącznie z prowizją)
 * currentValue - aktualna wartość akcji tej firmy
 * @author Rafał, Joanna*/
public class PlayerStock {

    /** Umożliwia porównanie dwóch elementów.
     * Domyślnie porównywanie odbywa się po nazwie firmy.
     * 
     * @return komparator
     */
    public static Comparator<PlayerStock> getComparator() {
        return PlayerStock.getByNameComparator();
    }
    
    /** Umożliwia porównanie dwóch obiektów po nazwie. 
     * @return komparator
     */
    public static Comparator<PlayerStock> getByNameComparator() {
        return new Comparator<PlayerStock>() {
            @Override
            public int compare(PlayerStock o1, PlayerStock o2) {
                return o1.getCompanyName().compareTo(o2.getCompanyName());
            }  
        };
    }
    
    /** Umożliwia porównanie dwóch obiektów po ilości posiadanych akcji. 
     * @return komparator
     */
    public static Comparator<PlayerStock> getByAmountComparator() {
        return new Comparator<PlayerStock>() {
            @Override
            public int compare(PlayerStock o1, PlayerStock o2) {
                return o1.getAmount().compareTo(o2.getAmount());
            }  
        };
    }
    
    /** Umożliwia porównanie dwóch obiektów po cenie zakupu. 
     * @return komparator
     */
    public static Comparator<PlayerStock> getByPriceComparator() {
        return new Comparator<PlayerStock>() {
            @Override
            public int compare(PlayerStock o1, PlayerStock o2) {
                return o1.getStartPrice().compareTo(o2.getStartPrice());
            }  
        };
    }
    
    /** Umożliwia porównanie dwóch obiektów po aktualnej wartości. 
     * @return komparator
     */
    public static Comparator<PlayerStock> getByValueComparator() {
        return new Comparator<PlayerStock>() {
            @Override
            public int compare(PlayerStock o1, PlayerStock o2) {
                return o1.getCurrentValue().compareTo(o2.getCurrentValue());
            }  
        };
    }
    
	private String companyName;
	private Integer amount;
	private Integer startPrice;
	
	private Double currentValue = 0.0; // określa aktualną wartość, przydatne do sortowania

	/**Tworzy obiekt tej klasy. Ustawiane są w nim wartości pól tej klasy.
	 * @param companyName - nazwa firmy, której gracz posiada akcje
	 * @param amount - liczba posiadanych przez gracza akcji tej firmy 
	 * @param startPrice - pieniądze wydane przez gracza na zakup akcji (łącznie z prowizją)
	 * */
	public PlayerStock(String companyName, int amount, int startPrice) {
		this.companyName = companyName;
		this.amount = amount;
		this.startPrice = startPrice;
	}
	
	/** Zwraca nazwę firmy, której akcje posiada gracz.
	 * @return nazwa firmy, której akcje posiada gracz.*/
	public String getCompanyName() {
		return companyName;
	}
	
	/** Zwraca liczbę akcji danej firmy, które posiada gracz.
	 * @return liczba akcji danej firmy, które posiada gracz*/
	public Integer getAmount() {
		return amount;
	}
	
	/** Zwraca pieniądze wydane przez gracza na zakup akcji danej firmy (łącznie z prowizją)
	 * @return pieniądze wydane przez gracza na zakup akcji danej firmy (łącznie z prowizją)*/
	public Integer getStartPrice() {
		return startPrice;
	}
	
	/** Zwraca aktualną wartość akcji danej firmy jakie posiada gracz.
	 * @return aktualna wartość akcji danej firmy jakie posiada gracz*/
	public Double getCurrentValue() {
	    return currentValue;
	}
	
	/** Ustawia liczbę posiadanych akcji.
	 * @param amount - nowa liczba posiadanych akcji
	 */ 
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/** Ustawia sumę pieniędzy wydanych przez gracza na akcje danje firmy.
	 * @param amount - nowa suma pieniędzy wydanych przez gracza na akcje danje firmy
	 */ 
	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}
	
	/** Ustawia aktualną wartość akcji danej firmy jakie posiada gracz.
	 * @param amount - nowa aktualna wartość akcji danej firmy jakie posiada gracz
	 */ 
	public void setCurrentValue(Double value) {
	    this.currentValue = value;
	}
	
}
