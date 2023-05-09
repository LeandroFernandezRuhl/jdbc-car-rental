package carsharing;

import carsharing.dao.car.CarDAO;
import carsharing.dao.car.CarDAOImpl;
import carsharing.dao.company.CompanyDAO;
import carsharing.dao.company.CompanyDAOImpl;
import carsharing.dao.customer.CustomerDAO;
import carsharing.dao.customer.CustomerDAOImpl;
import carsharing.menu.MainMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Initialize database
        String databaseFileName = "carsharing.mv.db";
      /*  if (args[0].equals("-databaseFileName") &&
                args.length > 1) {
            databaseFileName = args[1];
        } */
        CompanyDAO companyDAO = new CompanyDAOImpl(databaseFileName);
        companyDAO.dropCompanyTable();
        companyDAO.createCompanyTable();

        CarDAO carDAO = new CarDAOImpl(databaseFileName);
        carDAO.dropCarTable();
        carDAO.createCarTable();

        CustomerDAO customerDAO = new CustomerDAOImpl(databaseFileName);
        customerDAO.dropCustomerTable();
        customerDAO.createCustomerTable();

        // Main menu
        Scanner scanner = new Scanner(System.in);
        MainMenu mainMenu = new MainMenu(scanner, companyDAO, carDAO, customerDAO);
        mainMenu.runMainMenu();
        scanner.close();
    }
}