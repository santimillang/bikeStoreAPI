package es.udc.ws.bikes.restservice.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.dto.ServiceBikeDto;
import es.udc.ws.bikes.restservice.json.JsonServiceBikeDtoConversor;
import es.udc.ws.bikes.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.bikes.serviceutil.BikeToBikeDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class BikesServlet extends HttpServlet {

	@Override /* DANDO DE ALTA UN NUEVO MODELO DE BICI (APARTADO 1) */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path != null && path.length() > 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid path " + path)),
					null);
			return;

		}

		ServiceBikeDto bikeDto;
		try {
			bikeDto = JsonServiceBikeDtoConversor.toServiceBikeDto(req.getInputStream());
		} catch (ParsingException e) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
					.toInputValidationException(new InputValidationException(e.getMessage())), null);
			return;
		}

		Bike bike = BikeToBikeDtoConversor.toBike(bikeDto);

		try {
			bike = BikeServiceFactory.getService().addBike(bike);
		} catch (InputValidationException e) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(e), null);
			return;
		}

		bikeDto = BikeToBikeDtoConversor.toBikeDto(bike);

		String bikeURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + bike.getBikeId();
		Map<String, String> headers = new HashMap<>(1);
		headers.put("Location", bikeURL);

		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
				JsonServiceBikeDtoConversor.toObjectNode(bikeDto), headers);
	}

	@Override /* ACTUALIZANDO UNA BICI (APARTADO 2) */
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
					.toInputValidationException(new InputValidationException("Invalid Request: " + "invalid bike id")),
					null);
			return;
		}
		String bikeIdAsString = path.substring(1);
		Long bikeId;
		try {
			bikeId = Long.valueOf(bikeIdAsString);
		} catch (NumberFormatException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
							"Invalid Request: " + "invalid bike id '" + bikeIdAsString + "'")),
					null);
			return;
		}

		ServiceBikeDto bikeDto;
		try {
			bikeDto = JsonServiceBikeDtoConversor.toServiceBikeDto(req.getInputStream());
		} catch (ParsingException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
					.toInputValidationException(new InputValidationException(ex.getMessage())), null);
			return;

		}
		if (!bikeId.equals(bikeDto.getBikeId())) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
					.toInputValidationException(new InputValidationException("Invalid Request: " + "invalid bikeId")),
					null);
			return;
		}

		Bike bike = BikeToBikeDtoConversor.toBike(bikeDto);

		try {
			BikeServiceFactory.getService().updateBike(bike);
		} catch (InputValidationException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(ex), null);
			return;
		} catch (InstanceNotFoundException ex) {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
					JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
			return;
		}
		ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	}

	/*
	 * BUSCANDO BICIS POR ID (RECURSO INDIVIDUAL) Y EN LA COLECCION (RECURSO
	 * COLECTIVO). APARTADO 3 Y 4
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());

		if (path == null || path.length() == 0) {
			Date date;
			String keyWords;
			try {
				keyWords = req.getParameter("keywords");
				if(keyWords == null) {
					ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
							JsonServiceExceptionConversor.toInputValidationException(
									new InputValidationException("Field 'keywords' is mandatory")),
							null);
					return;
					
				}
				String initialDateString = req.getParameter("initialDate");
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				date = sdf.parse(initialDateString);

			} catch (ParseException e) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Invalid date: it should be of the form dd-MM-yyyy")),
						null);
				return;

			}

			Calendar initialDate = Calendar.getInstance();
			initialDate.setTime(date);

			List<Bike> bikes = BikeServiceFactory.getService().findBikes(keyWords, initialDate);
			List<ServiceBikeDto> bikeDtos = BikeToBikeDtoConversor.toBikeDtos(bikes);
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					JsonServiceBikeDtoConversor.toArrayNode(bikeDtos), null);

		} else {

			String bikeIdAsString = path.substring(1);
			Long bikeId;
			try {
				bikeId = Long.valueOf(bikeIdAsString);
			} catch (NumberFormatException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
								"Invalid Request: " + "invalid bike id '" + bikeIdAsString)),
						null);
				return;
			}

			Bike bike;

			try {
				bike = BikeServiceFactory.getService().findBike(bikeId);
			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
						JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			}

			ServiceBikeDto bikeDto = BikeToBikeDtoConversor.toBikeDto(bike);

			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					JsonServiceBikeDtoConversor.toObjectNode(bikeDto), null);

		}
	}
}
