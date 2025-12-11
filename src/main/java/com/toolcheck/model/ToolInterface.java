package com.toolcheck.model;

//Interface for Tool objects with ID, name, condition, status, and user assignment.
public interface ToolInterface {
    // Gets the unique ID of the tool
    long getId();

    // Sets the unique ID of the tool
    void setId(long id);

    // Gets the name of the tool
    String getName();

    // Sets the name of the tool
    void setName(String name);

    // Gets the condition of the tool
    String getCondition();

    // Sets the condition of the tool
    void setCondition(String condition);

    // Gets the status of the tool
    String getStatus();

    // Sets the status of the tool
    void setStatus(String status);

    // Gets the ID of the user who currently checked out the tool
    long getUserId();

    // Sets the ID of the user who currently checked out the tool
    void setUserId(long userId);
}