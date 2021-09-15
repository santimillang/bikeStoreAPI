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

import es.udc.ws.app.model.bikeservice.BikeServiceFactory;
import es.udc.ws.app.model.bikeservice.exceptions.AlreadyRatedException;
import es.udc.ws.app.model.bikeservice.exceptions.BikeNotAvailableYetException;
import es.udc.ws.app.model.bikeservice.exceptions.MoreThanFifteenDaysException;
import es.udc.ws.app.model.bikeservice.exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.bikeservice.exceptions.OutOfDateException;
import es.udc.ws.app.model.bikeservice.exceptions.RentNotYetException;
import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.bikes.dto.ServiceRentDto;
import es.udc.ws.bikes.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.bikes.restservice.json.JsonServiceRentDtoConversor;
import es.udc.ws.bikes.serviceutil.RentToRentDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class RentsServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());

		if (path == null || path.length() == 0) {

			String bikeIdParameter = req.getParameter("bikeId");
			if (bikeIdParameter == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Invalid Request: " + "parameter 'bikeId' is mandatory")),
						null);
				return;
			}
			Long bikeId;
			try {
				bikeId = Long.valueOf(bikeIdParameter);
			} catch (NumberFormatException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
								"Invalid Request: " + "parameter 'bikeId' is invalid '" + bikeIdParameter + "'")),
						null);

				return;
			}
			String userId = req.getParameter("userId");
			if (userId == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Invalid Request: " + "parameter 'userId' is mandatory")),
						null);
				return;
			}
			String creditCardNumber = req.getParameter("creditCardNumber");
			if (creditCardNumber == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
								"Invalid Request: " + "parameter 'creditCardNumber' is mandatory")),
						null);

				return;
			}

			String unitsParameter = req.getParameter("units");
			if (unitsParameter == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Invalid Request: " + "parameter 'units' is mandatory")),
						null);
				return;
			}
			Long units;
			try {
				units = Long.valueOf(unitsParameter);
			} catch (NumberFormatException ex) {
				ServletUtils
						.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
								JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
										"Invalid Request: " + "parameter 'units' is invalid '" + unitsParameter + "'")),
								null);

				return;
			}

			String initialDateParameter = req.getParameter("initialDate");
			if (initialDateParameter == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
								"Invalid Request: " + "parameter 'initialDate' is mandatory")),
						null);
				return;
			}
			Calendar initialDate = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date date = sdf.parse(initialDateParameter);
				initialDate.setTime(date);
			} catch (ParseException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor
								.toInputValidationException(new InputValidationException("Invalid Request: "
										+ "parameter 'initialDate' is invalid '" + initialDateParameter + "'")),
						null);

				return;
			}

			String expirationDateParameter = req.getParameter("expirationDate");
			if (expirationDateParameter == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
								"Invalid Request: " + "parameter 'expiratonDate' is mandatory")),
						null);
				return;
			}
			Calendar expirationDate = Calendar.getInstance();
			try {
				Date date = sdf.parse(expirationDateParameter);
				expirationDate.setTime(date);
			} catch (ParseException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor
								.toInputValidationException(new InputValidationException("Invalid Request: "
										+ "parameter 'expirationDate' is invalid '" + expirationDateParameter + "'")),
						null);

				return;
			}

			Rent rent;
			try {
				rent = BikeServiceFactory.getService().rentBike(bikeId, userId, creditCardNumber, units, initialDate,
						expirationDate);
			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
						JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			} catch (InputValidationException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(ex), null);
				return;
			} catch (NotEnoughUnitsException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toNotEnoughUnitsException(ex), null);
				return;
			} catch (MoreThanFifteenDaysException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toMoreThanFifteenDaysException(ex), null);
				return;
			} catch (OutOfDateException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toOutOfDateException(ex), null);
				return;
			} catch (BikeNotAvailableYetException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
						JsonServiceExceptionConversor.toBikeNotAvailableYetException(ex), null);
				return;
			}

			ServiceRentDto rentDto = RentToRentDtoConversor.toRentDto(rent);

			String rentURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/"
					+ rent.getRentalId().toString();

			Map<String, String> headers = new HashMap<>(1);
			headers.put("Location", rentURL);

			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
					JsonServiceRentDtoConversor.toJsonObject(rentDto), headers);

			/*---------------AQUI ESTAMOS EN EL CASO DE USO DE PUNTUAR-------------------*/

		} else {
			String rentalIdAsString = path.substring(1, path.indexOf("/punctuate"));

			Long rentalId;
			try {
				rentalId = Long.valueOf(rentalIdAsString);
			} catch (NumberFormatException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
								"Invalid Request: " + "invalid rental id '" + rentalIdAsString + "'")),
						null);
				return;
			}
			String user = req.getParameter("user");
			if (user == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Invalid Request: " + "parameter 'user' is mandatory")),
						null);
				return;
			}
			String punctuationString = req.getParameter("punctuation");
			if (punctuationString == null) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
								"Invalid Request: " + "parameter 'punctuation' is mandatory")),
						null);
				return;
			}
			float punctuation;
			try {
				punctuation = Float.valueOf(punctuationString.trim()).floatValue();
			} catch (NumberFormatException e) {
				ServletUtils
						.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
								JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
										"Invalid Request: " + "invalid punctuation value '" + punctuationString + "'")),
								null);
				return;

			}
			try {
				BikeServiceFactory.getService().punctuate(rentalId, punctuation, user);
			} catch (InputValidationException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(ex), null);
				return;
			} catch (InstanceNotFoundException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
						JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
				return;
			} catch (AlreadyRatedException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
						JsonServiceExceptionConversor.toAlreadyRatedException(ex), null);
				return;
			} catch (RentNotYetException ex) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
						JsonServiceExceptionConversor.toRentNotYetException(ex), null);
				return;
			}

			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK, null, null);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = ServletUtils.normalizePath(req.getPathInfo());
		if (path == null || path.length() == 0) {
			String user = req.getParameter("user");
			if (user == null) {
				ServletUtils.writeServiceResponse(
						resp, HttpServletResponse.SC_BAD_REQUEST, JsonServiceExceptionConversor
								.toInputValidationException(new InputValidationException("Please, specify a user")),
						null);

			}

			List<Rent> rents = BikeServiceFactory.getService().findRents(user);
			List<ServiceRentDto> rentDtos = RentToRentDtoConversor.toRentDto(rents);
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
					JsonServiceRentDtoConversor.toArrayNode(rentDtos), null);
		} else {
			ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
					JsonServiceExceptionConversor.toInputValidationException(
							new InputValidationException("Invalid Request: " + "invalid path " + path)),
					null);
		}
	}

}
