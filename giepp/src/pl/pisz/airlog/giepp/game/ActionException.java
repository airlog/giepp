package pl.pisz.airlog.giepp.game;

/**Wyjątek wykorzystywany w klasie {@link Game} do informowania o błędach w trakcie transakcji. */
public class ActionException extends Exception {

	private ActionError reason;

	protected ActionException(ActionError reason) {
		this.reason = reason;
	}
	
	/** Zwraca informację o powodzie błędu.
	 * @return powód błędu*/
	@Override
	public String getMessage() {
	    return reason.toString();
	}
	
	/** Zwraca informację o powodzie błędu.
	 * @return powód błędu*/
	public ActionError getReason() {
		return reason;
	}
	
}
