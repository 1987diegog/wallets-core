package uy.com.demente.ideas.wallets.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.com.demente.ideas.wallets.model.User;

import java.util.List;

/**
 * @author 1987diegog
 */
public interface IUserRepository extends JpaRepository<User, Long> {

	/**
	 * Query generated dynamically using Spring and the reserved name findBy
	 * 
	 * @param name
	 * @return
	 */
	List<User> findByName(String name);

	/**
	 * Query generated with @NamedQuery
	 * 
	 * @param email
	 * @return
	 */
	User findByEmail(String email);

}
