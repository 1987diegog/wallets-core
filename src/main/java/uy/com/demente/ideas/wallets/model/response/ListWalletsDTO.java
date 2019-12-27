package uy.com.demente.ideas.wallets.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author 1987diegog
 */
public class ListWalletsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<WalletDTO> wallets;

	public List<WalletDTO> getWallets() {
		return wallets;
	}

	public void setWallets(List<WalletDTO> wallets) {
		this.wallets = wallets;
	}
}