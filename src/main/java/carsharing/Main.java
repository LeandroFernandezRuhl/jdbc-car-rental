package carsharing;

import carsharing.dao.car.CarDAO;
import carsharing.dao.car.CarDAOImpl;
import carsharing.dao.company.CompanyDAO;
import carsharing.dao.company.CompanyDAOImpl;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Initialize database
        String databaseFileName = "carsharing.mv.carsharing.db";
       /* if (args[0].equals("-databaseFileName") &&
                args.length > 1) {
            databaseFileName = args[1];
        } */
        CompanyDAO companyDAO = new CompanyDAOImpl(databaseFileName);
        companyDAO.dropCompanyTable();
        companyDAO.createCompanyTable();

        CarDAO carDAO = new CarDAOImpl(databaseFileName);
        carDAO.dropCarTable();
        carDAO.createCarTable();

        // carsharing.Main menu
        Scanner scanner = new Scanner(System.in);
        CarRentalMenu carRentalMenu = new CarRentalMenu(scanner, companyDAO, carDAO);
        carRentalMenu.runMenu();
        scanner.close();
    }
}