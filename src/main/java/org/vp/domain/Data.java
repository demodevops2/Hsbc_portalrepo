package org.vp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class Data holds the information like payment id , payment submission id and
 * its status, creation date and Initiation details
 *
 */
public class Data {

	/**
	 * Holds the PaymentId of Data.
	 */
	private String PaymentId;
	/**
	 * Holds the Authentication Status of Data.
	 */
	private String AuthenticationStatus;
	/**
	 * Holds the Authorisation Status of Data.
	 */
	private String AuthorisationStatus;
	/**
	 * Holds the Status of Data.
	 */
	private String Status;

	/**
	 * Holds the Payment Status
	 */
	private String PaymentSetupStatus;
	/**
	 * Holds the Debtor Agent of Data.
	 */
	private DebtorAgent debtorAgent;
	/**
	 * Holds the Debtor Account of Data.
	 */
	private DebtorAccount debtorAccount;
	/**
	 * @return the paymentId
	 */
	@JsonProperty("PaymentId")
	public String getPaymentId() {
		return PaymentId;
	}
	/**
	 * @param paymentId the paymentId to set
	 */
	public void setPaymentId(String paymentId) {
		PaymentId = paymentId;
	}
	/**
	 * @return the authenticationStatus
	 */
	@JsonProperty("AuthenticationStatus")
	public String getAuthenticationStatus() {
		return AuthenticationStatus;
	}
	/**
	 * @param authenticationStatus the authenticationStatus to set
	 */
	public void setAuthenticationStatus(String authenticationStatus) {
		AuthenticationStatus = authenticationStatus;
	}
	/**
	 * @return the authorisationStatus
	 */
	@JsonProperty("AuthorisationStatus")
	public String getAuthorisationStatus() {
		return AuthorisationStatus;
	}
	/**
	 * @param authorisationStatus the authorisationStatus to set
	 */
	public void setAuthorisationStatus(String authorisationStatus) {
		AuthorisationStatus = authorisationStatus;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}
	/**
	 * @return the paymentSetupStatus
	 */
	public String getPaymentSetupStatus() {
		return PaymentSetupStatus;
	}
	/**
	 * @param paymentSetupStatus the paymentSetupStatus to set
	 */
	public void setPaymentSetupStatus(String paymentSetupStatus) {
		PaymentSetupStatus = paymentSetupStatus;
	}
	/**
	 * @return the debtorAgent
	 */
	@JsonProperty("DebtorAgent")
	public DebtorAgent getDebtorAgent() {
		return debtorAgent;
	}
	/**
	 * @param debtorAgent the debtorAgent to set
	 */
	public void setDebtorAgent(DebtorAgent debtorAgent) {
		this.debtorAgent = debtorAgent;
	}
	/**
	 * @return the debtorAccount
	 */
	@JsonProperty("DebtorAccount")
	public DebtorAccount getDebtorAccount() {
		return debtorAccount;
	}
	/**
	 * @param debtorAccount the debtorAccount to set
	 */
	public void setDebtorAccount(DebtorAccount debtorAccount) {
		this.debtorAccount = debtorAccount;
	}

}
