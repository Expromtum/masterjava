package ru.javaops.masterjava.web.handler;

import com.sun.xml.ws.api.handler.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.web.Statistics;

@Slf4j
public class SoapServerStatisticsHandler extends SoapBaseHandler {

    private static final String PAYLOAD = "PAYLOAD";
    private static final String START_TIME = "START_TIME";

    @Override
    public boolean handleMessage(MessageHandlerContext context) {

        if (isOutbound(context)) {
            Statistics.count((String) context.get(PAYLOAD), (Long) context.get(START_TIME), Statistics.RESULT.SUCCESS);
        } else {
            String payload = context.getMessage().getPayloadLocalPart();
            log.info("Statistics : handleMessage " + payload);
            context.put(PAYLOAD, payload);
            context.put(START_TIME, System.currentTimeMillis());
        }

        return true;
    }

    @Override
    public boolean handleFault(MessageHandlerContext context) {

        Statistics.count((String) context.get(PAYLOAD), (Long) context.get(START_TIME), Statistics.RESULT.FAIL);

        return true;
    }
}
