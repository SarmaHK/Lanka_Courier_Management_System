package com.lankacourier.Service;

import com.lankacourier.DAO.CustomerMobileNumberDAO;
import com.lankacourier.Model.CustomerMobileNumber;

import java.util.List;

public class CustomerMobileNumberService {

    private final CustomerMobileNumberDAO dao = new CustomerMobileNumberDAO();

    public boolean addMobile(CustomerMobileNumber m) {
        return m != null && m.getCustomerId() > 0 && dao.addMobile(m);
    }

    public boolean deleteMobile(int id) {
        return id > 0 && dao.deleteMobile(id);
    }

    public List<CustomerMobileNumber> getMobilesForCustomer(int customerId) {
        return customerId > 0 ? dao.getMobilesForCustomer(customerId) : List.of();
    }

    public boolean updateMobile(CustomerMobileNumber m) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateMobile'");
    }
    
}
