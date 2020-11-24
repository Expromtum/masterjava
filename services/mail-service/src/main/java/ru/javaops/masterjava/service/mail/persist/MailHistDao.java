package ru.javaops.masterjava.service.mail.persist;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.dao.AbstractDao;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MailHistDao implements AbstractDao {

    @SqlUpdate("TRUNCATE mail_hist CASCADE ")
    @Override
    public abstract void clean();

    @SqlQuery("SELECT * FROM mail_hist ORDER BY datetime, list_to, list_cc, subject, state")
    public abstract List<MailHist> getAll();

    @SqlUpdate("INSERT INTO mail_hist (list_to, list_cc, subject, state, datetime)  VALUES (:listTo, :listCc, :subject, :state, :datetime)")
    @GetGeneratedKeys
    public abstract int insertGeneratedId(@BindBean MailHist mailHist);

    public void insert(MailHist mailHist) {
        int id = insertGeneratedId(mailHist);
        mailHist.setId(id);
    }
}
