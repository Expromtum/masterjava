package ru.javaops.masterjava.webapp;

import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.util.MailObject;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.IllegalStateException;

@WebServlet("/sendJms")
@Slf4j
@MultipartConfig
public class JmsSendServlet extends HttpServlet {
    private Connection connection;
    private Session session;
    private MessageProducer producer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            InitialContext initCtx = new InitialContext();
            ConnectionFactory connectionFactory = (ConnectionFactory) initCtx.lookup("java:comp/env/jms/ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer((Destination) initCtx.lookup("java:comp/env/jms/queue/MailQueue"));
        } catch (Exception e) {
            throw new IllegalStateException("JMS init failed", e);
        }
    }

    @Override
    public void destroy() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException ex) {
                log.warn("Couldn't close JMSConnection: ", ex);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        try {
            log.info("Start sending");
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            MailObject mailObject = new MailObject();
            mailObject.setSubject(req.getParameter("subject"));
            mailObject.setBody(req.getParameter("body"));
            mailObject.setUsers(req.getParameter("users"));

            Part filePart = req.getPart("attach");

            if (filePart != null) {
                mailObject.setAttachName(filePart.getSubmittedFileName());
                mailObject.setAttachData(ByteStreams.toByteArray(filePart.getInputStream()));
            }

            result = sendJms(mailObject);

            log.info("Processing finished with result: {}", result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            result = e.toString();
        }
        resp.getWriter().write(result);
    }

    private synchronized String sendJms(MailObject mailObject) throws JMSException {

//        HashMap<String, MailObject> messageContent = new HashMap<>();
//        messageContent.put("mailObject", mailObject);
//        ObjectMessage objectMessage = session.createObjectMessage(messageContent);

        ObjectMessage objectMessage = session.createObjectMessage();
        objectMessage.setObject(mailObject);

        producer.send(objectMessage);
        return "Successfully sent JMS message";
    }
}