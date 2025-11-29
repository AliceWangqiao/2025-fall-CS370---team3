package com.toolcheck.controller;

import com.toolcheck.dao.CheckoutRecordDAOInterface;
import com.toolcheck.dao.ToolDAOInterface;
import com.toolcheck.model.CheckoutRecord;
import com.toolcheck.model.Tool;

import java.time.LocalDateTime;
import java.util.List;

// It acts as a bridge between the data layer (DAO) and the application logic.
public class CheckoutRecordController implements CheckoutRecordControllerInterface {

    // DAO interfaces for accessing tools and checkout records
    private final ToolDAOInterface toolDAO;
    private final CheckoutRecordDAOInterface recordDAO;

    public CheckoutRecordController(ToolDAOInterface toolDAO, CheckoutRecordDAOInterface recordDAO) {
        this.toolDAO = toolDAO;
        this.recordDAO = recordDAO;
    }


    // Checkout a tool for a user.
    @Override
    public boolean checkoutTool(long userId, long toolId) {
        // Get the tool by its ID
        Tool tool = toolDAO.getToolById(toolId);
        if (tool == null) {
            System.out.println("Tool not found!");
            return false;
        }

        // Check if the tool is available
        if (!"available".equalsIgnoreCase(tool.getStatus())) {
            System.out.println("Tool is not available!");
            return false;
        }

        // Create a new checkout record with default status "checked_out"
        CheckoutRecord record = new CheckoutRecord(userId, toolId);
        record.setCheckoutDate(LocalDateTime.now());
        record.setStatus("checked_out");

        // Add the record to DAO
        boolean added = recordDAO.addRecord(record);

        // If added successfully, update the tool's status
        if (added) {
            tool.setStatus("checked_out");
            toolDAO.updateTool(tool);
        }
        return added;
    }

    // Return a tool that was previously checked out.
    @Override
    public boolean returnTool(long recordId, String conditionAfter) {
        // Get checkout record by ID
        CheckoutRecord record = recordDAO.getRecordById(recordId);
        if (record == null) {
            System.out.println("Record not found!");
            return false;
        }
        // Get the tool associated with the record
        Tool tool = toolDAO.getToolById(record.getToolId());
        if (tool == null) {
            System.out.println("Tool not found!");
            return false;
        }

        // Update checkout record
        record.setReturnDate(LocalDateTime.now());
        record.setConditionAfter(conditionAfter);
        record.setStatus("returned");
        recordDAO.updateRecord(record);

        // Update tool's condition and status
        tool.setToolCondition(conditionAfter);
        tool.setStatus("available");
        toolDAO.updateTool(tool);

        return true;
    }

    // Get a list of all checkout records
    @Override
    public List<CheckoutRecord> listAllRecords() {
        return recordDAO.getAllRecords();
    }
}