package com.lankacourier.DAO;

import com.lankacourier.DB.dbconnection;
import com.lankacourier.Model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    // Add new employee. If emp.getBranchId() <= 0, branch_id will be stored as NULL.
    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO employees (first_name, last_name, username, password_hash, role, mobile_no, branch_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {


            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getUsername());
            ps.setString(4, emp.getPasswordHash());
            ps.setString(5, emp.getRole());
            ps.setString(6, emp.getMobileNo());

            // treat 0 or negative as "no branch" -> store NULL
            if (emp.getBranchId() <= 0) ps.setNull(7, Types.INTEGER);
            else ps.setInt(7, emp.getBranchId());

            int affected = ps.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) emp.setEmployeeId(keys.getInt(1));
            }
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Update existing employee. If emp.getBranchId() <= 0, branch_id will be stored as NULL.
    public boolean updateEmployee(Employee emp) {
        String sql = "UPDATE employees SET first_name=?, last_name=?, username=?, password_hash=?, role=?, mobile_no=?, branch_id=? WHERE employee_id=?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {          

            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getUsername());
            ps.setString(4, emp.getPasswordHash());
            ps.setString(5, emp.getRole());
            ps.setString(6, emp.getMobileNo());

            if (emp.getBranchId() <= 0) ps.setNull(7, Types.INTEGER);
            else ps.setInt(7, emp.getBranchId());

            ps.setInt(8, emp.getEmployeeId());

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Delete employee by id
    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id=?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, employeeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // Get employee by id
    public Employee getEmployeeById(int id) {
        String sql = "SELECT employee_id, first_name, last_name, username, password_hash, role, mobile_no, branch_id FROM employees WHERE employee_id=?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Get employee by username
    public Employee getByUsername(String username) {
        String sql = "SELECT employee_id, first_name, last_name, username, password_hash, role, mobile_no, branch_id FROM employees WHERE username=?";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // List all employees
    public List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT employee_id, first_name, last_name, username, password_hash, role, mobile_no, branch_id FROM employees ORDER BY first_name";
        try (Connection conn = dbconnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
                
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    // Helper: map ResultSet -> Employee
    private Employee mapRow(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmployeeId(rs.getInt("employee_id"));
        emp.setFirstName(rs.getString("first_name"));
        emp.setLastName(rs.getString("last_name"));
        emp.setUsername(rs.getString("username"));
        emp.setPasswordHash(rs.getString("password_hash"));
        emp.setRole(rs.getString("role"));
        emp.setMobileNo(rs.getString("mobile_no"));

        // If branch_id is NULL in DB, set branchId to 0 (since model uses primitive int).
        Object branchObj = rs.getObject("branch_id");
        if (branchObj == null) {
            emp.setBranchId(0);
        } else {
            emp.setBranchId(((Number) branchObj).intValue());
        }

        return emp;
    }

    // quick test runner
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();
        System.out.println("Employees:");
        for (Employee e : dao.getAllEmployees()) {
            System.out.println(e.getEmployeeId() + " | " + e.getFirstName() + " " + e.getLastName()
                    + " | username=" + e.getUsername() + " | branch=" + e.getBranchId());
        }
    }
}
