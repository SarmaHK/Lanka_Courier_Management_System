package com.lankacourier.Service;

import com.lankacourier.DAO.CustomerDAO;
import com.lankacourier.Model.Customer;

import java.util.List;

public class CustomerService {

    private final CustomerDAO dao = new CustomerDAO();

    public List<Customer> getAllCustomers() {
        return dao.getAllCustomers();
    }

    public Customer getById(int id) {
        return id > 0 ? dao.getCustomerById(id) : null;
    }

    public boolean addCustomer(Customer c) {
        return c != null && dao.addCustomer(c);
    }

    public boolean updateCustomer(Customer c) {
        return c != null && c.getCustomerId() > 0 && dao.updateCustomer(c);
    }

    public boolean deleteCustomer(int id) {
        return id > 0 && dao.deleteCustomer(id);
    }

    public Customer findByMobile(String mobile) {
        return dao.findByMobile(mobile);
    }

    public Customer findByNic(String nic) {
        return dao.findByNic(nic);
    }

    public Customer findOrCreate(Customer c) {
        // Try lookup by Mobile or NIC
        Customer existing = null;
        if (c.getMobileNo() != null && !c.getMobileNo().isEmpty()) {
            existing = dao.findByMobile(c.getMobileNo());
        }
        if (existing == null && c.getContactPersonNic() != null && !c.getContactPersonNic().isEmpty()) {
            existing = dao.findByNic(c.getContactPersonNic());
        }

        if (existing != null)
            return existing;

        // No match, create new
        if (addCustomer(c)) {
            return c; // ID should be set by addCustomer
        }
        return null; // Failed to create
    }

    public int getNextId() {
        return dao.getNextCustomerId();
    }
}
