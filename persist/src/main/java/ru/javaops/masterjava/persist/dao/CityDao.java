package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import one.util.streamex.StreamEx;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    @SqlUpdate("INSERT INTO city (ref, name) VALUES (:ref, :name)")
    public abstract void insert(@BindBean City city);

    @SqlQuery("SELECT * FROM city ORDER BY name, ref")
    public abstract List<City> getAll();

    @SqlUpdate("TRUNCATE city CASCADE")
    @Override
    public abstract void clean();

    public Map<String, City> getAsMap() {
        return StreamEx.of(getAll()).toMap(City::getRef, identity());
    }

    @SqlBatch("INSERT INTO city (ref, name) VALUES (:ref, :name)")
    public abstract int[] insertBatch(@BindBean List<City> citys, @BatchChunkSize int chunkSize);
}
