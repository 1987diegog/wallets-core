package uy.com.demente.ideas.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 1987diegog
 */
public class WalletDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idWallet;
	private Long idUser;
	private String hash;
	private String name;
	private BigDecimal balance;
	private String typeCoin;
	private Date created;

	public Long getIdWallet() {
		return idWallet;
	}

	public void setIdWallet(Long idWallet) {
		this.idWallet = idWallet;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getTypeCoin() {
		return typeCoin;
	}

	public void setTypeCoin(String typeCoin) {
		this.typeCoin = typeCoin;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
