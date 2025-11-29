package com.toolcheck.model;

import java.time.LocalDateTime;

// Base Model class for common fields shared by all models.
public abstract class Model {
    protected long id;
    protected LocalDateTime createdAt = LocalDateTime.now();

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}