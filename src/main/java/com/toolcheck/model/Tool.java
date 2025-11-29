package com.toolcheck.model;

// It extends the Model class to inherit common fields like id and createdAt.
public class Tool extends Model {
    private String name;                        // Name of the tool
    private String description;                 // Description or details about the tool
    private String category;                    // Category of the tool (e.g., hand tool, power tool)
    private String toolCondition = "good";      // Condition of the tool, default is "good"
    private String status = "available";        // Status of the tool (available, checked_out, maintenance)
    private String location;                    //  Physical location of the tool

    // No-argument constructor
    // Useful for creating an empty Tool object and setting fields later.
    public Tool() {}
    public Tool(String name, String description, String category, String toolCondition, String status, String location) {
        this.name = name;
        this.description = description;
        this.category = category;

        // Normalize condition and status to ensure valid values
        if(toolCondition != null && !toolCondition.isBlank()) this.toolCondition = normalizeCondition(toolCondition);
        if(status != null && !status.isBlank()) this.status = normalizeStatus(status);
        this.location = location;
    }

    // Getters and setters
    public String getName() {
        return name;                    // Get the name of the tool.
    }
    public void setName(String name) {
        this.name = name;               // Set the name of the tool.
    }
    public String getDescription() {
        return description;             // Get the description of the tool.
    }
    public void setDescription(String description) {
        this.description = description; // Set the description of the tool.
    }
    public String getCategory() {
        return category;                // Get the category of the tool.
    }
    public void setCategory(String category) {
        this.category = category;       // Set the category of the tool.
    }
    public String getToolCondition() {
        return toolCondition;           // Get the condition of the tool.
    }
    public void setToolCondition(String toolCondition) {
        this.toolCondition = normalizeCondition(toolCondition); // Set the condition of the tool.
    }
    public String getStatus() {
        return status;                 // Get the status of the tool.
    }
    public void setStatus(String status) {
        this.status = normalizeStatus(status);  // Set the status of the tool.
    }
    public String getLocation() {
        return location;                // Get the location of the tool.
    }
    public void setLocation(String location) {
        this.location = location;       // Set the location of the tool
    }


    // Ensures the tool condition is valid.
    // Acceptable values: "excellent", "good", "fair", "poor"
    // Defaults to "good" if invalid
    private String normalizeCondition(String cond) {
        cond = cond.toLowerCase();
        switch (cond) {
            case "excellent", "good", "fair", "poor" -> {}
            default -> cond = "good";
        }
        return cond;
    }


    // Ensures the tool status is valid.
    // Acceptable values: "available", "checked_out", "maintenance"
    // Defaults to "available" if invalid
    private String normalizeStatus(String status) {
        status = status.toLowerCase().replace(" ", "_");
        switch (status) {
            case "available", "checked_out", "maintenance" -> {}
            default -> status = "available";
        }
        return status;
    }
}
