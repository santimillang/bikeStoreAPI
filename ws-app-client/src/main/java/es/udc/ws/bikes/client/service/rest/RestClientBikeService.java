package es.udc.ws.bikes.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.bikes.client.service.ClientBikeService;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.bikes.client.service.dto.ClientRentDto;
import es.udc.ws.bikes.client.service.exceptions.ClientAlreadyRatedException;
import es.udc.ws.bikes.client.service.exceptions.ClientBikeNotAvailableYetException;
import es.udc.ws.bikes.client.service.exceptions.ClientMoreThanFifteenDaysException;
import es.udc.ws.bikes.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.bikes.client.service.exceptions.ClientOutOfDateException;
import es.udc.ws.bikes.client.service.exceptions.ClientRentNotYetException;
import es.udc.ws.bikes.client.service.rest.json.JsonClientBikeDtoConversor;
import es.udc.ws.bikes.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.bikes.client.service.rest.json.JsonClientRentDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;

public class RestClientBikeService implements ClientBikeService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeService.endpointAddress";
	private String endpointAddress;

	@Override
	public Long addBike(ClientBikeDto bike) throws InputValidationException {
		try {

			HttpResponse response = Request.Post(getEndpointAddress() + "bikes")
					.bodyStream(toInputStream(bike), ContentType.create("application/json")).execute().returnResponse();

			validateStatusCode(HttpStatus.SC_CREATED, response);

			return JsonClientBikeDtoConversor.toClientBikeDto(response.getEntity().getContent()).getBikeId();
		} catch (InputValidationException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void updateBike(ClientBikeDto bike) throws InputValidationException, InstanceNotFoundException {

		try {

			HttpResponse response = Request.Put(getEndpointAddress() + "bikes/" + bike.getBikeId())
					.bodyStream(toInputStream(bike), ContentType.create("application/json")).execute().returnResponse();

			validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

		} catch (InputValidationException | InstanceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public ClientBikeDto findBike(Long bikeId) throws InstanceNotFoundException {

		try {

			HttpResponse response = Request.Get(getEndpointAddress() + "bikes/" + bikeId).execute().returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

			return JsonClientBikeDtoConversor.toClientBikeDto(response.getEntity().getContent());

		} catch (InstanceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public List<ClientBikeDto> findBikes(String keywords, Calendar initialDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strdate = sdf.format(initialDate.getTime());

		try {
			HttpResponse response = Request.Get(getEndpointAddress() + "bikes?keywords="
					+ URLEncoder.encode(keywords, "UTF-8") + "&initialDate=" + URLEncoder.encode(strdate, "UTF-8"))
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);
			// System.out.println(response);
			return JsonClientBikeDtoConversor.toClientBikeDtos(response.getEntity().getContent());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public List<ClientRentDto> findRents(String user) {

		try {
			HttpResponse response = Request.Get(getEndpointAddress() + "rents?user=" + URLEncoder.encode(user, "UTF-8"))
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);
			return JsonClientRentDtoConversor.toClientRentDtos(response.getEntity().getContent());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public Long rentBike(Long bikeId, String userId, String creditCard, Long units, Calendar initialDate,
			Calendar expirationDate) throws InputValidationException, InstanceNotFoundException,
			ClientMoreThanFifteenDaysException, ClientOutOfDateException, ClientNotEnoughUnitsException, ClientBikeNotAvailableYetException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strdate = sdf.format(initialDate.getTime());
		String strdateEx = sdf.format(expirationDate.getTime());

		try {

			HttpResponse response = Request.Post(getEndpointAddress() + "rents")
					.bodyForm(Form.form().add("bikeId", Long.toString(bikeId)).add("userId", userId)
							.add("creditCardNumber", creditCard).add("units", Long.toString(units))
							.add("initialDate", strdate).add("expirationDate", strdateEx).build())
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_CREATED, response);

			return JsonClientRentDtoConversor.toClientRentDto(response.getEntity().getContent()).getRentalId();

		} catch (InputValidationException | InstanceNotFoundException | ClientMoreThanFifteenDaysException
				| ClientOutOfDateException | ClientNotEnoughUnitsException | ClientBikeNotAvailableYetException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public void punctuate(Long rentalId, float score, String userId)
			throws InputValidationException, InstanceNotFoundException, ClientAlreadyRatedException, ClientRentNotYetException {

		try {

			HttpResponse response = Request.Post(getEndpointAddress() + "rents/" + rentalId + "/punctuate")
					.bodyForm(Form.form().add("punctuation", Float.toString(score)).add("user", userId).build())
					.execute().returnResponse();

			validateStatusCode(HttpStatus.SC_OK, response);

		} catch (InputValidationException | InstanceNotFoundException | ClientAlreadyRatedException | ClientRentNotYetException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private synchronized String getEndpointAddress() {
		if (endpointAddress == null) {
			endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
		}
		return endpointAddress;
	}

	private void validateStatusCode(int successCode, HttpResponse response)
			throws InstanceNotFoundException, ClientMoreThanFifteenDaysException, ClientNotEnoughUnitsException,
			ClientOutOfDateException, ClientAlreadyRatedException, InputValidationException, Exception {

		try {

			int statusCode = response.getStatusLine().getStatusCode();

			/* Success? */
			if (statusCode == successCode) {
				return;
			}

			/* Handler error. */
			switch (statusCode) {

			case HttpStatus.SC_NOT_FOUND:
				throw JsonClientExceptionConversor.fromNotFoundCode(response.getEntity().getContent());

			case HttpStatus.SC_BAD_REQUEST:
				throw JsonClientExceptionConversor.fromBadRequestCode(response.getEntity().getContent());
				
			case HttpStatus.SC_FORBIDDEN:
				throw JsonClientExceptionConversor.fromAlreadyRatedException(response.getEntity().getContent());
				
			default:
				throw new RuntimeException("HTTP error; status code = " + statusCode);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private InputStream toInputStream(ClientBikeDto bike) {

		try {

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
					JsonClientBikeDtoConversor.toJsonObject(bike));

			return new ByteArrayInputStream(outputStream.toByteArray());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
