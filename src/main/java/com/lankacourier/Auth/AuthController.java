package com.lankacourier.Auth;

import com.lankacourier.Controller.EmployeeController;
import com.lankacourier.Model.Employee;
import com.lankacourier.Util.HashUtil;

import java.util.List;

public class AuthController {

    private final EmployeeController employeeController = new EmployeeController();
    

    public boolean login(String uname, String password) {
        if (uname == null || password == null) return false;
        String username = uname.trim();
        if (username.isEmpty() || password.isEmpty()) return false;

        // Fetch all employees and find matching username
        Employee found = null;
        try {
            List<Employee> all = employeeController.fetchAll();
            if (all != null) {
                for (Employee e : all) {
                    if (e.getUsername() != null &&
                        e.getUsername().trim().equalsIgnoreCase(username)) {
                        found = e;
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        if (found == null) return false;

        // Check password
        String storedHash = found.getPasswordHash();
        if (storedHash == null || storedHash.isBlank()) return false;

        if (!HashUtil.verifyPassword(password, storedHash)) {
            return false;
        }

        // SUCCESS — save session
        Session.setCurrentUser(found);

        // Rehash if needed
        int cost = HashUtil.getCostFromBcryptHash(storedHash);
        if (cost > 0 && cost < HashUtil.DEFAULT_COST) {
            String newHash = HashUtil.hashPassword(password, HashUtil.DEFAULT_COST);
            found.setPasswordHash(newHash);
            try {
                employeeController.update(found);
            } catch (Exception ignored) {}
        }

        return true;
    }

    public void logout() {
        Session.clear();
    }
}
