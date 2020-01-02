package uy.com.demente.ideas.wallets.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "PAGACOIN_T_USERS")
@NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = ?1")
public class User extends Person {

	private static final long serialVersionUID = 1L;

	// ----------------
	// --- POSTGRES ---
	// ----------------
	// @GeneratedValue(generator = "system-uuid")
	// @GenericGenerator(name = "system-uuid", strategy = "uuid2")

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ID_USER", updatable = false, nullable = false, unique = true)
	private Long idUser;

	@Column(name = "USERNAME", length = 50, unique = true)
	private String username;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "STATUS", length = 30)
	private Status status;

	@OneToMany(//
			mappedBy = "user", //
			fetch = FetchType.LAZY, //
			cascade = CascadeType.ALL, //
			orphanRemoval = true //
	)
	private Set<Wallet> wallets;

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Set<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(Set<Wallet> wallets) {
		this.wallets = wallets;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(idUser, user.idUser) &&
				Objects.equals(username, user.username) &&
				status == user.status &&
				Objects.equals(wallets, user.wallets);
	}

	@Override
	public int hashCode() {
		return Objects.hash(idUser, username, status, wallets);
	}
}
