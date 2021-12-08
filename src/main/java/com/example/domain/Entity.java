package com.example.domain;

import java.io.Serial;
import java.io.Serializable;

public class Entity<ID> implements Serializable {
    @Serial
    private static final long serialVersionUID = 987654678921L;

    private ID id;

    /**
     * Returns the id of the entity
     * @return an ID representing the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets the id of the entity
     * @param id ID representing the new id
     */
    public void setId(ID id) {
        this.id = id;
    }
}
