package uy.com.demente.ideas.wallets.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uy.com.demente.ideas.wallets.model.User;

import java.util.List;
import java.util.Optional;

/**
 * @author 1987diegog
 */
public interface IUserRepository extends JpaRepository<User, Long> {

	/**
	 * Query generated dynamically using Spring Data Query Methods
	 * 
	 * @return
	 */
	Optional<User> findByUsernameOrEmail(String username, String email);

	/**
	 * Query generated dynamically using Spring Data Query Methods
	 *
	 * @param username
	 * @return
	 */
	Boolean existsByUsername(String username);

	/**
	 * Query generated with @NamedQuery
	 * { SELECT u FROM User u WHERE u.email = ?1 }
	 *
	 * @param email
	 * @return
	 */
	User findByEmail(String email);

}
