package es.udc.ws.app.model.rent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlRentDao extends AbstractSqlRentDao{

	@Override
    public Rent create(Connection connection, Rent rent) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Rent"
                + " (bikeId, userId, initialDate, expirationDate, CreditCardNumber,"
                + " units, saleDate, score) VALUES (?, ?, ?, ?, ?, ?, ?, -1)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, rent.getBikeId());
            preparedStatement.setString(i++, rent.getUserId());
            Timestamp date = new Timestamp(rent.getInitialDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, date);
            Timestamp dateEx = new Timestamp(rent.getExpirationDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, dateEx);
            preparedStatement.setString(i++, rent.getCreditCardNumber());
            preparedStatement.setLong(i++, rent.getUnits());
            Timestamp saleDate = new Timestamp(rent.getRentalTime().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, saleDate);

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long rentalId = resultSet.getLong(1);

            /* Return sale. */
            return new Rent(rentalId, rent.getBikeId(), rent.getUserId(),
                    rent.getCreditCardNumber(), rent.getInitialDate(), rent.getExpirationDate(),
                    rent.getUnits(), rent.getRentalTime());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
