package carsharing.dao.customer;

import carsharing.dao.ConnectionFactory;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class CustomerDAOImpl implements CustomerDAO {
    private ConnectionFactory factory;
    private static final String DROP_CUSTOMER_SQL = "DROP TABLE IF EXISTS CUSTOMER";
    private static final String CREATE_CUSTOMER_TABLE_SQL =
            "CREATE TABLE CUSTOMER " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    "name VARCHAR(255) UNIQUE NOT NULL , " +
                    "PRIMARY KEY ( id ), " +
                    "rented_car_id INTEGER, " +
                    "CONSTRAINT fk_car FOREIGN KEY (rented_car_id) REFERENCES CAR(id))";
    private static final String INSERT_CUSTOMER_SQL =
            "INSERT INTO CUSTOMER(name) VALUES(?)";
    private static final String RENT_CAR_SQL =
            "UPDATE CUSTOMER SET rented_car_id = ? WHERE rented_car_id IS NULL AND id = ?";
    private static final String RETURN_CAR_SQL =
            "UPDATE CUSTOMER SET rented_car_id = null WHERE id = ?";
    private static final String GET_RENTED_CAR_SQL =
            "SELECT COMPANY.name AS company_name, CAR.name AS car_name FROM CUSTOMER INNER JOIN CAR ON " +
                    "CUSTOMER.rented_car_id = CAR.id INNER JOIN COMPANY ON CAR.company_id = COMPANY.id WHERE ? = CUSTOMER.id";
    private static final String HAS_RENTED_CAR_SQL =
            "SELECT EXISTS(SELECT 1 FROM customer WHERE id = ? AND rented_car_id IS NOT NULL)";

    public CustomerDAOImpl(String fileName) {
        this.factory = new ConnectionFactory(fileName);
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
             //"UPDATE CUSTOMER SET car_id = ? WHERE car_id IS NULL AND id = ?";
        ) {
            statement.setInt(1, carId);
            statement.setInt(2, customerId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Renting car failed, no rows affected.");
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean returnCar(Integer customerId) {
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(RETURN_CAR_SQL)) {
            statement.setInt(1, customerId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Returning car failed, no rows affected.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ArrayList<String> getRentedCarInfo(Integer customerId) {
        try (Connection connection = factory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_RENTED_CAR_SQL)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            ArrayList<String> carInfo = new ArrayList<>();
            rs.next();
            String carName = rs.getString("car_name");
            String companyName = rs.getString("company_name");
            carInfo.add(carName);
            carInfo.add(companyName);
            return carInfo;
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean hasRentedCar(Integer customerId) {
        try (Connection connection = factory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(HAS_RENTED_CAR_SQL)) {
            //"SELECT EXISTS(SELECT 1 FROM customer WHERE id = ? AND car_id IS NOT NULL)"
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
