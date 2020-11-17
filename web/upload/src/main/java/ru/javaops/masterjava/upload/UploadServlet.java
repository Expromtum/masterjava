package ru.javaops.masterjava.upload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "/", loadOnStartup = 1)
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 10) //10 MB in memory limit
public class UploadServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(UploadServlet.class);

    private final UserProcessor userProcessor = new UserProcessor();
    private final UserService userService = new UserService();

    private static final int DEFAULT_BATCH_CHUNK_SIZE = 1000;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        webContext.setVariable("batchChunkSize", DEFAULT_BATCH_CHUNK_SIZE);
        engine.process("upload", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        try {
            int batchChunkSize = Integer.parseInt(req.getParameter("batchChunkSize"));

//            http://docs.oracle.com/javaee/6/tutorial/doc/glraq.html
            Part filePart = req.getPart("fileToUpload");
            if (filePart.getSize() == 0) {
                throw new IllegalStateException("Upload file have not been selected");
            }
            try (InputStream is = filePart.getInputStream()) {

                List<User> users = new ArrayList<>();
                ExecutorService exec = Executors.newCachedThreadPool();
                List<Future<?>> futures = new ArrayList<>();

                Consumer<List<User>> cunsumer = (userChank) -> {
                    futures.add(
                            exec.submit(() -> {
                                userService.insertOnlyNew(batchChunkSize, userChank);
                                LOG.info("Processing users with chank: {} users, {} ", userChank.size(), userChank);
                                users.addAll(userChank);
                            }));
                };

                userProcessor.processChank(is, batchChunkSize, cunsumer);

                for (Future<?> future : futures) {
                    try {
                        future.get();
                    } catch(Exception e) {
                        // do logging and nothing else
                    }
                }

                webContext.setVariable("users", users);
                engine.process("result", webContext, resp.getWriter());
            }
        } catch (Exception e) {
            webContext.setVariable("exception", e);
            engine.process("exception", webContext, resp.getWriter());
        }
    }
}
