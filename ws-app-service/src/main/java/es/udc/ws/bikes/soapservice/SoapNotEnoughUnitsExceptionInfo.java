package es.udc.ws.bikes.soapservice;

public class SoapNotEnoughUnitsExceptionInfo {

	private Long units;
	private Long bikeId;

	public SoapNotEnoughUnitsExceptionInfo() {
	}

	public SoapNotEnoughUnitsExceptionInfo(Long units, Long bikeId) {
		super();
		this.units = units;
		this.bikeId = bikeId;
	}

	public Long getUnits() {
		return units;
	}

	public void setUnits(Long units) {
		this.units = units;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

}
