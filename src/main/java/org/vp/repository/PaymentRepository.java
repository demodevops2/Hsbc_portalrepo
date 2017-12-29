package org.vp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.vp.model.Payment;

/**
 * Class used to update values from Payment Table
 *
 */
public interface PaymentRepository extends Repository<Payment, Integer> {
	
	/**
	 * Get Details by Payment id
	 * 
	 * @param paymentId
	 * @return List Payment
	 */
	@Query("select b from Payment b where b.payment_ref_id = ?1")
	List<Payment> getDetailsByPaymentRefId(String payment_ref_id);

	/**
	 * Update Payment Table with Status
	 * 
	 * @param payment_ref_id
	 * @param payment_setup_status
	 * @param internal_status
	 * @param internal_status_error_code
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Payment p set p.payment_setup_status = ?2 , p.internal_status = ?3,p.internal_status_error_code = ?4 where p.payment_ref_id=?1")
	void updatePaymentValuesByPaymentRefId(String payment_ref_id, String payment_setup_status, String internal_status,
			String internal_status_error_code);

	/**
	 * Update Payment Table with Status
	 * 
	 * @param payment_ref_id
	 * @param payment_setup_status
	 * @param internal_status
	 * @param internal_status_error_code
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Payment p set p.internal_status = ?3,p.internal_status_error_code = ?2 where p.payment_ref_id=?1")
	void updatePaymentValuesByPaymentRefIdNoStatus(String payment_ref_id, String internal_status,
			String internal_status_error_code);
	
	/**
	 * Update Payment Table with Debtor and Status
	 * 
	 * @param payment_ref_id
	 * @param payment_setup_status
	 * @param internal_status
	 * @param internal_status_error_code
	 * @param debtor_agent_scheme_name
	 * @param debtor_agent_identification
	 * @param debtor_account_schemename
	 * @param debtor_account_identification
	 * @param debtor_account_secondary_identification
	 * @param debtor_account_name
	 */
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("update Payment p set p.payment_setup_status = ?2 , p.internal_status = ?4,p.internal_status_error_code = ?3,p.debtor_account_name = ?5,p.debtor_agent_identification = ?6,p.debtor_account_schemename =?7,p.debtor_account_identification = ?8,p.debtor_account_secondary_identification = ?9,p.debtor_account_name = ?10 where p.payment_ref_id=?1")
	void updatePaymentValuesWithDebtorByPaymentRefId(String payment_ref_id, String payment_setup_status,
			String internal_status, String internal_status_error_code, String debtor_agent_scheme_name,
			String debtor_agent_identification, String debtor_account_schemename, String debtor_account_identification,
			String debtor_account_secondary_identification, String debtor_account_name);

}
