package com.toolcheck.dao;

import com.toolcheck.model.CheckoutRecord;
import java.util.List;

//  CheckoutRecordDAOInterface defines the standard operations for managing User data.
public interface CheckoutRecordDAOInterface {
    boolean addRecord(CheckoutRecord record);  // Add a new record in the system.
    boolean updateRecord(CheckoutRecord record);// Update check out record
    List<CheckoutRecord> getAllRecords();       // Get all check out record in the system
    CheckoutRecord getRecordById(long id);      // Get a checkout record by ID
}