package carsharing.dao.car;

import carsharing.model.Car;

import java.util.ArrayList;

public interface CarDAO {
    boolean dropCarTable();
    boolean createCarTable();
    ArrayList<Car> getCarsByCompanyId(Integer companyId);
    boolean insertCar(Integer companyId, String carName);
}
