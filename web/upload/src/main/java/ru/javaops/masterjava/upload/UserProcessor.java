package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class UserProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);

    public List<User> process(final InputStream is) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();

        JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
            final User user = new User(xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
            users.add(user);
        }
        return users;
    }

    public void processChank(final InputStream is, final int batchChunkSize,
                                   Consumer<List<User>> chuncFunc) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        List<User> users = new ArrayList<>();

        JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
            final User user = new User(xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
            users.add(user);

            if (users.size() == batchChunkSize) {
                chuncFunc.accept(users);
                users = new ArrayList<>();
            }
        }

        if (users.size() > 0)
            chuncFunc.accept(users);
    }

}
