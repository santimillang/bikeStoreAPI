package es.udc.ws.bikes.client.service.exceptions;

public class ClientAlreadyRatedException extends Exception {

	private Long rentalId;

	public ClientAlreadyRatedException(Long rentalId) {
		super("Rent with id ' " + rentalId + " ' has been already rated");
		this.rentalId = rentalId;
	}

	public Long getRentalId() {
		return rentalId;
	}

	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
	}



}
