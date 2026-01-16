package com.lankacourier.Model;

import java.time.LocalDateTime;

public class TrackingHistory {
    private int historyId;
    private int parcelId;
    private int branchId;
    private Integer employeeId; // <-- nullable now
    private String status;
    private LocalDateTime timestamp;

    public TrackingHistory() {}

    public int getHistoryId() { return historyId; }
    public void setHistoryId(int historyId) { this.historyId = historyId; }

    public int getParcelId() { return parcelId; }
    public void setParcelId(int parcelId) { this.parcelId = parcelId; }

    public int getBranchId() { return branchId; }
    public void setBranchId(int branchId) { this.branchId = branchId; }

    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
