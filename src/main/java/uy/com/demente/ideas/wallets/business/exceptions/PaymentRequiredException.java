package uy.com.demente.ideas.wallets.business.exceptions;

/**
 * @author 1987diegog
 */
public class PaymentRequiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public PaymentRequiredException() {
		super();
	}

	public PaymentRequiredException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentRequiredException(String message) {
		super(message);
	}

	public PaymentRequiredException(Throwable cause) {
		super(cause);
	}
}
