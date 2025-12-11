package com.toolcheck.model;

// representing a Tool with ID, name, condition, status, and current user ID.
public class Tool implements ToolInterface {

    // Unique ID of the tool
    private long id;

    // Name of the tool
    private String name;

    // Condition of the tool
    private String condition;

    // Status of the tool
    private String status;

    // ID of the user who currently checked out the tool
    // 0 if the tool is available
    private long userId;

    // Gets the unique ID of the tool
    @Override
    public long getId() {
        return id;
    }

    // Sets the unique ID of the tool
    @Override
    public void setId(long id) {
        this.id = id;
    }

    // Gets the name of the tool
    @Override
    public String getName() {
        return name;
    }

    // Sets the name of the tool
    @Override
    public void setName(String name) {
        this.name = name;
    }

    // Gets the condition of the tool
    @Override
    public String getCondition() {
        return condition;
    }

    // Sets the condition of the tool
    @Override
    public void setCondition(String condition) {
        this.condition = condition;
    }

    // Gets the status of the tool
    @Override
    public String getStatus() {
        return status;
    }

    // Sets the status of the tool
    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    // Gets the ID of the user who currently checked out the tool
    @Override
    public long getUserId() {
        return userId;
    }

    // Sets the ID of the user who currently checked out the tool
    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }
}