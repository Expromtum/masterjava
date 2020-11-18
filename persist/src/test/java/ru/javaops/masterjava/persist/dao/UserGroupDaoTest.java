package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.UserGroupTestData;

import java.util.Set;

import static ru.javaops.masterjava.persist.GroupTestData.*;
import static ru.javaops.masterjava.persist.UserGroupTestData.getByGroupId;

public class UserGroupDaoTest extends AbstractDaoTest<UserGroupDao> {

    public UserGroupDaoTest() {
        super(UserGroupDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        UserGroupTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        UserGroupTestData.setUp();
    }

    @Test
    public void getAll() throws Exception {
        Set<Integer> userIds = dao.getUserIds(GROUP1_ID);
        Assert.assertEquals(getByGroupId(GROUP1_ID), userIds);

        userIds = dao.getUserIds(GROUP2_ID);
        Assert.assertEquals(getByGroupId(GROUP2_ID), userIds);
    }
}