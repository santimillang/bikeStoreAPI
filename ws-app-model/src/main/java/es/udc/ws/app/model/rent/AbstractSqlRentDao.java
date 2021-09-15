package es.udc.ws.app.model.rent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.bike.Bike;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlRentDao implements SqlRentDao {
	protected AbstractSqlRentDao() {
	}

	@Override
	public List<Rent> find(Connection connection, String userId) {

		/* Create "queryString". */
		String queryString = "SELECT rentalId, bikeId, initialDate, expirationDate,"
				+ " creditCardNumber, units, saleDate, score FROM Rent WHERE userId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setString(i++, userId);

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			List<Rent> rents = new ArrayList<Rent>();
			/* Get results. */
			while (resultSet.next()) {
				i = 1;
				Long rentalId = resultSet.getLong(i++);
				Long bikeId = resultSet.getLong(i++);
				Calendar initialDate = Calendar.getInstance();
				initialDate.setTime(resultSet.getTimestamp(i++));
				Calendar expirationDate = Calendar.getInstance();
				expirationDate.setTime(resultSet.getTimestamp(i++));
				String creditCardNumber = resultSet.getString(i++);
				Long units = resultSet.getLong(i++);
				Calendar saleDate = Calendar.getInstance();
				saleDate.setTime(resultSet.getTimestamp(i++));
				Float score = resultSet.getFloat(i++);

				/* Return sale. */

				rents.add(new Rent(rentalId, bikeId, userId, creditCardNumber, initialDate, expirationDate, units,
						saleDate, score));
			}
			return rents;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(Connection connection, Rent rent) throws InstanceNotFoundException {

		/* Create "queryString". */
		String queryString = "UPDATE Rent" + " SET bikeId = ?, userId = ?, initialDate = ?, expirationDate = ?,"
				+ " creditCardNumber = ?, units = ?, score = ? WHERE rentalId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, rent.getBikeId());
			preparedStatement.setString(i++, rent.getUserId());
			Timestamp date = rent.getInitialDate() != null ? new Timestamp(rent.getInitialDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, date);
			Timestamp dateExpiration = rent.getExpirationDate() != null
					? new Timestamp(rent.getExpirationDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, dateExpiration);
			preparedStatement.setString(i++, rent.getCreditCardNumber());
			preparedStatement.setLong(i++, rent.getUnits());
			preparedStatement.setFloat(i++, rent.getPunctuation());
			preparedStatement.setLong(i++, rent.getRentalId());

			/* Execute query. */
			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(rent.getBikeId(), Rent.class.getName());
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void remove(Connection connection, Long rentalId) throws InstanceNotFoundException {

		/* Create "queryString". */
		String queryString = "DELETE FROM Rent WHERE" + " rentalId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, rentalId);

			/* Execute query. */
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(rentalId, Rent.class.getName());
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public Rent find(Connection connection, Long rentalId) throws InstanceNotFoundException {

		String queryString = "SELECT bikeId, userId, initialDate, expirationDate, "
				+ "creditCardNumber, units, saleDate, score FROM Rent WHERE rentalId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			int i = 1;
			preparedStatement.setLong(i++, rentalId.longValue());

			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(rentalId, Bike.class.getName());
			}

			i = 1;
			Long bikeId = resultSet.getLong(i++);
			String userId = resultSet.getString(i++);
			Calendar initialDate = Calendar.getInstance();
			initialDate.setTime(resultSet.getTimestamp(i++));
			Calendar expirationDate = Calendar.getInstance();
			expirationDate.setTime(resultSet.getTimestamp(i++));
			String creditCardNumber = resultSet.getString(i++);
			Long units = resultSet.getLong(i++);
			Calendar saleDate = Calendar.getInstance();
			saleDate.setTime(resultSet.getTimestamp(i++));
			Float score = resultSet.getFloat(i++);

			/* Return sale. */

			return (new Rent(rentalId, bikeId, userId, creditCardNumber, initialDate, expirationDate, units, saleDate,
					score));

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Rent> findByBikeId(Connection connection, Long bikeId) {

		/* Create "queryString". */
		String queryString = "SELECT rentalId, userId, initialDate, expirationDate,"
				+ " creditCardNumber, units, saleDate, score FROM Rent WHERE bikeId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, bikeId);

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			List<Rent> rents = new ArrayList<Rent>();
			/* Get results. */
			while (resultSet.next()) {
				i = 1;
				Long rentalId = resultSet.getLong(i++);
				String userId = resultSet.getString(i++);
				Calendar initialDate = Calendar.getInstance();
				initialDate.setTime(resultSet.getTimestamp(i++));
				Calendar expirationDate = Calendar.getInstance();
				expirationDate.setTime(resultSet.getTimestamp(i++));
				String creditCardNumber = resultSet.getString(i++);
				Long units = resultSet.getLong(i++);
				Calendar saleDate = Calendar.getInstance();
				saleDate.setTime(resultSet.getTimestamp(i++));
				Float score = resultSet.getFloat(i++);

				/* Return sale. */

				rents.add(new Rent(rentalId, bikeId, userId, creditCardNumber, initialDate, expirationDate, units,
						saleDate, score));
			}
			return rents;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	public boolean rentExists(Connection connection, Long bikeId) {
		/* Create "queryString". */
		String queryString = "SELECT bikeId FROM Bike WHERE EXISTS (SELECT rentalId FROM Rent WHERE bikeId = ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, bikeId);

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
}
