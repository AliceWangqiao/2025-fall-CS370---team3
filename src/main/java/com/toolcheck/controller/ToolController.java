package com.toolcheck.controller;

import com.toolcheck.dao.ToolDAOInterface;
import com.toolcheck.model.Tool;

import java.util.List;

// It acts as a bridge between the data layer (DAO) and the application logic.
public class ToolController implements ToolControllerInterface {

    // DAO interface to perform database or storage operations for tools
    private final ToolDAOInterface dao;

    // This allows the controller to use any DAO implementation.
    public ToolController(ToolDAOInterface dao) {
        this.dao = dao;
    }

    // Create a new tool
    @Override
    public boolean addTool(Tool tool) {
        // Normalize condition and status
        tool.setToolCondition(normalizeCondition(tool.getToolCondition()));
        tool.setStatus(normalizeStatus(tool.getStatus()));
        return dao.addTool(tool);
    }

    // Updates an existing tool.
    @Override
    public boolean updateTool(Tool tool) {
        tool.setToolCondition(normalizeCondition(tool.getToolCondition()));
        tool.setStatus(normalizeStatus(tool.getStatus()));
        return dao.updateTool(tool);
    }

    // Deletes a tool by ID.
    @Override
    public boolean deleteTool(long id) {
        return dao.deleteTool(id);
    }


    // Get a tool by ID
    @Override
    public Tool getToolById(long id) {
        return dao.getToolById(id);
    }

    // Get all tools in the system.
    @Override
    public List<Tool> getAllTools() {
        return dao.getAllTools();
    }

    // Ensures the tool condition is valid and standardized.
    // Converts input to lowercase and trims whitespace.
    // If the input is invalid or null, defaults to "good".
    private String normalizeCondition(String cond) {
        if (cond == null) return "good";
        cond = cond.trim().toLowerCase();
        return switch (cond) {
            case "excellent", "good", "fair", "poor" -> cond;
            default -> "good";
        };
    }

    // Ensures the tool condition is valid and standardized.
    // Converts input to lowercase and trims whitespace, and replaces spaces with underscores.
    // If the input is invalid or null, blank, defaults to "available".
    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) return "available";
        status = status.trim().toLowerCase().replace(' ', '_');
        return switch (status) {
            case "available", "checked_out", "maintenance" -> status;
            default -> "available";
        };
    }
}