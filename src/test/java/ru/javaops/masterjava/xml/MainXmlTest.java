package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class MainXmlTest {

    @Test
    public void printUsersByProject() throws Exception {
        InputStream is = Resources.getResource("payload.xml").openStream();
        MainXml.printUsersByProject("Topjava", is);

    }
}