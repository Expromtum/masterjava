package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import ru.javaops.web.WebStateException;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MailServiceClient {

    public static void main(String[] args) throws WebStateException, IOException {
        Service service = Service.create(
                new URL("http://localhost:8080/mail/mailService?wsdl"),
                new QName("http://mail.javaops.ru/", "MailServiceImplService"));

        MailService mailService = service.getPort(MailService.class);

        String state = mailService.sendToGroup(ImmutableSet.of(new Addressee("masterjava@javaops.ru", null)),
                null,
                "Group mail subject ",
                "Group mail body");
        System.out.println("Group mail state: " + state);

        Path path1 = Paths.get("G:\\ttt.txt");
        byte[] bytes1 = Files.readAllBytes(path1);

        Path path2 = Paths.get("G:\\ttt2.txt");
        byte[] bytes2 = Files.readAllBytes(path1);

        List<Attachment> attachments = ImmutableList.of(new Attachment(path1.getFileName().toString(), bytes1),
                new Attachment(path2.getFileName().toString(), bytes2));

        GroupResult groupResult = mailService.sendBulk(ImmutableSet.of(
                    new Addressee("Stagger <stagger@mail.ru>"),
                    new Addressee("Bad Email <bad_email.ru>")),
                "Bulk mail subject111 ПРИВЕТ",
                "Bulk mail body222 ПРИВЕТ",
                attachments);

        System.out.println("\nBulk mail groupResult:\n" + groupResult);
    }
}
