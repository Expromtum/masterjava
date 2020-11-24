package ru.javaops.masterjava.service.mail.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MailHistTestData {
    public static MailHist MAIL_HIST_1;
    public static MailHist MAIL_HIST_2;
    public static MailHist MAIL_HIST_3;
    public static List<MailHist> MAIL_HIST;

    public static int MAIL_HIST_ID1;
    public static int MAIL_HIST_ID2;
    public static int MAIL_HIST_ID3;

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static void init() throws ParseException {
        MAIL_HIST_1 = new MailHist("stagger@mail.ru", "expromtum@gmail.com", "TOPIC STAGGER", "OK",
                formatter.parse("01.01.2020 11:11:11"));

        MAIL_HIST_2 = new MailHist("expromtum@gmail.com", "", "TOPIC EXPROMTUM", "FAILED",
                formatter.parse("01.01.2020 12:12:12"));

        MAIL_HIST_3 = new MailHist("expromtum@gmail.com", "", "TOPIC EXPROMTUM", "OK",
                formatter.parse("01.01.2020 13:13:13"));

        MAIL_HIST = ImmutableList.of(MAIL_HIST_1, MAIL_HIST_2);
    }

    public static void setUp() {
        MailHistDao dao = DBIProvider.getDao(MailHistDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            MAIL_HIST.forEach(dao::insert);
        });
        MAIL_HIST_ID1 = MAIL_HIST_1.getId();
        MAIL_HIST_ID2 = MAIL_HIST_2.getId();
    }
}

