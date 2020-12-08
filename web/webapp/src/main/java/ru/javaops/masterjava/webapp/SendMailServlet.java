package ru.javaops.masterjava.webapp;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.service.mail.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@Slf4j
@WebServlet(urlPatterns = "/sendMail")
public class SendMailServlet extends HttpServlet {

    private UserDao userDao = DBIProvider.getDao(UserDao.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        out(req, resp, "");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result = "";

        try {
            String subject = req.getParameter("subject");
            String body = req.getParameter("body");
            String users = req.getParameter("users");

            if (users.length() == 0) {
                result = "Users count must be not zero";
            } else {

                Set<Addressee> addressee = new HashSet<>();
                for (String s : users.split(","))
                    addressee.add(new Addressee(s));

                GroupResult bulkResult = MailWSClient.sendBulk(addressee, subject, body);

                if (!bulkResult.isAllSuccess())
                    result = bulkResult.toString();
                else {
                    result = "Сообщение отправлено";
                }
                log.info(result);
            }
        } catch (Exception e) {
            log.info(e.getMessage(), e);
            result = e.toString();
        }

        resp.getWriter().write(result);
    }

    private void out(HttpServletRequest req, HttpServletResponse resp, String message) throws IOException {
        log.debug("SendMailServlet");

        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "x-requested-with");

        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale(),
                ImmutableMap.of("message", message,
                                "users", userDao.getWithLimit(20)));
        engine.process("sendMail", webContext, resp.getWriter());
    }
}
