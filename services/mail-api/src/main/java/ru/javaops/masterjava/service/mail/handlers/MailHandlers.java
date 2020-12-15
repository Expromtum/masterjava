package ru.javaops.masterjava.service.mail.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.web.handler.SoapServerSecurityHandler;

@Slf4j
public class MailHandlers {

    public static class SecurityHandler extends SoapServerSecurityHandler {

        public SecurityHandler() {
            super(MailWSClient.USER, MailWSClient.PASSWORD);
        }
    }
}
