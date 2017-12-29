package org.vp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.vp.domain.DebtorAccount;
import org.vp.domain.DebtorAgent;
import org.vp.domain.OBPaymentStatus;
import org.vp.model.Account;
import org.vp.model.Payment;
import org.vp.repository.AccountRepository;
import org.vp.repository.PaymentRepository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * This class consists of 4 end points initiating the single domestic immediate
 * payment initiated as Person2Person and from Merchant payment setup post
 * request payment setup get request payment submission post request payment
 * submission get request
 *
 */

@RestController
public class Payments {

	/**
	 * List of Status in Payment initiation Initial as Pending, API call is
	 * valid - AcceptedTechnicalValidation, Authorization failed or Payment
	 * rejected - Rejected, Payment submitted to ASPSP -
	 * AcceptedSettlementInProcess
	 */

	public static final String STATUS_REJECTED = "Rejected";
	public static final String STATUS_PENDING = "Pending";
	public static final String STATUS_ACCEPTED = "AcceptedTechnicalValidation";
	public static final String STATUS_ACCEPTED_CP = "AcceptedCustomerProfile";
	public static final String STATUS_ACCEPTED_SP = "AcceptedSettlementInProcess";

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	PaymentRepository paymentRepository;

	final static Logger LOG = LoggerFactory.getLogger(Payments.class);

	/**
	 * Method Creates the Payment setup generates payment id
	 * 
	 * @param Request
	 *            body
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @return payment setup response
	 */
	@RequestMapping(value = "/payments-status", method = RequestMethod.POST)
	public String updatePaymentStatus(@RequestBody OBPaymentStatus body, HttpServletResponse response) {

		LOG.info("updatePaymentStatus Starts");
		String responseResult = "";
		String internalStatusCode = "";
		String status = "";
		String paymentID = "";
		String authenticatedStatus = "";
		String authorisedStatus = "";
		Map<String, Object> mapForPostPayment = new HashMap<String, Object>();
		List<Account> debtorAccount = new ArrayList<Account>();
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		try {
			paymentID = body.getData().getPaymentId();
			authenticatedStatus = body.getData().getAuthenticationStatus();
			authorisedStatus = body.getData().getAuthorisationStatus();
			mapForPostPayment.put("internalStatusCode", "");
			String accountIdentification = "";
			String schemeName = "";
			String secondaryidentification = "";
			String name = "";
			String agentidentification = "";
			String agentSchemeName = "";
			DebtorAgent debAgent = new DebtorAgent();
			DebtorAccount debAccount = new DebtorAccount();
			List<Payment> payments = new ArrayList<Payment>();
			if (paymentID != null && paymentID != "") {
				payments = paymentRepository.getDetailsByPaymentRefId(paymentID);

				debAgent = body.getData().getDebtorAgent();
				debAccount = body.getData().getDebtorAccount();
				if (debAgent != null) {
					agentSchemeName = body.getData().getDebtorAgent().getSchemeName();
					agentidentification = body.getData().getDebtorAgent().getIdentification();
				}
				if (debAccount != null) {
					schemeName = body.getData().getDebtorAccount().getSchemeName();
					accountIdentification = body.getData().getDebtorAccount().getIdentification();
					secondaryidentification = body.getData().getDebtorAccount().getSecondaryIdentification();
					name = body.getData().getDebtorAccount().getName();
				}

				if (!payments.isEmpty()) {
					if (STATUS_ACCEPTED.equalsIgnoreCase(payments.get(0).getPayment_setup_status())) {
						if (accountIdentification != null && accountIdentification != "" && schemeName != null
								&& schemeName != "") {
							debtorAccount = accountRepository.getDetailsBySchemeNameAndAccountIdentification(schemeName,
									accountIdentification);

							if (debtorAccount.isEmpty() || debtorAccount.size() == 0) {
								internalStatusCode = "Debtor Account is not Valid ";
								status = STATUS_REJECTED;
							}

						} else if (accountIdentification != null && accountIdentification != ""
								&& (schemeName == null || schemeName == "")) {
							internalStatusCode = "Debtor Account Scheme Name Needed ";
							status = STATUS_REJECTED;
						} else if (schemeName != null && schemeName != ""
								&& (accountIdentification == null || accountIdentification == "")) {
							internalStatusCode = "Debtor Account Identification Needed ";
							status = STATUS_REJECTED;
						}

						if (!STATUS_REJECTED.equalsIgnoreCase(status)) {
							if ("Authenticated".equalsIgnoreCase(authenticatedStatus)
									&& "Authorised".equalsIgnoreCase(authorisedStatus)) {
								response.setStatus(HttpServletResponse.SC_CREATED);
							} else {
								response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
								internalStatusCode = "Not Authorised/Authenticated to Approve the Payment Setup ";
								LOG.info(internalStatusCode);
							}
						}
						if (STATUS_REJECTED.equalsIgnoreCase(status)) {
							LOG.info("Response Code - 400");
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						} else {
							status = STATUS_ACCEPTED_CP;
							LOG.info("Response Code - 201");
							response.setStatus(HttpServletResponse.SC_CREATED);
						}
					} else {
						status = STATUS_REJECTED;
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						internalStatusCode = "Payment was Rejected/already Authorised";
						LOG.info(internalStatusCode);
					}
				} else {
					status = STATUS_REJECTED;
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					internalStatusCode = "Payment ID is not available";
					LOG.info(internalStatusCode);
				}
				if (STATUS_ACCEPTED.equalsIgnoreCase(payments.get(0).getPayment_setup_status())) {
					if (accountIdentification != null && accountIdentification != "" && schemeName != null
							&& schemeName != "") {
						paymentRepository.updatePaymentValuesWithDebtorByPaymentRefId(paymentID, status,
								String.valueOf(response.getStatus()), internalStatusCode, agentSchemeName,
								agentidentification, schemeName, accountIdentification, secondaryidentification, name);
						LOG.info("updatePaymentValuesWithDebtorByPaymentRefId");
					} else {
						paymentRepository.updatePaymentValuesByPaymentRefId(paymentID, status,
								String.valueOf(response.getStatus()), internalStatusCode);
						LOG.info("updatePaymentValuesByPaymentRefId");
					}
				} else {
					paymentRepository.updatePaymentValuesByPaymentRefIdNoStatus(paymentID,
							String.valueOf(response.getStatus()), internalStatusCode);
					LOG.info("updatePaymentValuesByPaymentRefIdNoStatus");
				}
			} else {
				status = STATUS_REJECTED;
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				internalStatusCode = "Payment ID is mandatory";
			}
			body.getData().setDebtorAccount(null);
			body.getData().setDebtorAgent(null);
			body.getData().setAuthenticationStatus(null);
			body.getData().setAuthorisationStatus(null);
			body.getData().setPaymentSetupStatus(status);
			responseResult = gson.toJson(body);
		} catch (Exception exception) {
			LOG.info("Exception --> " + exception);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseResult = "{\"Status\" : \"" + exception.getCause() + "\" - \"" + exception + "\"}";
		}
		LOG.info("updatePaymentStatus Ends");

		return responseResult;

	}

}
