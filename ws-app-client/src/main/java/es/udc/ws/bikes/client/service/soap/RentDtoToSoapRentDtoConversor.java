package es.udc.ws.bikes.client.service.soap;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.client.service.soap.wsdl.ServiceRentDto;
import es.udc.ws.bikes.client.service.dto.ClientRentDto;

public class RentDtoToSoapRentDtoConversor {

	public static ClientRentDto toClientRentDto(ServiceRentDto rent) {

		Calendar firstDate = rent.getInitialDate().toGregorianCalendar();
		Calendar secondDate = rent.getExpirationDate().toGregorianCalendar();
		Long duration = ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant());

		return new ClientRentDto(rent.getRentalId(), rent.getBikeId(), rent.getUnits(), firstDate, duration.intValue(),
				rent.getPunctuation());
	}

	public static List<ClientRentDto> toClientRentDtos(List<ServiceRentDto> rents) {
		List<ClientRentDto> rentDtos = new ArrayList<>(rents.size());
		for (int i = 0; i < rents.size(); i++) {
			ServiceRentDto rent = rents.get(i);
			rentDtos.add(toClientRentDto(rent));
		}
		return rentDtos;
	}

}
