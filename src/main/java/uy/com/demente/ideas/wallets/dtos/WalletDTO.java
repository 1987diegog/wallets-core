package uy.com.demente.ideas.wallets.dtos;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 1987diegog
 */
public class WalletDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idWallet;
	@NotBlank(message = "Name is mandatory")
	private String name;
	@NotBlank(message = "User id is mandatory")
	private Long idUser;
	@NotBlank(message = "Balance is mandatory")
	private BigDecimal balance;
	@NotBlank(message = "Type coin is mandatory")
	private String typeCoin;

	private String hash;
	private Date createdAt;

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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
