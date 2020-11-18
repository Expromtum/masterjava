package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Project extends BaseEntity {

    private @NonNull String name;
    private String description;

    public Project(Integer id, String name, String description) {
        this(name, description);
        this.id = id;
    }
}
