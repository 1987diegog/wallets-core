package uy.com.demente.ideas.wallets.exceptions;

/**
 * @author 1987diegog
 */
public class WalletNotFoundException extends NotFoundException {

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
