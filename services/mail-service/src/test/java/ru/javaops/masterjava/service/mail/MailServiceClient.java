package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class MailServiceClient {


    /* Для исключения ошибки ... выключить AVAST антивирус, тогда письмо отправится

     Caused by: javax.mail.MessagingException: Could not connect to SMTP host: smtp.mail.ru, port: 465;
    nested exception is:
	javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException:
	PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
	unable to find valid certification
    * */

    public static void main(String[] args) throws  MalformedURLException {

        Service  service = Service.create(
            new URL("http://localhost:8080/mail/mailService?wsdl"),
            new QName("http://mail.service.masterjava.javaops.ru/", "MailServiceImplService"));

        MailService mailService = service.getPort(MailService.class);
        mailService.sendMail(ImmutableList.of(new Addressee("expromtum@gmail.com", null)), null, "Subject", "Body");

    }
}
