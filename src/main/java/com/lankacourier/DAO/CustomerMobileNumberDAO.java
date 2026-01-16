package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.CustomerMobileNumber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerMobileNumberDAO {

    public boolean addMobile(CustomerMobileNumber m) {
        String sql = "INSERT INTO customer_mobile_numbers (customer_id, mobile_no) VALUES (?,?)";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, m.getCustomerId());
            ps.setString(2, m.getMobileNo());

            if (ps.executeUpdate() == 0) return false;

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) m.setMobileNoId(rs.getInt(1));
            }
            return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteMobile(int mobileNoId) {
        String sql = "DELETE FROM customer_mobile_numbers WHERE mobile_no_id=?";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mobileNoId);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<CustomerMobileNumber> getMobilesForCustomer(int customerId) {

        List<CustomerMobileNumber> list = new ArrayList<>();
        String sql = "SELECT * FROM customer_mobile_numbers WHERE customer_id=?";

        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CustomerMobileNumber m = new CustomerMobileNumber();
                    m.setMobileNoId(rs.getInt("mobile_no_id"));
                    m.setCustomerId(rs.getInt("customer_id"));
                    m.setMobileNo(rs.getString("mobile_no"));
                    list.add(m);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
        public boolean updateMobile(CustomerMobileNumber m) {

        String sql = """
            UPDATE customer_mobile_number
            SET mobile_no = ?
            WHERE mobile_no_id = ?
        """;

        try (Connection con = dbconnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, m.getMobileNo());
            ps.setInt(2, m.getMobileNoId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
