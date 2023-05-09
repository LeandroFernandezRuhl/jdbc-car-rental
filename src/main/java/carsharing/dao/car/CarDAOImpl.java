package carsharing.dao.car;

import carsharing.dao.ConnectionFactory;
import carsharing.model.Car;

import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("ALL")
/**
 * The CarDAOImpl class implements the CarDAO interface and provides methods for interacting with the CAR table in the database.
 * The class defines methods for creating and dropping the table, retrieving cars by company ID, and inserting new cars into the table.
 */
public class CarDAOImpl implements CarDAO {
    // Object that creates the connection to the database
    private ConnectionFactory factory;

    // SQL code
    private static final String DROP_CAR_SQL = "DROP TABLE IF EXISTS CAR";
    private static final String CREATE_CAR_TABLE_SQL =
            "CREATE TABLE CAR " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    "name VARCHAR(255) UNIQUE NOT NULL , " +
                    "PRIMARY KEY ( id ), " +
                    "company_id INTEGER NOT NULL, " +
                    "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES COMPANY(id))";
    private static final String INSERT_CAR_SQL =
            "INSERT INTO CAR(name,company_id) VALUES(?,?)";

    public CarDAOImpl(String fileName) {
        this.factory = new ConnectionFactory(fileName);
    }

    /**
     * Drops the CAR table in the database.
     * @return true if the table was successfully dropped, false otherwise
     */
    @Override
    public boolean dropCarTable() {
        try (Connection connection = factory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE CUSTOMER DROP CONSTRAINT fk_car");
            statement.executeUpdate(DROP_CAR_SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new 'CAR' table in the database. This method executes an SQL query that creates a table
     * with columns for 'id' (an auto-incremented primary key), 'name' (a unique non-null string), and 'company_id'
     * (a non-null foreign key to the 'COMPANY' table).
     * @return true if the table was successfully created, false if an error occurred.
     * @throws SQLException if an error occurs while executing the SQL query
     */
    @Override
    public boolean createCarTable() {
        try (Connection connection = factory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_CAR_TABLE_SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all cars associated with a given company ID from the database.
     * The cars are ordered by ID.
     * @param companyId the ID of the company whose cars are to be retrieved.
     * @return an ArrayList containing all the cars associated with the given company ID,
     * or null if an SQLException occurred
     */
    @Override
    public ArrayList<Car> getCarsByCompanyId(Integer companyId) {
        String sql = "SELECT * FROM CAR WHERE company_id = ? ORDER BY id";
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, companyId);
            ResultSet rs = statement.executeQuery();
            ArrayList<Car> cars = new ArrayList<>();
            while (rs.next()) {
                Car car = new Car(rs.getInt("id"), rs.getString("name"));
                cars.add(car);
            }
            return cars;
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Car> getNotRentedCarsByCompanyId(Integer companyId) {
        String sql = "SELECT * FROM CAR WHERE company_id = ? AND id NOT IN (SELECT rented_car_id FROM CUSTOMER WHERE rented_car_id IS NOT NULL)";

        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setInt(1, companyId);
            ResultSet rs = statement.executeQuery();
            ArrayList<Car> cars = new ArrayList<>();
            while (rs.next()) {
                Car car = new Car(rs.getInt("id"), rs.getString("name"));
                cars.add(car);
            }
            return cars;
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }

    /**
     * Inserts a new car into the database with the specified company ID and name.
     * @param companyId the ID of the company that the car belongs to
     * @param carName the name of the car
     * @return true if the car was successfully inserted into the database, false otherwise
     * @throws IllegalArgumentException if the carName parameter is null or empty
     */
    @Override
    public boolean insertCar(Integer companyId, String carName) throws IllegalArgumentException {
        if (carName == null || carName.isEmpty()) {
            throw new IllegalArgumentException("Car name is null or empty");
        }

        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_CAR_SQL)) {
            statement.setString(1, carName);
            statement.setInt(2, companyId);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting car failed, no rows affected.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
