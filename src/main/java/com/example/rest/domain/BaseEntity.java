package com.example.rest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/*
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
public int id;
*/
@Getter
@Setter
@AllArgsConstructor
public abstract class BaseEntity {
    private boolean isChanged;

    public BaseEntity() {
    }

    public <T> T update(final T prevVal, final T newVal) {
        if (newVal != null && !newVal.equals(prevVal)) {
            this.isChanged = true;
            return newVal;
        }
        return prevVal;
    }
}
