package com.lankacourier.Model;

public class Employee {
    private int employeeId;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String role;      // e.g. "admin", "clerk", "driver"
    private int branchId;     // FK to Branch
    private String username;
    private String mobileNo;
    private String photoPath;


    public Employee() {}

    public Employee(int employeeId, String firstName, String lastName,
                    String passwordHash, String role, int branchId,
                    String username, String mobileNo) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.role = role;
        this.branchId = branchId;
        this.username = username;
        this.mobileNo = mobileNo;
    }

    // getters & setters

    public int getEmployeeId() { 
        return employeeId;
    }

    public void setEmployeeId(int employeeId) { 
        this.employeeId = employeeId;
     }

    public String getFirstName() { 
        return firstName; 
    }

    public void setFirstName(String firstName) { 
        this.firstName = firstName; 
    }

    public String getLastName() { 
        return lastName; 
    }

    public void setLastName(String lastName) { 
        this.lastName = lastName; 
    }

    public String getPasswordHash() { 
        return passwordHash; 
    }

    public void setPasswordHash(String passwordHash) { 
        this.passwordHash = passwordHash; 
    }

    public String getRole() { 
        return role; 
    }

    public void setRole(String role) { 
        this.role = role; 
    }

    public int getBranchId() { 
        return branchId; 
    }

    public void setBranchId(int branchId) { 
        this.branchId = branchId; 
    }

    public String getUsername() { 
        return username; 
    
    }
    public void setUsername(String username) { 
        this.username = username; 
    }

    public String getMobileNo() { 
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) { 
        this.mobileNo = mobileNo;
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + role + ")";
    }

    public String getPhotoPath() {
    return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

}
