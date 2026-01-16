package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public boolean addCustomer(Customer c) {

        String sql = "INSERT INTO customers " +
                "(customer_type, business_name, business_reg_no, " +
                "contact_person_name, contact_person_nic, address, email, " +
                "mobile_no, username, password_hash, account_status) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getCustomerType());
            ps.setString(2, c.getBusinessName());
            ps.setString(3, c.getBusinessRegNo());
            ps.setString(4, c.getContactPersonName());
            ps.setString(5, c.getContactPersonNic());
            ps.setString(6, c.getAddress());
            ps.setString(7, c.getEmail());
            ps.setString(8, c.getMobileNo());

            // Allow null username/password for quick registration
            ps.setString(9, c.getCustomerId() > 0 ? "user" + c.getCustomerId() : null);
            ps.setString(10, null);
            ps.setString(11, "Active");

            int affected = ps.executeUpdate();
            if (affected == 0)
                return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setCustomerId(rs.getInt(1));
                }
            }

            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateCustomer(Customer c) {
        String sql = """
                    UPDATE customers SET
                    customer_type=?, business_name=?, business_reg_no=?,
                    contact_person_name=?, contact_person_nic=?,
                    address=?, email=?, mobile_no=?
                    WHERE customer_id=?
                """;

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, c.getCustomerType());
            ps.setString(2, c.getBusinessName());
            ps.setString(3, c.getBusinessRegNo());
            ps.setString(4, c.getContactPersonName());
            ps.setString(5, c.getContactPersonNic());
            ps.setString(6, c.getAddress());
            ps.setString(7, c.getEmail());
            ps.setString(8, c.getMobileNo());
            ps.setInt(9, c.getCustomerId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(int id) {
        String sql = "DELETE FROM customers WHERE customer_id=?";

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public Customer getCustomerById(int id) {
        String sql = "SELECT * FROM customers WHERE customer_id=?";

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return map(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Customer getByUsername(String username) {
        String sql = "SELECT * FROM customers WHERE username=?";

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next())
                return map(rs);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Customer findByMobile(String mobile) {
        String sql = "SELECT * FROM customers WHERE mobile_no=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mobile);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Customer findByNic(String nic) {
        String sql = "SELECT * FROM customers WHERE contact_person_nic=?";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nic);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next())
                list.add(map(rs));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    private Customer map(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setCustomerType(rs.getString("customer_type"));
        c.setBusinessName(rs.getString("business_name"));
        c.setBusinessRegNo(rs.getString("business_reg_no"));
        c.setContactPersonName(rs.getString("contact_person_name"));
        c.setContactPersonNic(rs.getString("contact_person_nic"));
        c.setAddress(rs.getString("address"));
        c.setEmail(rs.getString("email"));
        try {
            c.setMobileNo(rs.getString("mobile_no"));
        } catch (SQLException ignored) {
        }

        return c;
    }

    public boolean updatePassword(int customerId, String passwordHash) {

        String sql = "UPDATE customers SET password_hash=? WHERE customer_id=?";

        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, passwordHash);
            ps.setInt(2, customerId);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int getNextCustomerId() {
        String sql = "SELECT MAX(customer_id) FROM customers";
        try (Connection conn = dbconnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 1;
    }
}
