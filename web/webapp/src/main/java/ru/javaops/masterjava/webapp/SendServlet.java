package ru.javaops.masterjava.webapp;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.service.mail.Attachment;
import ru.javaops.masterjava.service.mail.GroupResult;
import ru.javaops.masterjava.service.mail.MailWSClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/send")
@Slf4j
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10) //10 MB in memory limit
public class SendServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        try {
            log.info("Start sending");
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            String users = req.getParameter("users");
            String subject = req.getParameter("subject");
            String body = req.getParameter("body");

            Part filePart = req.getPart("fileToUpload");

            try (InputStream is = filePart.getInputStream()) {
                log.info("fileToUpload name: " + req.getParameter("fileUploadName"));

                List<Attachment> attachments = ImmutableList.of(
                        new Attachment(req.getParameter("fileUploadName"), ByteStreams.toByteArray(is)));

                GroupResult groupResult = MailWSClient.sendBulk(MailWSClient.split(users), subject, body, attachments);
                result = groupResult.toString();
            }

            log.info("Processing finished with result: {}", result);
        } catch (Exception e) {
            log.error("Processing failed", e);
            result = e.toString();
        }
        resp.getWriter().write(result);
    }
}
