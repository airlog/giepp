package pl.pisz.airlog.giepp.game;

public class ActionException extends Exception {

	private ActionError reason;

	protected ActionException(ActionError reason) {
		this.reason = reason;
	}
	
	public ActionError getReason() {
		return reason;
	}
}