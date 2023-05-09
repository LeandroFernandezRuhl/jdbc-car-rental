package carsharing.menu;

import carsharing.dao.car.CarDAO;
import carsharing.dao.company.CompanyDAO;
import carsharing.dao.customer.CustomerDAO;
import carsharing.model.Car;
import carsharing.model.Company;
import carsharing.model.Customer;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CustomerMenu {
    Scanner scanner;
    CompanyDAO companyDAO;
    CarDAO carDAO;
    CustomerDAO customerDAO;

    private final String customerPersonalMenu = """
            1. Rent a car
            2. Return a rented car
            3. My rented car
            0. Back
                    """;

    public CustomerMenu(Scanner scanner, CompanyDAO companyDAO, CarDAO carDAO, CustomerDAO customerDAO) {
        this.scanner = scanner;
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
        this.customerDAO = customerDAO;
    }

    public void runChooseCustomerMenu() {
        ArrayList<Customer> customers = customerDAO.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            System.out.println("Choose a customer:");
            int i = 1;
            for (Customer customer : customers) {
                System.out.println(i + ". " + customer.getName());
                i++;
            }
            System.out.println("0. Back");
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                if (option != 0) {
                    Customer customer = customers.get(option-1);
                    runCustomerPersonalMenu(customer.getId());
                }
            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                System.out.println("Invalid ID!");
            }
        }
    }

    private void runCustomerPersonalMenu(Integer customerId) {
        boolean running = true;
        while (running) {
            System.out.println(customerPersonalMenu);
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> {
                    if (!customerDAO.hasRentedCar(customerId)) {
                        runChooseCompanyMenu(customerId);
                    } else {
                        System.out.println("You've already rented a car!");
                    }
                }
                case "2" -> {
                    if (customerDAO.hasRentedCar(customerId)) {
                        boolean returnedSuccessfully = customerDAO.returnCar(customerId);
                        if (returnedSuccessfully) {
                            System.out.println("You've returned a rented car!");
                        }
                    } else {
                        System.out.println("You didn't rent a car!");
                    }
                }
                case "3" -> {
                    if (customerDAO.hasRentedCar(customerId)) {
                        ArrayList<String> carInfo = customerDAO.getRentedCarInfo(customerId);
                        System.out.println("Your rented car:");
                        System.out.println(carInfo.get(1));
                        System.out.println("Company: ");
                        System.out.println(carInfo.get(0));
                    } else {
                        System.out.println("You didn't rent a car!");
                    }
                }
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void runChooseCompanyMenu(Integer customerId) {
        ArrayList<Company> companies = companyDAO.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose a company:");
            int i = 1;
            for (Company company : companies) {
                System.out.println(i + ". " + company.getName());
                i++;
            }
            System.out.println("0. Back");
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                if (option != 0) {
                    Company company = companies.get(option-1);
                    runChooseCarMenu(company, customerId);
                }
            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                System.out.println("Invalid ID!");
            }
        }
    }

    private void runChooseCarMenu(Company company, Integer customerId) {
        ArrayList<Car> cars = carDAO.getNotRentedCarsByCompanyId(company.getId());
        if (cars.isEmpty()) {
            System.out.println("No available cars in the '" + company.getName() + "' company");
        } else {
            System.out.println("Choose a car:");
            int i = 1;
            for (Car car : cars) {
                System.out.println(i + ". " + car.getName());
                i++;
            }
            System.out.println("0. Back");
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                if (option != 0) {
                    Car car = cars.get(option-1);
                    boolean rentedSuccessfully = customerDAO.rentCar(customerId,car.getId());
                    if (rentedSuccessfully) {
                        System.out.println("You rented '" + car.getName() + "'");
                    }
                }
            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                System.out.println("Invalid ID!");
            }
        }
    }
}
