package com.lankacourier.Auth;

import com.lankacourier.Model.Employee;

//store the signed-in user
public final class Session {
    private static Employee currentUser = null;

    private Session() {}

    public static void setCurrentUser(Employee user) {
        currentUser = user;
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static boolean isAuthenticated() {
        return currentUser != null;
    }

    public static void clear() {
        currentUser = null;
    }
}
