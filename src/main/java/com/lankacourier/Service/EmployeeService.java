package com.lankacourier.Service;

import com.lankacourier.DAO.EmployeeDAO;
import com.lankacourier.Model.Employee;

import java.util.List;

public class EmployeeService {
    private final EmployeeDAO dao = new EmployeeDAO();

    public List<Employee> getAllEmployees() {
        return dao.getAllEmployees();
    }

    public Employee getById(int id) {
        if (id <= 0) return null;
        return dao.getEmployeeById(id);
    }

    public Employee getByUsername(String username) {
        if (username == null || username.isBlank()) return null;
        return dao.getByUsername(username);
    }

    public boolean addEmployee(Employee e) {
        if (e == null) return false;
        if (e.getFirstName() == null || e.getFirstName().isBlank()) return false;
        if (e.getLastName() == null || e.getLastName().isBlank()) return false;
        if (e.getUsername() == null || e.getUsername().isBlank()) return false;
        // NOTE: password hashing should be done before calling this service
        return dao.addEmployee(e);
    }

    public boolean updateEmployee(Employee e) {
        if (e == null || e.getEmployeeId() <= 0) return false;
        if (e.getUsername() == null || e.getUsername().isBlank()) return false;
        return dao.updateEmployee(e);
    }

    public boolean deleteEmployee(int id) {
        if (id <= 0) return false;
        return dao.deleteEmployee(id);
    }
}
