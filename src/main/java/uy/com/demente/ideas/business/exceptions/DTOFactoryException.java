package uy.com.demente.ideas.business.exceptions;

/**
 * @author 1987diegog
 */
public class DTOFactoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public DTOFactoryException() {
		super();
	}

	public DTOFactoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public DTOFactoryException(String message) {
		super(message);
	}

	public DTOFactoryException(Throwable cause) {
		super(cause);
	}
}
