package es.udc.ws.app.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlBikeDao extends AbstractSqlBikeDao {

	@Override
	public Bike create(Connection connection, Bike bike) {
		String queryString = "INSERT INTO Bike (model, units, description, price, creationDate, "
				+ "firstDate, averageScore, reviewCount)" + " VALUES (?,?,?,?,?,?,?,?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
				Statement.RETURN_GENERATED_KEYS)) {

			int i = 1;
			preparedStatement.setString(i++, bike.getModel());
			preparedStatement.setFloat(i++, bike.getUnits());
			preparedStatement.setString(i++, bike.getDescription());
			preparedStatement.setFloat(i++, bike.getPrice());
			Timestamp date = bike.getCreationDate() != null ? new Timestamp(bike.getCreationDate().getTime().getTime())
					: null;
			preparedStatement.setTimestamp(i++, date);
			Timestamp dateFirst = new Timestamp(bike.getFirstDate().getTime().getTime());
			preparedStatement.setTimestamp(i++, dateFirst);
			preparedStatement.setFloat(i++, 0F);
			preparedStatement.setLong(i++, (long) 0);

			preparedStatement.executeUpdate();

			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException("JDBC driver did not return the key.");
			}
			Long bikeId = resultSet.getLong(1);

			return new Bike(bikeId, bike.getModel(), bike.getDescription(), bike.getFirstDate(), bike.getPrice(),
					bike.getUnits(), bike.getCreationDate(), (float) 0, (long) 0);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}

}
