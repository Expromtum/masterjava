package ru.javaops.masterjava.service.mail;

import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.config.Configs;
import org.apache.commons.mail.*;

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
    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));

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
            e.printStackTrace(); //TODO:
        }
    }
}
