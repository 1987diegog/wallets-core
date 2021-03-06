package uy.com.demente.ideas.wallets.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 1987diegog
 */
public class TransferDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idTransfer;
	private String adminName;
	private BigDecimal amount;
	private String typeCoin;
	private Date createdAt;
	private String originWallet;
	private String destinationWallet;

	public String getTypeCoin() {
		return typeCoin;
	}

	public void setTypeCoin(String typeCoin) {
		this.typeCoin = typeCoin;
	}

	public Long getIdTransfer() {
		return idTransfer;
	}

	public void setIdTransfer(Long idTransfer) {
		this.idTransfer = idTransfer;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getOriginWallet() {
		return originWallet;
	}

	public void setOriginWallet(String originWallet) {
		this.originWallet = originWallet;
	}

	public String getDestinationWallet() {
		return destinationWallet;
	}

	public void setDestinationWallet(String destinationWallet) {
		this.destinationWallet = destinationWallet;
	}

}
