package es.udc.ws.bikes.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientBikeDtoConversor {
	
	public static JsonNode toJsonObject(ClientBikeDto bike) throws IOException {

		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();

		if (bike.getBikeId() != null) {
			bikeObject.put("bikeId", bike.getBikeId());
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strdate = sdf.format(bike.getFirstDate().getTime());

		bikeObject.put("model", bike.getModel()).
			put("description", bike.getDescription()).
			put("firstDate", strdate).
			put("price", bike.getPrice()).
			put("units", bike.getUnits()).
			put("average", bike.getAverage()).
			put("reviewNumber", bike.getReviewNumber());

		return bikeObject;
	}
	
	public static ClientBikeDto toClientBikeDto(InputStream jsonBike) throws ParsingException, ParseException{
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBike);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				return toClientBikeDto(rootNode);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	private static ClientBikeDto toClientBikeDto(JsonNode bikeNode) throws ParsingException, ParseException {
		if (bikeNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode bikeObject = (ObjectNode) bikeNode;

			JsonNode bikeIdNode = bikeObject.get("bikeId");
			Long bikeId = (bikeIdNode != null) ? bikeIdNode.longValue() : null;

			String model = bikeObject.get("model").textValue().trim();
			String description = bikeObject.get("description").textValue().trim();
			float price = bikeObject.get("price").floatValue();
			Long units = bikeObject.get("units").longValue();
			float average = bikeObject.get("average").floatValue();
			Long reviewNumber = bikeObject.get("reviewNumber").longValue();
			String firstDateStr = bikeObject.get("firstDate").textValue().trim();
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date date = sdf.parse(firstDateStr);
			Calendar firstDate = Calendar.getInstance();
			firstDate.setTime(date);
			
			
			return new ClientBikeDto(bikeId, model, description, firstDate, price, units, average, reviewNumber);
					
		}
	}
	
	public static List<ClientBikeDto> toClientBikeDtos(InputStream jsonBikes) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikes);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected)");
			} else {
				ArrayNode bikesArray = (ArrayNode) rootNode;
				List<ClientBikeDto> bikeDtos = new ArrayList<>(bikesArray.size());
				for (JsonNode bikeNode : bikesArray) {
					bikeDtos.add(toClientBikeDto(bikeNode));
				}

				return bikeDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

}
