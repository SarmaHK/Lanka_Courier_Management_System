package com.lankacourier.Model;

import java.time.LocalDateTime;

public class PasswordResetRequest {
    private int requestId;
    private String username;
    private String status; // PENDING, APPROVED, COMPLETED
    private LocalDateTime createdAt;

    public PasswordResetRequest() {
    }

    public PasswordResetRequest(String username, String status) {
        this.username = username;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
