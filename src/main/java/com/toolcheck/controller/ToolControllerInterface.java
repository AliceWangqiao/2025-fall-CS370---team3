package com.toolcheck.controller;

import com.toolcheck.model.Tool;
import java.util.List;

//  It is used by the controller layer to manage users in the system.
public interface ToolControllerInterface {
    boolean addTool(Tool tool);     // Create a new tool in the system.
    boolean updateTool(Tool tool);  // Update an existing tool's information.
    boolean deleteTool(long id);    // Delete a tool from the system by their ID.
    Tool getToolById(long id);      // Get a tool by their ID.
    List<Tool> getAllTools();       // Get a list of all tools in the system.
}