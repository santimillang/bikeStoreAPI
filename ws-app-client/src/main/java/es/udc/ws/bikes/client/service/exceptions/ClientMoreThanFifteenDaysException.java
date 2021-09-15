package es.udc.ws.bikes.client.service.exceptions;

@SuppressWarnings("serial")
public class ClientMoreThanFifteenDaysException extends Exception {

	private Long bikeId;

	public ClientMoreThanFifteenDaysException(Long bikeId) {
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
