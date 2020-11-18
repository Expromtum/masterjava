package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
abstract public class BaseEntity {

    protected Integer id;

    public boolean isNew() {
        return id == null;
    }
}
