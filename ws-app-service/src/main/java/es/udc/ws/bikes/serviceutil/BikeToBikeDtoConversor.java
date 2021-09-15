package es.udc.ws.bikes.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.bikes.dto.ServiceBikeDto;

public class BikeToBikeDtoConversor {

	public static List<ServiceBikeDto> toBikeDtos(List<Bike> bikes) {
		List<ServiceBikeDto> bikeDtos = new ArrayList<>(bikes.size());

		for (int i = 0; i < bikes.size(); i++) {
			Bike bike = bikes.get(i);
			bikeDtos.add(toBikeDto(bike));
		}
		return bikeDtos;
	}

	public static ServiceBikeDto toBikeDto(Bike bike) {
		return new ServiceBikeDto(bike.getBikeId(), bike.getModel(), bike.getDescription(), bike.getFirstDate(),
				bike.getPrice(), bike.getUnits(), bike.getAverage(), bike.getReviewNumber());

	}

	public static Bike toBike(ServiceBikeDto bike) {
		return new Bike(bike.getBikeId(), bike.getModel(), bike.getDescription(), bike.getFirstDate(), bike.getPrice(),
				bike.getUnits(), null, bike.getAverage(), bike.getReviewNumber());

	}

}
