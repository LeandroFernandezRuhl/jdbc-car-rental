package carsharing.menu;

import carsharing.dao.car.CarDAO;
import carsharing.dao.company.CompanyDAO;
import carsharing.dao.customer.CustomerDAO;

import java.util.Scanner;

public class MainMenu {
    Scanner scanner;
    CompanyDAO companyDAO;
    CarDAO carDAO;
    CustomerDAO customerDAO;

    public MainMenu(Scanner scanner, CompanyDAO companyDAO, CarDAO carDAO, CustomerDAO customerDAO) {
        this.scanner = scanner;
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
    }

    public void runMainMenu() {
        boolean running = true;
        ManagerMenu managerMenu = new ManagerMenu(scanner, companyDAO, carDAO, customerDAO);
        CustomerMenu customerMenu = new CustomerMenu(scanner, companyDAO, carDAO, customerDAO);
        while (running) {
            System.out.println("""
                    1. Log in as a manager
                    2. Log in as a customer
                    3. Create a customer
                    0. Exit
                    """);
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> managerMenu.runCompanyMenu();
                case "2" -> customerMenu.runChooseCustomerMenu();
                case "3" -> runCreateCustomerMenu();
                case "0" -> running = false;
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private void runCreateCustomerMenu() {
        System.out.println("Enter the customer name:");
        String customerName = scanner.nextLine();
        try {
            boolean isCreated = customerDAO.insertCustomer(customerName);
            if (isCreated) {
                System.out.println("The customer was added!");
            } else {
                System.out.println("The customer wasn't added!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
