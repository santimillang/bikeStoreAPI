package es.udc.ws.app.model.bikeservice;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.app.model.bike.SqlBikeDao;
import es.udc.ws.app.model.bike.SqlBikeDaoFactory;
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
import es.udc.ws.util.validation.PropertyValidator;

public class BikeServiceImpl implements BikeService {

	private final DataSource dataSource;
	private SqlBikeDao bikeDao = null;
	private SqlRentDao rentDao = null;

	public BikeServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(ModelConstants.BIKE_DATA_SOURCE);
		bikeDao = SqlBikeDaoFactory.getDao();
		rentDao = SqlRentDaoFactory.getDao();
	}

	private static void validateFutureDate(String propertyName, Calendar propertyValue)
			throws InputValidationException {

		Calendar now = Calendar.getInstance();
		if ((propertyValue == null) || (now.after(propertyValue))) {
			throw new InputValidationException(
					"Invalid " + propertyName + " value (it must be a future date): " + propertyValue);
		}

	}

	private static int calculateDays(Calendar initialDate, Calendar expirationDate) {
		return (int) ChronoUnit.DAYS.between(initialDate.toInstant(), expirationDate.toInstant());
	}
	
	private static boolean lessThanADay(Calendar now, Calendar initialDate) {
		Date rightNow = now.getTime();
		Date checkDate = initialDate.getTime();
		
		return (checkDate.getTime() - rightNow.getTime()) < (24*60*60*1000L);
				
	}

	private void validateBike(Bike bike) throws InputValidationException {
		PropertyValidator.validateMandatoryString("model", bike.getModel());
		PropertyValidator.validateMandatoryString("description", bike.getDescription());
		if (bike.getPrice() <= 0) {
			throw new InputValidationException("Price cannot be equal or less than 0.");
		}
		validateFutureDate("firstDate", bike.getFirstDate());
		if (bike.getUnits() <= 0) {
			throw new InputValidationException("Units must be greater than 0");
		}
	}

	private void validateRent(Long units, String creditCard, Calendar initialDate, Calendar expirationDate)
			throws InputValidationException {
		PropertyValidator.validateCreditCard(creditCard);
		PropertyValidator.validateNotNegativeLong("units", units);
		validateFutureDate("initialDate", initialDate);
		if (initialDate.after(expirationDate)) {
			throw new InputValidationException("ExpirationDate must be after initialDate");
		}
		if (units <= 0) {
			throw new InputValidationException("Units must be greater than 0");
		}
	}

	public Bike findBike(Long bikeId) throws InstanceNotFoundException { /* Caso de uso 3 */
		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.find(connection, bikeId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Bike> findBikes(String keywords, Calendar initialDate) { /* Caso de uso 4 */
		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.findByKeywords(connection, keywords, initialDate);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Bike addBike(Bike bike) throws InputValidationException { /* Caso de uso 1 */
		validateBike(bike);
		Calendar date = Calendar.getInstance();
		bike.setCreationDate(date);

		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				bike.setAverage((float) 0);
				bike.setReviewNumber((long) 0);

				Bike createdBike = bikeDao.create(connection, bike);

				connection.commit();

				return createdBike;
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

	public Rent rentBike(Long bikeId, String userId, String creditCardNumber, Long units, Calendar initialDate,
			Calendar expirationDate) throws InstanceNotFoundException, InputValidationException,
			NotEnoughUnitsException, MoreThanFifteenDaysException, OutOfDateException, BikeNotAvailableYetException {
		/* Caso de uso 5 */
		
		validateRent(units, creditCardNumber, initialDate, expirationDate);
		
		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				
				Bike bike = bikeDao.find(connection, bikeId);
				Calendar now = Calendar.getInstance();
				now.set(Calendar.MILLISECOND, 0);
				now.set(Calendar.SECOND, 0);
				now.set(Calendar.MINUTE, 0);
				now.set(Calendar.HOUR, 0);
				if (bike.getUnits() < units) {
					throw new NotEnoughUnitsException(bike.getBikeId(), bike.getUnits());
				} else if ((calculateDays(initialDate, expirationDate)) > 15) {
					throw new MoreThanFifteenDaysException(bike.getBikeId());
				} else if (bike.getFirstDate().after(initialDate)) {
					throw new BikeNotAvailableYetException(bike.getBikeId(), bike.getFirstDate());
				} else if (lessThanADay(now, initialDate)) {
					throw new OutOfDateException();
				} else {
					Calendar creationDate = Calendar.getInstance();
					Rent rent = rentDao.create(connection, new Rent(bikeId, userId, creditCardNumber, initialDate,
							expirationDate, units, creationDate));

					connection.commit();
					return rent;
				}
			} catch (InstanceNotFoundException | NotEnoughUnitsException | MoreThanFifteenDaysException
					| OutOfDateException | BikeNotAvailableYetException e) {
				connection.commit();
				throw e;
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

	public void punctuate(Long rentalId, float score, String userId)
			throws InputValidationException, InstanceNotFoundException, AlreadyRatedException, RentNotYetException { /* Caso de uso 7 */

		PropertyValidator.validateDouble("score", score, 0, 10);

		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				Rent rent = rentDao.find(connection, rentalId);
				if (rent.getUserId().equals(userId)) {
					if (rent.getPunctuation() == -1) {
						if ((Calendar.getInstance()).after(rent.getExpirationDate())) {
							rent.setPunctuation(score);
							rentDao.update(connection, rent);
							Long bikeId = rent.getBikeId();
							Bike bike = bikeDao.find(connection, bikeId);
							Long number = bike.getReviewNumber();
							Long newNumber = number;
							bike.setReviewNumber(++newNumber);
							float average = bike.getAverage();
							bike.setAverage((average * number + score) / newNumber);
							bikeDao.update(connection, bike);
							connection.commit();
						} else {
							throw new RentNotYetException(rent.getRentalId(), rent.getExpirationDate());
						}
					} else {
						throw new AlreadyRatedException(rentalId);
					}
				} else {
					throw new InputValidationException("Wrong user");
				}

			} catch (InstanceNotFoundException | AlreadyRatedException | InputValidationException e) {
				connection.commit();
				throw e;
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);

		}
	}

	public void updateBike(Bike bike) throws InputValidationException, InstanceNotFoundException {
		validateBike(bike); /* Caso de uso 2 */

		try (Connection connection = dataSource.getConnection()) {
			try {
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				Bike bikePast = bikeDao.find(connection, bike.getBikeId());
				bike.setAverage(bikePast.getAverage());
				bike.setReviewNumber(bikePast.getReviewNumber());

				if (!rentDao.rentExists(connection, bikePast.getBikeId())
						|| bikePast.getFirstDate().after(bike.getFirstDate())) {
					bikeDao.update(connection, bike);
					connection.commit();
				} else {
					throw new InputValidationException("Solo se permite atrasar la fecha");
				}

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
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

	public List<Rent> findRents(String userId) { /* Caso de uso 6 */
		try (Connection connection = dataSource.getConnection()) {
			return rentDao.find(connection, userId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
