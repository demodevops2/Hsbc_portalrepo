package org.vp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class that holds the Data, Risk , Link and Meta of payment request
 *
 */
public class OBPaymentStatus {

	/**
	 * Holds the Data of payment request
	 *
	 */
	private Data Data; 
	
	/**
	 * @return the data
	 */
	@JsonProperty("Data")
	public Data getData() {
		return Data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(Data data) {
		Data = data;
	}

}