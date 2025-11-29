package com.toolcheck.model;

import java.time.LocalDateTime;

// It extends the Model class to inherit common fields like id and createdAt.
public class Report extends Model {
    private long userId;
    private long toolId;
    private String reportType;
    private String description;
    private String status = "pending";
    private LocalDateTime reportedDate = LocalDateTime.now();

    //  No-argument constructor.
    // Useful for creating an empty Tool object and setting fields later.
    public Report() {}
    public Report(long userId, long toolId, String reportType, String description) {
        this.userId = userId;
        this.toolId = toolId;
        this.reportType = reportType.toLowerCase();
        this.description = description;
    }

    // Getters and setters
    public long getUserId() {
        return userId;          // Get the ID of the user
    }
    public void setUserId(long userId) {
        this.userId = userId;   // Set the ID of the user
    }
    public long getToolId() {
        return toolId;          // Get ID of the tool
    }
    public void setToolId(long toolId) {
        this.toolId = toolId;   // Set ID of the tool
    }
    public String getReportType() {
        return reportType;      // Get report type of the tool
    }
    public void setReportType(String reportType) {
        this.reportType = reportType;      // Set report type of the tool
    }
    public String getDescription() {
        return description;                // Get description of the tool
    }
    public void setDescription(String description) {
        this.description = description;     // Set description of the tool
    }
    public String getStatus() {
        return status;                      // Get status of the tool
    }
    public void setStatus(String status) {
        this.status = status;               // Set status of the tool
    }
    public LocalDateTime getReportedDate() {
        return reportedDate;                // Get reported date of the tool
    }
    public void setReportedDate(LocalDateTime reportedDate) {
        this.reportedDate = reportedDate;      // Set reported date of the tool
    }
}