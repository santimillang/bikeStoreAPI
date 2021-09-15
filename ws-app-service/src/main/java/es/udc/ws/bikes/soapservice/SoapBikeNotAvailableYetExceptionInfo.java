package es.udc.ws.bikes.soapservice;

import java.util.Calendar;

public class SoapBikeNotAvailableYetExceptionInfo {
	
	private Long bikeId;
	private Calendar initialDate;

	public SoapBikeNotAvailableYetExceptionInfo() {
	}

	public SoapBikeNotAvailableYetExceptionInfo(Long bikeId, Calendar initialDate) {
		super();
		this.bikeId = bikeId;
		this.initialDate = initialDate;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Calendar getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Calendar initialDate) {
		this.initialDate = initialDate;
	}
	
	

}
