package es.udc.ws.app.test.model.bikeservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bike.SqlBikeDao;
import es.udc.ws.app.model.bike.SqlBikeDaoFactory;
import es.udc.ws.app.model.bikeservice.BikeService;
import es.udc.ws.app.model.bikeservice.BikeServiceFactory;
import es.udc.ws.app.model.bikeservice.exceptions.AlreadyRatedException;
import es.udc.ws.app.model.bikeservice.exceptions.BikeNotAvailableYetException;
import es.udc.ws.app.model.bikeservice.exceptions.MoreThanFifteenDaysException;
import es.udc.ws.app.model.bikeservice.exceptions.NotEnoughUnitsException;
import es.udc.ws.app.model.bikeservice.exceptions.OutOfDateException;
import es.udc.ws.app.model.bikeservice.exceptions.RentNotYetException;
import es.udc.ws.app.model.rent.Rent;
import es.udc.ws.app.model.rent.SqlRentDao;
import es.udc.ws.app.model.rent.SqlRentDaoFactory;
import es.udc.ws.app.model.util.ModelConstants;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class BikeServiceTest {

	private final long NON_EXISTENT_BIKE_ID = -1;

	private final String USER_ID = "ws-user";

	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String INVALID_CREDIT_CARD_NUMBER = "";

	private static BikeService bikeService = null;

	private static SqlRentDao rentDao = null;
	private static SqlBikeDao bikeDao = null;

	@BeforeClass
	public static void init() {

		DataSource dataSource = new SimpleDataSource();

		/* Add "dataSource" to "DataSourceLocator". */
		DataSourceLocator.addDataSource(ModelConstants.BIKE_DATA_SOURCE, dataSource);

		bikeService = BikeServiceFactory.getService();

		rentDao = SqlRentDaoFactory.getDao();
		bikeDao = SqlBikeDaoFactory.getDao();

	}

	private Bike getValidBike(String model) {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, 3);
		return new Bike(model, "description", date, (float) 5, (long) 10);
	}

	private Bike getValidBikeDate(String model, int dateExcess) {
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, dateExcess);
		return new Bike(model, "description", date, (float) 5, (long) 10);
	}

	private Bike getValidBike() {
		return getValidBike("BikeModel");
	}

	private Bike createBike(Bike bike) {
		Bike addedBike = null;
		try {
			addedBike = bikeService.addBike(bike);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedBike;

	}

	private void removeBike(Long bikeId) throws InstanceNotFoundException {

		DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				bikeDao.remove(connection, bikeId);

				/* Commit. */
				connection.commit();

			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	private void removeRent(Long rentalId) {

		DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				rentDao.remove(connection, rentalId);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	private void updateRent(Rent rent) {

		DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				rentDao.update(connection, rent);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void testAddBikeAndFindBike() throws InputValidationException, InstanceNotFoundException {

		Bike bike = getValidBike();
		Bike addedBike = null;

		try {
			addedBike = bikeService.addBike(bike);
			Bike foundBike = bikeService.findBike(addedBike.getBikeId());

			assertEquals(addedBike, foundBike);

		} finally {
			// Clear Database
			if (addedBike != null) {
				removeBike(addedBike.getBikeId());
			}
		}
	}
	

	@Test
	public void testAddInvalidBike() throws InstanceNotFoundException {

		Bike bike = getValidBike();
		Bike addedBike = null;
		boolean exceptionCatched = false;

		try {

			bike.setModel(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			exceptionCatched = false;
			bike = getValidBike();
			bike.setModel("");
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			exceptionCatched = false;
			bike = getValidBike();
			bike.setDescription(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			exceptionCatched = false;
			bike = getValidBike();
			bike.setDescription("");
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			exceptionCatched = false;
			bike = getValidBike();
			bike.setPrice((short) -1);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			exceptionCatched = false;
			bike = getValidBike();
			bike.setPrice((short) (0));
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
			exceptionCatched = false;
			bike = getValidBike();
			bike.setUnits(new Long(-8));
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

		} finally {
			if (!exceptionCatched) {
				removeBike(addedBike.getBikeId());
			}
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentBike() throws InstanceNotFoundException {

		bikeService.findBike(NON_EXISTENT_BIKE_ID);

	}

	@Test
	public void testUpdateBike() throws InputValidationException, InstanceNotFoundException {

		Bike bike = createBike(getValidBike());
		try {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, 3);
			Bike bikeToUpdate = new Bike(bike.getBikeId(), "new model", "new description", date, 5F, 20,
					bike.getCreationDate());
			// bikeToUpdate.setBikeId(null);

			bikeService.updateBike(bikeToUpdate);

			Bike updatedBike = bikeService.findBike(bike.getBikeId());

			bikeToUpdate.setCreationDate(bike.getCreationDate());
			assertEquals(bikeToUpdate, updatedBike);

		} finally {

			removeBike(bike.getBikeId());
		}

	}

	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidBike() throws InputValidationException, InstanceNotFoundException {

		Bike bike = createBike(getValidBike());
		try {
			// Check Bike title not null
			bike = bikeService.findBike(bike.getBikeId());
			bike.setModel(null);
			bikeService.updateBike(bike);
		} finally {
			// Clear Database
			removeBike(bike.getBikeId());
		}

	}
	
	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidUnits() throws InputValidationException, InstanceNotFoundException {

		Bike bike = createBike(getValidBike());
		try {
			// Check Bike title not null
			bike = bikeService.findBike(bike.getBikeId());
			bike.setUnits(new Long (-8));
			bikeService.updateBike(bike);
		} finally {
			// Clear Database
			removeBike(bike.getBikeId());
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentBike() throws InputValidationException, InstanceNotFoundException {

		Bike bike = getValidBike();
		bike.setBikeId(NON_EXISTENT_BIKE_ID);
		bike.setCreationDate(Calendar.getInstance());
		bikeService.updateBike(bike);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveNonExistentBike() throws InstanceNotFoundException {
		removeBike(NON_EXISTENT_BIKE_ID);
	}

	@Test
	public void testFindBikes() throws InstanceNotFoundException { /* ALL COMBINATIONS */

		List<Bike> Bikes = new LinkedList<Bike>();
		Bike Bike1 = createBike(getValidBike("Bike model 1"));
		Bikes.add(Bike1);
		Bike Bike2 = createBike(getValidBike("Bike model 2"));
		Bikes.add(Bike2);
		Bike Bike3 = createBike(getValidBike("Bike model 3"));
		Bikes.add(Bike3);
		Bike Bike4 = createBike(getValidBikeDate("Bike model 4", 7));
		Bikes.add(Bike4);

		try {
			List<Bike> foundBikes = bikeService.findBikes("description", null);
			assertEquals(Bikes, foundBikes);

			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, 4);

			foundBikes = bikeService.findBikes("description", date);
			assertEquals(3, foundBikes.size());
			assertEquals(Bikes.get(0), foundBikes.get(0));

			foundBikes = bikeService.findBikes("", null);
			assertEquals(Bikes, foundBikes);

			foundBikes = bikeService.findBikes("", date);
			assertEquals(3, foundBikes.size());
			assertEquals(Bikes.get(0), foundBikes.get(0));

		} finally {

			for (Bike Bike : Bikes) {
				removeBike(Bike.getBikeId());
			}
		}

	}

	@Test(expected = InputValidationException.class)
	public void testRentBikeWithInvalidCreditCard() throws InputValidationException, InstanceNotFoundException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {

		Bike bike = createBike(getValidBike());
		Rent rent = null;
		try {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, 3);
			rent = bikeService.rentBike(bike.getBikeId(), USER_ID, INVALID_CREDIT_CARD_NUMBER, (long) 3,
					Calendar.getInstance(), date);

		} finally {
			removeBike(bike.getBikeId());
			if (rent != null) {
				removeRent(rent.getRentalId());
			}
		}

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRentNonExistentBike() throws InputValidationException, InstanceNotFoundException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {
		Rent rent = null;
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE, 3);
		Calendar dateFirst = Calendar.getInstance();
		dateFirst.add(Calendar.DATE, 1);
		rent = bikeService.rentBike(NON_EXISTENT_BIKE_ID, USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 3,
				dateFirst, date);
		if (rent != null) {
			removeRent(rent.getRentalId());
		}

	}

	@Test(expected = MoreThanFifteenDaysException.class)
	public void testRentBikeMoreThanFifteen() throws InputValidationException, InstanceNotFoundException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {

		Bike bike = createBike(getValidBike());
		Rent rent = null;
		try {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, 26);
			Calendar dateIni = Calendar.getInstance();
			dateIni.add(Calendar.DATE, 8);
			rent = bikeService.rentBike(bike.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 3, dateIni, date);

		} finally {
			if (rent != null) {
				removeRent(rent.getRentalId());
			}

			removeBike(bike.getBikeId());
		}

	}

	@Test(expected = NotEnoughUnitsException.class)
	public void testRentBikeTooManyUnits() throws InputValidationException, InstanceNotFoundException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {

		Bike bike = createBike(getValidBike());
		Rent rent = null;
		try {
			Calendar date = Calendar.getInstance();
			date.add(Calendar.DATE, 3);
			rent = bikeService.rentBike(bike.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 30,
					Calendar.getInstance(), date);

		} finally {
			if (rent != null) {
				removeRent(rent.getRentalId());
			}
			removeBike(bike.getBikeId());
		}

	}


	@Test(expected = BikeNotAvailableYetException.class)
	public void testRentBikeNotAvailableYet() throws InputValidationException, InstanceNotFoundException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {
		Bike bike = createBike(getValidBikeDate("BikeModel", 5));
		Rent rent = null;
		try {
			Calendar dateIni = Calendar.getInstance();
			dateIni.add(Calendar.DATE, 4);
			Calendar dateEx = Calendar.getInstance();
			dateEx.add(Calendar.DATE, 7);

			rent = bikeService.rentBike(bike.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 3, dateIni, dateEx);

		} finally {
			if (rent != null) {
				removeRent(rent.getRentalId());
			}
			removeBike(bike.getBikeId());

		}

	}

	@Test
	public void testFindRent() throws InputValidationException, InstanceNotFoundException, NotEnoughUnitsException,
			MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {
		Bike bike = createBike(getValidBike());
		Rent rent = null;
		try {
			Calendar dateIni = Calendar.getInstance();
			dateIni.add(Calendar.DATE, 4);
			Calendar dateEx = Calendar.getInstance();
			dateEx.add(Calendar.DATE, 7);

			rent = bikeService.rentBike(bike.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 3, dateIni, dateEx);

			List<Rent> foundRents = bikeService.findRents(USER_ID);

			assertEquals(1, foundRents.size());

			assertEquals(rent, foundRents.get(0));

		} finally {
			if (rent != null) {
				removeRent(rent.getRentalId());
			}
			removeBike(bike.getBikeId());

		}

	}

	@Test
	public void testCheckRent() throws InputValidationException, InstanceNotFoundException, NotEnoughUnitsException,
			MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {
		Bike bike = createBike(getValidBike());
		Rent rent = null;
		try {
			Calendar dateIni = Calendar.getInstance();
			dateIni.add(Calendar.DATE, 4);
			Calendar dateEx = Calendar.getInstance();
			dateEx.add(Calendar.DATE, 7);

			rent = bikeService.rentBike(bike.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 3, dateIni, dateEx);

			List<Rent> foundRents = bikeService.findRents(USER_ID);

			assertEquals(1, foundRents.size());

			// assertEquals(rent, foundRents.get(0));
			Rent foundRent = foundRents.get(0);

			assertEquals(rent.getBikeId(), foundRent.getBikeId());
			assertEquals(rent.getCreditCardNumber(), foundRent.getCreditCardNumber());
			assertEquals(rent.getExpirationDate(), foundRent.getExpirationDate());
			assertEquals(rent.getInitialDate(), foundRent.getInitialDate());
			assertEquals(rent.getPunctuation(), foundRent.getPunctuation(), 0);
			assertEquals(rent.getRentalId(), foundRent.getRentalId());
			assertEquals(rent.getRentalTime(), foundRent.getRentalTime());
			assertEquals(rent.getUnits(), foundRent.getUnits());

		} finally {
			if (rent != null) {
				removeRent(rent.getRentalId());
			}
			removeBike(bike.getBikeId());

		}

	}

	@Test(expected = InputValidationException.class)
	public void testUpdateBikeWithBookings() throws InputValidationException, InstanceNotFoundException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {
		Bike bike = createBike(getValidBike());
		Bike newBike = getValidBike();
		Rent rent1 = null;
		Rent rent2 = null;

		try {
			Calendar dateIni = Calendar.getInstance();
			dateIni.add(Calendar.DATE, 4);
			Calendar dateEx = Calendar.getInstance();
			dateEx.add(Calendar.DATE, 7);

			rent1 = bikeService.rentBike(bike.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 3, dateIni,
					dateEx);
			rent2 = bikeService.rentBike(bike.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, (long) 3, dateIni,
					dateEx);

			newBike.setBikeId(bike.getBikeId());
			newBike.setAverage(bike.getAverage());
			newBike.setCreationDate(bike.getCreationDate());
			newBike.setDescription(bike.getDescription());
			newBike.setModel(bike.getModel());
			newBike.setPrice(bike.getPrice());
			newBike.setReviewNumber(bike.getReviewNumber());
			newBike.setUnits(bike.getUnits());
			Calendar newFirstDate = Calendar.getInstance();
			newFirstDate.add(Calendar.DATE, 20);
			newBike.setFirstDate(newFirstDate);

			bikeService.updateBike(newBike);

		} finally {
			if (rent1 != null && rent2 != null) {
				removeRent(rent1.getRentalId());
				removeRent(rent2.getRentalId());
			}
			removeBike(bike.getBikeId());

		}

	}

	/*-------------------------------------------------------------------------------------*/

	@Test(expected = RentNotYetException.class)
	public void testPunctuationNotFinished()
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedException, RentNotYetException {

		DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);
		Rent rentCreated = null;
		Bike bikeCreated = null;

		try (Connection connection = dataSource.getConnection()) {

			try {

				Calendar dateInitBike = Calendar.getInstance();
				dateInitBike.add(Calendar.DATE, -9);
				Bike bike = new Bike("Model1", "description", dateInitBike, (float) 8.99, (long) 10);
				bike.setCreationDate(Calendar.getInstance());
				bikeCreated = bikeDao.create(connection, bike);

				Calendar dateIni = Calendar.getInstance();
				dateIni.add(Calendar.DATE, -6);
				Calendar dateEx = Calendar.getInstance();
				dateEx.add(Calendar.DATE, 1);
				Rent rent = new Rent(bikeCreated.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, dateIni, dateEx,
						(long) 3, Calendar.getInstance());
				rentCreated = rentDao.create(connection, rent);

				bikeService.punctuate(rentCreated.getRentalId(), (float) 7.5, USER_ID);

			} finally {
				if (rentCreated != null) {
					removeRent(rentCreated.getRentalId());
				}
				if (bikeCreated != null) {
					removeBike(bikeCreated.getBikeId());
				}

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Test(expected = InputValidationException.class)
	public void testPunctuationNotSameUser()
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedException, RentNotYetException {

		DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);
		Rent rentCreated = null;
		Bike bikeCreated = null;

		try (Connection connection = dataSource.getConnection()) {

			try {

				Calendar dateInitBike = Calendar.getInstance();
				dateInitBike.add(Calendar.DATE, -9);
				Bike bike = new Bike("Model1", "description", dateInitBike, (float) 8.99, (long) 10);
				bike.setCreationDate(Calendar.getInstance());
				bikeCreated = bikeDao.create(connection, bike);

				Calendar dateIni = Calendar.getInstance();
				dateIni.add(Calendar.DATE, -6);
				Calendar dateEx = Calendar.getInstance();
				dateEx.add(Calendar.DATE, -3);
				Rent rent = new Rent(bikeCreated.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, dateIni, dateEx,
						(long) 3, Calendar.getInstance());
				rentCreated = rentDao.create(connection, rent);

				bikeService.punctuate(rentCreated.getRentalId(), (float) 7.5, "AnotherUser");

			} finally {
				if (rentCreated != null) {
					removeRent(rentCreated.getRentalId());
				}
				if (bikeCreated != null) {
					removeBike(bikeCreated.getBikeId());
				}

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Test(expected = AlreadyRatedException.class)
	public void testPunctuationAlreadyRated()
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedException, RentNotYetException {

		DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);
		Rent rentCreated = null;
		Bike bikeCreated = null;

		try (Connection connection = dataSource.getConnection()) {

			try {

				Calendar dateInitBike = Calendar.getInstance();
				dateInitBike.add(Calendar.DATE, -9);
				Bike bike = new Bike("Model1", "description", dateInitBike, (float) 8.99, (long) 10);
				bike.setCreationDate(Calendar.getInstance());
				bikeCreated = bikeDao.create(connection, bike);

				Calendar dateIni = Calendar.getInstance();
				dateIni.add(Calendar.DATE, -6);
				Calendar dateEx = Calendar.getInstance();
				dateEx.add(Calendar.DATE, -3);
				Rent rent = new Rent(bikeCreated.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, dateIni, dateEx,
						(long) 3, Calendar.getInstance());
				rentCreated = rentDao.create(connection, rent);

				bikeService.punctuate(rentCreated.getRentalId(), (float) 7.5, USER_ID);
				bikeService.punctuate(rentCreated.getRentalId(), (float) 8.3, USER_ID);

			} finally {
				if (rentCreated != null) {
					removeRent(rentCreated.getRentalId());
				}
				if (bikeCreated != null) {
					removeBike(bikeCreated.getBikeId());
				}

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void testPunctuation() throws InputValidationException, InstanceNotFoundException, AlreadyRatedException, RentNotYetException {

		DataSource dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);
		Rent rentCreated1 = null;
		Rent rentCreated2 = null;
		Bike bikeCreated = null;

		try (Connection connection = dataSource.getConnection()) {

			try {

				Calendar dateInitBike = Calendar.getInstance();
				dateInitBike.add(Calendar.DATE, -9);
				Bike bike = new Bike("Model1", "description", dateInitBike, (float) 8.99, (long) 10);
				bike.setCreationDate(Calendar.getInstance());
				bikeCreated = bikeDao.create(connection, bike);

				Calendar dateIni = Calendar.getInstance();
				dateIni.add(Calendar.DATE, -6);
				Calendar dateEx = Calendar.getInstance();
				dateEx.add(Calendar.DATE, -2);
				Rent rent = new Rent(bikeCreated.getBikeId(), USER_ID, VALID_CREDIT_CARD_NUMBER, dateIni, dateEx,
						(long) 3, Calendar.getInstance());
				rentCreated1 = rentDao.create(connection, rent);
				rentCreated2 = rentDao.create(connection, rent);

				bikeService.punctuate(rentCreated1.getRentalId(), (float) 7.5, USER_ID);
				bikeService.punctuate(rentCreated2.getRentalId(), (float) 5, USER_ID);

				Bike bikeReviewed = bikeService.findBike(rentCreated1.getBikeId());
				List<Rent> foundRents = bikeService.findRents(USER_ID);

				assertEquals(foundRents.get(0).getPunctuation(), (float) 7.5, 0);
				assertEquals(foundRents.get(1).getPunctuation(), (float) 5, 0);
				assertEquals(bikeReviewed.getAverage(), (float) 6.25, 0);
				assertEquals(bikeReviewed.getReviewNumber(), (long) 2, 0);

			} finally {
				if (rentCreated1 != null && rentCreated2 != null) {
					removeRent(rentCreated1.getRentalId());
					removeRent(rentCreated2.getRentalId());
				}
				if (bikeCreated != null) {
					removeBike(bikeCreated.getBikeId());
				}

			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
