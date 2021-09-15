package es.udc.ws.app.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class AlreadyRatedException extends Exception {

	private Long rentalId;

	public AlreadyRatedException(Long rentalId) {
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