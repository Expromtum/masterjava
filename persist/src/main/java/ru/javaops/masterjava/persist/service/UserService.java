package ru.javaops.masterjava.persist.service;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;

import java.util.List;

public class UserService {

    private static final UserDao dao =  DBIProvider.getDao(UserDao.class);

    public UserService() {

    }

    public List<User> insertAll(int batchChunkSize, List<User> users) {
        return dao.insertAll(batchChunkSize, users);
    }

    public void insertOnlyNew(int batchChunkSize, List<User> users) {
        dao.insertOnlyNew(batchChunkSize, users);
    }

    public List<User> getWithLimit(int limit) {
        return dao.getWithLimit(limit);
    }
}
