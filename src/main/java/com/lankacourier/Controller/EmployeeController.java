package com.lankacourier.Controller;

import com.lankacourier.Model.Employee;
import com.lankacourier.Service.EmployeeService;

import java.util.List;

public class EmployeeController {
    private final EmployeeService service = new EmployeeService();

    public List<Employee> fetchAll() {
        return service.getAllEmployees();
    }

    public Employee findById(int id) {
        return service.getById(id);
    }

    public Employee findByUsername(String username) {
        return service.getByUsername(username);
    }

    public boolean create(Employee e) {
        return service.addEmployee(e);
    }

    public boolean update(Employee e) {
        return service.updateEmployee(e);
    }

    public boolean delete(int id) {
        return service.deleteEmployee(id);
    }
}
