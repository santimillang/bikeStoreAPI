package es.udc.ws.app.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlBikeDao implements SqlBikeDao {

	protected AbstractSqlBikeDao() {
	}

	@Override
	public Bike find(Connection connection, Long bikeId) throws InstanceNotFoundException {

		String queryString = "SELECT model, units, description, price, "
				+ "creationDate, firstDate, averageScore, reviewCount FROM Bike WHERE bikeId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			int i = 1;
			preparedStatement.setLong(i++, bikeId.longValue());

			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(bikeId, Bike.class.getName());
			}

			i = 1;
			String model = resultSet.getString(i++);
			Long units = resultSet.getLong(i++);
			String description = resultSet.getString(i++);
			float price = resultSet.getFloat(i++);
			Calendar creationDate = Calendar.getInstance();
			creationDate.setTime(resultSet.getTimestamp(i++));
			Calendar firstDate = Calendar.getInstance();
			firstDate.setTime(resultSet.getTimestamp(i++));
			Float averageScore = resultSet.getFloat(i++);
			Long reviewCount = resultSet.getLong(i++);

			return new Bike(bikeId, model, description, firstDate, price, units, creationDate, averageScore,
					reviewCount);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<Bike> findByKeywords(Connection connection, String keywords, Calendar firstDate) {

		String[] words = keywords.split(" ");
		String queryString = "SELECT bikeId, model, units, description, price, "
				+ "creationDate, firstDate, averageScore, reviewCount FROM Bike";
		if (words.length > 0) {
			queryString += " WHERE";

			for (int i = 0; i < words.length; i++) {
				if (i > 0) {
					queryString += " AND";
				}
				queryString += " LOWER(description) LIKE LOWER(?)";
			}
			if (firstDate != null) {
				queryString += " AND firstDate <= ?";
			}
		} else {
			queryString += " WHERE firstDate <= ?";
		}
		queryString += " ORDER BY model";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			if (words.length > 0) {
				/* Fill "preparedStatement". */
				for (int i = 0; i < words.length; i++) {
					preparedStatement.setString(i + 1, "%" + words[i] + "%");
				}
				if (firstDate != null) {
					Timestamp date = new Timestamp(firstDate.getTime().getTime());
					preparedStatement.setTimestamp(words.length + 1, date);
				}
			} else if (firstDate != null){
				Timestamp date = new Timestamp(firstDate.getTime().getTime());
				preparedStatement.setTimestamp( 1, date);
			}

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			/* Read movies. */
			List<Bike> bikes = new ArrayList<Bike>();

			while (resultSet.next()) {

				int i = 1;
				Long bikeId = new Long(resultSet.getLong(i++));
				String model = resultSet.getString(i++);
				short units = resultSet.getShort(i++);
				String description = resultSet.getString(i++);
				float price = resultSet.getFloat(i++);
				Calendar creationDate = Calendar.getInstance();
				creationDate.setTime(resultSet.getTimestamp(i++));
				Calendar firstDay = Calendar.getInstance();
				firstDay.setTime(resultSet.getTimestamp(i++));
				float averageScore = resultSet.getFloat(i++);
				Long reviewCount = resultSet.getLong(i++);

				bikes.add(new Bike(bikeId, model, description, firstDay, price, units, creationDate, averageScore,
						reviewCount));

			}

			/* Return movies. */
			return bikes;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void update(Connection connection, Bike bike) throws InstanceNotFoundException {
		String queryString = "UPDATE Bike SET model = ?, units = ?, description = ?, price = ?, firstDate = ?,"
				+ " averageScore = ?, reviewCount = ? WHERE bikeId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			int i = 1;
			preparedStatement.setString(i++, bike.getModel());
			preparedStatement.setLong(i++, bike.getUnits());
			preparedStatement.setString(i++, bike.getDescription());
			preparedStatement.setFloat(i++, bike.getPrice());
			Timestamp date = new Timestamp(bike.getFirstDate().getTime().getTime());
			preparedStatement.setTimestamp(i++, date);
			preparedStatement.setFloat(i++, bike.getAverage());
			preparedStatement.setLong(i++, bike.getReviewNumber());
			preparedStatement.setLong(i++, bike.getBikeId());

			int updatedRows = preparedStatement.executeUpdate();

			if (updatedRows == 0) {
				throw new InstanceNotFoundException(bike.getBikeId(), Bike.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void remove(Connection connection, Long bikeId) throws InstanceNotFoundException {
		String queryString = "DELETE FROM Bike WHERE bikeId = ?";
		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
			int i = 1;
			preparedStatement.setLong(i++, bikeId);
			int removedRows = preparedStatement.executeUpdate();

			if (removedRows == 0) {
				throw new InstanceNotFoundException(bikeId, Bike.class.getName());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
