package uy.com.demente.ideas.wallets.exceptions;

/**
 * @author 1987diegog
 */
public class WalletMatchesException extends BadRequestException {

	private static final long serialVersionUID = 1L;

	public WalletMatchesException() {
		super();
	}

	public WalletMatchesException(String message, Throwable cause) {
		super(message, cause);
	}

	public WalletMatchesException(String message) {
		super(message);
	}

	public WalletMatchesException(Throwable cause) {
		super(cause);
	}
}
