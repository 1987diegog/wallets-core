package uy.com.demente.ideas.wallets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author 1987diegog
 */
@Entity
@Table(name = "PAGACOIN_T_WALLETS")
@NamedQuery(name = "Wallet.findByHash", query = "SELECT w FROM Wallet w WHERE w.hash = ?1")
@EntityListeners(AuditingEntityListener.class)
public class Wallet implements Serializable {

	private static final long serialVersionUID = 1L;

	// ----------------
	// --- POSTGRES ---
	// ----------------
	// @GeneratedValue(generator = "system-uuid")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid2")

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID_WALLET", updatable = false, nullable = false, unique = true)
	private Long idWallet;

	@Column(name = "HASH", unique = true)
	private String hash;

	@Column(name = "NAME")
	private String name;

	@Column(name = "BALANCE")
	private BigDecimal balance;

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
	@JoinColumn(name = "ID_USER", //
			foreignKey = @ForeignKey(name = "FK_USER"))
	private User user;

	public Long getIdWallet() {
		return idWallet;
	}

	public void setIdWallet(Long idWallet) {
		this.idWallet = idWallet;
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

	public TypeCoin getTypeCoin() {
		return typeCoin;
	}

	public void setTypeCoin(TypeCoin typeCoin) {
		this.typeCoin = typeCoin;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Wallet wallet = (Wallet) o;
		return Objects.equals(idWallet, wallet.idWallet) &&
				Objects.equals(hash, wallet.hash) &&
				Objects.equals(name, wallet.name) &&
				Objects.equals(balance, wallet.balance) &&
				typeCoin == wallet.typeCoin &&
				Objects.equals(createdAt, wallet.createdAt) &&
				Objects.equals(user, wallet.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idWallet, hash, name, balance, typeCoin, createdAt, user);
	}
}
