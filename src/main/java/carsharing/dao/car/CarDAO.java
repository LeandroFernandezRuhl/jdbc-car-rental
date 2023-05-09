package carsharing.dao.car;

import carsharing.model.Car;
import carsharing.model.Company;

import java.util.ArrayList;

public interface CarDAO {
    boolean dropCarTable();
    boolean createCarTable();
    ArrayList<Car> getCarsByCompanyId(Integer companyId);
    public ArrayList<Car> getNotRentedCarsByCompanyId(Integer companyId);
    boolean insertCar(Integer companyId, String carName);
}
