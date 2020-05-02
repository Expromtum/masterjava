package ru.javaops.masterjava;

import com.google.common.io.Resources;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;
import ru.javaops.masterjava.xml.util.XsltProcessor;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

public class MainXml {

    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    private static Set<User> getUsersByProject(String projectName, URL url) throws Exception {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));

        try (InputStream is = url.openStream()) {
            Payload payload = parser.unmarshal(is);

            Project project = StreamEx.of(payload.getProjects().getProject())
                    .filter(p -> p.getName().equals(projectName))
                    .findAny().orElseThrow(() -> new IllegalArgumentException("Invalid ProjectName"));

            List<Project.Group> groups = project.getGroup();

            return payload.getUsers().getUser().stream()
                    .filter(u -> StreamEx.of(u.getGroupRefs())
                            .findAny(g -> groups.contains(g))
                            .isPresent())
                    .collect(Collectors.toCollection(() -> new TreeSet<>(USER_COMPARATOR)));
        }
    }

    private static String toHtml(Set<User> users, String projectName) {
        return html(
                head(
                        title(projectName),
                        link().withRel("stylesheet")
                ),
                body(
                        table(attrs("#users"),
                                tbody(
                                        tr(th("Email"), th("Name")
                                        ),
                                        each(users, user -> tr(
                                                td(user.getEmail()),
                                                td(user.getValue())
                                        ))
                                )
                        ).attr("border", "1")
                )
        ).render();
    }

    private static String transformXsl(URL xmlUrl, URL xslUrl, Map<String, String> params) throws Exception {
        try (InputStream xmlInputStream = xmlUrl.openStream();
             InputStream xslInputStream = xslUrl.openStream()) {

            TransformerFactory factory = TransformerFactory.newInstance();
            Source xslSource = new StreamSource(new BufferedReader(new InputStreamReader(xslInputStream, StandardCharsets.UTF_8)));
            Templates template = factory.newTemplates(xslSource);
            Transformer transformer = template.newTransformer();

            params.forEach((key, value) ->
                    transformer.setParameter(key, value));

            Source xmlSource = new StreamSource(new BufferedReader(new InputStreamReader(xmlInputStream, StandardCharsets.UTF_8)));
            StringWriter out = new StringWriter();
            transformer.transform(xmlSource, new StreamResult(out));

            return out.getBuffer().toString();
        }
    }

    private static String transformXsl2(URL xmlUrl, URL xslUrl, Map<String, String> params) throws Exception {
        try (InputStream xmlInputStream = xmlUrl.openStream();
             InputStream xslInputStream = xslUrl.openStream()) {

            XsltProcessor processor = new XsltProcessor(xslInputStream);
            params.forEach(processor::setParameter);

            return processor.transform(xmlInputStream);
        }
    }

    public static void main(String[] args) throws Exception {
        String projectName = "topjava";
        URL xml = Resources.getResource("payload.xml");
        URL xsl = Resources.getResource("payload.xsl");

        Set<User> users = MainXml.getUsersByProject(projectName, xml);
        String html = toHtml(users, projectName);
        Files.writeString(Paths.get("out/users.html"), html);

        Map<String, String> params = new HashMap<>();
        params.put("projectName", "topjava");

        html = transformXsl2(xml, xsl, params);
        System.out.println(html);
        Files.writeString(Paths.get("out/users2.html"), html);
    }
}
