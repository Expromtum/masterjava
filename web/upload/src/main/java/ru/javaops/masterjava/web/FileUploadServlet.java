package ru.javaops.masterjava.web;

import ru.javaops.masterjava.xml.PayloadParser;
import ru.javaops.masterjava.xml.schema.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebServlet({"/upload", "/"})
@MultipartConfig//(location = "/tmp")
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        getServletContext().getRequestDispatcher("/upload.jsp").forward(req, res);
        response.sendRedirect("upload.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        Collection<Part> parts = request.getParts();
//        for (Part part : parts) {
//            System.out.println("Name:");
//            System.out.println(part.getName());
//            System.out.println("Header: ");
//            for (String headerName : part.getHeaderNames()) {
//                System.out.println(headerName);
//                System.out.println(part.getHeader(headerName));
//            }
//            System.out.println("Size: ");
//            System.out.println(part.getSize());
//            part.write(part.getName() + "-down.xml");
//        }

        Part file = request.getPart("file");
        System.out.println(file.getSize());
        System.out.println(file.getContentType());

        try (InputStream is = file.getInputStream()) {
            Set<User> users = PayloadParser.processByStax(is);

            request.setAttribute("list", users);
            request.getRequestDispatcher("users.jsp").forward(request, response);

//        res.sendRedirect("upload");

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}