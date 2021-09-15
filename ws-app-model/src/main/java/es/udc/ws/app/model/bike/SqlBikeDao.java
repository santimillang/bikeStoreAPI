package es.udc.ws.app.model.bike;

import java.sql.Connection;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlBikeDao {

	public Bike create(Connection connection, Bike bike);

	public Bike find(Connection connection, Long bikeId) throws InstanceNotFoundException;

	public void update(Connection connection, Bike bike) throws InstanceNotFoundException;

	public void remove(Connection connection, Long bikeId) throws InstanceNotFoundException;

	public List<Bike> findByKeywords(Connection connection, String keywords, Calendar firstDate);

}
