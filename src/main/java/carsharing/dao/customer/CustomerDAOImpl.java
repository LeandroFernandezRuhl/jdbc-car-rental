package carsharing.dao.customer;

import carsharing.dao.ConnectionFactory;
import carsharing.model.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {
    private ConnectionFactory factory;
    private static final String DROP_CUSTOMER_SQL = "DROP TABLE IF EXISTS CUSTOMER";
    private static final String CREATE_CUSTOMER_TABLE_SQL =
            "CREATE TABLE CAR " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    "name VARCHAR(255) UNIQUE NOT NULL , " +
                    "PRIMARY KEY ( id ), " +
                    "car_id INTEGER NOT NULL, " +
                    "CONSTRAINT fk_company FOREIGN KEY (car_id) REFERENCES CAR(id))";
    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO CUSTOMER(name) VALUES(?)";
    public CustomerDAOImpl(ConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public boolean dropCustomerTable() {
        try (Connection connection = factory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_CUSTOMER_SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createCustomerTable() {
        try (Connection connection = factory.getConnection();
        Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_CUSTOMER_TABLE_SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<Customer> getAllCustomers() {
        return null;
    }

    @Override
    public boolean insertCustomer(String name) {
        return false;
    }

    @Override
    public boolean rentCar(Integer customerId, Integer carId) {
        return false;
    }

    @Override
    public boolean returnCar(Integer customerId) {
        return false;
    }

    @Override
    public boolean hasRentedCar(Integer customerId) {
        return false;
    }
}
