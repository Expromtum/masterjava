package ru.javaops.masterjava.persist;

import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.UserGroup;

import java.util.List;
import java.util.Set;

import static ru.javaops.masterjava.persist.GroupTestData.*;
import static ru.javaops.masterjava.persist.dao.UserGroupDao.toUserGroups;

public class UserGroupTestData {

    public static List<UserGroup> USER_GROUPS;

    public static void init() {
        UserTestData.init();
        UserTestData.setUp();

        GroupTestData.init();
        GroupTestData.setUp();

        USER_GROUPS = toUserGroups(UserTestData.ADMIN.getId(), GROUP1_ID, GROUP2_ID, GROUP3_ID);
        USER_GROUPS.addAll(toUserGroups(UserTestData.FULL_NAME.getId(), GROUP1_ID, GROUP2_ID));
        USER_GROUPS.addAll(toUserGroups(UserTestData.USER1.getId(), GROUP3_ID, GROUP4_ID));
        USER_GROUPS.add(new UserGroup(UserTestData.USER2.getId(), GROUP5_ID));
        USER_GROUPS.add(new UserGroup(UserTestData.USER3.getId(), GROUP5_ID));
    }

    public static void setUp() {
        UserGroupDao dao = DBIProvider.getDao(UserGroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            dao.insertBatch(USER_GROUPS);
        });
    }

    public static Set<Integer> getByGroupId(int groupId) {
        return UserGroupDao.getByGroupId(groupId, USER_GROUPS);
    }
}
