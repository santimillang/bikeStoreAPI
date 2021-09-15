package es.udc.ws.bikes.soapservice;

public class SoapAlreadyRatedExceptionInfo {

	private Long rentalId;

	public SoapAlreadyRatedExceptionInfo() {}
	
	public SoapAlreadyRatedExceptionInfo(Long rentalId) {
		this.rentalId = rentalId;
	}

	public Long getRentalId() {
		return rentalId;
	}

	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
	}
	
	
	
}
