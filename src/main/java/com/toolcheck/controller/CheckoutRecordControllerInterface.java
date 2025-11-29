package com.toolcheck.controller;

import com.toolcheck.model.CheckoutRecord;
import java.util.List;

// It is used by the controller layer to manage users in the system.
public interface CheckoutRecordControllerInterface {
    boolean checkoutTool(long userId, long toolId);     // Checkout a tool for a user.
    boolean returnTool(long recordId, String conditionAfter);   // Return a checked-out tool.
    List<CheckoutRecord> listAllRecords();                      // Get a list of all checkout records in the system.
}