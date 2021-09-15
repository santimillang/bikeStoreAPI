package es.udc.ws.app.model.rent;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlRentDao {

	public Rent create(Connection connection, Rent rent);

	public List<Rent> find(Connection connection, String userId);
	
	public Rent find(Connection connection, Long rentalId) throws InstanceNotFoundException;

	public void update(Connection connection, Rent rent) throws InstanceNotFoundException;

	public void remove(Connection connection, Long rentalId) throws InstanceNotFoundException;
	
	public List<Rent> findByBikeId(Connection connection, Long bikeId);
	
	public boolean rentExists (Connection connection, Long bikeId);

}
