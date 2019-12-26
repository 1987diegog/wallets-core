package uy.com.demente.ideas.wallets.business.exceptions;

/**
 * @author 1987diegog
 */
public class WalletNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public WalletNotFoundException() {
		super();
	}

	public WalletNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public WalletNotFoundException(String message) {
		super(message);
	}

	public WalletNotFoundException(Throwable cause) {
		super(cause);
	}

}
