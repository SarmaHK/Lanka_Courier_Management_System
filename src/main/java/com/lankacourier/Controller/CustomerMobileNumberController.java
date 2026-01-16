package com.lankacourier.Controller;

import com.lankacourier.Model.CustomerMobileNumber;
import com.lankacourier.Service.CustomerMobileNumberService;

import java.util.List;

public class CustomerMobileNumberController {

    private final CustomerMobileNumberService service =
            new CustomerMobileNumberService();

    // create a mobile number record
    public boolean create(CustomerMobileNumber m) {
        return service.addMobile(m);
    }

    // update existing mobile number  ✅ FIX
    public boolean update(CustomerMobileNumber m) {
        return service.updateMobile(m);
    }

    // delete by mobile_id
    public boolean delete(int mobileId) {
        return service.deleteMobile(mobileId);
    }

    // fetch all mobiles for a customer
    public List<CustomerMobileNumber> fetchForCustomer(int customerId) {
        return service.getMobilesForCustomer(customerId);
    }

    // check duplicate mobile
    public boolean existsForCustomer(int customerId, String mobile) {
        return fetchForCustomer(customerId)
                .stream()
                .anyMatch(m -> m.getMobileNo().equals(mobile));
    }
}
