package org.pathwayeditor.notationsubsystem.toolkit.ndom;

/**
 * @author nhanlon
 *  Throw if NDOM building fails. If building the NDOM succeeds but errors exist, these 
 *  should be set into the ReportBuilder as failed rules and no NdomException should be thrown.
 */
public class NdomException extends Exception {

	private static final long serialVersionUID = 7599384053290533617L;

	public NdomException() {
		super();
	}

	public NdomException(String message, Throwable cause) {
		super(message, cause);
	}

	public NdomException(String message) {
		super(message);
	}

	public NdomException(Throwable cause) {
		super(cause);
	}

}