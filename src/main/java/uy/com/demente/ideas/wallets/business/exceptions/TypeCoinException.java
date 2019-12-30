package uy.com.demente.ideas.wallets.business.exceptions;

/**
 * @author 1987diegog
 */
public class TypeCoinException extends BadRequestException {

	private static final long serialVersionUID = 1L;

	public TypeCoinException() {
		super();
	}

	public TypeCoinException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeCoinException(String message) {
		super(message);
	}

	public TypeCoinException(Throwable cause) {
		super(cause);
	}
}
