package es.udc.ws.app.model.rent;

import java.util.Calendar;

public class Rent {

	private Long rentalId;
	private Long bikeId;
	private Long units;
	private Calendar initialDate;
	private Calendar expirationDate;
	private String creditCardNumber;
	private Calendar rentalTime;
	private String userId;
	private Float punctuation;

	public Rent(Long bikeId, String userId, String creditCardNumber, Calendar initialDate, Calendar expirationDate,
			Long units, Calendar rentalTime) {
		this.bikeId = bikeId;
		this.userId = userId;
		this.creditCardNumber = creditCardNumber;
		this.initialDate = initialDate;
		if (initialDate != null) {
			this.initialDate.set(Calendar.MILLISECOND, 0);
			this.initialDate.set(Calendar.MINUTE, 0);
			this.initialDate.set(Calendar.SECOND, 0);
			this.initialDate.set(Calendar.HOUR, 0);
		}
		this.expirationDate = expirationDate;
		if (expirationDate != null) {
			this.expirationDate.set(Calendar.MILLISECOND, 0);
			this.expirationDate.set(Calendar.MINUTE, 0);
			this.expirationDate.set(Calendar.SECOND, 0);
			this.expirationDate.set(Calendar.HOUR, 0);
		}
		this.units = units;
		this.rentalTime = rentalTime;
		if (rentalTime != null) {
			this.rentalTime.set(Calendar.MILLISECOND, 0);

		}
		this.punctuation = (float)-1;

	}

	public Rent(Long rentalId, Long bikeId, String userId, String creditCardNumber, Calendar initialDate,
			Calendar expirationDate, Long units, Calendar rentalTime) {
		this(bikeId, userId, creditCardNumber, initialDate, expirationDate, units, rentalTime);
		this.rentalId = rentalId;
	}

	public Rent(Long rentalId, Long bikeId, String userId, String creditCardNumber, Calendar initialDate,
			Calendar expirationDate, Long units, Calendar rentalTime, Float score) {
		this(rentalId, bikeId, userId, creditCardNumber, initialDate, expirationDate, units, rentalTime);
		this.punctuation = score;
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

	public Calendar getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Calendar initialDate) {
		this.initialDate = initialDate;
		if (initialDate != null) {
			this.initialDate.set(Calendar.MILLISECOND, 0);
			this.initialDate.set(Calendar.MINUTE, 0);
			this.initialDate.set(Calendar.SECOND, 0);
			this.initialDate.set(Calendar.HOUR, 0);
		
		}
	}

	public Calendar getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Calendar expirationDate) {
		this.expirationDate = expirationDate;
		if (expirationDate != null) {
			this.expirationDate.set(Calendar.MILLISECOND, 0);
			this.expirationDate.set(Calendar.MINUTE, 0);
			this.expirationDate.set(Calendar.SECOND, 0);
			this.expirationDate.set(Calendar.HOUR, 0);
			
		}
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public Calendar getRentalTime() {
		return rentalTime;
	}

	public void setRentalTime(Calendar rentalTime) {
		this.rentalTime = rentalTime;
		if (rentalTime != null) {
			this.rentalTime.set(Calendar.MILLISECOND, 0);
		}
		
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Float getPunctuation() {
		return punctuation;
	}

	public void setPunctuation(Float punctuation) {
		this.punctuation = punctuation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((initialDate == null) ? 0 : initialDate.hashCode());
		result = prime * result + ((punctuation == null) ? 0 : punctuation.hashCode());
		result = prime * result + ((rentalId == null) ? 0 : rentalId.hashCode());
		result = prime * result + ((rentalTime == null) ? 0 : rentalTime.hashCode());
		result = prime * result + ((units == null) ? 0 : units.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		Rent other = (Rent) obj;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		if (creditCardNumber == null) {
			if (other.creditCardNumber != null)
				return false;
		} else if (!creditCardNumber.equals(other.creditCardNumber))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (initialDate == null) {
			if (other.initialDate != null)
				return false;
		} else if (!initialDate.equals(other.initialDate))
			return false;
		if (punctuation == null) {
			if (other.punctuation != null)
				return false;
		} else if (!punctuation.equals(other.punctuation))
			return false;
		if (rentalId == null) {
			if (other.rentalId != null)
				return false;
		} else if (!rentalId.equals(other.rentalId))
			return false;
		if (rentalTime == null) {
			if (other.rentalTime != null)
				return false;
		} else if (!rentalTime.equals(other.rentalTime))
			return false;
		if (units == null) {
			if (other.units != null)
				return false;
		} else if (!units.equals(other.units))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}



}
