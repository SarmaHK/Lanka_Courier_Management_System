package com.lankacourier.UI.base;

import com.lankacourier.Auth.Session;
import com.lankacourier.Model.Employee;
import com.lankacourier.Util.SwingUtils;

import java.awt.Component;

public class AdminGuard {

    private static String role() {
        Employee u = Session.getCurrentUser();
        return u == null ? "" : u.getRole().trim().toLowerCase();
    }

    // ===== ADMIN =====
    public static boolean isAdmin() {
        return role().equals("admin");
    }

    public static boolean requireAdmin(Component parent) {
        if (!isAdmin()) {
            SwingUtils.error(parent, "Admin permission required.");
            return false;
        }
        return true;
    }

    // ===== ADMIN OR CLERK =====
    public static boolean isAdminOrClerk() {
        return role().equals("admin") || role().equals("clerk");
    }

    public static boolean requireAdminOrClerk(Component parent) {
        if (!isAdminOrClerk()) {
            SwingUtils.error(parent, "Admin or Clerk permission required.");
            return false;
        }
        return true;
    }
}
