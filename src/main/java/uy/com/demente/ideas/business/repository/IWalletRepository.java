package uy.com.demente.ideas.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uy.com.demente.ideas.model.User;
import uy.com.demente.ideas.model.Wallet;

/**
 * @author 1987diegog
 */
public interface IWalletRepository extends JpaRepository<Wallet, Long> {

	/**
	 * Query generated dynamically using Spring and the reserved name findBy
	 * 
	 * @param name
	 * @return
	 */
	public List<Wallet> findByUser(User user);

	/**
	 * Query generated with @NamedQuery
	 * 
	 * @param hash
	 * @return
	 */
	public Wallet findByHash(String hash);

}
