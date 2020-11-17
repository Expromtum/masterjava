package ru.javaops.masterjava.persist.dao;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.*;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

@RegisterBeanMapper(User.class)
public interface UserDao extends AbstractDao {

    default User insert(User user) {
        if (user.isNew()) {
            int id = insertGeneratedId(user);
            user.setId(id);
        } else {
            insertWitId(user);
        }
        return user;
    }

    default List<User> insertAll(int batchChunkSize, List<User> users) {

        int[] ids = insertAllGeneratedId(batchChunkSize, users);

        for (int i = 0; i< ids.length; i++) {
            users.get(i).setId(ids[i]);
        }

        return users;
    }

    default void insertOnlyNew(int batchChunkSize, List<User> users) {

        insertOnlyNewGeneratedId(batchChunkSize, users);
    }

    @SqlUpdate("INSERT INTO users (full_name, email, flag) VALUES (:fullName, :email, CAST(:flag AS user_flag)) ")
    @GetGeneratedKeys
    int insertGeneratedId(@BindBean User user);

    @SqlBatch("INSERT INTO users (full_name, email, flag) VALUES (:user.fullName, :user.email, CAST(:user.flag AS user_flag)) ")
    @GetGeneratedKeys
    int[] insertAllGeneratedId(@BatchChunkSize int batchChunkSize, @BindBean("user") Iterable<User> users);

    @SqlBatch("INSERT INTO users (full_name, email, flag) VALUES (:user.fullName, :user.email, CAST(:user.flag AS user_flag))" +
            "    ON CONFLICT (email) DO NOTHING RETURNING id")
    void insertOnlyNewGeneratedId(@BatchChunkSize int batchChunkSize, @BindBean("user") Iterable<User> users);

    @SqlUpdate("INSERT INTO users (id, full_name, email, flag) VALUES (:id, :fullName, :email, CAST(:flag AS user_flag)) ")
    void insertWitId(@BindBean User user);

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email LIMIT :it")
    List<User> getWithLimit(@Bind("it") int limit);

    @SqlQuery("SELECT * FROM users ORDER BY full_name, email")
    List<User> getAll();

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users")
    @Override
    void clean();
}
