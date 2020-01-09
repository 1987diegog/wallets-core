package uy.com.demente.ideas.wallets.model;

/**
 * @author 1987diegog
 */
public enum TypeCoin {

	PAGACOIN, //
	BITCOIN, //
	ETHEREUM, //
	RIPPLE, //
	LITECOIN, //
	TRON;

	public static TypeCoin get(String typeCoin) {

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
