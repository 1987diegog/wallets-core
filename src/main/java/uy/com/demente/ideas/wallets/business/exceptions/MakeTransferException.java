package uy.com.demente.ideas.wallets.business.exceptions;

/**
 * @author 1987diegog
 */
public class MakeTransferException extends Exception {

	private static final long serialVersionUID = 1L;

	public MakeTransferException() {
		super();
	}

	public MakeTransferException(String message, Throwable cause) {
		super(message, cause);
	}

	public MakeTransferException(String message) {
		super(message);
	}

	public MakeTransferException(Throwable cause) {
		super(cause);
	}
}
