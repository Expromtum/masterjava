package ru.javaops.masterjava.service.mail.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.MailWSClient;
import ru.javaops.masterjava.web.handler.SoapLoggingHandlers;
import ru.javaops.masterjava.web.handler.SoapServerSecurityHandler;

@Slf4j
public class MailHandlers {

    public static class SecurityHandler extends SoapServerSecurityHandler {
        public SecurityHandler() {
            super(MailWSClient.getHostConfig().getAuthHeader());
        }
    }

    public static class LoggingHandler extends SoapLoggingHandlers.ServerHandler {
        public LoggingHandler() {
            super(MailWSClient.getHostConfig().serverDebugLevel);
        }
    }
}
