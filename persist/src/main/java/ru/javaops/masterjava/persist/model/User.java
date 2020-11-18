package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import ru.javaops.masterjava.persist.model.type.UserFlag;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull String fullName;

    private @NonNull String email;

    private @NonNull UserFlag flag;

    @Column("city_ref")
    private String cityRef;

    public User(Integer id, String fullName, String email, UserFlag flag, String cityRef) {
        this(fullName, email, flag, cityRef);
        this.id = id;
    }
}