package es.udc.ws.bikes.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.bikes.dto.ServiceRentDto;

public class RentToRentDtoConversor {

	public static List<ServiceRentDto> toRentDto(List<Rent> rents) {
		List<ServiceRentDto> rentDtos = new ArrayList<>(rents.size());
		for (int i = 0; i < rents.size(); i++) {
			Rent rent = rents.get(i);
			rentDtos.add(toRentDto(rent));
		}
		return rentDtos;
	}

	public static ServiceRentDto toRentDto(Rent rent) {
		return new ServiceRentDto(rent.getRentalId(), rent.getBikeId(), rent.getUnits(), rent.getInitialDate(),
				rent.getExpirationDate(), rent.getPunctuation());
	}

}
