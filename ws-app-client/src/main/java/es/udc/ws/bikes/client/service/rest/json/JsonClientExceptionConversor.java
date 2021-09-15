package es.udc.ws.bikes.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import es.udc.ws.bikes.client.service.exceptions.ClientAlreadyRatedException;
import es.udc.ws.bikes.client.service.exceptions.ClientBikeNotAvailableYetException;
import es.udc.ws.bikes.client.service.exceptions.ClientMoreThanFifteenDaysException;
import es.udc.ws.bikes.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.bikes.client.service.exceptions.ClientOutOfDateException;
import es.udc.ws.bikes.client.service.exceptions.ClientRentNotYetException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientExceptionConversor {
	
	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

	public static InputValidationException fromInputValidationException(JsonNode rootNode) throws ParsingException {
		try {

			// ObjectMapper objectMapper = ObjectMapperFactory.instance();
			// JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				String message = rootNode.get("inputValidationException").get("message").textValue();
				return new InputValidationException(message);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static InstanceNotFoundException fromInstanceNotFoundException(JsonNode rootNode) throws ParsingException {
		try {
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				String instanceId = rootNode.get("instanceId").textValue();
				String instanceType = rootNode.get("instanceType").textValue();
				return new InstanceNotFoundException(instanceId, instanceType);
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	

	public static ClientOutOfDateException fromOutOfDateException(JsonNode rootNode) throws ParsingException {
		try {

			// ObjectMapper objectMapper = ObjectMapperFactory.instance();
			// JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				return new ClientOutOfDateException();
			}
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientNotEnoughUnitsException fromNotEnoughUnitsException(JsonNode rootNode) throws ParsingException {
		try {

			// ObjectMapper objectMapper = ObjectMapperFactory.instance();
			// JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("notEnoughUnitsException");
				String bikeId = data.get("bikeId").textValue();
				String units = data.get("units").textValue();
				return new ClientNotEnoughUnitsException(Long.parseLong(bikeId), Long.parseLong(units));
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientMoreThanFifteenDaysException fromMoreThanFifteenDaysException(JsonNode rootNode)
			throws ParsingException {
		try {
			// ObjectMapper objectMapper = ObjectMapperFactory.instance();
			// JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("moreThanFifteenDaysException");
				String bikeId = data.get("bikeId").textValue();
				return new ClientMoreThanFifteenDaysException(Long.parseLong(bikeId));
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientAlreadyRatedException fromAlreadyRatedException(InputStream ex) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("alreadyRatedException");
				Long rentalId = data.get("rentalId").longValue();
				return new ClientAlreadyRatedException(rentalId);
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientBikeNotAvailableYetException fromBikeNotAvailableYetException(JsonNode rootNode)
			throws ParsingException {
		try {

			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("bikeNotAvailableYetException");
				String bikeId = data.get("bikeId").textValue();
				String firstDate = data.get("firstDate").textValue();
				Calendar calendar = null;
				if (firstDate != null) {
					SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
					calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(firstDate));
				}
				return new ClientBikeNotAvailableYetException(Long.parseLong(bikeId), calendar);
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static ClientRentNotYetException fromRentNotYetException(JsonNode rootNode) throws ParsingException {
		try {

			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				JsonNode data = rootNode.get("rentNotYetException");
				String bikeId = data.get("bikeId").textValue();
				String expirationDate = data.get("expirationDate").textValue();
				Calendar calendar = null;
				if (expirationDate != null) {
					SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
					calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(expirationDate));
				}
				return new ClientRentNotYetException(Long.parseLong(bikeId), calendar);
			}

		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static Exception fromBadRequestCode(InputStream ex) throws InputValidationException,
			ClientOutOfDateException, ClientMoreThanFifteenDaysException, ClientNotEnoughUnitsException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else if (rootNode.get("inputValidationException") != null) {
				return fromInputValidationException(rootNode);
			} else if (rootNode.get("moreThanFifteenDaysException") != null) {
				return fromMoreThanFifteenDaysException(rootNode);
			} else if (rootNode.get("outOfDateException") != null) {
				return fromOutOfDateException(rootNode);
			} else if (rootNode.get("notEnoughUnitsException") != null) {
				return fromNotEnoughUnitsException(rootNode);
			}
			return null;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	public static Exception fromNotFoundCode(InputStream ex) throws InstanceNotFoundException, ClientRentNotYetException,
			ClientBikeNotAvailableYetException{
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(ex);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else if (rootNode.get("instanceNotFoundException") != null) {
				return fromInstanceNotFoundException(rootNode);
			} else if (rootNode.get("rentNotYetException") != null) {
				return fromRentNotYetException(rootNode);
			} else if (rootNode.get("bikeNotAvailableYetException") != null) {
				return fromBikeNotAvailableYetException(rootNode);
			}
			return null;

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
