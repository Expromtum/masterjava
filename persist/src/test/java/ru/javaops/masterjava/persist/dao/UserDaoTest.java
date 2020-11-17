package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.UserTestData;
import ru.javaops.masterjava.persist.model.User;

import java.util.ArrayList;
import java.util.List;
import static ru.javaops.masterjava.persist.UserTestData.*;

public class UserDaoTest extends AbstractDaoTest<UserDao> {

    public UserDaoTest() {
        super(UserDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserTestData.setUp();
    }

    @Test
    public void getWithLimit() {
        List<User> users = dao.getWithLimit(5);
        Assert.assertEquals(FIST5_USERS, users);
    }

    @Test
    public void insert() {
        DBIProvider.getDBI().useTransaction((handle) -> {
            dao.insert(USER5);
        });

//        Assert.assertEquals(USER5, dao.getAll()); //TODO:
    }

    @Test
    public void insertOnlyNew() {
        List<User> users = new ArrayList<>();
        users.addAll(FIST5_USERS);
        users.add(USER3);
        users.add(USER4);
        users.add(USER5);

        DBIProvider.getDBI().useTransaction((handle) -> {
            dao.insertOnlyNew(5, users);
        });
    }

}