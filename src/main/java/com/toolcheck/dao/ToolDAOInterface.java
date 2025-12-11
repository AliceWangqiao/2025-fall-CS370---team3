package com.toolcheck.dao;

import com.toolcheck.model.ToolInterface;
import java.util.List;

// Data access operation on Tool objects.
// Includes CRUD operations and methods for checking out and returning tools.
public interface ToolDAOInterface {
    //  Get all tools from the system.
    List<ToolInterface> getAllTools();

    // Get a specific tool by its unique ID.
    ToolInterface getTool(long id);

    // Adds a new tool to the system.
    void addTool(ToolInterface tool);

    // Updates an existing tool's information in the system.
    void updateTool(ToolInterface tool);

    //Marks a tool as deleted or removes it from the system.
    void deleteTool(long id);

    // Checks out a tool to a specific user.
    // Updates the tool's status and associates it with the user.
    void checkoutTool(long toolId, long userId) throws Exception;

    //Returns a tool from a user and optionally updates its condition.
    // Updates the tool's status and clears the associated user.
    void returnTool(long toolId, long userId, String condition) throws Exception;

}