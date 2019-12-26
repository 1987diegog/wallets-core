package uy.com.demente.ideas.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uy.com.demente.ideas.model.User;

/**
 * @author 1987diegog
 */
public interface IUserRepository extends JpaRepository<User, Long> {

	/**
	 * Query generated dynamically using Spring and the reserved name findBy
	 * 
	 * @param title
	 * @return
	 */
	public List<User> findByName(String name);

	/**
	 * Query generated with @NamedQuery
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmail(String email);

}
