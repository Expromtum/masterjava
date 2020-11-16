package ru.javaops.masterjava.persist;

import org.jdbi.v3.core.ConnectionFactory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import ru.javaops.masterjava.persist.dao.AbstractDao;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import static org.slf4j.LoggerFactory.getLogger;

public class DBIProvider {
    private static final Logger log = getLogger(DBIProvider.class);

    private volatile static ConnectionFactory connectionFactory = null;

    private static class DBIHolder {
        static final Jdbi jDBI;

        static {
            final Jdbi dbi;
            if (connectionFactory != null) {
                log.info("Init jDBI with  connectionFactory");
                dbi = Jdbi.create(connectionFactory);
            } else {
                try {
                    log.info("Init jDBI with  JNDI");
                    InitialContext ctx = new InitialContext();
                    dbi = Jdbi.create((DataSource) ctx.lookup("java:/comp/env/jdbc/masterjava"));
                } catch (Exception ex) {
                    throw new IllegalStateException("PostgreSQL initialization failed", ex);
                }
            }
            jDBI = dbi;
//            jDBI.setSqlLogger(new SLF4JLog());
        }
    }

    public static void init(ConnectionFactory connectionFactory) {
        DBIProvider.connectionFactory = connectionFactory;
    }

    public static Jdbi getDBI() {
        return DBIHolder.jDBI;
    }

    public static <T extends AbstractDao> T getDao(Class<T> daoClass) {
        return DBIHolder.jDBI.installPlugin(new SqlObjectPlugin()).onDemand(daoClass);
    }
}
