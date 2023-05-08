package carsharing.dao.company;

import carsharing.dao.ConnectionFactory;
import carsharing.model.Company;

import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("ALL")
/**
 * The CompanyDAOImpl class implements the CompanyDAO interface and provides methods for interacting with the COMPANY table in the database.
 * The class defines methods for creating and dropping the table, retrieving all companies from the table, and inserting new ones.
 */
public class CompanyDAOImpl implements CompanyDAO {
    // Object that creates the connection to the database
    private ConnectionFactory factory;

    // SQL code
    private static final String DROP_COMPANY_SQL = "DROP TABLE IF EXISTS COMPANY";
    private static final String CREATE_COMPANY_TABLE_SQL =
            "CREATE TABLE COMPANY " +
                    "(id INTEGER AUTO_INCREMENT, " +
                    "name VARCHAR(255) UNIQUE NOT NULL , " +
                    "PRIMARY KEY ( id ))";
    private static final String INSERT_COMPANY_SQL =
            "INSERT INTO COMPANY(name) VALUES(?)";

    /**
     * This constructor creates a new CompanyDAOImpl object with a given fileName that represents the name of the database.
     * The ConnectionFactory object is used to establish a connection to the database, which will be used to perform database operations.
     * @param fileName the name of the file that contains the database. It's used to create a new ConnectionFactory object
     */
    public CompanyDAOImpl(String fileName) {
        this.factory = new ConnectionFactory(fileName);
    }

    /**
     * Drops the COMPANY table in the database.
     * @return true if the table was successfully dropped, false otherwise
     */
    @Override
    public boolean dropCompanyTable() {
        try (Connection connection = this.factory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE CAR DROP CONSTRAINT FK_COMPANY");
            statement.executeUpdate(DROP_COMPANY_SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new 'COMPANY' table in the database. This method executes an SQL query that creates a table
     * with columns for 'id' (an auto-incremented primary key) and 'name' (a unique non-null string).
     * @return true if the table was successfully created, false if an error occurred.
     * @throws SQLException if an error occurs while executing the SQL query
     */
    @Override
    public boolean createCompanyTable() {
        try (Connection connection = this.factory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_COMPANY_TABLE_SQL);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all companies from the database, ordered by ID.
     * @return an ArrayList containing all the companies, or null if an SQLException occurred
     */
    @Override
    public ArrayList<Company> getAllCompanies() {
        String sql = "SELECT * FROM COMPANY ORDER BY id";
        try (Connection connection = this.factory.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            ArrayList<Company> companies = new ArrayList<Company>();
            while (rs.next()) {
                Company company = new Company(rs.getInt("id"), rs.getString("name"));
                companies.add(company);
            }
            return companies;
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
    }

    /**
     * Inserts a new company into the database with the specified name.
     * @param name the name of the company
     * @return true if the company was successfully inserted into the database, false otherwise
     * @throws IllegalArgumentException if the name parameter is null or empty
     */
    @Override
    public boolean insertCompany(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Company name is null or empty");
        }

        try (Connection connection = this.factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_COMPANY_SQL)) {
            statement.setString(1, name);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting company failed, no rows affected.");
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}