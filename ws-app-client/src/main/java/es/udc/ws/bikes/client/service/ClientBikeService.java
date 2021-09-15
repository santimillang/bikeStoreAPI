package es.udc.ws.bikes.client.service;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.bikes.client.service.dto.ClientRentDto;
import es.udc.ws.bikes.client.service.exceptions.ClientAlreadyRatedException;
import es.udc.ws.bikes.client.service.exceptions.ClientBikeNotAvailableYetException;
import es.udc.ws.bikes.client.service.exceptions.ClientMoreThanFifteenDaysException;
import es.udc.ws.bikes.client.service.exceptions.ClientNotEnoughUnitsException;
import es.udc.ws.bikes.client.service.exceptions.ClientOutOfDateException;
import es.udc.ws.bikes.client.service.exceptions.ClientRentNotYetException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientBikeService {

	public Long addBike(ClientBikeDto bike) throws InputValidationException;

	public void updateBike(ClientBikeDto bike) throws InputValidationException, InstanceNotFoundException;

	public ClientBikeDto findBike(Long bikeId) throws InstanceNotFoundException;
	
	public List<ClientRentDto> findRents(String user);

	public List<ClientBikeDto> findBikes(String keywords, Calendar initialDate);

	public Long rentBike(Long bikeId, String userId, String creditCard, Long units, Calendar initialDate,
			Calendar expirationDate) throws InputValidationException, InstanceNotFoundException,
			ClientMoreThanFifteenDaysException, ClientOutOfDateException, ClientNotEnoughUnitsException, ClientBikeNotAvailableYetException;

	public void punctuate(Long rentalId, float score, String userId)
			throws InputValidationException, InstanceNotFoundException, ClientAlreadyRatedException, ClientRentNotYetException;

}
