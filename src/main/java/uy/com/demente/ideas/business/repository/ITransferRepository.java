package uy.com.demente.ideas.business.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import uy.com.demente.ideas.model.Transfer;
import uy.com.demente.ideas.model.Wallet;

/**
 * @author 1987diegog
 */
public interface ITransferRepository extends JpaRepository<Transfer, Long> {

	/**
	 * Query generated dynamically using Spring and the reserved name findBy
	 * 
	 * @param typeCoin
	 * @return
	 */
	public List<Transfer> findByTypeCoin(String typeCoin);

	/**
	 * Spring Data Query Method
	 * 
	 * @param from
	 * @param to
	 * @param originWallet
	 * @return
	 */
	@Query("SELECT t FROM Transfer t WHERE t.timestamp >=:from AND t.timestamp <=:to AND t.originWallet =:originWallet")
	public List<Transfer> findByFilter(@Param("from") Date from, @Param("to") Date to,
			@Param("originWallet") Wallet originWallet);

	/**
	 * Spring Data Query Method
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	@Query("SELECT t FROM Transfer t WHERE t.timestamp >=:from AND t.timestamp <=:to")
	public List<Transfer> findByFilter(@Param("from") Date from, @Param("to") Date to);
}
