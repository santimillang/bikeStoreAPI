package es.udc.ws.bikes.restservice.json;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.dto.ServiceBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonServiceBikeDtoConversor {

	public static ObjectNode toObjectNode(ServiceBikeDto bike) {

		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();

		if (bike.getBikeId() != null) {
			bikeObject.put("bikeId", bike.getBikeId());
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strdate = sdf.format(bike.getFirstDate().getTime());

		bikeObject.put("model", bike.getModel()).put("description", bike.getDescription()).put("price", bike.getPrice())
				.put("units", bike.getUnits()).put("average", bike.getAverage())
				.put("reviewNumber", bike.getReviewNumber()).put("firstDate", strdate);

		return bikeObject;
	}

	public static ArrayNode toArrayNode(List<ServiceBikeDto> bikes) {

		ArrayNode bikesNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < bikes.size(); i++) {
			ServiceBikeDto bikeDto = bikes.get(i);
			ObjectNode movieObject = toObjectNode(bikeDto);
			bikesNode.add(movieObject);
		}

		return bikesNode;
	}

	public static ServiceBikeDto toServiceBikeDto(InputStream jsonBike) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBike);

			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode bikeObject = (ObjectNode) rootNode;

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

				return new ServiceBikeDto(bikeId, model, description, firstDate, price, units, average, reviewNumber);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
}
