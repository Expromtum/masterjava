package ru.javaops.masterjava.xml;

import com.google.common.base.Splitter;
import ru.javaops.masterjava.model.User;
import ru.javaops.masterjava.model.UserFlag;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static com.google.common.base.Strings.nullToEmpty;

public class UserProcessor {
    private static final Comparator<User> USER_COMPARATOR = Comparator.comparing(User::getFullName).thenComparing(User::getEmail);

    public static Set<User> process(String projectName, URL payloadUrl) throws Exception {

        try (InputStream is = payloadUrl.openStream()) {
            return process(projectName, is);
        }
    }

    public static Set<User> process(String projectName, InputStream is) throws XMLStreamException, JAXBException {

        StaxStreamProcessor processor = new StaxStreamProcessor(is);
        final Set<String> groupNames = new HashSet<>();

        // Projects loop
        projects:
        while (processor.startElement("Project", "Projects")) {
            if (projectName.equals(processor.getAttribute("name"))) {
                while (processor.startElement("Group", "Project")) {
                    groupNames.add(processor.getAttribute("name"));
                }
                break;
            }
        }
        if (groupNames.isEmpty()) {
            throw new IllegalArgumentException("Invalid " + projectName + " or no groups");
        }

        // Users loop
        Set<User> users = new TreeSet<>(USER_COMPARATOR);

        JaxbParser parser = new JaxbParser(User.class);
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            String groupRefs = processor.getAttribute("groupRefs");
            if (!Collections.disjoint(groupNames, Splitter.on(' ').splitToList(nullToEmpty(groupRefs)))) {
//                User user = parser.unmarshal(processor.getReader(), User.class);

                final String email = processor.getAttribute("email");
                final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
                final String fullName = processor.getReader().getElementText();
                final User user = new User(fullName, email, flag);

                users.add(user);
            }
        }
        return users;
    }

    public static Set<User> process(InputStream is) throws XMLStreamException, JAXBException {

        StaxStreamProcessor processor = new StaxStreamProcessor(is);
        // Users loop
        Set<User> users = new TreeSet<>(USER_COMPARATOR);

        JaxbParser parser = new JaxbParser(User.class);
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
//            User user = parser.unmarshal(processor.getReader(), User.class);

            final String email = processor.getAttribute("email");
            final UserFlag flag = UserFlag.valueOf(processor.getAttribute("flag"));
            final String fullName = processor.getReader().getElementText();
            final User user = new User(fullName, email, flag);

            users.add(user);
        }
        return users;
    }

}
