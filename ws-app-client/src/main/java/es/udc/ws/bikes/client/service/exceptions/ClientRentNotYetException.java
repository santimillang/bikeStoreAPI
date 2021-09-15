package es.udc.ws.bikes.client.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ClientRentNotYetException extends Exception {
	
	private Long rentalId;
	private Calendar expirationDate;
	
	public ClientRentNotYetException(Long rentalId, Calendar expirationDate) {
		super("Rent with Id "+rentalId+" can be reviewed after "+expirationDate);
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
