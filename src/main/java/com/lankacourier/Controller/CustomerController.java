package com.lankacourier.Controller;

import com.lankacourier.Model.Customer;
import com.lankacourier.Service.CustomerService;

import java.util.List;

public class CustomerController {

    private final CustomerService service = new CustomerService();

    public List<Customer> fetchAll() {
        return service.getAllCustomers();
    }

    public Customer findById(int id) {
        return service.getById(id);
    }

    public boolean create(Customer c) {
        return service.addCustomer(c);
    }

    public boolean update(Customer c) {
        return service.updateCustomer(c);
    }

    public boolean delete(int id) {
        return service.deleteCustomer(id);
    }

    public Customer findByName(String name) {
        for (Customer c : fetchAll()) {
            if (c.toString().equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }

    public Customer findByMobile(String mobile) {
        return service.findByMobile(mobile);
    }

    public Customer findByNic(String nic) {
        return service.findByNic(nic);
    }

    public Customer findOrCreate(Customer c) {
        return service.findOrCreate(c);
    }

    public int getNextId() {
        return service.getNextId();
    }
}
