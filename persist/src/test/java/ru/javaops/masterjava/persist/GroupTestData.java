package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.type.GroupType;

import java.util.List;

import static ru.javaops.masterjava.persist.ProjectTestData.MASTERJAVA_ID;
import static ru.javaops.masterjava.persist.ProjectTestData.TOPJAVA_ID;

public class GroupTestData {
    public static Group GROUP1;
    public static Group GROUP2;
    public static Group GROUP3;
    public static Group GROUP4;
    public static Group GROUP5;
    public static List<Group> GROUPS;

    public static int GROUP1_ID;
    public static int GROUP2_ID;
    public static int GROUP3_ID;
    public static int GROUP4_ID;
    public static int GROUP5_ID;

    public static void init() {
        ProjectTestData.init();
        ProjectTestData.setUp();

        GROUP1 = new Group("MasterJava01", GroupType.FINISHED, MASTERJAVA_ID);
        GROUP2 = new Group("MasterJava02", GroupType.CURRENT, MASTERJAVA_ID);
        GROUP3 = new Group("MasterJava03", GroupType.REGISTERING, MASTERJAVA_ID);
        GROUP4 = new Group("Topjava04", GroupType.REGISTERING, TOPJAVA_ID);
        GROUP5 = new Group("Topjava05", GroupType.REGISTERING, TOPJAVA_ID);
        GROUPS = ImmutableList.of(GROUP1, GROUP2, GROUP3, GROUP4, GROUP5);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            GROUPS.forEach(dao::insert);
        });

        GROUP1_ID = GROUP1.getId();
        GROUP2_ID = GROUP2.getId();
        GROUP3_ID = GROUP3.getId();
        GROUP4_ID = GROUP4.getId();
        GROUP5_ID = GROUP5.getId();
    }
}
