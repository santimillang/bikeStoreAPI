package es.udc.ws.bikes.soapservice;

public class SoapMoreThanFifteenDaysExceptionInfo {
	
	private Long bikeId;

	public SoapMoreThanFifteenDaysExceptionInfo() {}

	public SoapMoreThanFifteenDaysExceptionInfo(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}
	
	
}
