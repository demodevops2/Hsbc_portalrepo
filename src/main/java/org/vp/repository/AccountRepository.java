package org.vp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.vp.model.Account;

/**
 * Class used to retrieve values from Account Table
 *
 */
public interface AccountRepository extends Repository<Account, Integer> {

	/**
	 * Get Account Details
	 * 
	 * @param account_id
	 * @return
	 */
	@Query("select a from Account a where a.scheme_name = ?1 and a.account_identification = ?2")
	List<Account> getDetailsBySchemeNameAndAccountIdentification(String scheme_name, String account_identification);

}
