package ru.javaops.masterjava.service.mail.persist;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.dao.AbstractDaoTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static ru.javaops.masterjava.service.mail.persist.MailHistTestData.MAIL_HIST;

public class MailHistDaoTest extends AbstractDaoTest<MailHistDao> {

    public MailHistDaoTest() {
        super(MailHistDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        MailHistTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        MailHistTestData.setUp();
    }

    @Test
    public void getAll() throws Exception {
        final List<MailHist> mailHist = dao.getAll();
        assertEquals(MAIL_HIST, mailHist);
        System.out.println(mailHist);
    }
}