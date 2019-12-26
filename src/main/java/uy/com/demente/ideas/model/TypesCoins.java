package uy.com.demente.ideas.model;

/**
 * @author 1987diegog
 */
public enum TypesCoins {

	PAGACOIN, //
	BITCOIN, //
	ETHEREUM, //
	RIPPLE, //
	LITECOIN, //
	TRON;

	public static TypesCoins get(String typeCoin) {

		if (PAGACOIN.name().equals(typeCoin)) {
			return PAGACOIN;
		} else if (BITCOIN.name().equals(typeCoin)) {
			return BITCOIN;
		} else if (ETHEREUM.name().equals(typeCoin)) {
			return ETHEREUM;
		} else if (RIPPLE.name().equals(typeCoin)) {
			return RIPPLE;
		} else if (LITECOIN.name().equals(typeCoin)) {
			return LITECOIN;
		} else if (TRON.name().equals(typeCoin)) {
			return TRON;
		}

		return null;
	}
}
