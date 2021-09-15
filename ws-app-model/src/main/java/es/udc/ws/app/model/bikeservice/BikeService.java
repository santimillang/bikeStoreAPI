package es.udc.ws.app.model.bikeservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bikeservice.exceptions.AlreadyRatedException;
import es.udc.ws.app.model.bikeservice.exceptions.BikeNotAvailableYetException;
import es.udc.ws.app.model.bikeservice.exceptions.MoreThanFifteenDaysException;
import es.udc.ws.app.model.bikeservice.exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.bikeservice.exceptions.OutOfDateException;
import es.udc.ws.app.model.bikeservice.exceptions.RentNotYetException;
import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface BikeService {

	public Bike addBike(Bike bike) throws InputValidationException;

	public void updateBike(Bike bike) throws InputValidationException, InstanceNotFoundException;

	public Bike findBike(Long bikeId) throws InstanceNotFoundException;

	public List<Bike> findBikes(String keywords, Calendar initialDate); /* throws InstanceNotFoundException; */

	public Rent rentBike(Long bikeId, String userId, String creditCardNumber, Long units, Calendar initialDate,
			Calendar expirationDate) throws InstanceNotFoundException, InputValidationException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException;

	public List<Rent> findRents(String userId);

	public void punctuate(Long rentalId, float score, String userId)
			throws InstanceNotFoundException, InputValidationException, AlreadyRatedException, RentNotYetException;

}
