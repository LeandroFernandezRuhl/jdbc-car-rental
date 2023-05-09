package carsharing.dao.customer;

import carsharing.model.Customer;

import java.util.ArrayList;

public interface CustomerDAO {
    boolean dropCustomerTable();
    boolean createCustomerTable();
    ArrayList<Customer> getAllCustomers();
    boolean insertCustomer(String name);
    boolean rentCar(Integer customerId, Integer carId);
    boolean returnCar(Integer customerId);
    boolean hasRentedCar(Integer customerId);
    ArrayList<String> getRentedCarInfo(Integer customerId);

}
