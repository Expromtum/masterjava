package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ru.javaops.web.WebStateException;

import javax.activation.FileDataSource;
import javax.jws.WebService;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@WebService(endpointInterface = "ru.javaops.masterjava.service.mail.MailService", targetNamespace = "http://mail.javaops.ru/"
//          , wsdlLocation = "WEB-INF/wsdl/mailService.wsdl"
)
public class MailServiceImpl implements MailService {
    public String sendToGroup(Set<Addressee> to, Set<Addressee> cc, String subject, String body) throws WebStateException {
        return MailSender.sendToGroup(to, cc, subject, body, null); //TODO
    }

    @Override
    public GroupResult sendBulk(Set<Addressee> to, String subject, String body, List<Attachment> attachments) throws WebStateException {
        return MailServiceExecutor.sendBulk(to, subject, body, attachments);
    }


    public static void main(String[] args) throws WebStateException, IOException {

        MailServiceImpl service = new MailServiceImpl();

        Path path1 = Paths.get("G:\\ttt.txt");
        byte[] bytes1 = Files.readAllBytes(path1);

        Path path2 = Paths.get("G:\\ttt.txt");
        byte[] bytes2 = Files.readAllBytes(path1);

        List<Attachment> attachments = ImmutableList.of(new Attachment(path1.getFileName().toString(), bytes1),
                new Attachment(path2.getFileName().toString(), bytes2));

        String state = service.sendToGroup(ImmutableSet.of(new Addressee("masterjava@javaops.ru", null)),
                Collections.<Addressee>emptySet(),
                "Group mail subject",
                "Group mail body");
        System.out.println("Group mail state: " + state);


        GroupResult groupResult = MailServiceExecutor.sendBulk(ImmutableSet.of(
                new Addressee("Stagger <stagger@mail.ru>"),
                new Addressee("Bad Email <bad_email.ru>")),
                "Bulk mail subject",
                "Bulk mail body",
                attachments);

        System.out.println("\nBulk mail groupResult:\n" + groupResult);
    }

}