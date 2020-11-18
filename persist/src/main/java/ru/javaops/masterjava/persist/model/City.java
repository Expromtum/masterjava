package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {

    private String ref;
    private  @NonNull String name;

    public boolean isNew() {
        return ref == null;
    }
}