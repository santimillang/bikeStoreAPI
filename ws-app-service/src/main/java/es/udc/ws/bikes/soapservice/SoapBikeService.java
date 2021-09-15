package es.udc.ws.bikes.soapservice;

import java.util.Calendar;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bikeservice.BikeServiceFactory;
import es.udc.ws.app.model.bikeservice.exceptions.AlreadyRatedException;
import es.udc.ws.app.model.bikeservice.exceptions.BikeNotAvailableYetException;
import es.udc.ws.app.model.bikeservice.exceptions.MoreThanFifteenDaysException;
import es.udc.ws.app.model.bikeservice.exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.bikeservice.exceptions.OutOfDateException;
import es.udc.ws.app.model.bikeservice.exceptions.RentNotYetException;
import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.bikes.dto.ServiceBikeDto;
import es.udc.ws.bikes.dto.ServiceRentDto;
import es.udc.ws.bikes.serviceutil.BikeToBikeDtoConversor;
import es.udc.ws.bikes.serviceutil.RentToRentDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

@WebService(name = "BikesProvider", serviceName = "BikesProviderService", targetNamespace = "http://soap.ws.udc.es/")

public class SoapBikeService {

	public List<ServiceBikeDto> findBikes(@WebParam(name = "keywords") String keywords,
			@WebParam(name = "since") Calendar since) {

		List<Bike> bikes = BikeServiceFactory.getService().findBikes(keywords, since);
		return BikeToBikeDtoConversor.toBikeDtos(bikes);
	}

	public Long rentBike(@WebParam(name = "bikeId") Long bikeId, @WebParam(name = "userId") String userId,
			@WebParam(name = "creditCardNumber") String creditCardNumber, @WebParam(name = "units") Long units,
			@WebParam(name = "initialDate") Calendar initialDate,
			@WebParam(name = "expirationDate") Calendar expirationDate)
			throws SoapInputValidationException, SoapInstanceNotFoundException, SoapMoreThanFifteenDaysException,
			SoapNotEnoughUnitsException, SoapOutOfDateException, SoapBikeNotAvailableYetException {
		try {
			Rent rent = BikeServiceFactory.getService().rentBike(bikeId, userId, creditCardNumber, units, initialDate,
					expirationDate);
			return rent.getRentalId();
		} catch (InstanceNotFoundException ex) {
			throw new SoapInstanceNotFoundException(
					new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(), ex.getInstanceType()));
		} catch (InputValidationException ex) {
			throw new SoapInputValidationException(ex.getMessage());
		} catch (MoreThanFifteenDaysException ex) {
			throw new SoapMoreThanFifteenDaysException(new SoapMoreThanFifteenDaysExceptionInfo(ex.getBikeId()));
		} catch (NotEnoughUnitsException ex) {
			throw new SoapNotEnoughUnitsException(new SoapNotEnoughUnitsExceptionInfo(ex.getUnits(), ex.getBikeId()));
		} catch (OutOfDateException ex) {
			throw new SoapOutOfDateException(ex.getMessage());
		} catch (BikeNotAvailableYetException ex) {
			throw new SoapBikeNotAvailableYetException(
					new SoapBikeNotAvailableYetExceptionInfo(ex.getBikeId(), ex.getFirstDay()));
		}
	}

	public List<ServiceRentDto> findRents(@WebParam(name = "userId") String userId) {

		List<Rent> rents = BikeServiceFactory.getService().findRents(userId);
		return RentToRentDtoConversor.toRentDto(rents);
	}

	public void punctuate(@WebParam(name = "rentalId") Long rentalId, @WebParam(name = "score") float score,
			@WebParam(name = "userId") String userId) throws SoapInputValidationException,
			SoapInstanceNotFoundException, SoapAlreadyRatedException, SoapRentNotYetException {
		try {
			BikeServiceFactory.getService().punctuate(rentalId, score, userId);
		} catch (InputValidationException ex) {
			throw new SoapInputValidationException(ex.getMessage());
		} catch (InstanceNotFoundException ex) {
			throw new SoapInstanceNotFoundException(
					new SoapInstanceNotFoundExceptionInfo(ex.getInstanceId(), ex.getInstanceType()));
		} catch (AlreadyRatedException ex) {
			throw new SoapAlreadyRatedException(new SoapAlreadyRatedExceptionInfo(ex.getRentalId()));
		} catch (RentNotYetException ex) {
			throw new SoapRentNotYetException(
					new SoapRentNotYetExceptionInfo(ex.getRentalId(), ex.getExpirationDate()));
		}
	}
}
