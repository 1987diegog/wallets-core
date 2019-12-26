package uy.com.demente.ideas.business.exceptions;

/**
 * @author 1987diegog
 */
public class TypeCoinDontMatchesException extends Exception {

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
