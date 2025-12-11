package com.toolcheck.controller;

import com.toolcheck.model.ToolInterface;
import java.util.List;

//handles CRUD operations as well as
//checkout and return actions for tools.
public interface ToolControllerInterface {
    // Get all tools in the system.
    List<ToolInterface> getAllTools();

    //Get  a specific tool by its unique ID.
    ToolInterface getTool(long id);

    //Adds a new tool to the system.
    void addTool(ToolInterface tool);

    // Updates an existing tool's information.
    void updateTool(ToolInterface tool);

    // Deletes a tool by its unique ID.
    void deleteTool(long id);

    // Checks out a tool to a specific user.
    boolean checkoutTool(long toolId, long userId);

    //Returns a tool from a user and optionally updates its condition.
    boolean returnTool(long toolId, long userId, String condition);

    // Get all tools currently checked out by a specific user.
    default List<ToolInterface> getToolsByUser(long userId) {

        // By default, return all tools checked out by user
        return getAllTools().stream()
                .filter(t -> t.getUserId() == userId)
                .toList();
    }
}