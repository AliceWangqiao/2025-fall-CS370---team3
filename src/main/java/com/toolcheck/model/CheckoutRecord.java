package com.toolcheck.model;

import java.time.LocalDateTime;

// // It extends the Model class to inherit common fields like id and createdAt.
public class CheckoutRecord extends Model {
    private long userId;
    private long toolId;
    private String status; // checked_out, returned
    private LocalDateTime checkoutDate;
    private LocalDateTime returnDate;
    private String conditionAfter;

    // No-arg constructor
    // Useful for creating an empty Tool object and setting fields later.
    public CheckoutRecord() {}

    // Constructor with userId and toolId, default status "checked_out"
    public CheckoutRecord(long userId, long toolId) {
        this.userId = userId;
        this.toolId = toolId;
        this.status = "checked_out"; // default
        this.checkoutDate = LocalDateTime.now();
    }

    // Full constructor
    public CheckoutRecord(long userId, long toolId, String status) {
        this.userId = userId;
        this.toolId = toolId;
        this.status = status;
        this.checkoutDate = LocalDateTime.now();
    }

    // ---------------- GETTERS AND SETTERS ----------------
    public long getUserId() {
        return userId;              // Get the ID of the user
    }
    public void setUserId(long userId) {
        this.userId = userId;       // Set the ID of the user
    }

    public long getToolId() {
        return toolId;              // Get the ID of the tool
    }
    public void setToolId(long toolId) {
        this.toolId = toolId;       // Set the ID of the tool
    }

    public String getStatus() {
        return status;             // Get the status of the tool
    }
    public void setStatus(String status) {
        this.status = status;       // Set the status of the tool
    }

    public LocalDateTime getCheckoutDate() {
        return checkoutDate;        // Get the checkout date of the tool
    }
    public void setCheckoutDate(LocalDateTime checkoutDate) {
        this.checkoutDate = checkoutDate; // Set the checkout date of the tool
    }

    public LocalDateTime getReturnDate() {
        return returnDate;                 // Get return date of the tool
    }
    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;       // Set return date of the tool
    }

    public String getConditionAfter() {
        return conditionAfter;              // Get condition after using of the tool
    }
    public void setConditionAfter(String conditionAfter) {
        this.conditionAfter = conditionAfter;   // Set condition after using of the tool
    }
}