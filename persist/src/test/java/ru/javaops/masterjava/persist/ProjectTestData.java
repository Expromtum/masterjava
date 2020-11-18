package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public class ProjectTestData {
    public static Project MASTERJAVA;
    public static Project TOPJAVA;

    public static int MASTERJAVA_ID = 1;
    public static int TOPJAVA_ID = 2;
    public static List<Project> PROJECTS;

    public static void init() {
        MASTERJAVA = new Project("MASTERJAVA", "MASTERJAVA Desc");
        TOPJAVA = new Project("TOPJAVA", "TOPJAVA Desc");

        PROJECTS = ImmutableList.of(MASTERJAVA, TOPJAVA);
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            dao.insert(MASTERJAVA);
            dao.insert(TOPJAVA);
        });

        TOPJAVA_ID = TOPJAVA.getId();
        MASTERJAVA_ID = MASTERJAVA.getId();
    }
}