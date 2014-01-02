package pl.pisz.airlog.giepp.game;

public class ActionException extends Exception {

	private ActionError reason;

	protected ActionException(ActionError reason) {
		this.reason = reason;
	}
	
	@Override
	public String getMessage() {
	    return reason.toString();
	}
	
	public ActionError getReason() {
		return reason;
	}
	
}
