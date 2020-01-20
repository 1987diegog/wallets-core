package uy.com.demente.ideas.wallets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uy.com.demente.ideas.wallets.model.Transfer;
import uy.com.demente.ideas.wallets.model.Wallet;

import java.util.Date;
import java.util.List;

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
	List<Transfer> findByTypeCoin(String typeCoin);

	List<Transfer> findByCreatedAtBetween(Date from, Date to);

	/**
	 * Spring Data Query Method
	 * 
	 * @param from
	 * @param to
	 * @param originWallet
	 * @return
	 */
	@Query("SELECT t FROM Transfer t WHERE t.createdAt >=:from AND t.createdAt <=:to AND t.originWallet =:originWallet")
	List<Transfer> findByFilter(@Param("from") Date from, @Param("to") Date to,
			@Param("originWallet") Wallet originWallet);

	/**
	 * Spring Data Query Method
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
//	@Query("SELECT t FROM Transfer t WHERE t.timestamp >=:from AND t.timestamp <=:to")
//	List<Transfer> findByFilter(@Param("from") Date from, @Param("to") Date to);
}
