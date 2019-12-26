package uy.com.demente.ideas.wallets.business.exceptions;

/**
 * @author 1987diegog
 */
public class InvalidSessionException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidSessionException() {
		super();
	}

	public InvalidSessionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidSessionException(String message) {
		super(message);
	}

	public InvalidSessionException(Throwable cause) {
		super(cause);
	}

}
