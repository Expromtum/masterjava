package ru.javaops.masterjava;

import com.google.common.io.Resources;
import one.util.streamex.StreamEx;
import ru.javaops.masterjava.xml.schema.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainXml {

    public static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getValue).thenComparing(User::getEmail);

    public static Set<User> getUsersByProject(String projectName, URL url)  throws Exception {
        JaxbParser parser = new JaxbParser(ObjectFactory.class);
        parser.setSchema(Schemas.ofClasspath("payload.xsd"));

        try (InputStream is = url.openStream()) {
            Payload payload = parser.unmarshal(is);

            Project project = StreamEx.of(payload.getProjects().getProject())
                    .filter(p -> p.getName().equals(projectName))
                    .findAny().orElseThrow(() -> new IllegalArgumentException("Invalid ProjectName"));

            List <Project.Group> groups = project.getGroup();

            return payload.getUsers().getUser().stream()
                    .filter(u -> StreamEx.of(u.getGroupRefs())
                            .findAny(g ->groups.contains(g))
                            .isPresent())
                    .collect(Collectors.toCollection(() -> new TreeSet<User>(USER_COMPARATOR)));
        }
    }

    public static void main(String[] args) throws Exception{
        String projectName = "topjava";
        Set<User> users = MainXml.getUsersByProject(projectName,
                Resources.getResource("payload.xml"));

        users.forEach(u -> System.out.println(u));
    }
}
