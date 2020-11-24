package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.*;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.persist.MailHist;
import ru.javaops.masterjava.service.mail.persist.MailHistDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*mail {
  host: smtp.yandex.ru
  port: 465
  username: "user@yandex.ru"
  password: password
  useSSL: true
  useTLS: false
  debug: true
  fromName: MasterJava
}*/

@Slf4j

public class MailSender {

    private static final MailHistDao dao = DBIProvider.getDao(MailHistDao.class);

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

        String state = "OK";
        try {
            Email email = MailConfig.createHtmlEmail();

            email.setSubject(subject);
            email.setMsg(body);

            for (Addressee a : to)
                email.addTo(a.getEmail(), a.getName());

            for (Addressee a : cc)
                email.addCc(a.getEmail(), a.getName());

            email.send();

        } catch (EmailException e) {
            log.error(e.getMessage(), e);
            state = e.getMessage();
        }

        dao.insert(MailHist.of(to, cc, subject, state));
        log.info("Sent with state: " + state);
    }
}
