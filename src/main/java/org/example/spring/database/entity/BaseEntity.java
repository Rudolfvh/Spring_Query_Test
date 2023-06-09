package org.example.spring.database.entity;

public interface BaseEntity <I>{

    I getId();

    void setId(I id);
}
