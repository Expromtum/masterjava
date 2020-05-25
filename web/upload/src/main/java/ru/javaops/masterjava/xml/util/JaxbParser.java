package ru.javaops.masterjava.xml.util;

import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import java.io.*;

/**
 * Marshalling/Unmarshalling JAXB helper
 * XML Facade
 */
public class JaxbParser {

    private final JAXBContext jaxbContext;

    public JaxbMarshaller createMarshaller() {
        try {
            JaxbMarshaller marshaller = new JaxbMarshaller(jaxbContext);
            if (schema != null) {
                marshaller.setSchema(schema);
            }
            return marshaller;
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    public JaxbUnmarshaller createUnMarshaller() {
        try {
            JaxbUnmarshaller unmarshaller = new JaxbUnmarshaller(jaxbContext);
            if (schema != null) {
                unmarshaller.setSchema(schema);
            }
            return unmarshaller;
        } catch (JAXBException e) {
            throw new IllegalStateException(e);
        }
    }

    protected Schema schema;

    public JaxbParser(Class... classesToBeBound) {
        try {
            jaxbContext = JAXBContext.newInstance(classesToBeBound);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    //    http://stackoverflow.com/questions/30643802/what-is-jaxbcontext-newinstancestring-contextpath
    public JaxbParser(String context) {
        try {
            jaxbContext = JAXBContext.newInstance(context);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public void validate(String str) throws IOException, SAXException {
        validate(new StringReader(str));
    }

    public void validate(Reader reader) throws IOException, SAXException {
        schema.newValidator().validate(new StreamSource(reader));
    }
}
