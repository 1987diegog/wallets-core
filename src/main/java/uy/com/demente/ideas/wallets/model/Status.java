package uy.com.demente.ideas.wallets.model;

/**
 * @author 1987diegog
 */
public enum Status {
	ENABLED, DISABLE;

    public static Status get(String status) {

        if (ENABLED.name().equals(status)) {
            return ENABLED;
        } else if (DISABLE.name().equals(status)) {
            return DISABLE;
        }

        return null;
    }
}
