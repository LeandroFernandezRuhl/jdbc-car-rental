package carsharing.dao.customer;

import carsharing.dao.ConnectionFactory;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.*;
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
                    "CONSTRAINT fk_car FOREIGN KEY (car_id) REFERENCES CAR(id))";
    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO CUSTOMER(name) VALUES(?)";
    private static final String RENT_CAR_SQL =
            "UPDATE customer SET car_id = ? WHERE car_id IS NULL AND car_id != ? AND customer_id = ?";
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
        String sql = "SELECT * FROM CUSTOMER ORDER BY id";
        try (Connection connection = factory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            ArrayList<Customer> customers = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt("id"), rs.getString("name"));
                customers.add(customer);
            }
            return customers;
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean insertCustomer(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Customer name is null or empty");
        }

        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CUSTOMER_SQL)) {
            statement.setString(1, name);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting customer failed, no rows affected.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean rentCar(Integer customerId, Integer carId) {
        try (Connection connection = factory.getConnection();
        PreparedStatement statement = connection.prepareStatement(RENT_CAR_SQL);
        ) {
            statement.setInt(1, carId);
            statement.setInt(2, carId);
            statement.setInt(3, customerId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Renting car failed, no rows affected.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
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
