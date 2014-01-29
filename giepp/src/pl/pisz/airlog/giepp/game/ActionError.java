package pl.pisz.airlog.giepp.game;

/** Przechowywane są tu możliwe wartości błędów podczas transakcji z przypisanym im tekstem.*/
public enum ActionError {

    LACK_OF_MONEY      ("not enough money"),
	LACK_OF_STOCK      ("not enough stocks"),
	COMPANY_NOT_FOUND  ("company not found"),
	TOO_OLD_DATA       ("data is too old, need to refresh"),
	NEGATIVE_AMOUNT    ("buying negative amount is forbidden");
    
    private String mMessage;
    
    private ActionError(String message) {
        mMessage = message;
    }
    
    /** Zwraca tekst odpowiadający błędowi.
     * @return tekst odpowiadający błędowi*/
    @Override
    public String toString() {
        return mMessage;
    }

}
