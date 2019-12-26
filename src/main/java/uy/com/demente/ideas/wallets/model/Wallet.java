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
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author 1987diegog
 */
@Entity
@Table(name = "PAGACOIN_T_WALLETS")
@NamedQuery(name = "Wallet.findByHash", query = "SELECT w FROM Wallet w WHERE w.hash = ?1")
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
	private TypesCoins typeCoin;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;

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

	public TypesCoins getTypeCoin() {
		return typeCoin;
	}

	public void setTypeCoin(TypesCoins typeCoin) {
		this.typeCoin = typeCoin;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((idWallet == null) ? 0 : idWallet.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((typeCoin == null) ? 0 : typeCoin.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Wallet other = (Wallet) obj;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (idWallet == null) {
			if (other.idWallet != null)
				return false;
		} else if (!idWallet.equals(other.idWallet))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (typeCoin != other.typeCoin)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
