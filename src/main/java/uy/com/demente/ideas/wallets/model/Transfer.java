package uy.com.demente.ideas.wallets.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * @author 1987diegog
 */
@Entity
@Table(name = "PAGACOIN_T_TRANSFERS")
@EntityListeners(AuditingEntityListener.class)
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
	private TypeCoin typeCoin;

	@Column(name="CREATED", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(name="UPDATED", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

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

	public TypeCoin getTypeCoin() {
		return typeCoin;
	}

	public void setTypeCoin(TypeCoin typeCoin) {
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Transfer transfer = (Transfer) o;
		return Objects.equals(idTransfer, transfer.idTransfer) &&
				Objects.equals(adminName, transfer.adminName) &&
				Objects.equals(amount, transfer.amount) &&
				typeCoin == transfer.typeCoin &&
				Objects.equals(createdAt, transfer.createdAt) &&
				Objects.equals(updatedAt, transfer.updatedAt) &&
				Objects.equals(originWallet, transfer.originWallet) &&
				Objects.equals(destinationWallet, transfer.destinationWallet);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idTransfer, adminName, amount, typeCoin, createdAt, updatedAt, originWallet, destinationWallet);
	}
}
