package es.udc.ws.bikes.restservice.json;

import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.dto.ServiceRentDto;

public class JsonServiceRentDtoConversor {

	public static ObjectNode toJsonObject(ServiceRentDto rent) {

		ObjectNode rentNode = JsonNodeFactory.instance.objectNode();

		if (rent.getRentalId() != null) {
			rentNode.put("rentalId", rent.getRentalId());
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strdateIni = sdf.format(rent.getInitialDate().getTime());
		String strdate = sdf.format(rent.getExpirationDate().getTime());

		rentNode.put("bikeId", rent.getBikeId()).put("units", rent.getUnits()).put("punctuation", rent.getPunctuation())
				.put("initialDate", strdateIni).put("expirationDate", strdate);

		return rentNode;
	}

	public static ArrayNode toArrayNode(List<ServiceRentDto> rents) {

		ArrayNode rentsNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < rents.size(); i++) {
			ServiceRentDto rentDto = rents.get(i);
			ObjectNode rentObject = toJsonObject(rentDto);
			rentsNode.add(rentObject);
		}

		return rentsNode;
	}

}
