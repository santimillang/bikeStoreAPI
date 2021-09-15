package es.udc.ws.bikes.client.service.soap;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

import es.udc.ws.app.client.service.soap.wsdl.BikesProvider;
import es.udc.ws.app.client.service.soap.wsdl.BikesProviderService;
import es.udc.ws.app.client.service.soap.wsdl.SoapAlreadyRatedException;
import es.udc.ws.app.client.service.soap.wsdl.SoapInputValidationException;
import es.udc.ws.app.client.service.soap.wsdl.SoapInstanceNotFoundException;
import es.udc.ws.app.client.service.soap.wsdl.SoapMoreThanFifteenDaysException;
import es.udc.ws.app.client.service.soap.wsdl.SoapNotEnoughUnitsException;
import es.udc.ws.app.client.service.soap.wsdl.SoapOutOfDateException;
import es.udc.ws.app.client.service.soap.wsdl.SoapBikeNotAvailableYetException;
import es.udc.ws.app.client.service.soap.wsdl.SoapRentNotYetException;
import es.udc.ws.bikes.client.service.ClientBikeService;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.bikes.client.service.dto.ClientRentDto;
import es.udc.ws.bikes.client.service.exceptions.ClientAlreadyRatedException;
import es.udc.ws.bikes.client.service.exceptions.ClientBikeNotAvailableYetException;
import es.udc.ws.bikes.client.service.exceptions.ClientMoreThanFifteenDaysException;
import es.udc.ws.bikes.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.bikes.client.service.exceptions.ClientOutOfDateException;
import es.udc.ws.bikes.client.service.exceptions.ClientRentNotYetException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class SoapClientBikeService implements ClientBikeService {
	private final static String ENDPOINT_ADDRESS_PARAMETER = "SoapClientBikeService.endpointAddress";

	private String endpointAddress;

	private BikesProvider bikesProvider;

	public SoapClientBikeService() {
		init(getEndpointAddress());
	}

	private void init(String bikesProviderURL) {
		BikesProviderService bikesProviderService = new BikesProviderService();
		bikesProvider = bikesProviderService.getBikesProviderPort();
		((BindingProvider) bikesProvider).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				bikesProviderURL);
	}

	private String getEndpointAddress() {

		if (endpointAddress == null) {
			endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
		}

		return endpointAddress;
	}

	private static XMLGregorianCalendar toXMLGregorianCalendar(Calendar c) throws DatatypeConfigurationException {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTimeInMillis(c.getTimeInMillis());
		XMLGregorianCalendar xc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		return xc;
	}

	@Override
	public List<ClientBikeDto> findBikes(String keywords, Calendar initialDate) {
		try {
			return BikeDtoToSoapBikeDtoConversor
					.toClientBikeDtos(bikesProvider.findBikes(keywords, toXMLGregorianCalendar(initialDate)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Long rentBike(Long bikeId, String userId, String creditCardNumber, Long units, Calendar initialDate,
			Calendar expirationDate)
			throws InstanceNotFoundException, InputValidationException, ClientMoreThanFifteenDaysException,
			ClientNotEnoughUnitsException, ClientOutOfDateException, ClientBikeNotAvailableYetException {
		try {
			return bikesProvider.rentBike(bikeId, userId, creditCardNumber, units, toXMLGregorianCalendar(initialDate),
					toXMLGregorianCalendar(expirationDate));
		} catch (SoapInputValidationException ex) {
			throw new InputValidationException(ex.getMessage());
		} catch (SoapInstanceNotFoundException ex) {
			throw new InstanceNotFoundException(ex.getFaultInfo().getInstanceId(), ex.getFaultInfo().getInstanceType());
		} catch (SoapMoreThanFifteenDaysException ex) {
			throw new ClientMoreThanFifteenDaysException(ex.getFaultInfo().getBikeId());
		} catch (SoapNotEnoughUnitsException ex) {
			throw new ClientNotEnoughUnitsException(ex.getFaultInfo().getBikeId(), ex.getFaultInfo().getUnits());
		} catch (SoapOutOfDateException ex) {
			throw new ClientOutOfDateException();
		} catch (SoapBikeNotAvailableYetException ex) {
			throw new ClientBikeNotAvailableYetException(ex.getFaultInfo().getBikeId(), ex.getFaultInfo().getInitialDate().toGregorianCalendar());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public List<ClientRentDto> findRents(String userId) {
		return RentDtoToSoapRentDtoConversor.toClientRentDtos(bikesProvider.findRents(userId));
	}

	public void punctuate(Long rentalId, float score, String userId) throws InputValidationException,
			InstanceNotFoundException, ClientAlreadyRatedException, ClientRentNotYetException {
		try {
			bikesProvider.punctuate(rentalId, score, userId);
		} catch (SoapInputValidationException ex) {
			throw new InputValidationException(ex.getMessage());
		} catch (SoapInstanceNotFoundException ex) {
			throw new InstanceNotFoundException(ex.getFaultInfo().getInstanceId(), ex.getFaultInfo().getInstanceType());
		} catch (SoapAlreadyRatedException ex) {
			throw new ClientAlreadyRatedException(ex.getFaultInfo().getRentalId());
		} catch (SoapRentNotYetException ex) {
			throw new ClientRentNotYetException(ex.getFaultInfo().getRentalId(), ex.getFaultInfo().getExpirationDate().toGregorianCalendar());
		}
	}

	public void updateBike(ClientBikeDto bike) {
	}

	public ClientBikeDto findBike(Long bikeId) {
		return new ClientBikeDto();
	}

	public Long addBike(ClientBikeDto bike) {
		return 1L;
	}
}
