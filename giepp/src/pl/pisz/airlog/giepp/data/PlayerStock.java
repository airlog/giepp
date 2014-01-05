package pl.pisz.airlog.giepp.data;

import java.util.Comparator;

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
	
	public Double getCurrentValue() {
	    return currentValue;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setStartPrice(int startPrice) {
		this.startPrice = startPrice;
	}
	
	public void setCurrentValue(Double value) {
	    this.currentValue = value;
	}
	
}
