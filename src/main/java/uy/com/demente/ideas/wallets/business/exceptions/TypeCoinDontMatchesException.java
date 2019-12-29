package uy.com.demente.ideas.wallets.business.exceptions;

/**
 * @author 1987diegog
 */
public class TypeCoinDontMatchesException extends BadRequestException {

	private static final long serialVersionUID = 1L;

	public TypeCoinDontMatchesException() {
		super();
	}

	public TypeCoinDontMatchesException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeCoinDontMatchesException(String message) {
		super(message);
	}

	public TypeCoinDontMatchesException(Throwable cause) {
		super(cause);
	}
}
