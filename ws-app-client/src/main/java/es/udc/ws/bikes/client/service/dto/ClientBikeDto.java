package es.udc.ws.bikes.client.service.dto;

import java.util.Calendar;

public class ClientBikeDto {

	private Long bikeId;
	private String model;
	private String description;
	private Calendar firstDate;
	private float price;
	private Long units;
	private float average;
	private Long reviewNumber;

	public ClientBikeDto() {
	}
	
	public ClientBikeDto(String model, String description, Calendar firstDate, float price, Long units) {
		this.model = model;
		this.description = description;
		this.firstDate = firstDate;
		this.price = price;
		this.units = units;
	}
	
	public ClientBikeDto(Long bikeId,String model, String description, Calendar firstDate, float price, Long units) {
		this.bikeId = bikeId;
		this.model = model;
		this.description = description;
		this.firstDate = firstDate;
		this.price = price;
		this.units = units;
	}

	public ClientBikeDto(Long bikeId, String model, String description, Calendar firstDate, float price, Long units,
			float average, Long reviewNumber) {

		this.bikeId = bikeId;
		this.model = model;
		this.description = description;
		this.firstDate = firstDate;
		this.price = price;
		this.units = units;
		this.average = average;
		this.reviewNumber = reviewNumber;
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getFirstDate() {
		return firstDate;
	}

	public void setFirstDate(Calendar firstDate) {
		this.firstDate = firstDate;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Long getUnits() {
		return units;
	}

	public void setUnits(Long units) {
		this.units = units;
	}

	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	public Long getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(Long reviewNumber) {
		this.reviewNumber = reviewNumber;
	}

	@Override
	public String toString() {
		return "ClientBikeDto [bikeId=" + bikeId + ", model=" + model + ", description=" + description + ", firstDate="
				+ firstDate + ", price=" + price + ", units=" + units + ", average=" + average + ", reviewNumber="
				+ reviewNumber + "]";
	}
}
