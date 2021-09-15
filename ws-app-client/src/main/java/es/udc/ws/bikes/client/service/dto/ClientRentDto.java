package es.udc.ws.bikes.client.service.dto;

import java.util.Calendar;

public class ClientRentDto {

		private Long rentalId;
		private Long bikeId;
		private Long units;
		private Calendar initialDate;
		private int duration;
		private Float punctuation;
		
		public ClientRentDto() {
		}
		
		public ClientRentDto(Long rentalId, Long bikeId, Long units, Calendar initialDate, int duration,
				Float punctuation) {
			super();
			this.rentalId = rentalId;
			this.bikeId = bikeId;
			this.units = units;
			this.initialDate = initialDate;
			this.duration = duration;
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

		public Calendar getInitialDate() {
			return initialDate;
		}

		public void setInitialDate(Calendar initialDate) {
			this.initialDate = initialDate;
		}

		public int getDuration() {
			return duration;
		}

		public void setDuration(int duration) {
			this.duration = duration;
		}

		public Float getPunctuation() {
			return punctuation;
		}

		public void setPunctuation(Float punctuation) {
			this.punctuation = punctuation;
		}

		@Override
		public String toString() {
			return "ClientRentDto [rentalId=" + rentalId + ", bikeId=" + bikeId + ", units=" + units + ", initialDate="
					+ initialDate + ", duration=" + duration + ", punctuation=" + punctuation + "]";
		}
		
		
		
		
		
		

		

}
