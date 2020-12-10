package ru.javaops.masterjava.service.mail;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "email")
@XmlAccessorType(XmlAccessType.FIELD)
public class Attachment {
    private String fileName;
    private byte[] bytes;
}
