package carsharing;

import carsharing.dao.car.CarDAO;
import carsharing.dao.company.CompanyDAO;
import carsharing.model.Car;
import carsharing.model.Company;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CarRentalMenu {
    Scanner scanner;
    CompanyDAO companyDAO;
    CarDAO carDAO;
    String companyMenu = """
                    1. Company list
                    2. Create a company
                    0. Back
                    """;
    String companyCarsMenu = """
            1. Car list
            2. Create a car
            0. Back
            """;

    public CarRentalMenu(Scanner scanner, CompanyDAO companyDAO, CarDAO carDAO) {
        this.scanner = scanner;
        this.companyDAO = companyDAO;
        this.carDAO = carDAO;
    }

    public void runMenu() {
        boolean running = true;
        while (running) {
            System.out.println("""
                    1. Log in as a manager
                    0. Exit
                    """);
            String option = scanner.nextLine();
            if (option.equals("1")) {
                runCompanyMenu();
            } else if (option.equals("0")) {
                running = false;
            } else {
                System.out.println("Invalid option!");
            }
        }
    }

    private void runCompanyMenu() {
        boolean running = true;
        while (running) {
            System.out.println(companyMenu);
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> runChooseCompanyMenu();
                case "2" -> runCreateCompanyMenu();
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void runChooseCompanyMenu() {
        ArrayList<Company> companies = companyDAO.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose the company:");
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
                    runCompanyCarsMenu(company.getId(), company.getName());
                }
            } catch (InputMismatchException | IndexOutOfBoundsException e) {
                System.out.println("Invalid ID!");
            }
        }
    }

    private void runCreateCompanyMenu() {
        System.out.println("Enter the company name:");
        String companyName = scanner.nextLine();
        try {
            boolean isCreated = companyDAO.insertCompany(companyName);
            if (isCreated) {
                System.out.println("The company was created!");
            } else {
                System.out.println("The company wasn't created!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void runCompanyCarsMenu(Integer companyId, String companyName) {
        boolean running = true;
        while (running) {
            System.out.println("'" + companyName + "': company");
            System.out.println(companyCarsMenu);
            String option = scanner.nextLine();
            switch (option) {
                case "1" -> {
                    ArrayList<Car> cars = carDAO.getCarsByCompanyId(companyId);
                    if (cars.isEmpty()) {
                        System.out.println("The car list is empty!");
                    } else {
                        int i = 1;
                        for (Car car : cars) {
                            System.out.println(i + ". " + car.getName());
                            i++;
                        }
                    }
                }
                case "2" -> runCreateCarMenu(companyId);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }


    private void runCreateCarMenu(Integer companyId) {
        System.out.println("Enter the car name:");
        String carName = scanner.nextLine();
        try {
            boolean isCreated = carDAO.insertCar(companyId, carName);
            if (isCreated) {
                System.out.println("The car was added!");
            } else {
                System.out.println("The car wasn't added!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
