package pl.pisz.airlog.giepp.game;

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
    
    @Override
    public String toString() {
        return mMessage;
    }

}
