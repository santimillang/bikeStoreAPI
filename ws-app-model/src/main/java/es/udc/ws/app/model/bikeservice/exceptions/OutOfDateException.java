package es.udc.ws.app.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class OutOfDateException extends Exception {

	public OutOfDateException() {
		super("You have to do the reservation with 1 day margin.");
	}

}
