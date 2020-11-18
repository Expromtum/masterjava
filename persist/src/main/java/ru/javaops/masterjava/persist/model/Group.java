package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import ru.javaops.masterjava.persist.model.type.GroupType;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Group extends BaseEntity {

    private @NonNull String name;
    private @NonNull GroupType type;

    @Column("project_id")
    private @NonNull int projectId;

    public Group(int id, String name, GroupType type, int projectId) {
        this(name, type, projectId);
        this.id = id;
    }
}