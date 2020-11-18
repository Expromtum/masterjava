package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {

    public Project insert(Project project) {
        if (project.isNew()) {
            int id = insertGeneratedId(project);
            project.setId(id);
        } else {
            insertWitId(project);
        }
        return project;
    }

    @SqlQuery("SELECT nextval('common_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE common_seq RESTART WITH " + (id + step)));
        return id;
    }

    @SqlUpdate("INSERT INTO project (name, description) VALUES (:name, :description) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Project project);

    @SqlUpdate("INSERT INTO project (id, name, description) VALUES (:name, :description) ")
    abstract void insertWitId(@BindBean Project project);

    @SqlQuery("SELECT * FROM project ORDER BY name, description")
    public abstract List<Project> getAll();

    @SqlUpdate("TRUNCATE project CASCADE")
    @Override
    public abstract void clean();

    @SqlBatch("INSERT INTO project (id, name, description) VALUES (:name, :description)")
    public abstract int[] insertBatch(@BindBean List<Project> projects, @BatchChunkSize int chunkSize);
}


