package ru.javaops.masterjava.web;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.model.User;
import ru.javaops.masterjava.web.util.ThymeleafUtil;
import ru.javaops.masterjava.xml.UserProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebServlet({"/upload", "/"})
@MultipartConfig//(location = "/tmp")
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
        final TemplateEngine engine = ThymeleafUtil.getTemplateEngine(request.getServletContext());
        engine.process("upload", webContext, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part file = request.getPart("file");

        final WebContext webContext = new WebContext(request, response, request.getServletContext(), request.getLocale());
        final TemplateEngine engine = ThymeleafUtil.getTemplateEngine(request.getServletContext());

        try (InputStream is = file.getInputStream()) {
            Set<User> users = UserProcessor.process(is);

            webContext.setVariable("currentDate", new Date());
            webContext.setVariable("users", users);
            engine.process("users", webContext, response.getWriter());
        } catch (Exception e) {
            // TODO Не реализовано
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, response.getWriter());
        }
    }
}