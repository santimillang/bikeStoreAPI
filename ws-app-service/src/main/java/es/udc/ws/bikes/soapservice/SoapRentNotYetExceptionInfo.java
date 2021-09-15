package es.udc.ws.bikes.soapservice;

import java.util.Calendar;

public class SoapRentNotYetExceptionInfo {

	private Long rentalId;
	private Calendar expirationDate;

	public SoapRentNotYetExceptionInfo() {
	}

	public SoapRentNotYetExceptionInfo(Long rentalId, Calendar expirationDate) {
		super();
		this.rentalId = rentalId;
		this.expirationDate = expirationDate;
	}

	public Long getRentalId() {
		return rentalId;
	}

	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
	}

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}


	
	
}
