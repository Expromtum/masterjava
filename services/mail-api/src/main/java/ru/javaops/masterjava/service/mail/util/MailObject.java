package ru.javaops.masterjava.service.mail.util;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailObject implements Serializable {

    private @NotNull String subject;
    private @NotNull String users;
    private String body;
    private String attachName;
    private byte[] attachData;

}
