package uy.com.demente.ideas.wallets.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author 1987diegog
 */
@Entity
@Table(name = "PAGACOIN_T_TRANSFERS")
public class Transfer implements Serializable {

	private static final long serialVersionUID = 1L;

	// ----------------
	// --- POSTGRES ---
	// ----------------
	// @GeneratedValue(generator = "system-uuid")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid2")

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID_TRANSFER", updatable = false, nullable = false, unique = true)
	private Long idTransfer;

	@Column(name = "ADMIN_NAME")
	private String adminName;

	@Column(name = "AMOUNT")
	private BigDecimal amount;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "TYPE_COIN")
	private TypesCoins typeCoin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP")
	private Date timestamp;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORIGIN_WALLET", //
			foreignKey = @ForeignKey(name = "FK_ORIGIN_WALLET"))
	private Wallet originWallet;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DESTINATION_WALLET", //
			foreignKey = @ForeignKey(name = "FK_DESTINATION_WALLET"))
	private Wallet destinationWallet;

	public TypesCoins getTypeCoin() {
		return typeCoin;
	}

	public void setTypeCoin(TypesCoins typeCoin) {
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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Wallet getOriginWallet() {
		return originWallet;
	}

	public void setOriginWallet(Wallet originWallet) {
		this.originWallet = originWallet;
	}

	public Wallet getDestinationWallet() {
		return destinationWallet;
	}

	public void setDestinationWallet(Wallet destinationWallet) {
		this.destinationWallet = destinationWallet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((adminName == null) ? 0 : adminName.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((destinationWallet == null) ? 0 : destinationWallet.hashCode());
		result = prime * result + ((idTransfer == null) ? 0 : idTransfer.hashCode());
		result = prime * result + ((originWallet == null) ? 0 : originWallet.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((typeCoin == null) ? 0 : typeCoin.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transfer other = (Transfer) obj;
		if (adminName == null) {
			if (other.adminName != null)
				return false;
		} else if (!adminName.equals(other.adminName))
			return false;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (destinationWallet == null) {
			if (other.destinationWallet != null)
				return false;
		} else if (!destinationWallet.equals(other.destinationWallet))
			return false;
		if (idTransfer == null) {
			if (other.idTransfer != null)
				return false;
		} else if (!idTransfer.equals(other.idTransfer))
			return false;
		if (originWallet == null) {
			if (other.originWallet != null)
				return false;
		} else if (!originWallet.equals(other.originWallet))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (typeCoin != other.typeCoin)
			return false;
		return true;
	}
}
