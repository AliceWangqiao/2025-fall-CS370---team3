package com.toolcheck.controller;

import com.toolcheck.dao.ToolDAOInterface;
import com.toolcheck.dao.ToolDAO;
import com.toolcheck.model.ToolInterface;

import java.util.List;

//communicates with the DAO layer (ToolDAO) to perform
// CRUD operations as well as checkout and return actions for tools.
public class ToolController implements ToolControllerInterface {

    private final ToolDAOInterface toolDAO;

    // Default constructor initializes the ToolDAO instance for database operations.
    public ToolController() {
        this.toolDAO = new ToolDAO(); // use DAO for DB operations
    }

   // Get all tools from the system by delegating to the DAO.
    @Override
    public List<ToolInterface> getAllTools() {
        return toolDAO.getAllTools();
    }

    // Get a specific tool by its unique ID by delegating to the DAO.
    @Override
    public ToolInterface getTool(long id) {
        return toolDAO.getTool(id);
    }

    // Adds a new tool to the system by delegating to the DAO.
    @Override
    public void addTool(ToolInterface tool) {
        toolDAO.addTool(tool);
    }

    // Updates an existing tool's information by delegating to the DAO.
    @Override
    public void updateTool(ToolInterface tool) {
        toolDAO.updateTool(tool);
    }

    // Deletes a tool by its unique ID by delegating to the DAO.
    @Override
    public void deleteTool(long id) {
        toolDAO.deleteTool(id);
    }

    // Checks out a tool to a specific user.
    // Delegates to the DAO and handles exceptions.
    @Override
    public boolean checkoutTool(long toolId, long userId) {
        try {
            toolDAO.checkoutTool(toolId, userId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Returns a tool from a user and optionally updates its condition.
    // Delegates to the DAO and handles exceptions.
    @Override
    public boolean returnTool(long toolId, long userId, String condition) {
        try {
            toolDAO.returnTool(toolId, userId, condition); // report is removed
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}