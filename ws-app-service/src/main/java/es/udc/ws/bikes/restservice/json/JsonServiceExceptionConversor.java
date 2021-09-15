package es.udc.ws.bikes.restservice.json;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.app.model.bikeservice.exceptions.AlreadyRatedException;
import es.udc.ws.app.model.bikeservice.exceptions.BikeNotAvailableYetException;
import es.udc.ws.app.model.bikeservice.exceptions.MoreThanFifteenDaysException;
import es.udc.ws.app.model.bikeservice.exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.bikeservice.exceptions.OutOfDateException;
import es.udc.ws.app.model.bikeservice.exceptions.RentNotYetException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonServiceExceptionConversor {

	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

	public static JsonNode toInputValidationException(InputValidationException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());

		exceptionObject.set("inputValidationException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toInstanceNotFoundException(InstanceNotFoundException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("instanceId", (ex.getInstanceId() != null) ? ex.getInstanceId().toString() : null);
		dataObject.put("instanceType", ex.getInstanceType());


		exceptionObject.set("instanceNotFoundException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toAlreadyRatedException(AlreadyRatedException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("rentalId", (ex.getRentalId() != null) ? ex.getRentalId() : null);
		exceptionObject.set("alreadyRatedException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toMoreThanFifteenDaysException(MoreThanFifteenDaysException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("bikeId", (ex.getBikeId() != null) ? ex.getBikeId().toString() : null);
		exceptionObject.set("moreThanFifteenDaysException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toNotEnoughUnitsException(NotEnoughUnitsException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("bikeId", (ex.getBikeId() != null) ? ex.getBikeId().toString(): null);
		dataObject.put("units", ex.getUnits().toString());

		exceptionObject.set("notEnoughUnitsException", dataObject);

		return exceptionObject;
	}

	public static JsonNode toOutOfDateException(OutOfDateException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("message", ex.getMessage());

		exceptionObject.set("outOfDateException", dataObject);

		return exceptionObject;
	}

	/*
	 * A LO MEJOR CREAMOS ALGUNA EXCEPCIÓN MÁS. SI ES CON FECHA, MIRAR BIEN EL
	 * EJEMPLO DE saleExpiration
	 */
	
	public static JsonNode toRentNotYetException(RentNotYetException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("bikeId", (ex.getRentalId() != null) ? ex.getRentalId().toString(): null);
		if (ex.getExpirationDate() != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
        	dataObject.put("expirationDate", dateFormatter.format(ex.getExpirationDate().getTime()));
        } else {
        	dataObject.set("expirationDate", null);
        }

        exceptionObject.set("rentNotYetException", dataObject);

		return exceptionObject;
	}
	
	public static JsonNode toBikeNotAvailableYetException(BikeNotAvailableYetException ex) {

		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
		ObjectNode dataObject = JsonNodeFactory.instance.objectNode();

		dataObject.put("bikeId", (ex.getBikeId() != null) ? ex.getBikeId().toString(): null);
		if (ex.getFirstDay() != null) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
        	dataObject.put("firstDate", dateFormatter.format(ex.getFirstDay().getTime()));
        } else {
        	dataObject.set("firstDate", null);
        }

        exceptionObject.set("bikeNotAvailableYetException", dataObject);

		return exceptionObject;
	}

}
