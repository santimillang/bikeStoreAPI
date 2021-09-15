package es.udc.ws.app.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class NotEnoughUnitsException extends Exception {

	private Long bikeId;
	private Long units;

	public NotEnoughUnitsException(Long bikeId, Long units) {
		super("Bike with id ' " + bikeId + " ' has " + units + " at max.");
		this.bikeId = bikeId;
		this.units = units;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Long getUnits() {
		return units;
	}

	public void setUnits(Long units) {
		this.units = units;
	}

}
