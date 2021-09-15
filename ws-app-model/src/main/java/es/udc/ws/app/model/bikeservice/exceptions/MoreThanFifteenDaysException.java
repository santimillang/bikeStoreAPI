package es.udc.ws.app.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class MoreThanFifteenDaysException extends Exception {

	private Long bikeId;

	public MoreThanFifteenDaysException(Long bikeId) {
		super("Bike with id ' " + bikeId + " ' cannot be rent for more than 15 days");
		this.bikeId = bikeId;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

}
