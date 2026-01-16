package com.lankacourier.UI.dialogs;

import com.lankacourier.Controller.CustomerController;
import com.lankacourier.Controller.CustomerMobileNumberController;
import com.lankacourier.Model.Customer;
import com.lankacourier.Model.CustomerMobileNumber;


import javax.swing.*;
import java.awt.*;

public class ParcelDialog extends JDialog {

    private JComboBox<Customer> cbSender;
    private JComboBox<String> cbSenderMobile;

    private JComboBox<Customer> cbReceiver;
    private JComboBox<String> cbReceiverMobile;

    private final CustomerController customerController =
            new CustomerController();
    private final CustomerMobileNumberController mobileController =
            new CustomerMobileNumberController();

    public ParcelDialog(Frame parent) {
        super(parent, "Register Parcel", true);
        setSize(540, 320);
        setLocationRelativeTo(parent);

        cbSender = createCustomerBox();
        cbReceiver = createCustomerBox();

        cbSenderMobile = createMobileBox();
        cbReceiverMobile = createMobileBox();

        loadCustomers();

        cbSender.addActionListener(e ->
                onCustomerSelected(cbSender, cbSenderMobile)
        );
        cbReceiver.addActionListener(e ->
                onCustomerSelected(cbReceiver, cbReceiverMobile)
        );

        JPanel form = new JPanel(new GridLayout(4,2,10,10));
        form.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        form.add(new JLabel("Sender"));
        form.add(cbSender);
        form.add(new JLabel("Sender Mobile"));
        form.add(cbSenderMobile);

        form.add(new JLabel("Receiver"));
        form.add(cbReceiver);
        form.add(new JLabel("Receiver Mobile"));
        form.add(cbReceiverMobile);

        add(form);
    }

    /* ================= COMBO HELPERS ================= */

    private JComboBox<Customer> createCustomerBox() {
        JComboBox<Customer> box = new JComboBox<>();
        box.setEditable(true);
        return box;
    }

    private JComboBox<String> createMobileBox() {
        JComboBox<String> box = new JComboBox<>();
        box.setEditable(true);
        return box;
    }

    /* ================= LOAD ================= */

    private void loadCustomers() {
        cbSender.removeAllItems();
        cbReceiver.removeAllItems();

        for (Customer c : customerController.fetchAll()) {
            cbSender.addItem(c);
            cbReceiver.addItem(c);
        }
    }

    /* ================= CORE LOGIC ================= */

    private void onCustomerSelected(
            JComboBox<Customer> customerBox,
            JComboBox<String> mobileBox
    ) {

        Object obj = customerBox.getSelectedItem();
        if (obj == null) return;

        Customer customer;

        if (obj instanceof Customer) {
            customer = (Customer) obj;
        } else {
            customer = getOrCreateCustomer(obj.toString());
            customerBox.addItem(customer);
            customerBox.setSelectedItem(customer);
        }

        loadMobiles(customer, mobileBox);

        mobileBox.addActionListener(e ->
                onMobileSelected(customer, mobileBox)
        );
    }

    private Customer getOrCreateCustomer(String name) {

        Customer existing = customerController.findByName(name);
        if (existing != null) return existing;

        Customer c = new Customer();
        c.setCustomerType("individual");
        c.setContactPersonName(name);

        customerController.create(c);

        return customerController.findByName(name);
    }

    private void loadMobiles(Customer c, JComboBox<String> mobileBox) {

        mobileBox.removeAllItems();

        mobileController.fetchForCustomer(c.getCustomerId())
                .forEach(m -> mobileBox.addItem(m.getMobileNo()));
    }

    private void onMobileSelected(
            Customer customer,
            JComboBox<String> mobileBox
    ) {

        Object obj = mobileBox.getSelectedItem();
        if (obj == null) return;

        String mobile = obj.toString().trim();
        if (mobile.isBlank()) return;

        if (mobileController.existsForCustomer(
                customer.getCustomerId(), mobile)) return;

        CustomerMobileNumber m = new CustomerMobileNumber();
        m.setCustomerId(customer.getCustomerId());
        m.setMobileNo(mobile);

        mobileController.create(m);
    }
}
