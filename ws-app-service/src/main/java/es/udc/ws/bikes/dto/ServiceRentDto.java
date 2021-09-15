package es.udc.ws.bikes.dto;

import java.util.Calendar;

public class ServiceRentDto {

	private Long rentalId;
	private Long bikeId;
	private Long units;
	private Calendar initialDate;
	private Calendar expirationDate;
	private Float punctuation;

	public ServiceRentDto() {
	}

	public ServiceRentDto(Long rentalId, Long bikeId, Long units, Calendar initialDate, Calendar expirationDate,
			Float punctuation) {
		super();
		this.rentalId = rentalId;
		this.bikeId = bikeId;
		this.units = units;
		this.initialDate = initialDate;
		this.expirationDate = expirationDate;
		this.punctuation = punctuation;
	}

	public Long getRentalId() {
		return rentalId;
	}

	public void setRentalId(Long rentalId) {
		this.rentalId = rentalId;
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

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Float getPunctuation() {
		return punctuation;
	}

	public void setPunctuation(Float punctuation) {
		this.punctuation = punctuation;
	}

	public Calendar getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Calendar initialDate) {
		this.initialDate = initialDate;
	}

	@Override
	public String toString() {
		return "ServiceRentDto [rentalId=" + rentalId + ", bikeId=" + bikeId + ", units=" + units + ", initialDate="
				+ initialDate + ", expirationDate=" + expirationDate + ", punctuation=" + punctuation + "]";
	}

}
