package uy.com.demente.ideas.business.exceptions;

/**
 * @author 1987diegog
 */
public class TransferNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public TransferNotFoundException() {
		super();
	}

	public TransferNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransferNotFoundException(String message) {
		super(message);
	}

	public TransferNotFoundException(Throwable cause) {
		super(cause);
	}

}
