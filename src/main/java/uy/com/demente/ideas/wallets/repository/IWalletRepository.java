package uy.com.demente.ideas.wallets.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uy.com.demente.ideas.wallets.model.User;
import uy.com.demente.ideas.wallets.model.Wallet;

/**
 * @author 1987diegog
 */
public interface IWalletRepository extends JpaRepository<Wallet, Long> {

	/**
	 * Query generated dynamically using Spring and the reserved name findBy
	 * 
	 * @param user
	 * @return
	 */
	List<Wallet> findByUser(User user);

	/**
	 * Query generated with @NamedQuery
	 * 
	 * @param hash
	 * @return
	 */
	Optional<Wallet> findByHash(String hash);

}
