package es.udc.ws.bikes.client.service.rest.json;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.bikes.client.service.dto.ClientRentDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientRentDtoConversor {

	public static ClientRentDto toClientRentDto(InputStream jsonRent) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonRent);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode rentObject = (ObjectNode) rootNode;

				JsonNode rentIdNode = rentObject.get("rentalId");
				Long rentalId = (rentIdNode != null) ? rentIdNode.longValue() : null;

				Long bikeId = rentObject.get("bikeId").longValue();
				Long units = rentObject.get("units").longValue();
				float punctuation = rentObject.get("punctuation").floatValue();
				String firstDateStr = rentObject.get("initialDate").textValue().trim();
				String expirationDateStr = rentObject.get("expirationDate").textValue().trim();
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				Date date = sdf.parse(firstDateStr);
				Date dateEx = sdf.parse(expirationDateStr);
				Calendar firstDate = Calendar.getInstance();
				Calendar expirationDate = Calendar.getInstance();
				firstDate.setTime(date);
				expirationDate.setTime(dateEx);
				Long duration = ChronoUnit.DAYS.between(firstDate.toInstant(), expirationDate.toInstant());
				

				return new ClientRentDto(rentalId, bikeId, units, firstDate, duration.intValue(), punctuation);

			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static List<ClientRentDto> toClientRentDtos(InputStream jsonRents) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonRents);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected)");
			} else {
				ArrayNode rentsArray = (ArrayNode) rootNode;
				List<ClientRentDto> rentDtos = new ArrayList<>(rentsArray.size());
				for (JsonNode rentNode : rentsArray) {
					rentDtos.add(toClientRentDto(rentNode));
				}

				return rentDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	private static ClientRentDto toClientRentDto(JsonNode rentNode) throws ParsingException, ParseException {
		if (rentNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode rentObject = (ObjectNode) rentNode;

			JsonNode rentalIdNode = rentObject.get("rentalId");
			Long rentalId = (rentalIdNode != null) ? rentalIdNode.longValue() : null;

			Long bikeId = rentObject.get("bikeId").longValue();
			Long units = rentObject.get("units").longValue();
			float punctuation= rentObject.get("punctuation").floatValue();
			String initialDateStr = rentObject.get("initialDate").textValue().trim();
			String expirationDateStr = rentObject.get("expirationDate").textValue().trim();
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = sdf.parse(initialDateStr);
			Date dateEx = sdf.parse(expirationDateStr);
			Calendar expirationDate = Calendar.getInstance();
			Calendar initialDate = Calendar.getInstance();
			initialDate.setTime(date);
			expirationDate.setTime(dateEx);
			Long duration = ChronoUnit.DAYS.between(initialDate.toInstant(), expirationDate.toInstant());
	
			return new ClientRentDto(rentalId,bikeId, units, initialDate, duration.intValue(), punctuation);
					
		}
	}

}
