package es.udc.ws.bikes.client.service.exceptions;

public class ClientOutOfDateException extends Exception {

	public ClientOutOfDateException() {
		super("You have to do the reservation with 1 day margin.");
	}

}
