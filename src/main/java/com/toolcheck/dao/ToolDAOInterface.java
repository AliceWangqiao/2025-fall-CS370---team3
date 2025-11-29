package com.toolcheck.dao;

import com.toolcheck.model.Tool;
import java.util.List;

//  ToolDAOInterface defines the standard operations for managing User data.
public interface ToolDAOInterface {
    boolean addTool(Tool tool);         // Add a new tool to the system
    boolean updateTool(Tool tool);      // Update an existing tool's information
    boolean deleteTool(long id);        // Delete a tool by their ID.
    Tool getToolById(long id);          // Get a tool by their ID.
    List<Tool> getAllTools();           // Get all tools  in the system.
}