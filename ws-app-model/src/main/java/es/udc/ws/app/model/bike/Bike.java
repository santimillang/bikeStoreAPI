package es.udc.ws.app.model.bike;

import java.util.Calendar;

public class Bike {

	private Long bikeId;
	private String model;
	private String description;
	private Calendar firstDate;
	private float price;
	private Long units;
	private Calendar creationDate;
	private Long reviewNumber;
	private float average;

	public Bike(String model, String description, Calendar firstDay, float price, long units) {
		this.model = model;
		this.description = description;
		this.firstDate = firstDay;
		if (firstDay != null) {
			this.firstDate.set(Calendar.MILLISECOND, 0);
			this.firstDate.set(Calendar.MINUTE, 0);
			this.firstDate.set(Calendar.SECOND, 0);
			this.firstDate.set(Calendar.HOUR, 0);
		}
		this.price = price;
		this.units = units;
		this.reviewNumber = (long) 0;
	}

	public Bike(Long bikeId, String model, String description, Calendar firstDay, float price, long units) {
		this(model, description, firstDay, price, units);
		this.bikeId = bikeId;
	}

	public Bike(Long bikeId, String model, String description, Calendar firstDay, float price, long units,
			Calendar creationDate) {
		this(bikeId, model, description, firstDay, price, units);
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}
	}

	public Bike(Long bikeId, String model, String description, Calendar firstDay, float price, long units,
			Calendar creationDate, Float averageScore, Long reviewNumber) {
		this(bikeId, model, description, firstDay, price, units, creationDate);
		this.average = averageScore;
		this.reviewNumber = reviewNumber;
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
		if (firstDate != null) {
			this.firstDate.set(Calendar.MILLISECOND, 0);
			this.firstDate.set(Calendar.MINUTE, 0);
			this.firstDate.set(Calendar.SECOND, 0);
			this.firstDate.set(Calendar.HOUR, 0);
		}
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

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);

		}
	}

	public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public Long getReviewNumber() {
		return reviewNumber;
	}

	public void setReviewNumber(Long reviewNumber) {
		this.reviewNumber = reviewNumber;
	}

	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(average);
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((firstDate == null) ? 0 : firstDate.hashCode());
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result + ((reviewNumber == null) ? 0 : reviewNumber.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bike other = (Bike) obj;
		if (Float.floatToIntBits(average) != Float.floatToIntBits(other.average))
			return false;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (firstDate == null) {
			if (other.firstDate != null)
				return false;
		} else if (!firstDate.equals(other.firstDate))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
			return false;
		if (reviewNumber == null) {
			if (other.reviewNumber != null)
				return false;
		} else if (!reviewNumber.equals(other.reviewNumber))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		return true;
	}

}
