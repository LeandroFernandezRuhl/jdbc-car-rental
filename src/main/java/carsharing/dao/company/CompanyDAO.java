package carsharing.dao.company;

import carsharing.model.Company;

import java.util.ArrayList;

public interface CompanyDAO {
    boolean dropCompanyTable();
    boolean createCompanyTable();
    ArrayList<Company> getAllCompanies();
    boolean insertCompany(String name);
}
