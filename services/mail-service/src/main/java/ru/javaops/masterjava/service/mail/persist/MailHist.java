package ru.javaops.masterjava.service.mail.persist;

import com.bertoncelj.jdbi.entitymapper.Column;
import com.google.common.base.Joiner;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;
import ru.javaops.masterjava.service.mail.Addressee;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MailHist extends BaseEntity {
    private @Column("list_to") String listTo;
    private @Column("list_cc") String listCc;
    private String subject;
    @NonNull private String state;
    @NonNull private Date datetime;

    public static MailHist of(List<Addressee> to, List<Addressee> cc, String subject, String state){
        return new MailHist(Joiner.on(", ").join(to), Joiner.on(", ").join(cc), subject, state, new Date());
    }
}