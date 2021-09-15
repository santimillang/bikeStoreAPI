package es.udc.ws.bikes.client.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ClientBikeNotAvailableYetException extends Exception {
	
	private Long bikeId;
	private Calendar firstDay;
	
	public ClientBikeNotAvailableYetException (Long bikeId, Calendar firstDay) {
		super("Bike with id ' " + bikeId + " ' will be rentable at " + firstDay);
		this.bikeId = bikeId;
		this.firstDay = firstDay;
		
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Calendar getFirstDay() {
		return firstDay;
	}

	public void setFirstDay(Calendar firstDay) {
		this.firstDay = firstDay;
	}
	
	

}