package ru.javaops.masterjava;

import com.google.common.io.Resources;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import java.io.InputStream;
import java.net.URL;
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


    public static void main(String[] args) throws Exception {
        String projectName = "topjava";
        Set<User> users = MainXml.getUsersByProject(projectName,
                Resources.getResource("payload.xml"));

        String html = toHtml(users, projectName);

        Files.writeString(Paths.get("out/users.html"), html);
    }
}
